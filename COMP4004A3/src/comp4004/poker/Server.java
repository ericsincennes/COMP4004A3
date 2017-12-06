package comp4004.poker;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import comp4004.poker.Card.*;
import comp4004.poker.testcases.Log;
import comp4004.poker.Optcodes;

public class Server{

	private int 			port = 0;
	private int 			numplayers;
	private int 			numhumans;
	private boolean 		isAcceptingConnections = true;
	private ServerSocket 	listeningSocket;
	private BlockingQueue<List<Object>> eventQueue; //interthread communication - active thread will never poll but will be only sender
											  //except in the case of ivanhoe...
	private Log log;				//log = new Log(this.getClass().getName(), "ServerLog");
	private RulesEngine		rules;


	public Server(){
		Scanner in = new Scanner(System.in);
		log = new Log(this.getClass().getName(), "ServerLog");
		print("Enter a port for the game server: ");
		port = in.nextInt();

		while(numplayers < 2 || numplayers > 4){
			print("Enter number of players to play (between 2 and 4)");
			numplayers = in.nextInt();
		}
		
		while(numhumans < numplayers || numhumans > numplayers){
			print("Enter number of human players (between 1 and "+ numplayers +")");
			numhumans = in.nextInt();
		}
		in.close();
		rules = new RulesEngine(numplayers, numhumans);
		eventQueue = new LinkedBlockingQueue<List<Object>>();
		connectAndRecieve(numplayers);
	}

	private void connectAndRecieve(int count){
		try{

			print(getTimestamp() + ": server listening on port " + port);
			listeningSocket = new ServerSocket(port);
			ArrayList<PlayerThread> threads = new ArrayList<PlayerThread>();
			while(isAcceptingConnections){

				Socket clientSocket = listeningSocket.accept();

				print(getTimestamp() + ": New client connected from address " + clientSocket.getInetAddress() + " on port " +clientSocket.getPort());

				count--;
				threads.add(new PlayerThread(clientSocket));

				if(count == 0){
					listeningSocket.close();
					isAcceptingConnections = false;
					break;
				}
			}

			print(getTimestamp() +": Expected number of clients connected. Starting Game");
			rules.initGame();
			for(PlayerThread p : threads){
				p.start();
			}

		} catch(IOException e){
			error(getTimestamp() + ": Server socket unable to connect to port" + port);
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		new Server();
	}

	/**
	 * creates a time stamp with the format Year.Month.Day.Hour.Min.Sec
	 * @return String representation of time stamp
	 */
	public String getTimestamp(){
		return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public void print(String s){
		System.out.println(s);
	}

	/**
	 * Convenience function
	 * @param s String to be printed
	 */
	public void error(String s){
		System.err.println(s);
	}

	class PlayerThread extends Thread {

		private Socket client;
		private int port;
		private InetAddress addr;
		private boolean isRunning = true;
		private ObjectOutputStream out;
		private ObjectInputStream in;
		private long threadID = getId(); //used to identify the individual threads in the rules/logic engine
		private int playerNum;

		public PlayerThread(Socket c){
			client = c;
			port = c.getPort();
			addr = c.getInetAddress();
			playerNum = rules.registerThread(threadID);
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new ObjectInputStream(client.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}

		public void run(){
			//log.logmsg(threadID + ": Main loop started");

			//Send player their player number
			send(playerNum);
			sendBoardState();
			log.logmsg("Thread " + threadID + " starting up.");
			while(isRunning){
				if (rules.gameWinner() != null) {
					if (rules.gameWinner().getID() == threadID) {
						//send winner msg to client
						send(Optcodes.GameWinner);
					}
					else {
						send(Optcodes.GameOver);
						send(Long.valueOf(rules.gameWinner().getID()));
					}
					break; //or possibly ask to start again?
				}

				if (rules.getPlayerList().get(0).getID() != threadID) {
					try {
						send(Optcodes.ClientNotActiveTurn);
						List<Object> event = eventQueue.poll(200, TimeUnit.MILLISECONDS);
						if (event != null) {
							handleEvent(event);
						}
						sendBoardState();
						continue;
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						continue;
					}
				}
				else {
				}

				if (rules.isRunning()) {
					send(Optcodes.ClientActiveTurn);
					/*sendBoardState();
					//Is the tournament running AND not first turn in tournament
					if (!rules.isColourChosen()) {
						//choose colour
						if (rules.canStartTournament(threadID)) {
							CardColour c;
							c = GetTournamentColourFromClient();
							log.logmsg("Thread " + threadID + ": got tourney colour " + c + ".");
							while(!rules.initializeTournamentColour(threadID, c)) {
								//send some message about bad colour input
								c = GetTournamentColourFromClient();
							}	

						} else {
							log.logmsg("Thread " + threadID + ": failed to start a tournament.");
							List<Object> eventmsg = new ArrayList<Object>(2);
							eventmsg.add(Long.valueOf(threadID));
							eventmsg.add("failstart");
							eventmsg.add(rules.getPlayerById(threadID).getHand().getHand());
							send(Optcodes.ClientFailStartTournament);
							sendEvent(eventmsg);
							rules.failInitTournamentColour();
							continue;
						}
					}*/
					//Send updated hand to client
					sendBoardState();

					//get what cards the client wants to play
					int cardIndex, eventIndex = -1;
					
					while(true){
						//while not end turn optcode
						eventIndex = getCardsToBePlayed();
						print("Type 11 to exchange cards, type 99 end your turn.");
						//print("Choose any number of cards to exchange by their position (1-5)");
						if(eventIndex == -2) { 
							//end turn optcode received
							if (rules.endTurn(threadID)) {
								log.logmsg("Thread " + threadID + ": ended turn.");
								List<Object> eventmsg = new ArrayList<Object>(2);
								eventmsg.add(Long.valueOf(threadID));
								eventmsg.add("endturn");
								sendEvent(eventmsg);
								send(Optcodes.ClientNotActiveTurn);
								break;
							}
							else {
								send(Optcodes.ClientActiveTurn);
								continue;
							}
							
						} else if(eventIndex == -1){
							log.logmsg("Thread " + threadID + ": invalid card.");
							send(Optcodes.InvalidCard);
						} else if(eventIndex == -3) {
							print("Choose any number of cards to exchange by their position (1-5), type 99 to finish exchanging and end turn.");
							while(true) {
								cardIndex = getCardsToBePlayed();
								if (cardIndex == -1) {
									log.logmsg("Thread " + threadID + ": invalid card.");
									send(Optcodes.InvalidCard);
								} else if (cardIndex == -2){
									rules.exchange(threadID);
									//rules.determineHandStrength(threadID);
									sendBoardState();
									eventIndex = -2;
									break;
								} else {
									Card cardChosen = rules.getPlayerById(threadID).getHand().getCardbyIndex(cardIndex);
									log.logmsg("Thread " + threadID + ": exchanging card " + cardIndex + ": " +	cardChosen.getCardName());
									if (rules.exchangeCard(cardIndex, threadID)){
										log.logmsg("Thread " + threadID + ": successfully exchanged card " + cardIndex + ": " + cardChosen.getCardName());
									} else {
										send(Optcodes.InvalidCard);
										log.logmsg("Thread " + threadID + ": invalid card " + cardIndex + ": " + cardChosen.getCardName());
									}
								}
							}
						} 
					}
					sendBoardState();
				}
								
				else {
					//if tournament is not running
					if (rules.getPlayerById(threadID).getPlaying()) { //then you are winner of previous tourney
						List<Object> eventmsg = new ArrayList<Object>(2);
						eventmsg.add(Long.valueOf(threadID));
						eventmsg.add("tournamentover");
						sendEvent(eventmsg);
					}
					rules.roundCleanup();
				}
			}
		}
		
		/**
		 * Sends the boardstate 
		 */
		private void sendBoardState() {
			BoardState board = rules.makeBoardState(rules.getPlayerById(threadID));
			send(Optcodes.ClientUpdateBoardState);
			send(board);
		}

		/**
		 * Get the index of the card to be played and plays the card
		 */
		private int getCardsToBePlayed(){
			send(Optcodes.ClientGetCardsToBePlayed);
			int index = (int) get();
			String cardname = "";

			if (index == Optcodes.ClientEndTurn){ //client calls end turn
				return -2;
			} else if (index == Optcodes.ClientExchange) {
				return -3;
			}

			if(index < 0 || index > rules.getPlayerById(threadID).getHandSize()){
				return -1;
			}
			return index;
		}


		/**
		 * puts a copy of a server event into the eventqueue for each other thread to poll
		 * @param msg to be sent, a list of objects with first element being threadID, and 2nd being the event
		 */
		private void sendEvent(List<Object> msg) {
			if (msg.size() == 0) return;
			try {
				if (msg.get(0) instanceof Long && (long) msg.get(0) == threadID) {
					for (int i=0; i<numplayers-1; i++) {
						eventQueue.add(msg);
					}
				} else {
					throw new IllegalStateException("Sending an event without correct ID");
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * handles an event, somehow
		 * @param event - the event msg received, with prepended sender ID
		 * @return whatever it needs to, mainly for ivanhoe and adapt
		 */
		private Object handleEvent(List<Object> event) {
			if (event == null || event.size() < 2) { return null; }
			if (event.get(0) instanceof Long && (long) event.get(0) == threadID) {
				eventQueue.add(event);
				return null;
			}
			if (!(event.get(1) instanceof String)) { return null; }
			switch ((String) event.get(1)) {
			case "gameover":
				send(Optcodes.GameOver);
				send(((Long) event.get(0)).toString());
				break;
			case "endturn":
				send(Optcodes.OppEndTurn);
				send(event.get(0));
				break;
			default:
				break;
			}
			return null;
		}

		/**
		 * Gets an object from the client
		 * Does not verify the typeOf an object
		 * @return
		 */
		private Object get(){
			Object o = new Object();
			try {
				o = in.readObject();
			} catch (ClassNotFoundException | IOException e) {
				rules.removePlayer(threadID);
				this.interrupt();
			}
			log.logmsg("Received a " + o.getClass().getName() + " " + o.toString() + " from thread " + threadID);
			return o;
		}

		/**
		 * Sends an object to the client
		 * @param o Object to be sent
		 * @return boolean if successful
		 */
		private boolean send(Object o){
			try {
				if (loggable(o))
					log.logmsg("Thread " + threadID + " sending a " + o.getClass().getName() + " " + o.toString());
				out.writeObject(o);
				out.flush();
				out.reset();
			} catch (IOException e) {
				rules.removePlayer(threadID);
				this.isRunning = false;
				return false;
			}
			return true;
		}
		
		private boolean loggable(Object o) {
			if ((o instanceof BoardState)) {
				return false;
			} else if (o.equals(101)) {
				return false;
			} else if (threadID != rules.getPlayerList().get(0).getID() && o.equals(150)) {
				return false;
			}
			return true;
		}
	}
}
