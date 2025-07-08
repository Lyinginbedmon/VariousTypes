package com.lying.template.operation;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
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
		private List<AbilityInstance> abilities = Lists.newArrayList();
		
		public Add(Identifier nameIn, AbilityInstance inst)
		{
			super(nameIn);
			add(inst);
		}
		
		public Text describe(DynamicRegistryManager manager)
		{
			MutableText name = null;
			if(abilities.isEmpty())
				name = Text.empty();
			else
			{
				name = abilities.get(0).displayName(manager).copy();
				if(abilities.size() > 1)
					for(int i=1; i<abilities.size(); i++)
						name = name.append(", ").append(abilities.get(i).displayName(manager));
			}
			
			return Text.translatable("operation.vartypes.add_ability", name);
		}
		
		private static AbilityInstance conform(@NotNull AbilityInstance inst)
		{
			if(inst == null)
				throw new NullPointerException();
			
			AbilityInstance ability = inst.ability().instance(AbilitySource.TEMPLATE);
			ability.copyDetails(inst);
			return ability;
		}
		
		public void add(@Nullable Ability ability)
		{
			if(ability == null)
				throw new NullPointerException();
			add(ability.instance());
		}
		
		public void add(@Nullable AbilityInstance inst)
		{
			if(inst != null)
				abilities.add(conform(inst));
		}
		
		public static Add of(Ability... ability)
		{
			if(ability.length == 0)
				throw new NullPointerException();
			
			Add op = new Add(Operation.ADD_ABILITY.get().registryName(), ability[0].instance());
			for(int i=1; i<ability.length; i++)
				op.add(ability[i]);
			return op;
		}
		
		public static Add of(AbilityInstance... inst)
		{
			if(inst.length == 0)
				throw new NullPointerException();
			
			Add op = new Add(Operation.ADD_ABILITY.get().registryName(), inst[0]);
			for(int i=1; i<inst.length; i++)
				op.add(inst[i]);
			return op;
		}
		
		public void applyToAbilities(AbilitySet abilitySet) { abilities.forEach(inst -> abilitySet.add(inst.copy())); }
		
		protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
		{
			if(abilities.size() == 1)
				data.add("Ability", abilities.get(0).writeToJson(manager, true));
			else
			{
				JsonArray list = new JsonArray();
				abilities.forEach(inst -> list.add(inst.writeToJson(manager, true)));
				data.add("Ability", list);
			}
			return data;
		}
		
		protected void read(JsonObject data)
		{
			abilities.clear();
			JsonElement ability = data.get("Ability");
			if(ability.isJsonArray())
			{
				JsonArray list = ability.getAsJsonArray();
				list.forEach(entry -> add(conform(AbilityInstance.readFromJson(entry, AbilitySource.TEMPLATE))));
			}
			else
				add(conform(AbilityInstance.readFromJson(ability, AbilitySource.TEMPLATE)));
		}
	}
	
	/** Removes any ability with the given map name(s) */
	public static class Remove extends AbilityOperation
	{
		protected final List<Identifier> mapNames = Lists.newArrayList();
		
		public Remove(Identifier nameIn, Identifier... mapNameIn)
		{
			super(nameIn);
			for(Identifier name : mapNameIn)
				mapNames.add(name);
		}
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.remove_ability", namesToDisplay());
		}
		
		protected Text namesToDisplay()
		{
			MutableText name = null;
			if(mapNames.isEmpty())
				name = Text.empty();
			else
			{
				name = Text.literal(mapNames.get(0).toString()).copy();
				if(mapNames.size() > 1)
					for(int i=1; i<mapNames.size(); i++)
						name = name.append(", ").append(Text.literal(mapNames.get(i).toString()));
			}
			
			return name;
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
		
		protected void read(JsonObject data)
		{
			mapNames.clear();
			JsonArray list = data.getAsJsonArray("Abilities");
			for(int i=0; i<list.size(); i++)
				mapNames.add(new Identifier(list.get(i).getAsString()));
		}
	}
	
	/** Removes all abilities with the given registry name(s) */
	public static class RemoveAll extends AbilityOperation.Remove
	{
		public RemoveAll(Identifier nameIn, Identifier... regNameIn)
		{
			super(nameIn, regNameIn);
		}
		
		public Text describe(DynamicRegistryManager manager)
		{
			return Text.translatable("operation.vartypes.remove_all_ability", namesToDisplay());
		}
		
		public static RemoveAll of(Identifier... mapNames) { return new RemoveAll(Operation.REMOVE_ALL_ABILITY.get().registryName(), mapNames); }
		
		public void applyToAbilities(AbilitySet abilitySet) { mapNames.forEach(name -> abilitySet.removeAll(name)); }
	}
}
