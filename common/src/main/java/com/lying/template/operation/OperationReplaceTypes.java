package com.lying.template.operation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.type.Type;
import com.lying.type.TypeSet;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OperationReplaceTypes extends TypesOperation
{
	private final TypeSet targets = new TypeSet();
	
	public OperationReplaceTypes(Identifier nameIn, TypeSet toReplace, Type... typesIn)
	{
		super(nameIn, typesIn);
		targets.addAll(toReplace.contents().toArray(new Type[0]));
	}
	
	public Text describe(DynamicRegistryManager manager)
	{
		return Text.translatable("operation.vartypes.replace_types", targets.asNameList(), types.asNameList());
	}
	
	public static OperationReplaceTypes of(TypeSet targets, Type... typesIn) { return new OperationReplaceTypes(Operation.REPLACE_TYPES.get().registryName(), targets, typesIn); }
	
	protected JsonObject write(JsonObject data, RegistryWrapper.WrapperLookup manager)
	{
		super.write(data, manager);
		data.add("Targets", targets.writeToJson(manager));
		return data;
	}
	
	protected void read(JsonObject data)
	{
		super.read(data);
		targets.clear();
		if(data.has("Targets"))
		{
			JsonArray list = data.getAsJsonArray("Targets");
			for(JsonElement entry : list.asList())
			{
				Type type = Type.readFromJson(entry);
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
			typeSet.addAll(this.types.contents().toArray(new Type[0]));
	}
}
