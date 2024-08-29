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
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class VTItemTagsProvider extends TagProvider<Item>
{
	public VTItemTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.ITEM, completableFuture);
	}

	protected void configure(WrapperLookup lookup)
	{
		// Pregen vanilla tags we need
		getOrCreateTagBuilder(ItemTags.MEAT);
		getOrCreateTagBuilder(ItemTags.FISHES);
		
		registerToTag(VTTags.SILVER_ITEM, 
				Items.IRON_AXE, 
				Items.IRON_SWORD, 
				Items.IRON_PICKAXE, 
				Items.IRON_SHOVEL, 
				Items.IRON_HOE, 
				Items.SHEARS);
		registerToTag(VTTags.FRUIT, 
				Items.APPLE,
				Items.CHORUS_FRUIT,
				Items.ENCHANTED_GOLDEN_APPLE,
				Items.GOLDEN_APPLE,
				Items.GLOW_BERRIES,
				Items.MELON_SLICE,
				Items.PUMPKIN_PIE,
				Items.SWEET_BERRIES);
		registerToTag(VTTags.VEGETABLE, 
				Items.BAKED_POTATO,
				Items.BEETROOT,
				Items.BEETROOT_SOUP,
				Items.CARROT,
				Items.DRIED_KELP,
				Items.GOLDEN_CARROT,
				Items.MUSHROOM_STEW,
				Items.POISONOUS_POTATO,
				Items.POTATO,
				Items.SUSPICIOUS_STEW);
		
		registerToTag(VTTags.VEGETARIAN, Items.CAKE);
		getOrCreateTagBuilder(VTTags.VEGETARIAN).addTag(VTTags.VEGETABLE).addTag(VTTags.FRUIT);
		
		registerToTag(VTTags.CARNIVORE, Items.EGG, Items.SNIFFER_EGG);
		getOrCreateTagBuilder(VTTags.CARNIVORE).addTag(ItemTags.MEAT);
		
		getOrCreateTagBuilder(VTTags.PESCETARIAN).addTag(VTTags.VEGETARIAN).addTag(ItemTags.FISHES);
		
	}
	
	@SuppressWarnings("deprecation")
	private void registerToTag(TagKey<Item> tag, Item... types)
	{
		ProvidedTagBuilder<Item> builder = getOrCreateTagBuilder(tag);
		for(Item type : types)
			builder.add(type.getRegistryEntry().registryKey());
	}
}
