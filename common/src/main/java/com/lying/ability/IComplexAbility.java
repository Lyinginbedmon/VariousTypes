package com.lying.ability;

import net.minecraft.nbt.NbtCompound;

/** Utility interface for abilities with lots of configurable values */
public interface IComplexAbility<T extends Object>
{
	public T memoryToValues(NbtCompound data);
}
