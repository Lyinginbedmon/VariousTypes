package com.lying.init;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.google.common.base.Predicates;
import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.data.ReloadListener;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.species.SpeciesRegistry;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public class VTSpecies implements ReloadListener<Map<Identifier, JsonObject>>
{
	public Identifier getFabricId()
	{
		return new Identifier(Reference.ModInfo.MOD_ID, "species");
	}
	
	public static void init()
	{
		VTSpecies inst = new VTSpecies();
		ReloadListenerRegistry.register(ResourceType.SERVER_DATA, inst, inst.getId());
	}
	
	public CompletableFuture<Map<Identifier, JsonObject>> load(ResourceManager manager, Profiler profiler, Executor executor)
	{
		return CompletableFuture.supplyAsync(() -> 
		{
			VariousTypes.LOGGER.info("Loading VT species...");
			Map<Identifier, JsonObject> objects = new HashMap<>();
			manager.findAllResources(SpeciesRegistry.FILE_PATH, Predicates.alwaysTrue()).forEach((fileName,fileSet) -> 
			{
				for(Resource file : fileSet)
				{
					try
					{
						JsonObject element = JsonHelper.deserialize(SpeciesRegistry.GSON, (Reader)file.getReader(), JsonObject.class);
						// TODO Use filename as RegistryName
						objects.put(new Identifier("vartypes:test"), element);
					}
					catch(Exception e) { VariousTypes.LOGGER.error("Error while loading species "+fileName.toString()); }
				}
			});
			return objects;
		});
	}
	
	public CompletableFuture<Void> apply(Map<Identifier, JsonObject> data, ResourceManager manager, Profiler profiler, Executor executor)
	{
		return CompletableFuture.runAsync(() -> 
		{
			for(Entry<Identifier, JsonObject> prep : data.entrySet())
			{
				Species species = Species.Builder.of(prep.getKey()).build();
				species.readFromJson(prep.getValue(), null);	// FIXME Identify DynamicRegistryManager input value
				SpeciesRegistry.add(species);
			}
		});
	}
	
	public Identifier getId() { return Reference.ModInfo.prefix("species"); }
}
