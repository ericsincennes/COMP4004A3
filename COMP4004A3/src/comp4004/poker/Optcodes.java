package comp4004.poker;

public class Optcodes {
	//Server to Client Opt codes
	public static final int ClientUpdateBoardState 		= 101;
	public static final int ClientGetCardsToBePlayed	= 102;
	public static final int ClientEndTurn				= 103;
	public static final int ClientExchange				= 104;
	
	public static final int ClientNotActiveTurn			= 150;
	public static final int ClientActiveTurn 			= 151;
	
	public static final int InvalidCard					= 201;
	public static final int SuccessfulCardPlay			= 202;
	
	public static final int OppEndTurn					= 302;
	
	public static final int GameWinner 					= 9001;
	public static final int GameOver					= 8999;

}
