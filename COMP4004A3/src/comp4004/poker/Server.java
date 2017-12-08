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
import comp4004.poker.ai.*;

public class Server{

	private int 			port = 0;
	private int 			numplayers = -1;
	private int 			numhumans = -1;
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
		
		while(numhumans < 0 || numhumans > numplayers){
			print("Enter number of human players (between 1 and "+ numplayers +")");
			numhumans = in.nextInt();
		}
		in.close();
		rules = new RulesEngine(numplayers);
		eventQueue = new LinkedBlockingQueue<List<Object>>();
		connectAndRecieve(numhumans);
	}

	private void connectAndRecieve(int count){
		try{

			print(getTimestamp() + ": server listening on port " + port);
			listeningSocket = new ServerSocket(port);
			ArrayList<PlayerThread> threads = new ArrayList<PlayerThread>();
			while(isAcceptingConnections){
				if (count == 0) { break; }
				
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
			
			for (int i = 0; i<(numplayers-numhumans); i++) {
				threads.add(new PlayerThread(true));
			}
			
			print(getTimestamp() +": Expected number of clients connected. Starting Game");
			rules.initFGame();
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
		StrategyContext strat = new StrategyContext();

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
		
		public PlayerThread(boolean ai){
			playerNum = rules.registerThread(threadID);
			rules.getPlayerById(threadID).setAI(ai);
		}

		public void run(){
			//log.logmsg(threadID + ": Main loop started");
			boolean isAI = rules.getPlayerById(threadID).getAI();
			BoardState b = getBoardStateAI();
			//Send player their player number
			if (!isAI) {
				send(playerNum);
				sendBoardState();
			} else {
				b = getBoardStateAI();
			}
			log.logmsg("Thread " + threadID + " starting up.");
			while(isRunning){
				if (rules.gameWinner() != null) {
					rules.getPlayerById(threadID).setPlaying(false);
					if (rules.gameWinner().getID() == threadID) {
						//send winner msg to client
						if (!isAI) {
							send(Optcodes.GameWinner);
						}
					}
					else {
						if (!isAI) {
							send(Optcodes.GameOver);
							send(Long.valueOf(rules.gameWinner().getID()));
						}
					}
					break;
				}

				if (rules.getPlayerList().get(0).getID() != threadID) {
					try {
						if (!isAI) {
							send(Optcodes.ClientNotActiveTurn);
							List<Object> event = eventQueue.poll(200, TimeUnit.MILLISECONDS);
							if (event != null) {
								handleEvent(event);
							}
							sendBoardState();
						} else {
							List<Object> event = eventQueue.poll(200, TimeUnit.MILLISECONDS);
							if (event != null) {
								handleEvent(event);
							}
							b = getBoardStateAI();
						}
						continue;
					} catch (InterruptedException ie) {
						ie.printStackTrace();
						continue;
					}
				}
				else {
				}

				if (rules.isRunning()) {
					//Start client turn and draw a card
					if (!rules.startTurn(threadID)) {
						continue;
					}
					if (!isAI) {
						send(Optcodes.ClientActiveTurn);
						//Send updated hand to client
						sendBoardState();
					} else {
						b = getBoardStateAI();
					}
					//get what cards the client wants to play
					int cardIndex = -1;
					
					while(true){
						if (!isAI) {
							//while not end turn optcode
							cardIndex = getCardsToBePlayed();
						} else {
							//execute strategy then end turn
							if (!(rules.getFirst() == threadID)) {
								strat.setStrategy(new StrategyTwo());
								cardIndex = strat.executeStrategy(rules, b, threadID);
								if (cardIndex == 0) {
									strat.setStrategy(new StrategyOne());
									cardIndex = strat.executeStrategy(rules, b, threadID);
								}
							} else {
								strat.setStrategy(new StrategyOne());
								cardIndex = strat.executeStrategy(rules, b, threadID);
							}
						}
					
						if(cardIndex == -3) { 
							//end turn optcode received
							if (rules.endTurn(threadID)) {
								log.logmsg("Thread " + threadID + ": ended turn.");
								List<Object> eventmsg = new ArrayList<Object>(2);
								eventmsg.add(Long.valueOf(threadID));
								eventmsg.add("endturn");
								sendEvent(eventmsg);
								if (!isAI) {
									send(Optcodes.ClientNotActiveTurn);
								}
								break;
							}
							else {
								if (!isAI) {
									send(Optcodes.ClientActiveTurn);
								}
								continue;
							}
						} else if (cardIndex == -2) {
							rules.exchangeCard(threadID);
							log.logmsg("Thread " + threadID + ": exchanged cards.");
							send(Optcodes.ClientExchange);
													
						} else if(cardIndex == -1){
							log.logmsg("Thread " + threadID + ": invalid card.");
							send(Optcodes.InvalidCard);
						}
						else {
							Card cardChosen = rules.getPlayerById(threadID).getHand().getCardbyIndex(cardIndex);
							log.logmsg("Thread " + threadID + ": attempting to play card " + cardIndex + ": " + cardChosen.getCardName());
							
							if (rules.discardCard(cardIndex, threadID)) {
								send(Optcodes.SuccessfulCardPlay);
								log.logmsg("Thread " + threadID + ": successfully discarded card " + cardIndex + ": " + 
										cardChosen.getCardName());
							}
							else {
								send(Optcodes.InvalidCard);
								log.logmsg("Thread " + threadID + ": invalid play value card " + cardIndex + ": " + 
										cardChosen.getCardName());
							}
						}
						sendBoardState();
					}
				}
				
				else {
					//if tournament is not running
					if (rules.getPlayerById(threadID).getPlaying()) { //then you are winner of previous tourney
						List<Object> eventmsg = new ArrayList<Object>(2);
						eventmsg.add(Long.valueOf(threadID));
						eventmsg.add("gameover");
						sendEvent(eventmsg);

					}
					rules.roundCleanup();
					rules.initGame();
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
		
		private BoardState getBoardStateAI() {
			return rules.makeBoardState(rules.getPlayerById(threadID));
		}

		/**
		 * Get the index of the card to be played and plays the card
		 */
		private int getCardsToBePlayed(){
			send(Optcodes.ClientGetCardsToBePlayed);
			int index = (int) get();
			String cardname = "";

			if (index == Optcodes.ClientEndTurn){ //client calls end turn
				return -3;
			} else if (index == Optcodes.ClientExchange) {
				return -2;
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
		 * @return whatever it needs to
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
				//send(Optcodes.OppEndTurn);
				//send(event.get(0));
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
