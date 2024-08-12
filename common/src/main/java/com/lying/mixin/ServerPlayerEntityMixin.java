package com.lying.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.init.VTSheetElements;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.mojang.datafixers.util.Either;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin
{
	@Shadow
	public void setSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage) { }
	
	@Shadow
	public ServerStatHandler getStatHandler() { return null; }
	
	@Inject(method = "trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;", at = @At("HEAD"), cancellable = true)
	private void vt$trySleep(BlockPos pos, final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents sleeping at beds if the player cannot sleep
			if(!sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
			{
				setSpawnPoint(getWorld().getRegistryKey(), pos, getYaw(), false, true);
				ci.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
			}
		});
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	private void vt$serverPlayerTick(final CallbackInfo ci)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			// Prevents phantoms from spawning from players who cannot sleep
			if(!sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				getStatHandler().setStat(player, Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST), 0);
		});
	}
}
