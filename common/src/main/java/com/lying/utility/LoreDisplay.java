package com.lying.utility;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

/**
 * A self-contained object for holding displayed text information.
 * @author Lying
 */
public class LoreDisplay
{
	public static final Codec<LoreDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TextCodecs.CODEC.fieldOf("Title").forGetter(LoreDisplay::title), 
			TextCodecs.CODEC.optionalFieldOf("Description").forGetter(LoreDisplay::description))
				.apply(instance, LoreDisplay::new));
	private final Text title;
	private final Optional<Text> description;
	
	public LoreDisplay()
	{
		this(Text.empty());
	}
	
	public LoreDisplay(Text title)
	{
		this(title, Optional.empty());
	}
	
	public LoreDisplay(Text title, Optional<Text> description)
	{
		this.title = title;
		this.description = description;
	}
	
	public Text title() { return title; }
	
	public Optional<Text> description() { return description; }
	
	public JsonElement toJson(RegistryWrapper.WrapperLookup lookup)
	{
		RegistryOps<JsonElement> registryOps = lookup.getOps(JsonOps.INSTANCE);
		return LoreDisplay.CODEC.encodeStart(registryOps, this).getOrThrow();
	}
	
	public static LoreDisplay fromJson(JsonElement obj)
	{
		return CODEC.parse(JsonOps.INSTANCE, obj).getOrThrow();
	}
	
	public NbtElement writeNbt()
	{
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static LoreDisplay fromNbt(NbtCompound nbt)
	{
		return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
}
