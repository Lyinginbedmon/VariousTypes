package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ToggledAbility extends ActivatedAbility
{
	public ToggledAbility(Identifier regName)
	{
		super(regName);
	}
	
	public AbilityType type() { return AbilityType.TOGGLED; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		NbtCompound mem = instance.memory();
		mem.putBoolean("IsActive", !isActive(instance));
		instance.setMemory(mem);
		
		if(isActive(instance))
			onActivation(owner, instance);
		else
			onDeactivation(owner, instance);
	}
	
	public boolean isActive(AbilityInstance instance)
	{
		return instance.memory().getBoolean("IsActive");
	}
	
	protected void onActivation(LivingEntity owner, AbilityInstance instance) { }
	
	protected void onDeactivation(LivingEntity owner, AbilityInstance instance) { }
}