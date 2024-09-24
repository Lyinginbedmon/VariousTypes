package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.event.MiscEvents;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.Vibrations;

@Mixin(ServerWorld.class)
public class WorldMixin
{
	@Inject(method = "emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V", at = @At("TAIL"))
	private void vt$emitGameEvent(RegistryEntry<GameEvent> entry, Vec3d pos, GameEvent.Emitter emitter, final CallbackInfo ci)
	{
		ServerWorld world = (ServerWorld)(Object)this;
		if(!world.isClient() && causesVibration(entry))
		{
			BlockPos block = BlockPos.ofFloored(pos.getX(), pos.getY(), pos.getZ());
			MiscEvents.ON_VIBRATION_EVENT.invoker().onVibration(entry, emitter.sourceEntity() == null ? Optional.empty() : Optional.of(emitter.sourceEntity()), Optional.of(block), world);
		}
	}
	
	private static boolean causesVibration(RegistryEntry<GameEvent> entry)
	{
		return Vibrations.getFrequency(entry) > 0;
	}
}
