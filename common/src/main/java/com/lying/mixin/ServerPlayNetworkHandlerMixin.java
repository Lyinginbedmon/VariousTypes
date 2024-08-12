package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.utility.ServerEvents;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin
{
	@Shadow
	public ServerPlayerEntity player;
	
	@Inject(method = "onPlayerInput(Lnet/minecraft/network/packet/c2s/play/PlayerInputC2SPacket;)V", at = @At("TAIL"))
	private void vt$onPlayerInput(PlayerInputC2SPacket input, final CallbackInfo ci)
	{
		System.out.println("Invoking player input event");
		ServerEvents.LivingEvents.PLAYER_INPUT_EVENT.invoker().onPlayerInput(player, input.getForward(), input.getSideways(), input.isJumping(), input.isSneaking());
	}
}
