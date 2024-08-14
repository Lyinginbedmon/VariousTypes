package com.lying.ability;

import com.lying.reference.Reference;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class AbilityNightVision extends ToggledAbility implements IStatusEffectSpoofAbility
{
	public AbilityNightVision(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public boolean isAffectingStatus(RegistryEntry<StatusEffect> effect, AbilityInstance inst) { return effect == StatusEffects.NIGHT_VISION && isActive(inst); }
	
	public StatusEffectInstance getSpoofed(RegistryEntry<StatusEffect> effect, AbilityInstance inst)
	{
		return isActive(inst) ? new StatusEffectInstance(StatusEffects.NIGHT_VISION, Reference.Values.TICKS_PER_MINUTE, 0, true, false) : null;
	}
}
