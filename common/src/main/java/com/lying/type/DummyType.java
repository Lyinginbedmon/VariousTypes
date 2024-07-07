package com.lying.type;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DummyType extends Type
{
	protected NbtCompound data = new NbtCompound();
	protected Identifier listID;
	protected Text displayName = null;
	
	protected DummyType(Identifier nameIn, Identifier listIDIn, @Nullable Text displayNameIn)
	{
		super(nameIn, Tier.SUBTYPE, new AbilitySet(), ActionHandler.NONE, Predicates.alwaysTrue());
		listID = listIDIn;
		displayName = displayNameIn;
		data.putString("ID", listID.toString());
	}
	
	public static DummyType create(Identifier listID, @Nullable Text displayName)
	{
		return new DummyType(VTTypes.DUMMY.get().registryName(), listID, displayName);
	}
	
	public Identifier listID() { return listID; }
	
	protected void write(NbtCompound compound, DynamicRegistryManager manager)
	{
		compound.putString("ID", listID.toString());
		compound.putString("DisplayName", Text.Serialization.toJsonString(displayName(manager), manager));
	}
	
	public void read(NbtCompound compound)
	{
		listID = new Identifier(data.getString("ID"));
		data = compound;
		displayName = null;
	}
	
	public Text displayName(DynamicRegistryManager manager)
	{
		if(displayName != null)
			return displayName;
		
		Text name = super.displayName(manager);
		if(data.contains("DisplayName"))
		{
			String s = data.getString("DisplayName");
			try
			{
				name = (Text)Text.Serialization.fromJson((String)s, manager);
			}
			catch (Exception exception) { }
		}
		return displayName = name;
	}
	
	public JsonElement writeToJson(DynamicRegistryManager manager)
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("ID", listID.toString());
//		if(displayName != null)
//			obj.add("DisplayName", displayName.toJson(manager));
		return obj;
	}
	
	public static class Builder extends Type.Builder
	{
		protected Text display;
		protected Identifier spoofName;
		
		protected Builder(Identifier nameIn)
		{
			super(nameIn, Tier.SUBTYPE);
		}
		
		public static Builder of(Identifier nameIn)
		{
			return of(nameIn, nameIn, null);
		}
		
		public static Builder of(Identifier nameIn, Identifier idIn, Text displayIn)
		{
			Builder builder = new Builder(nameIn);
			builder.spoofName = idIn;
			builder.display = displayIn;
			return builder;
		}
		
		public Type build()
		{
			return new DummyType(name, spoofName, display);
		}
	}
}
