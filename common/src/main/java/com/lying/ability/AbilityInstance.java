package com.lying.ability;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.Ability.AbilityType;
import com.lying.init.VTAbilities;
import com.lying.utility.LoreDisplay;
import com.lying.utility.VTUtils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** A unique instance of a given ability */
public class AbilityInstance
{
	private final Ability ability;
	private final AbilitySource source;
	private boolean locked = false;
	private Optional<LoreDisplay> display = Optional.empty();
	private NbtCompound memory;
	
	public AbilityInstance(Ability abilityIn, AbilitySource sourceIn)
	{
		ability = abilityIn;
		source = sourceIn;
		memory = ability.initialiseNBT(new NbtCompound());
	}
	
	public AbilityInstance(Ability abilityIn, AbilitySource sourceIn, Consumer<NbtCompound> dataModifier)
	{
		this(abilityIn, sourceIn);
		dataModifier.accept(memory);
	}
	
	/** Returns a comparator for sorting abilities alphabetically by their display name */
	public static Comparator<AbilityInstance> sortFunc(DynamicRegistryManager manager)
	{
		return (a, b) -> {
			int comparison;
			return (comparison = AbilityType.compare(a.ability().type(), b.ability().type())) == 0 ? VTUtils.stringComparator(a.displayName(manager).getString(), b.displayName(manager).getString()) : comparison;
			};
	}
	
	/** The variable map name for this specific ability instance */
	public Identifier mapName() { return ability.mapName(this); }
	
	public AbilitySource source() { return source; }
	
	public void setDisplay(LoreDisplay displayIn) { display = Optional.of(displayIn); }
	
	public Optional<LoreDisplay> display() { return display; }
	
	public void setDisplayName(Text component)
	{
		display = Optional.of(new LoreDisplay(component, display.isPresent() ? display.get().description() : Optional.empty()));
	}
	
	public Text displayName(DynamicRegistryManager world)
	{
		if(display.isPresent())
			return display.get().title();
		return ability.displayName(this);
	}
	
	public Optional<Text> description(DynamicRegistryManager world)
	{
		if(display.isPresent())
		{
			LoreDisplay lore = display.get();
			if(lore.description().isPresent())
				return lore.description();
		}
		return ability.description(this);
	}
	
	public NbtCompound writeToNbt(NbtCompound compound, WrapperLookup manager)
	{
		compound.putString("Ability", ability.registryName().toString());
		compound.putString("Source", source.asString());
		
		if(display.isPresent())
			compound.putString("Display", display.get().toJson(manager).toString());
		
		if(isReadOnly())
			compound.putBoolean("ReadOnly", true);
		
		if(!memory().isEmpty())
			compound.put("Memory", memory);
		
		return compound;
	}
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager, boolean vitalOnly)
	{
		JsonObject json = new JsonObject();
		json.addProperty("Name", ability.registryName().toString());
		if(!vitalOnly)
		{
			json.addProperty("Source", source.asString());
			if(!display.isEmpty())
				json.add("Display", display.get().toJson(manager));
			if(isReadOnly())
				json.addProperty("ReadOnly", true);
		}
		if(!memory.isEmpty())
			json.addProperty("Memory", memory.toString());
		
		if(json.size() == 1)
			return new JsonPrimitive(ability.registryName().toString());
		return json;
	}
	
	@Nullable
	public static AbilityInstance readFromNbt(NbtCompound data)
	{
		Ability ability = data.contains("Ability", NbtElement.STRING_TYPE) ? VTAbilities.get(new Identifier(data.getString("Ability"))) : null;
		if(ability == null)
			return null;
		
		AbilitySource source = AbilitySource.fromName(data.getString("Source"));
		AbilityInstance instance = ability.instance(source);
		
//		if(data.contains("Display", NbtElement.COMPOUND_TYPE))
//			instance.setDisplay(new JsonObject(data.getString("Display")));	// FIXME Identify nbt -> JSON to load LoreDisplay
		
		if(data.contains("ReadOnly") && data.getBoolean("ReadOnly"))
			instance.lock();
		
		if(data.contains("Memory", NbtElement.COMPOUND_TYPE))
			instance.setMemory(data.getCompound("Memory"));
		
		return instance;
	}
	
	public static AbilityInstance readFromJson(JsonElement element, AbilitySource forceSource)
	{
		if(element.isJsonObject())
		{
			JsonObject data = element.getAsJsonObject();
			if(!data.has("Name"))
				throw new NullPointerException();
			
			Ability ability = VTAbilities.get(new Identifier(data.get("Name").getAsString()));
			
			AbilitySource source = forceSource == null ? AbilitySource.fromName(data.get("Source").getAsString()) : forceSource;
			AbilityInstance instance = ability.instance(source);
			
			if(data.has("Display"))
				instance.display = Optional.of(LoreDisplay.fromJson(data.get("Display")));
			
			if(data.has("ReadOnly") && data.get("ReadOnly").getAsBoolean())
				instance.lock();
			
			if(data.has("Memory"))
				instance.memory = tryParseNbt(data.get("Memory"));
			
			return instance;
		}
		else
		{
			Ability ability = VTAbilities.get(new Identifier(element.getAsString()));
			AbilitySource source = forceSource == null ? AbilitySource.MISC : forceSource;
			return ability.instance(source);
		}
	}
	
	private static NbtCompound tryParseNbt(JsonElement element)
	{
		try
		{
			return StringNbtReader.parse(element.getAsString());
		}
		catch(Exception e) { return new NbtCompound(); }
	}
	
	/**
	 * Returns true if this ability instance is immutable.<br>
	 * This usually indicates that it is a default model not to be directly utilised.
	 */
	public boolean isReadOnly() { return locked; }
	
	public AbilityInstance lock()
	{
		locked = true;
		return this;
	}
	
	public Ability ability() { return ability; }
	
	public NbtCompound memory() { return memory; }
	
	public void setMemory(NbtCompound dataIn)
	{
		if(isReadOnly()) return;
		memory = dataIn;
	}
	
	/** Returns a modifiable duplicate of this instance */
	public final AbilityInstance copy()
	{
		AbilityInstance clone = new AbilityInstance(ability, source);
		clone.setMemory(memory());
		return clone;
	}
}