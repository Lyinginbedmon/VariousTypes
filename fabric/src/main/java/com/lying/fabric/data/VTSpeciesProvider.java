package com.lying.fabric.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.lying.species.SpeciesRegistry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput.OutputType;
import net.minecraft.data.DataOutput.PathResolver;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;

public class VTSpeciesProvider implements DataProvider
{
	private final PathResolver path;
	
	public VTSpeciesProvider(FabricDataOutput generator)
	{
		this.path = generator.getResolver(OutputType.DATA_PACK, SpeciesRegistry.FILE_PATH+"/");
	}
	
	public CompletableFuture<?> run(DataWriter dataWriter)
	{
		List<CompletableFuture<?>> futures = Lists.newArrayList();
//		SpeciesRegistry.defaultSpecies().forEach(supplier -> futures.add(DataProvider.writeToPath(dataWriter, supplier.get().writeToJson(), this.path.resolveJson(supplier.get().registryName()))));
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public String getName()
	{
		return "Various Types default species";
	}
}
