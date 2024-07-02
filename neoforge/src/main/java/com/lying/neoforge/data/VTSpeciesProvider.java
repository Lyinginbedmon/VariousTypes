package com.lying.neoforge.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.lying.species.SpeciesRegistry;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class VTSpeciesProvider implements DataProvider
{
	private final DataOutput path;
	
	public VTSpeciesProvider(DataGenerator generator, ExistingFileHelper fileHelper)
	{
		this.path = generator.getPackOutput(SpeciesRegistry.FILE_PATH+"/");
	}
	
	public CompletableFuture<?> run(DataWriter dataWriter)
	{
		List<CompletableFuture<?>> futures = Lists.newArrayList();
//		SpeciesRegistry.defaultSpecies().forEach(supplier -> futures.add(DataProvider.writeToPath(dataWriter, supplier.get().writeToJson(), this.path.resolvePath(supplier.get().registryName()))));
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public String getName()
	{
		return "Various Types default species";
	}
}
