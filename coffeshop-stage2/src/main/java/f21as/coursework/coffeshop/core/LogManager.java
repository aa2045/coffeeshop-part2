package f21as.coursework.coffeshop.core;

import java.io.IOException;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

//Log Manager Class
//implements the singleton Pattern

public class LogManager {
	//private static Logger logger = null;
	private static LogManager loggerinstance = new LogManager();
	private Logger logger;
	//private constructor
	private LogManager() {
		this.logger = null;
	}
	

	public Logger getLogger() {
		if (logger == null) {
			try {
				FileHandler handler = new FileHandler("frequency.log", true);

				logger = Logger.getLogger("com.javacodegeeks.snippets.core");
				logger.addHandler(handler);

				logger.finest("Session started - " + new java.util.Date());
			} catch (IOException e) {
				System.out.println("Technical problem while creating lofile");
				System.out.println("Program must stop");
				System.exit(1);
			}
		}
		return logger;
	}
	//public static method
	public static LogManager getInstance() {
		return loggerinstance;
	}
}