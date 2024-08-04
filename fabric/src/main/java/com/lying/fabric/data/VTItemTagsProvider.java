package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class VTItemTagsProvider extends TagProvider<Item>
{
	public VTItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.ITEM, completableFuture);
	}

	@SuppressWarnings("deprecation")
	protected void configure(WrapperLookup lookup)
	{
		getOrCreateTagBuilder(VTTags.SILVER_ITEM).add(
				Items.IRON_AXE.getRegistryEntry().registryKey(), 
				Items.IRON_SWORD.getRegistryEntry().registryKey(), 
				Items.IRON_PICKAXE.getRegistryEntry().registryKey(), 
				Items.IRON_SHOVEL.getRegistryEntry().registryKey(), 
				Items.IRON_HOE.getRegistryEntry().registryKey(), 
				Items.SHEARS.getRegistryEntry().registryKey());
	}
}
