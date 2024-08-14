package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionHandler;
import com.lying.init.VTSheetElements;
import com.lying.type.Action;
import com.lying.utility.ServerEvents;
import com.mojang.datafixers.util.Either;

import dev.architectury.event.EventResult;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntityMixin
{
	@Shadow
	protected boolean canChangeIntoPose(EntityPose pose) { return false; }
	
	@Shadow
	public void startFallFlying() { }
	
	@Shadow
	public PlayerInventory getInventory() { return null; }
	
	@Inject(method = "canFoodHeal()Z", at = @At("TAIL"), cancellable = true)
	private void vt$canFoodHeal(final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents natural health regeneration if the player does not have that action
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.REGEN.get()))
				ci.setReturnValue(false);
		});
	}
	
	@Inject(method = "canResetTimeBySleeping()Z", at = @At("HEAD"), cancellable = true)
	private void vt$canResetTime(final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			/**
			 * Automatically includes players that can't sleep in the list of sleeping players.
			 * This prevents situations where a server population wants to skip night but can't due to unsleeping players.
			 */
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				ci.setReturnValue(true);
		});
	}
	
	@Inject(method = "trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;", at = @At("HEAD"), cancellable = true)
	private void vt$trySleep(BlockPos pos, final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents natural health regeneration if the player does not have that action
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				ci.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
		});
	}
	
	@Inject(method = "updatePose()V", at = @At("HEAD"), cancellable = true)
	private void vt$updatePose(final CallbackInfo ci)
	{
		if(!canChangeIntoPose(EntityPose.SWIMMING))
			return;
		
		PlayerEntity player = (PlayerEntity)(Object)this;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
			sheet.<Optional<EntityPose>>elementValue(VTSheetElements.SPECIAL_POSE).ifPresent(pose -> 
			{
				if(getPose() != pose)
					setPose(pose);
				ci.cancel();
			}));
	}
	
	@Inject(method = "checkFallFlying()Z", at = @At("HEAD"), cancellable = true)
	private void vt$checkFallFlying(final CallbackInfoReturnable<Boolean> ci)
	{
		PlayerEntity player = (PlayerEntity)(Object)this;
		if(ServerEvents.LivingEvents.CAN_FLY_EVENT.invoker().canCurrentlyFly(player) == EventResult.interruptFalse())
		{
			ci.setReturnValue(false);
			return;
		}
		
		if(!isOnGround() && !isFallFlying() && !isTouchingWater() && !hasStatusEffect(StatusEffects.LEVITATION) && ServerEvents.LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.invoker().passesElytraCheck(player, false) == EventResult.interruptTrue())
		{
			startFallFlying();
			ci.setReturnValue(true);
		}
	}
}
