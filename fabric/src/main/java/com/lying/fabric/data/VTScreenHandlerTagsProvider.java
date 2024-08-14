package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.data.VTTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;

public class VTScreenHandlerTagsProvider extends TagProvider<ScreenHandlerType<?>>
{
	public VTScreenHandlerTagsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture)
	{
		super(output, RegistryKeys.SCREEN_HANDLER, completableFuture);
	}
	
	protected void configure(WrapperLookup art)
	{
		registerToTag(VTTags.CRAFTING_MENU,
				ScreenHandlerType.CRAFTING,
				ScreenHandlerType.CRAFTER_3X3,
				ScreenHandlerType.BREWING_STAND,
				ScreenHandlerType.CARTOGRAPHY_TABLE,
				ScreenHandlerType.STONECUTTER,
				ScreenHandlerType.GRINDSTONE,
				ScreenHandlerType.ANVIL,
				ScreenHandlerType.SMITHING,
				ScreenHandlerType.BEACON,
				ScreenHandlerType.ENCHANTMENT,
				ScreenHandlerType.LOOM);
	}
	
	private void registerToTag(TagKey<ScreenHandlerType<?>> tag, ScreenHandlerType<?>... types)
	{
		ProvidedTagBuilder<ScreenHandlerType<?>> builder = getOrCreateTagBuilder(tag);
		for(ScreenHandlerType<?> type : types)
			builder.add(Registries.SCREEN_HANDLER.getKey(type).get());
	}
}
