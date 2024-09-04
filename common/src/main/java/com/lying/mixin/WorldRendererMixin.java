package com.lying.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.client.utility.ClientEvents;

import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin
{
	@Shadow
	BufferBuilderStorage bufferBuilders;
	
	@Inject(method = "render(FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At("HEAD"))
	private void renderHead(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, final CallbackInfo ci)
	{
		VertexConsumerProvider.Immediate vertexConsumerProvider = this.bufferBuilders.getEntityVertexConsumers();
		ClientEvents.Rendering.BEFORE_WORLD_RENDER_EVENT.invoker().onRender(tickDelta, camera, gameRenderer, lightmapTextureManager, matrix4f, matrix4f2, vertexConsumerProvider);
	}
	
	@Inject(method = "render(FJZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V", at = @At("TAIL"))
	private void renderTail(float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, final CallbackInfo ci)
	{
		VertexConsumerProvider.Immediate vertexConsumerProvider = this.bufferBuilders.getEntityVertexConsumers();
		ClientEvents.Rendering.AFTER_WORLD_RENDER_EVENT.invoker().onRender(tickDelta, camera, gameRenderer, lightmapTextureManager, matrix4f, matrix4f2, vertexConsumerProvider);
	}
}
