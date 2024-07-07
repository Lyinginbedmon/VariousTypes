package com.lying.fabric.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.lying.reference.Reference;
import com.lying.species.SpeciesRegistry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput.OutputType;
import net.minecraft.data.DataOutput.PathResolver;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

public class VTSpeciesProvider implements DataProvider
{
	private final PathResolver path;
	
	public VTSpeciesProvider(FabricDataOutput generator)
	{
		this.path = generator.getResolver(OutputType.DATA_PACK, "species/");
	}
	
	public CompletableFuture<?> run(DataWriter dataWriter)
	{
		List<CompletableFuture<?>> futures = Lists.newArrayList();
		SpeciesRegistry.defaultSpecies().forEach(species -> 
			futures.add(DataProvider.writeToPath(dataWriter, species.get().writeToJson(null), this.path.resolveJson(new Identifier(Reference.ModInfo.MOD_ID, species.get().registryName().getPath())))));
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public String getName() { return Reference.ModInfo.MOD_NAME+" default species"; }
}