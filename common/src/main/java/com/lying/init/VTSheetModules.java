package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Supplier;
import com.lying.component.module.AbstractSheetModule;
import com.lying.component.module.ModuleHome;
import com.lying.component.module.ModuleSpecies;
import com.lying.component.module.ModuleTemplates;

import net.minecraft.util.Identifier;

/** Defines modifiers applied to a character sheet's elements */
public class VTSheetModules
{
	private static final Map<Identifier, Supplier<AbstractSheetModule>> MODULES = new HashMap<>();
	
	public static final Supplier<ModuleSpecies> SPECIES	= register("species", ModuleSpecies::new);
	public static final Supplier<ModuleTemplates> TEMPLATES	= register("templates", ModuleTemplates::new);
	public static final Supplier<ModuleHome> HOME	= register("home", ModuleHome::new);
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractSheetModule> Supplier<T> register(String name, Supplier<T> supplier)
	{
		MODULES.put(prefix(name), (Supplier<AbstractSheetModule>)supplier);
		return supplier;
	}
	
	public static void init() { }
	
	public static Collection<Supplier<AbstractSheetModule>> getAll() { return MODULES.values(); }
}