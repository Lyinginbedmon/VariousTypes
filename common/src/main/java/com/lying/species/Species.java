package com.lying.species;

import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;

import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilitySet;
import com.lying.type.Type;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** A configurable package of types and abilities applied to a creature */
public class Species
{
	private final Identifier id;
	private int power;
	private RegistryKey<World> homeDim;
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	
	private Species(Identifier idIn)
	{
		id = idIn;
	}
	
	public Identifier registryName() { return id; }
	
	public int power() { return power; }
	
	public RegistryKey<World> homeDimension() { return homeDim; }
	
	public boolean hasConfiguredHome() { return homeDim != null; }
	
	public TypeSet types() { return types.copy(); }
	
	public AbilitySet abilities() { return abilities.copy(); }
	
	public JsonObject writeToJson()
	{
		JsonObject obj = new JsonObject();
		if(power > 0)
			obj.addProperty("Power", power);
		if(homeDim != null)
			obj.addProperty("Home", homeDim.getValue().toString());
		obj.add("Types", types.writeToJson());
		obj.add("Abilities", abilities.writeToJson());
		
		return obj;
	}
	
	public void readFromJson(JsonObject data)
	{
		clear();
		
		if(data.has("Power"))
			power = data.get("Power").getAsInt();
		
		if(data.has("Home"))
			homeDim = World.CODEC.parse(NbtOps.INSTANCE, NbtString.of(data.get("Home").getAsString())).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		
		if(data.has("Types"))
			types = TypeSet.readFromJson(data.get("Types").getAsJsonArray());
		
		if(data.has("Abilities"))
			abilities = AbilitySet.readFromJson(data.get("Abilities").getAsJsonArray());
	}
	
	public void clear()
	{
		power = 0;
		homeDim = null;
		types.clear();
		abilities.clear();
	}
	
	public static class Builder
	{
		private Identifier id;
		private int power = 0;
		private RegistryKey<World> homeDim = null;
		private TypeSet types = new TypeSet();
		private AbilitySet abilities = new AbilitySet();
		
		protected Builder(Identifier idIn)
		{
			this.id = idIn;
		}
		
		public static Builder of(Identifier idIn)
		{
			return new Builder(idIn);
		}
		
		public Builder power(int powerIn)
		{
			power = Math.max(0, powerIn);
			return this;
		}
		
		public Builder from(RegistryKey<World> world)
		{
			homeDim = world;
			return this;
		}
		
		public Builder setTypes(Type... typesIn)
		{
			types = new TypeSet();
			for(Type type : typesIn)
				types.add(type);
			return this;
		}
		
		public Builder addAbility(Ability inst)
		{
			return addAbility(inst, Consumers.nop());
		}
		
		public Builder addAbility(Ability inst, Consumer<NbtCompound> dataModifier)
		{
			abilities.add(inst.instance(AbilitySource.SPECIES, dataModifier));
			return this;
		}
		
		public Species build()
		{
			Species species = new Species(id);
			species.power = power;
			species.homeDim = homeDim;
			species.types = types;
			species.abilities = abilities;
			return species;
		}
	}
}