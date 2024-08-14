package com.lying.ability;

import com.lying.reference.Reference;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class AbilityInvisibility extends Ability implements IStatusEffectSpoofAbility
{
	public AbilityInvisibility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public boolean isAffectingStatus(RegistryEntry<StatusEffect> effect, AbilityInstance inst) { return effect == StatusEffects.INVISIBILITY; }
	
	public StatusEffectInstance getSpoofed(RegistryEntry<StatusEffect> effect, AbilityInstance inst)
	{
		return new StatusEffectInstance(StatusEffects.INVISIBILITY, Reference.Values.TICKS_PER_MINUTE, 0, true, false);
	}
}
