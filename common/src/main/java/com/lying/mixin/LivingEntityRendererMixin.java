package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.lying.client.renderer.VertexConsumerProviderWrapped;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin
{
	@ModifyArg(
			method = "render(Lnet/minecraft/client/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", 
			at = @At(
				value = "INVOKE", 
				target = "Lnet/minecraft/client/render/entity/feature/FeatureRenderer;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/Entity;FFFFFF)V"),
			index = 1)
	private VertexConsumerProvider vt$modifyVertexConsumerProvider(VertexConsumerProvider provider)
	{
		return provider instanceof VertexConsumerProviderWrapped ? ((VertexConsumerProviderWrapped)provider).internal() : provider;
	}
}
