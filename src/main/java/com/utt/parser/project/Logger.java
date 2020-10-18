package com.utt.parser.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logger {
	// If the user wants to check what is been logged into the file on the screen
	public static boolean LOGTOMONITOR = false;

	// Private constructor as its a singleton class
	private Logger() {
	}

	// Singleton so the reference should be set to null
	private static Logger loggerObj = null;

	public static Logger getInstance() {
		// Check only single copy reference should be created
		if (loggerObj == null) {
			loggerObj = new Logger();
			return loggerObj;
		} else {
			return loggerObj;
		}
	}

// Method used for logging the data
	public void log(final String data) {
		//This executes simultaneously and does not hinder the program execution
		new Thread(new Runnable() {
			public void run() {
				Date date = new Date();
				BufferedWriter bufferedWriter = null;
				try {
					String message = date + " : " + data;
					bufferedWriter = new BufferedWriter(new FileWriter("log.txt", true));
					bufferedWriter.write(message);
					bufferedWriter.newLine();
					if (LOGTOMONITOR == true) {
						System.out.println(message);
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if (bufferedWriter != null) {
						try {
							bufferedWriter.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}).start();

	}
}
