package com.lying.utility;

import java.util.Optional;

import net.minecraft.util.Identifier;

public class Cosmetic
{
	private final Identifier registryName;
	private final Type type;
	private int colour = -1;
	
	public Cosmetic(Identifier regName, Type typeIn)
	{
		registryName = regName;
		type = typeIn;
	}
	
	public Cosmetic(Identifier regName, Type typeIn, int colourIn)
	{
		this(regName, typeIn);
		colour = colourIn;
	}
	
	public Identifier registryName() { return registryName; }
	
	public Type type() { return type; }
	
	public Cosmetic tint(int colour)
	{
		this.colour = colour;
		return this;
	}
	
	public boolean tinted() { return colour >= 0; }
	
	public Optional<Integer> color() { return tinted() ? Optional.of(colour) : Optional.empty(); }
	
	public static enum Type
	{
		EARS(1),
		TAIL(1),
		WINGS(1),
		HORNS(3),
		NOSE(1),
		MISC(-1);
		
		private final int capacity;
		
		private Type(int limitIn)
		{
			capacity = limitIn;
		}
		
		public int capacity() { return capacity; }
		
		public boolean uncapped() { return capacity < 1; }
	}
}