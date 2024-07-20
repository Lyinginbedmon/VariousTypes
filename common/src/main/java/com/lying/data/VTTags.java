package com.lying.data;

import com.lying.reference.Reference;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class VTTags
{
	/** A tag for fluids that are considered breathable by creatures that breathe air */
	public static final TagKey<Fluid> AIR = TagKey.of(RegistryKeys.FLUID, new Identifier("air"));
	
	public static final TagKey<StatusEffect> POISONS = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier(Reference.ModInfo.MOD_ID, "poison"));
}
