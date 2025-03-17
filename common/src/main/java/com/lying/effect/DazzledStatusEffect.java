package com.lying.effect;

import com.lying.reference.Reference;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;

public class DazzledStatusEffect extends StatusEffect
{
	public static final Identifier VIGNETTE = Reference.ModInfo.prefix("textures/misc/vignette_dazzled.png");
	
	public DazzledStatusEffect()
	{
		super(StatusEffectCategory.HARMFUL, 0xFFFEBC);
		addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "3967a2c0-2554-45ae-b500-fa59ee720b8a", -0.15F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "3bcc2171-81b1-4166-8199-fba959573057", -0.1F, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		fadeTicks(100);
	}
}
