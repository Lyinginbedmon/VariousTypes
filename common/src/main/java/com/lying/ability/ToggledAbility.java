package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public abstract class ToggledAbility extends ActivatedAbility
{
	public ToggledAbility(Identifier regName)
	{
		super(regName);
	}
	
	public NbtCompound initialiseNBT(NbtCompound data)
	{
		data.putBoolean("IsActive", false);
		return data;
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		NbtCompound mem = instance.memory();
		mem.putBoolean("IsActive", !isActive(instance));
		instance.setMemory(mem);
	}
	
	public boolean isActive(AbilityInstance instance)
	{
		return instance.memory().getBoolean("IsActive");
	}
}