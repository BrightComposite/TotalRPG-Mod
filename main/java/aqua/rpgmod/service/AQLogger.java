package aqua.rpgmod.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aqua.rpgmod.AQModInfo;

public class AQLogger
{
	private static Logger logger = LogManager.getLogger(AQModInfo.NAME);
	
	public static void log(Level logLevel, String message)
	{
		logger.log(logLevel, message);
	}
	
	public static void log(String message)
	{
		logger.log(Level.INFO, message);
	}
}
