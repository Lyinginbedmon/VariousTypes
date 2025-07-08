package com.lying.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RageStatusEffect extends StatusEffect
{
	public RageStatusEffect()
	{
		super(StatusEffectCategory.BENEFICIAL, 0xFC3D03);
		addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9", 6.0, EntityAttributeModifier.Operation.ADD_VALUE);
		addAttributeModifier(EntityAttributes.GENERIC_MAX_ABSORPTION, "EAE29CF0-701E-4ED6-883A-96F798F3DAB5", 36.0, EntityAttributeModifier.Operation.ADD_VALUE);
	}
	
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier)
    {
        return entity.getAbsorptionAmount() > 0.0f || entity.getWorld().isClient();
    }
    
    public boolean canApplyUpdateEffect(int duration, int amplifier) { return true; }
    
    public void onApplied(LivingEntity entity, int amplifier)
    {
        super.onApplied(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(36 * (1 + amplifier))));
    }
}
