package com.lying.template.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lying.init.VTTypes;
import com.lying.type.Type;
import com.lying.type.TypeSet;

import net.minecraft.util.Identifier;

public class OperationReplaceTypes extends TypesOperation
{
	private final TypeSet targets = new TypeSet();
	
	public OperationReplaceTypes(Identifier nameIn, TypeSet toReplace, Type... typesIn)
	{
		super(nameIn, typesIn);
		targets.addAll(toReplace.contents().toArray(new Type[0]));
	}
	
	public static OperationReplaceTypes of(TypeSet targets, Type... typesIn) { return new OperationReplaceTypes(Operation.REPLACE_TYPES.get().registryName(), targets, typesIn); }
	
	protected JsonObject write(JsonObject data)
	{
		super.write(data);
		
		JsonArray list = new JsonArray();
		for(Type type : targets.contents())
			list.add(type.registryName().toString());
		data.add("Targets", list);
		
		return data;
	}
	
	protected void read(JsonObject data)
	{
		super.read(data);
		targets.clear();
		if(data.has("Targets"))
		{
			JsonArray list = data.getAsJsonArray("Targets");
			for(int i=0; i<list.size(); i++)
			{
				Type type = VTTypes.get(new Identifier(list.get(i).getAsString()));
				if(type != null)
					targets.add(type);
			}
		}
	}
	
	public void applyToTypes(TypeSet typeSet)
	{
		boolean shouldReplace = false;
		for(Type type : targets.contents())
		{
			if(typeSet.contains(type))
			{
				typeSet.remove(type);
				shouldReplace = true;
			}
		}
		
		if(shouldReplace)
			typeSet.addAll(this.types.toArray(new Type[0]));
	}
}
