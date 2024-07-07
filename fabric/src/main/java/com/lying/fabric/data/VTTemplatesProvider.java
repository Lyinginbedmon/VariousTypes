package com.lying.fabric.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Lists;
import com.lying.reference.Reference;
import com.lying.template.TemplateRegistry;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput.OutputType;
import net.minecraft.data.DataOutput.PathResolver;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

public class VTTemplatesProvider implements DataProvider
{
	private final PathResolver path;
	
	public VTTemplatesProvider(FabricDataOutput generator)
	{
		this.path = generator.getResolver(OutputType.DATA_PACK, "template/");
	}
	
	public CompletableFuture<?> run(DataWriter dataWriter)
	{
		List<CompletableFuture<?>> futures = Lists.newArrayList();
		TemplateRegistry.defaultTemplates().forEach(template -> 
			futures.add(DataProvider.writeToPath(dataWriter, template.get().writeToJson(null), this.path.resolveJson(new Identifier(Reference.ModInfo.MOD_ID, template.get().registryName().getPath())))));
		return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
	}
	
	public String getName() { return Reference.ModInfo.MOD_NAME+" default templates"; }
}