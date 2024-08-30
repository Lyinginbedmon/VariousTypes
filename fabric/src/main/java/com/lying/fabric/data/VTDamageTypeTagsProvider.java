package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class VTDamageTypeTagsProvider extends TagProvider<DamageType>
{
	public VTDamageTypeTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.DAMAGE_TYPE, completableFuture);
	}
	
	protected void configure(WrapperLookup art)
	{
		getOrCreateTagBuilder(VTTags.PHYSICAL).add(
				DamageTypes.ARROW,
				DamageTypes.CACTUS,
				DamageTypes.FALL,
				DamageTypes.FLY_INTO_WALL,
				DamageTypes.MOB_ATTACK,
				DamageTypes.PLAYER_ATTACK);
	}
}
