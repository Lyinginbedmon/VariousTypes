package com.lying.type;

import java.util.Optional;

import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

public class DummyType extends Type
{
	public static final Codec<DummyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("ID").forGetter(DummyType::listID), 
			TextCodecs.CODEC.optionalFieldOf("DisplayName").forGetter(DummyType::customName))
				.apply(instance, (a, b)-> DummyType.create(a, b)));
	
	protected NbtCompound data = new NbtCompound();
	protected Identifier listID;
	protected Optional<Text> displayName = null;
	
	protected DummyType(Identifier nameIn, Identifier listIDIn, Optional<Text> displayNameIn)
	{
		super(nameIn, Tier.SUBTYPE, new AbilitySet(), ActionHandler.NONE, Predicates.alwaysTrue());
		listID = listIDIn;
		displayName = displayNameIn;
		data.putString("ID", listID.toString());
	}
	
	public static DummyType create(Identifier listID, Optional<Text> displayName)
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
		if(customName().isPresent())
			return customName().get();
		
		Text name = super.displayName(manager);
		if(data.contains("DisplayName"))
		{
			String s = data.getString("DisplayName");
			try
			{
				name = Text.Serialization.fromJson(s, manager);
			}
			catch (Exception exception) { }
		}
		return name;
	}
	
	public Optional<Text> customName() { return this.displayName; }
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		RegistryOps<JsonElement> registryops = manager.getOps(JsonOps.INSTANCE);
		return CODEC.encodeStart(registryops, this).getOrThrow();
	}
	
	public static DummyType fromJson(JsonObject obj)
	{
		return CODEC.parse(JsonOps.INSTANCE, obj).getOrThrow();
	}
	
	public static class Builder extends Type.Builder
	{
		protected Text display = null;
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
			return new DummyType(name, spoofName, display == null ? Optional.empty() : Optional.of(display));
		}
	}
}
