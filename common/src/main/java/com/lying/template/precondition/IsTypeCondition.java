package com.lying.template.precondition;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lying.component.CharacterSheet;
import com.lying.init.VTTypes;
import com.lying.type.Type;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class IsTypeCondition extends Precondition
{
	private final List<Type> types = Lists.newArrayList();
	
	protected IsTypeCondition(Identifier idIn) { super(idIn); }
	
	public static IsTypeCondition of(Type... typesIn)
	{
		IsTypeCondition condition = new IsTypeCondition(Precondition.HAS_TYPE.get().registryName);
		for(Type type : typesIn)
			condition.types.add(type);
		return condition;
	}
	
	public boolean isValidFor(CharacterSheet sheet, LivingEntity owner)
	{
		return types.isEmpty() || types.stream().allMatch(type -> sheet.getTypes().contains(type));
	}
	
	protected JsonObject write(JsonObject data)
	{
		JsonArray list = new JsonArray();
		types.forEach(type -> list.add(type.registryName().toString()));
		data.add("Types", list);
		return data;
	}
	
	protected void read(JsonObject data)
	{
		types.clear();
		JsonArray list = data.getAsJsonArray("Types");
		for(int i=0; i<list.size(); i++)
		{
			Type type = VTTypes.get(new Identifier(list.get(i).getAsString()));
			if(type != null)
				types.add(type);
		}
	}
}
