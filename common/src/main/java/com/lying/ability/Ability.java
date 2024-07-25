package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.function.Consumer;

import com.lying.init.VTAbilities;
import com.lying.reference.Reference;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

/** A distinct gameplay-modifying property */
public class Ability
{
	public static final Codec<Ability> CODEC = RecordCodecBuilder.create(instance -> instance.group(Identifier.CODEC.fieldOf("Ability").forGetter(Ability::registryName)).apply(instance, VTAbilities::get));
	
	private final Identifier registryName;
	
	public Ability(Identifier regName)
	{
		registryName = regName;
	}
	
	/** Returns what type of ability this is */
	public AbilityType type() { return AbilityType.PASSIVE; }
	
	public final AbilityInstance instance() { return instance(AbilitySource.MISC); }
	
	/** Returns a blank instance of this ability from the given source */
	public final AbilityInstance instance(AbilitySource source) { return new AbilityInstance(this, source); }
	
	/** Returns an instance of this ability from the given source, with modified memory data */
	public final AbilityInstance instance(AbilitySource source, Consumer<NbtCompound> dataModifier) { return new AbilityInstance(this, source, dataModifier); }
	
	/** The unique registry name of abilities of this type */
	public Identifier registryName() { return registryName; }
	
	public Identifier mapName(AbilityInstance instance) { return registryName(); }
	
	public boolean isHidden(AbilityInstance instance) { return false; }
	
	/** Sets the initial values of any necessary memory values */
	protected NbtCompound initialiseNBT(NbtCompound data) { return data; }
	
	public Text displayName(AbilityInstance instance) { return translate("ability",registryName.getPath()); }
	
	public Optional<Text> description(AbilityInstance instance) { return Optional.of(translate("ability", registryName.getPath()+".desc")); }
	
	/** Registers any event handlers needed by this ability to operate. Called during initialisation. */
	public void registerEventHandlers() { }
	
	public static enum AbilityType
	{
		PASSIVE(Integer.MAX_VALUE),
		ACTIVATED(0),
		TOGGLED(1);
		
		public final int displayOrder;
		
		private AbilityType(int orderIn)
		{
			displayOrder = orderIn;
		}
		
		public Text translate() { return Reference.ModInfo.translate("gui", "ability_"+name().toLowerCase()); }
		
		public static int compare(AbilityType a, AbilityType b) { return (int)Math.signum(a.displayOrder - b.displayOrder); }
	}
	
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
		
		@SuppressWarnings("deprecation")
		public static final StringIdentifiable.EnumCodec<AbilitySource> CODEC = StringIdentifiable.createCodec(AbilitySource::values);
		
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