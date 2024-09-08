package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.event.PlayerEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypeFilter;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin extends EntityMixin
{
	@Shadow
	private PlayerEntity target;
	
	@Shadow
	private boolean isMergeable(ExperienceOrbEntity other) { return false; }
	
	@Shadow
	private void merge(ExperienceOrbEntity other) { }
	
	@Inject(method = "expensiveUpdate()V", at = @At("HEAD"), cancellable = true)
	private void vt$expensiveUpdate(final CallbackInfo ci)
	{
		ExperienceOrbEntity orb = (ExperienceOrbEntity)(Object)this;
		ci.cancel();
		
		if(target == null || target.squaredDistanceTo(orb) > 64)
			this.target = getWorld().getClosestPlayer(getX(), getY(), getZ(), 8, entity -> 
			{
				if(entity.getType() != EntityType.PLAYER)
					return false;
				
				PlayerEntity player = (PlayerEntity)entity;
				if(player.isDead() || player.isSpectator())
					return false;
				
				// If we couldn't collide with the player, don't set them as our target in the first place
				return !PlayerEvents.CAN_COLLECT_XP_EVENT.invoker().canPlayerCollectExperienceOrbs(orb, player).isFalse();
			});
		
		if(getWorld() instanceof ServerWorld)
			getWorld().getEntitiesByType(TypeFilter.instanceOf(ExperienceOrbEntity.class), getBoundingBox().expand(0.5), this::isMergeable).forEach(other -> merge(other));
	}
	
	@Inject(method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), cancellable = true)
	private void vt$playerCollision(PlayerEntity player, final CallbackInfo ci)
	{
		if(PlayerEvents.CAN_COLLECT_XP_EVENT.invoker().canPlayerCollectExperienceOrbs((ExperienceOrbEntity)(Object)this, player).isFalse())
			ci.cancel();
	}
}
