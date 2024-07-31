package com.lying.config;

import java.io.FileWriter;
import java.util.Properties;

public class ServerConfig extends Config
{
	private static final Properties DEFAULT_SETTINGS = new Properties();
	
	private int maxPower = 5;
	private boolean fatigue = true;
	
	public ServerConfig(String fileIn)
	{
		super(fileIn);
	}
	
	protected Properties getDefaults() { return DEFAULT_SETTINGS; }
	
	protected void readValues(Properties valuesIn)
	{
		maxPower = parseIntOr(valuesIn.getProperty("Power"), 5);
		fatigue = parseBoolOr(valuesIn.getProperty("Fatigue"), true);
	}
	
	protected void writeValues(FileWriter writer)
	{
		writeInt(writer, "Power", maxPower);
		writeBool(writer, "Fatigue", fatigue);
	}
	
	/** Returns the power budget value available to players during character creation */
	public int maxPower() { return maxPower; }
	
	/** Returns true if players should suffer from increasing levels of Fatigue due to accrued nonlethal damage */
	public boolean fatigueEnabled() { return fatigue; }
	
	static
	{
		DEFAULT_SETTINGS.setProperty("Power", "5");
		DEFAULT_SETTINGS.setProperty("Fatigue", "true");
	}
}
