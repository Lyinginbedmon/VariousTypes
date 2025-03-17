package com.lying.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class StealthStatusEffect extends StatusEffect
{
	public StealthStatusEffect()
	{
		super(StatusEffectCategory.BENEFICIAL, 0xF6F6F6);
		addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "6af9457c-af92-48e4-8fca-adbf9a45a0a3", 0.15F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "d1c6b637-0620-44c1-8cda-9a700868d245", 3.0F, EntityAttributeModifier.Operation.ADD_VALUE);
	}
}
