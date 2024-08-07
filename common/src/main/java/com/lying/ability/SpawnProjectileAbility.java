package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class SpawnProjectileAbility extends ActivatedAbility
{
	public SpawnProjectileAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	protected final void activate(LivingEntity owner, AbilityInstance instance)
	{
		shootFrom(owner, instance);
	}
	
	protected abstract void shootFrom(LivingEntity owner, AbilityInstance instance);
}
