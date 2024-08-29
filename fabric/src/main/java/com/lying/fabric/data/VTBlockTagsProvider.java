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
import net.minecraft.registry.tag.TagKey;

public class VTBlockTagsProvider extends TagProvider<Block>
{
	public VTBlockTagsProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture)
	{
		super(output, RegistryKeys.BLOCK, registryLookupFuture);
	}
	
	protected void configure(WrapperLookup lookup)
	{
		registerToTag(VTTags.WEBS, Blocks.COBWEB);
		registerToTag(VTTags.SILVER_BLOCK,
				Blocks.IRON_BARS, 
				Blocks.IRON_BLOCK, 
				Blocks.IRON_DOOR, 
				Blocks.IRON_TRAPDOOR);
	}
	
	@SuppressWarnings("deprecation")
	private void registerToTag(TagKey<Block> tag, Block... types)
	{
		ProvidedTagBuilder<Block> builder = getOrCreateTagBuilder(tag);
		for(Block type : types)
			builder.add(type.getRegistryEntry().registryKey());
	}

}
