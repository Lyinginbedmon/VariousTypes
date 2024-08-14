package com.lying.ability;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;

public interface IStatusEffectSpoofAbility
{
	/** Returns true if this ability is actively spoofing the given status effect */
	public boolean isAffectingStatus(RegistryEntry<StatusEffect> effect, AbilityInstance inst);
	
	/** Returns the spoofed status effect provided by this ability */
	@Nullable
	public StatusEffectInstance getSpoofed(RegistryEntry<StatusEffect> effect, AbilityInstance inst);
}
