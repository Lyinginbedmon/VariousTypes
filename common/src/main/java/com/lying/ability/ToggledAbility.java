package com.lying.ability;

public abstract class ToggledAbility extends ActivatedAbility
{
	public NbtCompound initialiseNBT(NBTCompound data)
	{
		data.putBoolean("IsActive", false);
		return data;
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		NbtCompound mem = instance.memory();
		mem.putBoolean("IsActive", !isActive(instance));
		mem.setMemory(mem);
	}
	
	public boolean isActive(AbilityInstance instance)
	{
		return instance.memory().getBoolean("IsActive");
	}
}