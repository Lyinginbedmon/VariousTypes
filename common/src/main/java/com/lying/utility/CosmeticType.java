package com.lying.utility;

import java.util.function.Predicate;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public record CosmeticType(Identifier regName, int capacity, Predicate<LivingEntity> hideIf)
{
	public boolean shouldBeHidden(LivingEntity living) { return hideIf.test(living); }
	
	public boolean uncapped() { return capacity < 0; }
}
