package com.lying.config;

import java.io.FileWriter;
import java.util.Properties;

public class ServerConfig extends Config
{
	private static final Properties DEFAULT_SETTINGS = new Properties();
	
	private int maxPower = 5;
	
	public ServerConfig(String fileIn)
	{
		super(fileIn);
	}
	
	protected Properties getDefaults() { return DEFAULT_SETTINGS; }
	
	protected void readValues(Properties valuesIn)
	{
		maxPower = parseIntOr(valuesIn.getProperty("Power"), 5);
	}
	
	protected void writeValues(FileWriter writer)
	{
		writeInt(writer, "Power", maxPower);
	}
	
	public int maxPower() { return maxPower; }
	
	static
	{
		DEFAULT_SETTINGS.setProperty("Power", "5");
	}
}
