package com.lying.ability;

/** A distinct gameplay-modifying property */
public abstract class Ability
{
	private final Identifier registryName;
	
	public Ability(Identifier regName)
	{
		registryName = regName;
	}
	
	public final AbilityInstance instance(AbilitySource source) { return new AbilityInstance(this, source); }
	
	public final AbilityInstance instance(AbilitySource source, Consumer<NbtCompound> dataModifier) { return new AbilityInstance(this, source, data); }
	
	/** The unique registry name of abilities of this type */
	public Identifier registryName() { return registryName; }
	
	/** Sets the initial values of any necessary memory values */
	protected NbtCompound initialiseNBT(NbtCompound data) { return data; }
	
	/**
	 * Where a specific ability instance originates<br>
	 * This determines which ability is retained if two or more share the same map name
	 */
	public static enum AbilitySource implements StringIdentifiable
	{
		MISC(Int.MAX_VALUE),
		TYPE(5),
		SUBTYPE(4),
		SPECIES(3),
		TEMPLATE(2),
		CUSTOM(-1);
		
		private final int priority;
		
		private AbilitySource(int priorityIn)
		{
			priority = priorityIn;
		}
		
		public String getName() { return name().toLowerCase(); }
		
		public static AbilitySource fromName(String name)
		{
			for(AbilitySource source : values())
				if(source.getName().equalsIgnoreCase(name))
					return source;
			return MISC;
		}
		
		public boolean overrules(AbilitySource other) { return other.priority > priority; }
	}
	
	public static class AbilityInstance
	{
		private final Ability ability;
		private final AbilitySource source;
		private boolean locked = false;
		private NbtCompound data;
		
		public AbilityInstance(Ability abilityIn, AbilitySource sourceIn)
		{
			ability = abilityIn;
			source = sourceIn;
			data = ability.initialiseNBT(new NbtCompound());
		}
		
		public AbilityInstance(Ability abilityIn, AbilitySource sourceIn, Consumer<NbtCompound> dataModifier)
		{
			this(abilityIn, sourceIn);
			dataModifier.accept(data);
		}
		
		/** The variable map name for this specific ability instance */
		public Identifier getMapName() { return ability.registryName(); }
		
		public AbilitySource source() { return source; }
		
		/**
		 * Returns true if this ability instance is immutable.<br>
		 * This usually indicates that it is a default model.
		 */
		public boolean isReadOnly() { return isDefault; }
		
		public AbilityInstance lock()
		{
			locked = true;
			return this;
		}
		
		public Ability ability() { return ability; }
		
		public NbtCompound memory() { return data; }
		
		public void setMemory(NbtCompound dataIn)
		{
			if(isReadOnly()) return;
			data = dataIn;
		}
		
		public NbtCompound write(NbtCompound data)
		{
			data.putString("Ability", ability.registryName().toString());
			data.putString("Source", source.getName());
			
			if(isReadOnly())
				data.putBoolean("ReadOnly", true);
			
			if(!memory().isEmpty())
				data.put("Memory", memory());
			return data;
		}
		
		/** Returns a modifiable duplicate of this instance */
		public final AbilityInstance copy()
		{
			return new AbilityInstance(ability, source, data);
		}
	}
}