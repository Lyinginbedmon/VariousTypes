package com.lying.template;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.type.TypeSet;

import net.minecraft.util.Identifier;

/** A configurable modification applied to a creature, on top of its species */
public class Template
{
	private final Identifier registryName;
	
	private final List<Precondition> preconditions = Lists.newArrayList();
	private final List<Operation> operations = Lists.newArrayList();
	
	public Template(Identifier nameIn)
	{
		registryName = nameIn;
	}
	
	public Identifier registryName() { return registryName; }
	
	public List<Precondition> preconditions() { return preconditions; }
	
	public List<Operation> operations() { return operations; }
	
	public void applyTypeOperations(TypeSet typeSet) { operations.forEach(operation -> operation.applyToTypes(typeSet)); }
	
	public void applyAbilityOperations(AbilitySet abilitySet) { operations.forEach(operation -> operation.applyToAbilities(abilitySet)); }
	
	/** A requirement that must be met prior to applying this template during character creation */
	public static class Precondition
	{
		public boolean isValidFor(CharacterSheet sheet) { return true; }
	}
	
	/** Something this template does to the creature when it is applied*/
	public static class Operation
	{
		public void applyToTypes(TypeSet typeSet) { }
		
		public void applyToAbilities(AbilitySet abilitySet) { }
	}
}