package com.lying.template;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.util.Identifier;

public class TemplateRegistry
{
	private static final Map<Identifier, Template> TEMPLATES = new HashMap<>();
	
	private static final Map<Identifier, Supplier<Template>> DEFAULTS = new HashMap<>();
	
	
	
	private static Supplier<Template> register(Identifier name, Supplier<Template> template)
	{
		DEFAULTS.put(name, template);
		return template;
	}
	
	@Nullable
	public static Template get(Identifier registryName)
	{
		return TEMPLATES.getOrDefault(registryName, null);
	}
}