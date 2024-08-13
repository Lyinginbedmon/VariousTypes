package com.lying.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class VTDataGenerators implements DataGeneratorEntrypoint
{
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(VTSpeciesProvider::new);
		pack.addProvider(VTTemplatesProvider::new);
		pack.addProvider(VTStatusEffectTagsProvider::new);
		pack.addProvider(VTBlockTagsProvider::new);
		pack.addProvider(VTItemTagsProvider::new);
		pack.addProvider(VTRecipeProvider::new);
	}
}
