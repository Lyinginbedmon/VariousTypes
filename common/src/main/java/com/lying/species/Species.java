package com.lying.species;

import com.lying.ability.AbilitySet;
import com.lying.type.TypeSet;

import net.minecraft.util.Identifier;

/** A configurable package of types and abilities applied to a creature */
public class Species
{
	private final Identifier id;
	
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	
	public Species(Identifier idIn)
	{
		id = idIn;
	}
	
	public Identifier registryName() { return id; }
	
	public TypeSet types() { return types; }
	
	public AbilitySet abilities() { return abilities; }
}