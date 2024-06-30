package com.lying.fabric.data;

import com.lying.VariousTypes;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class VTDataGenerators implements DataGeneratorEntrypoint
{
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
	{
		VariousTypes.LOGGER.info(" # Building Various Types datapack # ");
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(VTSpeciesProvider::new);
	}
}
