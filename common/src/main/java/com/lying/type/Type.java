package com.lying.type;

/**
 * A hard-coded set of properties shared by all creatures of the same type.<br>
 * This serves as the constant on which later variation is built.
 */
public abstract class Type
{
	private final Map<Identifier, Ability> defaultAbilities = new HashMap<>();
	
	public Type(AbilityInstance... abilitiesIn)
	{
		for(AbilityInstance ability : abilitiesIn)
			defaultAbilities.put(ability.getMapName(), ability);
	}
	
	public abstract boolean compatibleWith(Type other);
	
	public static class TypeSet
	{
		private final Type[] types;
		
		public TypeSet(Type... typesIn)
		{
			types = typesIn;
		}
	}
}