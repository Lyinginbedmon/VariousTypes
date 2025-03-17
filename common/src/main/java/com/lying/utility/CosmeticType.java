package com.lying.utility;

import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public record CosmeticType(Identifier registryName, int capacity, Predicate<LivingEntity> hideCondition)
{
	public boolean shouldBeHidden(LivingEntity living) { return hideCondition.test(living); }
	
	public boolean uncapped() { return capacity < 0; }
}
