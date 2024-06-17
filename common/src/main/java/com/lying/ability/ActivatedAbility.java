package com.lying.ability;

public abstract class ActivatedAbility extends Ability
{
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return true; }
	
	public final void trigger(LivingEntity owner, AbilityInstance instance)
	{
		if(!instance.isReadOnly() && canTrigger(owner, instance))
			activate(owner, instance);
	}
	
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
}