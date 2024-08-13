package com.lying.fabric.data;

import java.util.concurrent.CompletableFuture;

import com.lying.init.VTItems;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

public class VTRecipeProvider extends FabricRecipeProvider
{
	public VTRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture)
	{
		super(output, registriesFuture);
	}
	
	public void generate(RecipeExporter exporter)
	{
		ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, VTItems.SHEET.get())
			.input(Items.ENCHANTED_BOOK).input(Items.BLUE_DYE).input(Items.EXPERIENCE_BOTTLE)
			.criterion("has_enchanted_book", FabricRecipeProvider.conditionsFromItem(Items.ENCHANTED_BOOK))
			.criterion("has_blue_dye", FabricRecipeProvider.conditionsFromItem(Items.BLUE_DYE))
			.criterion("has_xp_bottle", FabricRecipeProvider.conditionsFromItem(Items.EXPERIENCE_BOTTLE)).offerTo(exporter);
	}
}
