package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.network.PlayerFlightInputPacket;

import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends LivingEntityMixin
{
	@Inject(method = "tickMovement()V", at = @At("HEAD"), cancellable = true)
	private void vt$tickFallFlying(final CallbackInfo ci)
	{
		final ClientPlayerEntity living = (ClientPlayerEntity)(Object)this;
		if(isFallFlying() && living.isMainPlayer())
			PlayerFlightInputPacket.send(living.input.movementForward, living.input.movementSideways, living.input.jumping, living.input.sneaking);
	}
}
