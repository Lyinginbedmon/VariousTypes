package com.lying.type;

import java.util.Optional;

import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.utility.LoreDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DummyType extends Type
{
	public static final Codec<DummyType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("ID").forGetter(DummyType::listID), 
			LoreDisplay.CODEC.fieldOf("Display").forGetter(DummyType::display))
				.apply(instance, (a, b)-> DummyType.create(a, b)));
	
	protected NbtCompound data = new NbtCompound();
	protected Identifier listID;
	protected LoreDisplay customDisplay;
	
	protected DummyType(Identifier nameIn, Identifier listIDIn, LoreDisplay displayIn)
	{
		super(nameIn, new AbilitySet(), ActionHandler.NONE, Predicates.alwaysTrue(), new LoreDisplay());
		listID = listIDIn;
		customDisplay = displayIn;
	}
	
	public static DummyType create(Identifier listID, LoreDisplay displayIn)
	{
		return new DummyType(VTTypes.DUMMY.get().registryName(), listID, displayIn);
	}
	
	public Identifier listID() { return listID; }
	
	protected void write(NbtCompound compound)
	{
		compound.putString("ID", listID.toString());
		compound.put("Display", customDisplay.writeNbt());
	}
	
	public void read(NbtCompound compound)
	{
		listID = new Identifier(data.getString("ID"));
		customDisplay = LoreDisplay.fromNbt(compound.getCompound("Display"));
	}
	
	public Text displayName() { return customDisplay.title(); }
	
	public Optional<Text> description() { return customDisplay.description(); }
	
	public LoreDisplay display() { return customDisplay; }
	
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
		protected Identifier spoofName;
		
		private Text displayName;
		private Optional<Text> displayDesc = Optional.empty();
		
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
			builder.displayName = displayIn;
			return builder;
		}
		
		public Type build()
		{
			return new DummyType(name, spoofName, new LoreDisplay(displayName, displayDesc));
		}
	}
}
