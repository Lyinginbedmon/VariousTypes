package com.lying.template;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.template.operation.Operation;
import com.lying.template.precondition.Precondition;
import com.lying.type.TypeSet;
import com.lying.utility.LoreDisplay;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** A configurable modification applied to a creature, on top of its species */
public class Template
{
	private final Identifier registryName;
	private final LoreDisplay display;
	
	private final int power;
	private final List<Precondition> preconditions = Lists.newArrayList();
	private final List<Operation> operations = Lists.newArrayList();
	
	private Template(Identifier nameIn, int powerIn, LoreDisplay displayIn, List<Precondition> conditionsIn, List<Operation> operationsIn)
	{
		registryName = nameIn;
		power = powerIn;
		display = displayIn;
		preconditions.addAll(conditionsIn);
		operations.addAll(operationsIn);
	}
	
	public Identifier registryName() { return registryName; }
	
	public Text displayName() { return display.title(); }
	
	public List<Precondition> preconditions() { return preconditions; }
	
	public List<Operation> operations() { return operations; }
	
	public int power() { return power; }
	
	public LoreDisplay display() { return display; }
	
	public boolean validFor(CharacterSheet sheet, LivingEntity owner) { return preconditions.stream().allMatch(condition -> condition.isValidFor(sheet, owner)); }
	
	public void applyTypeOperations(TypeSet typeSet) { operations.forEach(operation -> operation.applyToTypes(typeSet)); }
	
	public void applyAbilityOperations(AbilitySet abilitySet) { operations.forEach(operation -> operation.applyToAbilities(abilitySet)); }
	
	public JsonObject writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		JsonObject data = new JsonObject();
		data.add("Display", display.toJson(manager));
		
		if(power > 0)
			data.addProperty("Power", power);
		
		if(!preconditions.isEmpty())
		{
			JsonArray list = new JsonArray();
			preconditions.forEach(condition -> list.add(condition.writeToJson(manager)));
			data.add("Conditions", list);
		}
		
		if(!operations.isEmpty())
		{
			JsonArray list = new JsonArray();
			operations.forEach(operation -> list.add(operation.writeToJson(manager)));
			data.add("Operations", list);
		}
		
		return data;
	}
	
	public static Template readFromJson(Identifier registryName, JsonObject data)
	{
		Builder builder = Builder.of(registryName);
		
		LoreDisplay display = LoreDisplay.fromJson(data.get("Display"));
		builder.display(display.title());
		if(display.description().isPresent())
			builder.description(display.description().get());
		
		if(data.has("Power"))
			builder.power(data.get("Power").getAsInt());
		
		if(data.has("Conditions"))
		{
			JsonArray list = data.getAsJsonArray("Conditions");
			for(JsonElement element : list.asList())
			{
				Precondition condition = Precondition.readFromJson(element);
				if(condition != null)
					builder.condition(condition);
			}
		}
		
		if(data.has("Operations"))
		{
			JsonArray list = data.getAsJsonArray("Operations");
			for(JsonElement element : list.asList())
			{
				Operation operation = Operation.readFromJson(element);
				if(operation != null)
					builder.operation(operation);
			}
		}
		
		return builder.build();
	}
	
	public static class Builder
	{
		private final Identifier name;
		
		private Text displayName;
		private Optional<Text> displayDesc = Optional.empty();
		
		private int power = 0;
		private final List<Precondition> preconditions = Lists.newArrayList();
		private final List<Operation> operations = Lists.newArrayList();
		
		private Builder(Identifier nameIn)
		{
			name = nameIn;
			displayName = Text.translatable("template."+nameIn.getNamespace()+"."+nameIn.getPath());
		}
		
		public static Builder of(Identifier nameIn) { return new Builder(nameIn); }
		
		public Builder power(int powerIn)
		{
			power = powerIn;
			return this;
		}
		
		public Builder display(Text nameIn)
		{
			displayName = nameIn;
			return this;
		}
		
		public Builder description(Text descIn)
		{
			displayDesc = Optional.of(descIn);
			return this;
		}
		
		public Builder condition(Precondition conIn)
		{
			preconditions.add(conIn);
			return this;
		}
		
		public Builder operation(Operation opIn)
		{
			operations.add(opIn);
			return this;
		}
		
		public Template build()
		{
			return new Template(name, power, new LoreDisplay(displayName, displayDesc), preconditions, operations);
		}
	}
}