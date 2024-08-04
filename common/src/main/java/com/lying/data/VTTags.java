package com.lying.data;

import static com.lying.reference.Reference.ModInfo.prefix;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class VTTags
{
	/** A tag for fluids that are considered breathable by creatures that breathe air */
	public static final TagKey<Fluid> AIR = TagKey.of(RegistryKeys.FLUID, new Identifier("air"));
	
	public static final TagKey<StatusEffect> POISONS = TagKey.of(RegistryKeys.STATUS_EFFECT, prefix("poison"));
	
	public static final TagKey<Item> SILVER_ITEM = TagKey.of(RegistryKeys.ITEM, prefix("silver"));
	
	public static final TagKey<Block> SILVER_BLOCK = TagKey.of(RegistryKeys.BLOCK, prefix("silver"));
}
