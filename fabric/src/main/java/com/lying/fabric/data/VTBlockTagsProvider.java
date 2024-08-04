package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class VTBlockTagsProvider extends TagProvider<Block>
{
	public VTBlockTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture)
	{
		super(output, RegistryKeys.BLOCK, registryLookupFuture);
	}
	
	@SuppressWarnings("deprecation")
	protected void configure(WrapperLookup lookup)
	{
		getOrCreateTagBuilder(VTTags.SILVER_BLOCK).add(
				Blocks.IRON_BARS.getRegistryEntry().registryKey(), 
				Blocks.IRON_BLOCK.getRegistryEntry().registryKey(), 
				Blocks.IRON_DOOR.getRegistryEntry().registryKey(), 
				Blocks.IRON_TRAPDOOR.getRegistryEntry().registryKey());
	}

}
