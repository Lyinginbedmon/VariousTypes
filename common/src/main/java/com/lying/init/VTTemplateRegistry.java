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
import com.lying.template.Template;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;

public class VTTemplateRegistry implements ReloadListener<Map<Identifier, JsonObject>>
{
	private static VTTemplateRegistry INSTANCE;
	
	public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public static final String FILE_PATH = "template";
	public static final Pattern REGEX = Pattern.compile("[ \\w-]+?(?=\\.)");
	
	private final Map<Identifier, Template> TEMPLATES = new HashMap<>();
	
	public static VTTemplateRegistry instance() { return INSTANCE; }
	
	public void clear() { TEMPLATES.clear(); }
	
	private void add(Template template)
	{
		TEMPLATES.put(template.registryName(), template);
		VariousTypes.LOGGER.info(" #  Loaded template {}", template.registryName().toString());
	}
	
	public Optional<Template> get(Identifier registryName)
	{
		return TEMPLATES.containsKey(registryName) ? Optional.of(TEMPLATES.get(registryName)) : Optional.empty();
	}
	
	public Collection<Template> getAll() { return TEMPLATES.values(); }
	
	public Collection<Identifier> getAllIDs() { return TEMPLATES.keySet(); }
	
	public Identifier getId() { return Reference.ModInfo.prefix("template"); }
	
	public static void init()
	{
		INSTANCE = new VTTemplateRegistry();
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
				catch(Exception e) { VariousTypes.LOGGER.error("Error while loading template {}", fileName.toString()); }
			});
			return objects;
		});
	}
	
	public CompletableFuture<Void> apply(Map<Identifier, JsonObject> data, ResourceManager manager, Profiler profiler, Executor executor)
	{
		return CompletableFuture.runAsync(() -> 
		{
			VariousTypes.LOGGER.info(" # Loading VT templates...");
			clear();
			for(Entry<Identifier, JsonObject> prep : data.entrySet())
				add(Template.readFromJson(prep.getKey(), prep.getValue()));
		});
	}
}
