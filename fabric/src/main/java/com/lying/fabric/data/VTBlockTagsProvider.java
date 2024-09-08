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
		registerToTag(VTTags.UNPHASEABLE, 
				Blocks.BARRIER, Blocks.BEDROCK, 
				Blocks.END_PORTAL, Blocks.END_PORTAL_FRAME, Blocks.END_GATEWAY, 
				Blocks.COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK, 
				Blocks.STRUCTURE_BLOCK, Blocks.JIGSAW, Blocks.LIGHT, 
				Blocks.REINFORCED_DEEPSLATE);
	}
	
	@SuppressWarnings("deprecation")
	private void registerToTag(TagKey<Block> tag, Block... types)
	{
		ProvidedTagBuilder<Block> builder = getOrCreateTagBuilder(tag);
		for(Block type : types)
			builder.add(type.getRegistryEntry().registryKey());
	}

}
