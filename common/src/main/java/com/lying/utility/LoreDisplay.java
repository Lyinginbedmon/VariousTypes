package com.lying.utility;

import java.util.Optional;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;

public class LoreDisplay
{
	public static final Codec<LoreDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TextCodecs.CODEC.fieldOf("Title").forGetter(LoreDisplay::title), 
			TextCodecs.CODEC.optionalFieldOf("Description").forGetter(LoreDisplay::description))
				.apply(instance, LoreDisplay::new));
	private final Text title;
	private final Optional<Text> description;
	
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
	
	public static LoreDisplay fromJson(JsonElement obj, RegistryWrapper.WrapperLookup manager)
	{
		return CODEC.decode(manager.getOps(JsonOps.INSTANCE), obj).getOrThrow().getFirst();
	}
}
