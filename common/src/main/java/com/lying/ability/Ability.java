package com.lying.ability;

import java.util.function.Consumer;

import com.lying.reference.Reference;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

/** A distinct gameplay-modifying property */
public abstract class Ability
{
	private final Identifier registryName;
	
	public Ability(Identifier regName)
	{
		registryName = regName;
	}
	
	/** Returns a blank instance of this ability from the given source */
	public final AbilityInstance instance(AbilitySource source) { return new AbilityInstance(this, source); }
	
	/** Returns an instance of this ability from the given source, with modified memory data */
	public final AbilityInstance instance(AbilitySource source, Consumer<NbtCompound> dataModifier) { return new AbilityInstance(this, source, dataModifier); }
	
	/** The unique registry name of abilities of this type */
	public Identifier registryName() { return registryName; }
	
	public Identifier mapName(AbilityInstance instance) { return registryName(); }
	
	/** Sets the initial values of any necessary memory values */
	protected NbtCompound initialiseNBT(NbtCompound data) { return data; }
	
	public Text name(AbilityInstance instance) { return Text.translatable("ability."+Reference.ModInfo.MOD_ID+"."+registryName.getPath()); }
	
	/**
	 * Where a specific ability instance originates<br>
	 * This determines which ability is retained if two or more share the same map name
	 */
	public static enum AbilitySource implements StringIdentifiable
	{
		MISC(Integer.MAX_VALUE),
		TYPE(4),
		SPECIES(3),
		TEMPLATE(2),
		CUSTOM(-1);
		
		public static final Codec<AbilitySource> SOURCE = Codec.STRING.comapFlatMap(string -> DataResult.success(AbilitySource.fromName(string)), AbilitySource::asString);
		
		private final int priority;
		
		private AbilitySource(int priorityIn)
		{
			priority = priorityIn;
		}
		
		public String asString() { return name().toLowerCase(); }
		
		public static AbilitySource fromName(String name)
		{
			for(AbilitySource source : values())
				if(source.asString().equalsIgnoreCase(name))
					return source;
			return MISC;
		}
		
		public boolean overrules(AbilitySource other) { return other.priority >= priority; }
	}
}