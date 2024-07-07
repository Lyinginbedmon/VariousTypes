package com.lying.template.operation;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public abstract class AbilityOperation extends ConfigurableOperation
{
	protected AbilityOperation(Identifier nameIn)
	{
		super(nameIn);
	}

	public abstract void applyToAbilities(AbilitySet abilitySet);
	
	public static class Add extends AbilityOperation
	{
		private AbilityInstance ability;
		
		public Add(Identifier nameIn, AbilityInstance inst)
		{
			super(nameIn);
			ability = inst.copy();
		}
		
		public static Add of(AbilityInstance inst) { return new Add(Operation.ADD_ABILITY.get().registryName(), inst); }
		
		public void applyToAbilities(AbilitySet abilitySet) { abilitySet.add(ability.copy()); }
		
		protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
		{
			JsonObject ab = ability.writeToJson();
			ab.remove("Source");
			data.add("Ability", ab);
			return data;
		}
		
		protected void read(JsonObject data, DynamicRegistryManager manager)
		{
			JsonObject info = data.getAsJsonObject("Ability");
			info.addProperty("Source", AbilitySource.TEMPLATE.asString());
			ability = AbilityInstance.readFromJson(data.getAsJsonObject("Ability"), manager);
		}
	}
	
	public static class Remove extends AbilityOperation
	{
		private final List<Identifier> mapNames = Lists.newArrayList();
		
		public Remove(Identifier nameIn, Identifier... mapNameIn)
		{
			super(nameIn);
			for(Identifier name : mapNameIn)
				mapNames.add(name);
		}
		
		public static Remove of(Identifier... mapNames) { return new Remove(Operation.REMOVE_ABILITY.get().registryName(), mapNames); }
		
		public void applyToAbilities(AbilitySet abilitySet) { mapNames.forEach(name -> abilitySet.remove(name)); }
		
		protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
		{
			JsonArray list = new JsonArray();
			mapNames.forEach(name -> list.add(name.toString()));
			data.add("Abilities", list);
			return data;
		}
		
		protected void read(JsonObject data, DynamicRegistryManager manager)
		{
			mapNames.clear();
			JsonArray list = data.getAsJsonArray("Abilities");
			for(int i=0; i<list.size(); i++)
				mapNames.add(new Identifier(list.get(i).getAsString()));
		}
	}
	
	public static class RemoveAll extends AbilityOperation
	{
		private final List<Identifier> registryNames = Lists.newArrayList();
		
		public RemoveAll(Identifier nameIn, Identifier... mapNameIn)
		{
			super(nameIn);
			for(Identifier name : mapNameIn)
				registryNames.add(name);
		}
		
		public static RemoveAll of(Identifier... mapNames) { return new RemoveAll(Operation.REMOVE_ALL_ABILITY.get().registryName(), mapNames); }
		
		public void applyToAbilities(AbilitySet abilitySet) { registryNames.forEach(name -> abilitySet.removeAll(name)); }
		
		protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
		{
			JsonArray list = new JsonArray();
			registryNames.forEach(name -> list.add(name.toString()));
			data.add("Abilities", list);
			return data;
		}

		protected void read(JsonObject data, DynamicRegistryManager manager)
		{
			registryNames.clear();
			JsonArray list = data.getAsJsonArray("Abilities");
			for(int i=0; i<list.size(); i++)
				registryNames.add(new Identifier(list.get(i).getAsString()));
		}
	}
}
