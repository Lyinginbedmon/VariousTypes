package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class ActivatedAbility extends Ability
{
	public ActivatedAbility(Identifier regName)
	{
		super(regName);
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return true; }
	
	public final void trigger(LivingEntity owner, AbilityInstance instance)
	{
		if(!instance.isReadOnly() && canTrigger(owner, instance))
			activate(owner, instance);
	}
	
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
}