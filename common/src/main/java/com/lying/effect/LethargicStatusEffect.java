package com.lying.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class LethargicStatusEffect extends StatusEffect
{
	public LethargicStatusEffect()
	{
		super(StatusEffectCategory.HARMFUL, 0x084D9C);
		addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "22653B89-116E-49DC-9B6B-9971489B5BE5", -4.0, EntityAttributeModifier.Operation.ADD_VALUE);
		addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15f, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}
}
