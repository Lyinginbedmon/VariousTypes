package com.lying.ability;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilitySource;
import com.lying.init.VTAbilities;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** A unique instance of a given ability */
public class AbilityInstance
{
	private final Ability ability;
	private final AbilitySource source;
	private boolean locked = false;
	private NbtCompound display = new NbtCompound();
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
	
	public Text displayName(World world)
	{
		if(display.contains("CustomName", NbtElement.COMPOUND_TYPE))
		{
			String s = display.getString("CustomName");
			try
			{
				return (Text)Text.Serialization.fromJson((String)s, world.getRegistryManager());
			}
			catch (Exception exception)
			{
				VariousTypes.LOGGER.warn("Failed to parse abiilty custom name {}", (Object)s, (Object)exception);
			}
		}
		return ability.name(this);
	}
	
	/** The variable map name for this specific ability instance */
	public Identifier mapName() { return ability.mapName(this); }
	
	public AbilitySource source() { return source; }
	
	public void setDisplay(NbtCompound compound) { display = compound; }
	
	public void clearDisplay() { display = new NbtCompound(); }
	
	public void setDisplayName(Text component, World world) { display.putString("Name", Text.Serialization.toJsonString((Text)component, world.getRegistryManager())); }
	
	public NbtCompound writeToNbt(NbtCompound compound)
	{
		compound.putString("Ability", ability.registryName().toString());
		compound.putString("Source", source.asString());
		
		if(!display.isEmpty())
			compound.put("Display", display);
		
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
		
		if(data.contains("Display", NbtElement.COMPOUND_TYPE))
			instance.setDisplay(data.getCompound("Display"));
		
		if(data.contains("ReadOnly") && data.getBoolean("ReadOnly"))
			instance.lock();
		
		if(data.contains("Memory", NbtElement.COMPOUND_TYPE))
			instance.setMemory(data.getCompound("Memory"));
		
		return instance;
	}
	
	public static AbilityInstance readFromJson(JsonObject data)
	{
		Ability ability = data.has("Ability") ? VTAbilities.get(new Identifier(data.get("Ability").getAsString())) : null;
		if(ability == null)
			return null;
		
		AbilitySource source = AbilitySource.fromName(data.get("Source").getAsString());
		AbilityInstance instance = ability.instance(source);
		
		if(data.has("Display"))
			instance.display = tryParseNbt(data.get("Display"));
		
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