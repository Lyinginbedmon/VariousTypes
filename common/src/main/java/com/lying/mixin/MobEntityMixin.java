package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.entity.ai.HatePariahsTask;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;

@Mixin(MobEntity.class)
public class MobEntityMixin
{
	@Shadow
	public GoalSelector targetSelector;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	public void vt$initMobGoals(final CallbackInfo ci)
	{
		this.targetSelector.add(1, new HatePariahsTask((MobEntity)(Object)this, true, false));
	}
}
