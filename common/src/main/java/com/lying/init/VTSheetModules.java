package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.lying.component.module.AbstractSheetModule;
import com.lying.component.module.ModuleCustomAbilities;
import com.lying.component.module.ModuleCustomHome;
import com.lying.component.module.ModuleCustomTypes;
import com.lying.component.module.ModuleSpecies;
import com.lying.component.module.ModuleTemplates;

import net.minecraft.util.Identifier;

/** Defines modifiers applied to a character sheet's elements */
public class VTSheetModules
{
	private static final Map<Identifier, Supplier<AbstractSheetModule>> MODULES = new HashMap<>();
	
	public static final Supplier<ModuleSpecies> SPECIES	= register("species", ModuleSpecies::new);
	public static final Supplier<ModuleTemplates> TEMPLATES	= register("templates", ModuleTemplates::new);
	
	public static final Supplier<ModuleCustomHome> HOME	= register("custom_home", ModuleCustomHome::new);
	public static final Supplier<ModuleCustomTypes> TYPES	= register("custom_types", ModuleCustomTypes::new);
	public static final Supplier<ModuleCustomAbilities> ABILITIES	= register("custom_abilities", ModuleCustomAbilities::new);
	
	@SuppressWarnings("unchecked")
	public static <T extends AbstractSheetModule> Supplier<T> register(String name, Supplier<T> supplier)
	{
		MODULES.put(prefix(name), (Supplier<AbstractSheetModule>)supplier);
		return supplier;
	}
	
	public static void init() { }
	
	@Nullable
	public static Supplier<AbstractSheetModule> get(Identifier idIn)
	{
		return MODULES.getOrDefault(idIn, null);
	}
	
	@Nullable
	public static Supplier<AbstractSheetModule> byName(String nameIn)
	{
		for(Entry<Identifier, Supplier<AbstractSheetModule>> entry : MODULES.entrySet())
			if(entry.getKey().getPath().equalsIgnoreCase(nameIn))
				return entry.getValue();
		return null;
	}
	
	public static Collection<Supplier<AbstractSheetModule>> getAll() { return MODULES.values(); }
	
	public static Collection<Identifier> commandIds() { return MODULES.keySet(); }
	
	public static Collection<String> commandNames()
	{
		List<String> names = Lists.newArrayList();
		commandIds().forEach(id -> names.add(id.getPath()));
		return names;
	}
}