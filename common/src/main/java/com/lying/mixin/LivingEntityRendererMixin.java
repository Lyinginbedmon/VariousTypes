package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin
{
	@ModifyArg(
			method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", 
			at = @At(
				value = "INVOKE", 
				target = "Lnet/minecraft/client/render/entity/PlayerEntityRenderer;render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"),
			index = 4)
	private VertexConsumerProvider vt$modifyVertexConsumerProvider(VertexConsumerProvider provider)
	{
		System.out.println("Modifying vertex consumer provider argument from PlayerEntityRenderer");
		return provider;
	}
}
