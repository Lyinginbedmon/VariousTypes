package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

public class AbilityEffectOnDemand extends ActivatedAbility
{
	private final StatusEffectInstance[] effects;
	
	public AbilityEffectOnDemand(Identifier registryName, Category category, StatusEffectInstance... effectIn)
	{
		super(registryName, category);
		this.effects = effectIn;
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(!owner.getWorld().isClient())
			for(StatusEffectInstance effect : effects)
				owner.addStatusEffect(effect);
	}
}
