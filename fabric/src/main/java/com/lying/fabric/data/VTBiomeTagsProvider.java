package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class VTBiomeTagsProvider extends TagProvider<Biome>
{
	public VTBiomeTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.BIOME, completableFuture);
	}
	
	protected void configure(WrapperLookup art)
	{
		getOrCreateTagBuilder(VTTags.COLD_BIOMES).add(
			BiomeKeys.FROZEN_PEAKS,
			BiomeKeys.JAGGED_PEAKS,
			BiomeKeys.SNOWY_TAIGA,
			BiomeKeys.SNOWY_SLOPES,
			BiomeKeys.GROVE,
			BiomeKeys.DEEP_FROZEN_OCEAN,
			BiomeKeys.FROZEN_RIVER,
			BiomeKeys.SNOWY_PLAINS,
			BiomeKeys.ICE_SPIKES,
			BiomeKeys.SNOWY_BEACH);
		getOrCreateTagBuilder(VTTags.HOT_BIOMES).add(
			BiomeKeys.DESERT,
			BiomeKeys.SAVANNA,
			BiomeKeys.WINDSWEPT_SAVANNA,
			BiomeKeys.WINDSWEPT_SAVANNA,
			BiomeKeys.BADLANDS,
			BiomeKeys.WOODED_BADLANDS,
			BiomeKeys.ERODED_BADLANDS,
			BiomeKeys.SAVANNA,
			BiomeKeys.SAVANNA_PLATEAU,
			
			BiomeKeys.BASALT_DELTAS,
			BiomeKeys.CRIMSON_FOREST,
			BiomeKeys.NETHER_WASTES,
			BiomeKeys.SOUL_SAND_VALLEY,
			BiomeKeys.WARPED_FOREST);
	}
}
