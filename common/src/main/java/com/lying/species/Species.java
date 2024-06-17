package com.lying.species;

/** A configurable package of types and abilities applied to a creature */
public class Species
{
	private TypeSet types;
	private Map<Identifier, AbilityInstance> abilities = new HashMap<>();
	
	public Species()
	{
		
	}
}