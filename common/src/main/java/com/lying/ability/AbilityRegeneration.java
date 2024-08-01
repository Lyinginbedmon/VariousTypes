package com.lying.ability;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Identifier;

public class AbilityRegeneration extends AbilityFastHeal
{
	public AbilityRegeneration(Identifier registryName, Category category)
	{
		super(registryName, category);
	}
	
	public void registerEventHandlers()
	{
		super.registerEventHandlers();
		EntityEvent.LIVING_HURT.register((LivingEntity entity, DamageSource source, float amount) -> 
		{
			
			return EventResult.pass();
		});
	}
}
