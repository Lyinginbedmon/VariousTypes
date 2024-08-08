package com.lying.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public abstract class SpawnProjectileAbility extends ActivatedAbility
{
	public SpawnProjectileAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	protected final void activate(LivingEntity owner, AbilityInstance instance)
	{
		shootFrom(owner, Vec3d.fromPolar(owner.getPitch(), owner.getHeadYaw()), instance);
	}
	
	protected abstract void shootFrom(LivingEntity owner, Vec3d direction, AbilityInstance instance);
}
