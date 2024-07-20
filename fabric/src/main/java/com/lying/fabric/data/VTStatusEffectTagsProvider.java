package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class VTStatusEffectTagsProvider extends TagProvider<StatusEffect>
{
	public VTStatusEffectTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.STATUS_EFFECT, completableFuture);
	}
	
	protected void configure(WrapperLookup art)
	{
		getOrCreateTagBuilder(VTTags.POISONS).add(
				StatusEffects.POISON.getKey().get(),
				StatusEffects.NAUSEA.getKey().get());
	}
}
