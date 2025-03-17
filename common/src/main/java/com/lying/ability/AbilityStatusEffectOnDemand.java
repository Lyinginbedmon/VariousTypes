package com.lying.ability;

import java.util.List;
import java.util.function.Function;

import com.google.common.collect.Lists;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

public class AbilityStatusEffectOnDemand extends ActivatedAbility
{
	private final List<Function<DynamicRegistryManager, StatusEffectInstance>> effects = Lists.newArrayList();
	
	public AbilityStatusEffectOnDemand(Identifier registryName, Category category, Function<DynamicRegistryManager, StatusEffectInstance> effectIn)
	{
		this(registryName, category, List.of(effectIn));
	}
	
	public AbilityStatusEffectOnDemand(Identifier registryName, Category category, List<Function<DynamicRegistryManager, StatusEffectInstance>> effectIn)
	{
		super(registryName, category);
		this.effects.addAll(effectIn);
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(!owner.getWorld().isClient())
			for(Function<DynamicRegistryManager, StatusEffectInstance> func : effects)
			{
				StatusEffectInstance effect = func.apply(owner.getRegistryManager());
				owner.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles()));
			}
	}
}
