package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.client.utility.ClientEvents;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin extends LivingEntityRendererMixin
{
	@Inject(method = "render(Lnet/minecraft/entity/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
	private void vt$renderPlayerHead(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, final CallbackInfo ci)
	{
		if(ClientEvents.Rendering.PLAYER_RENDER_PERMISSION.invoker().shouldPlayerRender(player).isFalse())
		{
			ci.cancel();
			return;
		}
		
		ClientEvents.Rendering.BEFORE_RENDER_PLAYER_EVENT.invoker().onRender(player, yaw, tickDelta, matrices, vertexConsumerProvider, light, (PlayerEntityRenderer)(Object)this);
	}
	
	@Inject(method = "render(Lnet/minecraft/entity/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	private void vt$renderPlayerTail(AbstractClientPlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, final CallbackInfo ci)
	{
		ClientEvents.Rendering.AFTER_RENDER_PLAYER_EVENT.invoker().onRender(player, yaw, tickDelta, matrices, vertexConsumerProvider, light, (PlayerEntityRenderer)(Object)this);
	}
	
	
}
