package com.lying.ability;

import net.minecraft.nbt.NbtCompound;

public interface IComplexAbility<T extends Object>
{
	public T memoryToValues(NbtCompound data);
}
