package com.lying.client.config;

import java.io.FileWriter;
import java.util.Properties;

import com.lying.config.Config;

import net.minecraft.util.StringIdentifiable;

public class ClientConfig extends Config
{
	private static final Properties DEFAULT_SETTINGS = new Properties();
	
	private TextAlign textAlignment = TextAlign.LEFT;
	private ButtonLayout buttonLayout = ButtonLayout.CATS_TOP;
	
	public ClientConfig(String fileIn)
	{
		super(fileIn);
	}
	
	protected Properties getDefaults() { return DEFAULT_SETTINGS; }
	
	protected void readValues(Properties valuesIn)
	{
		textAlignment = TextAlign.fromString(parseStringOr(valuesIn.getProperty("TextAlignment"), TextAlign.LEFT.asString()));
		buttonLayout = ButtonLayout.fromString(parseStringOr(valuesIn.getProperty("ButtonLayout"), ButtonLayout.CATS_TOP.asString()));
	}
	
	protected void writeValues(FileWriter writer)
	{
		writeString(writer, "TextAlignment", textAlignment.asString());
		writeString(writer, "ButtonLayout", buttonLayout.asString());
	}
	
	public TextAlign textAlignment() { return textAlignment; }
	
	public ButtonLayout buttonLayout() { return buttonLayout; }
	
	public static enum TextAlign implements StringIdentifiable
	{
		RIGHT,
		LEFT,
		CENTRE;
		
		public String asString() { return name(); }
		
		public static TextAlign fromString(String name)
		{
			for(TextAlign val : values())
				if(val.asString().equalsIgnoreCase(name))
					return val;
			return LEFT;
		}
	}
	
	public static enum ButtonLayout implements StringIdentifiable
	{
		CATS_TOP,
		CATS_BOT;
		
		public String asString() { return name(); }
		
		public static ButtonLayout fromString(String name)
		{
			for(ButtonLayout val : values())
				if(val.asString().equalsIgnoreCase(name))
					return val;
			return CATS_TOP;
		}
	}
	
	static
	{
		DEFAULT_SETTINGS.setProperty("TextAlignment", TextAlign.LEFT.asString());
		DEFAULT_SETTINGS.setProperty("ButtonLayout", ButtonLayout.CATS_TOP.asString());
	}
}
