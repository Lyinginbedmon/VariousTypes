package com.lying.type;

import java.util.Optional;

import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.reference.Reference;
import com.lying.utility.LoreDisplay;
import com.mojang.serialization.JsonOps;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DummyType extends Type
{
	public static final Identifier REG_NAME = Reference.ModInfo.prefix("dummy");
	
	protected NbtCompound data = new NbtCompound();
	protected Identifier listID;
	protected Optional<LoreDisplay> customDisplay;
	
	protected DummyType(Identifier nameIn, Identifier listIDIn, Optional<LoreDisplay> displayIn)
	{
		super(nameIn, new AbilitySet(), ActionHandler.NONE, Predicates.alwaysTrue(), new LoreDisplay());
		listID = listIDIn;
		customDisplay = displayIn;
	}
	
	public static DummyType create(Identifier listID, Optional<LoreDisplay> displayIn)
	{
		return new DummyType(REG_NAME, listID, displayIn);
	}
	
	public static DummyType create(Identifier listID, Text displayNameIn)
	{
		return new DummyType(REG_NAME, listID, Optional.of(new LoreDisplay(displayNameIn, Optional.empty())));
	}
	
	public static DummyType create(Identifier listID, Text displayNameIn, Text descIn)
	{
		return new DummyType(REG_NAME, listID, Optional.of(new LoreDisplay(displayNameIn, Optional.of(descIn))));
	}
	
	public Identifier listID() { return listID; }
	
	public NbtElement toNbt()
	{
		return CODEC_OBJ.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static DummyType fromNbt(NbtElement compound)
	{
		return CODEC_OBJ.parse(NbtOps.INSTANCE, compound).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	public Text displayName() { return customDisplay.isPresent() ? customDisplay.get().title() : Text.literal(listID.getPath()); }
	
	public Optional<Text> description() { return customDisplay.isPresent() ? customDisplay.get().description() : Optional.empty(); }
	
	public Optional<LoreDisplay> display() { return customDisplay; }
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		RegistryOps<JsonElement> registryops = manager.getOps(JsonOps.INSTANCE);
		return CODEC_OBJ.encodeStart(registryops, this).getOrThrow();
	}
	
	public static DummyType fromJson(JsonObject obj)
	{
		return CODEC_OBJ.parse(JsonOps.INSTANCE, obj).getOrThrow();
	}
	
	public static class Builder extends Type.Builder
	{
		protected Identifier spoofListID;
		
		private Text displayName;
		private Optional<Text> displayDesc = Optional.empty();
		
		protected Builder(Identifier nameIn)
		{
			super(nameIn, Tier.SUBTYPE);
		}
		
		public static Builder of(Identifier nameIn)
		{
			return of(nameIn, nameIn, Text.translatable("subtype."+nameIn.getNamespace()+"."+nameIn.getPath()));
		}
		
		public static Builder of(Identifier nameIn, Identifier idIn, Text displayIn)
		{
			Builder builder = new Builder(nameIn);
			builder.spoofListID = idIn;
			builder.displayName = displayIn;
			return builder;
		}
		
		public Type build()
		{
			return new DummyType(name, spoofListID, Optional.of(new LoreDisplay(displayName, displayDesc)));
		}
	}
}
