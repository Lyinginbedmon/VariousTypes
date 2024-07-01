package com.lying.ability;

import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.Ability.AbilitySource;
import com.lying.init.VTAbilities;
import com.lying.utility.LoreDisplay;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.DynamicRegistryManager;
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
	
	/** The variable map name for this specific ability instance */
	public Identifier mapName() { return ability.mapName(this); }
	
	public AbilitySource source() { return source; }
	
	public void setDisplay(LoreDisplay compound) { display = Optional.of(compound); }
	
	public void setDisplayName(Text component)
	{
		display = Optional.of(new LoreDisplay(component, Optional.empty()));
	}
	
	public Text displayName(DynamicRegistryManager world)
	{
		if(display.isPresent())
			return display.get().title();
		return ability.displayName(this);
	}
	
	public NbtCompound writeToNbt(NbtCompound compound, DynamicRegistryManager manager)
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
	
	public JsonObject writeToJson()
	{
		JsonObject json = new JsonObject();
		json.addProperty("Ability", ability.registryName().toString());
		json.addProperty("Source", source.asString());
		if(!display.isEmpty())
			json.addProperty("Display", display.toString());
		if(isReadOnly())
			json.addProperty("ReadOnly", true);
		if(!memory.isEmpty())
			json.addProperty("Memory", memory.toString());
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
	
	public static AbilityInstance readFromJson(JsonObject data, DynamicRegistryManager manager)
	{
		Ability ability = data.has("Ability") ? VTAbilities.get(new Identifier(data.get("Ability").getAsString())) : null;
		if(ability == null)
			return null;
		
		AbilitySource source = AbilitySource.fromName(data.get("Source").getAsString());
		AbilityInstance instance = ability.instance(source);
		
		if(data.has("Display"))
			instance.display = Optional.of(LoreDisplay.fromJson(data.get("Display"), manager));
		
		if(data.has("ReadOnly") && data.get("ReadOnly").getAsBoolean())
			instance.lock();
		
		if(data.has("Memory"))
			instance.memory = tryParseNbt(data.get("Memory"));
		
		return instance;
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
	 * This usually indicates that it is a default model.
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
		clone.memory = this.memory;
		return clone;
	}
}