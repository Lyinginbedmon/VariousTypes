package com.lying.data;

import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class VTTags
{
	/** A tag for fluids that are considered breathable by creatures that breathe air */
	public static final TagKey<Fluid> AIR = TagKey.of(RegistryKeys.FLUID, new Identifier("air"));
}
