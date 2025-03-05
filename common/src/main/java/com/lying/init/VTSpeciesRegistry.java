package com.lying.init;

import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Predicates;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.data.ReloadListener;
import com.lying.reference.Reference;
import com.lying.species.Species;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public class VTSpeciesRegistry implements ReloadListener<Map<Identifier, JsonObject>>
{
	public static final Identifier DEFAULT_SPECIES = new Identifier("vartypes:human");
	private static VTSpeciesRegistry INSTANCE;
	
	public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public static final String FILE_PATH = "species";
	public static final Pattern REGEX = Pattern.compile("[ \\w-]+?(?=\\.)");
	
	private final Map<Identifier, Species> SPECIES = new HashMap<>();
	
	public static VTSpeciesRegistry instance() { return INSTANCE; }
	
	public void clear() { SPECIES.clear(); }
	
	private void add(Species species)
	{
		SPECIES.put(species.registryName(), species);
		VariousTypes.LOGGER.info(" #  Loaded species "+species.registryName().toString());
	}
	
	public Optional<Species> get(Identifier registryName)
	{
		return SPECIES.containsKey(registryName) ? Optional.of(SPECIES.get(registryName)) : Optional.empty();
	}
	
	public Collection<Species> getAll() { return SPECIES.values(); }
	
	public Collection<Identifier> getAllIDs() { return SPECIES.keySet(); }
	
	public Identifier getId() { return Reference.ModInfo.prefix("species"); }
	
	public static void init()
	{
		INSTANCE = new VTSpeciesRegistry();
		ReloadListenerRegistry.register(ResourceType.SERVER_DATA, INSTANCE, INSTANCE.getId());
	}
	
	public CompletableFuture<Map<Identifier, JsonObject>> load(ResourceManager manager, Profiler profiler, Executor executor)
	{
		return CompletableFuture.supplyAsync(() -> 
		{
			Map<Identifier, JsonObject> objects = new HashMap<>();
			manager.findAllResources(FILE_PATH, Predicates.alwaysTrue()).forEach((fileName,fileSet) -> 
			{
				// The datapack source this species came from
				String namespace = fileName.getNamespace();
				
				// The filename of this species, to be used as the registry name
				String name = fileName.getPath();
				Matcher match = REGEX.matcher(name);
				if(!match.find())
					return;
				
				name = match.group().replaceAll(" ", "_");
				Identifier registryID = new Identifier(namespace, name);
				Resource file = fileSet.getFirst();
				try
				{
					objects.put(registryID, JsonHelper.deserialize(GSON, (Reader)file.getReader(), JsonObject.class));
				}
				catch(Exception e) { VariousTypes.LOGGER.error("Error while loading species "+fileName.toString()); }
			});
			return objects;
		});
	}
	
	public CompletableFuture<Void> apply(Map<Identifier, JsonObject> data, ResourceManager manager, Profiler profiler, Executor executor)
	{
		return CompletableFuture.runAsync(() -> 
		{
			VariousTypes.LOGGER.info(" # Loading VT species...");
			clear();
			for(Entry<Identifier, JsonObject> prep : data.entrySet())
				add(Species.readFromJson(prep.getKey(), prep.getValue()));
		});
	}
}
