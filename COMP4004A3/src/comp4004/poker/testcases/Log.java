package comp4004.poker.testcases;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class Log {
	Logger logger;
	FileHandler fh;
	private String LogDirectory = (System.getProperty("user.dir") + "/Logs/");

	public Log(String loggername, String classname){
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd.HH.mm.ss").format(new Date());
		logger = Logger.getLogger(loggername);
		try {  

			//configure the logger  
			fh = new FileHandler(LogDirectory + classname +"_" + timeStamp + ".log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();  
			fh.setFormatter(formatter);

			logger.info("Logger Configured");  
		} catch (SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}

	public void logmsg(String message){
		logger.info(message);
	}
	
	public static void main(String[] args) {}
	
}
