package com.lying.client.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.google.common.reflect.AbstractInvocationHandler;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class RenderEvents
{
	public static final Event<RenderEvents.RenderPermissionEvent> PLAYER_RENDER_PERMISSION = EventFactory.createEventResult(RenderEvents.RenderPermissionEvent.class);
	
	@FunctionalInterface
	public interface RenderPermissionEvent
	{
		EventResult shouldPlayerRender(PlayerEntity player);
	}
	
	public static final Event<RenderEvents.PlayerColorEvent> GET_PLAYER_COLOR_EVENT = EventFactory.of(listeners -> (RenderEvents.PlayerColorEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{RenderEvents.PlayerColorEvent.class}, new AbstractInvocationHandler()
	{
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			PlayerEntity player = (PlayerEntity)args[0];
			float red = 1F;
			float green = 1F;
			float blue = 1F;
			
			for(RenderEvents.PlayerColorEvent listener : listeners)
			{
				Vector3f result = listener.getColor(player);
				red *= result.x();
				green *= result.y();
				blue = result.z();
			}
			return new Vector3f(red, green, blue);
		}
	}));
	
	@FunctionalInterface
	public interface PlayerColorEvent
	{
		Vector3f getColor(LivingEntity player);
	}
	
	public static final Event<RenderEvents.PlayerAlphaEvent> GET_PLAYER_ALPHA_EVENT = EventFactory.of(listeners -> (RenderEvents.PlayerAlphaEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{RenderEvents.PlayerAlphaEvent.class}, new AbstractInvocationHandler()
	{
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			LivingEntity player = (LivingEntity)args[0];
			float alpha = 1F;
			
			for(RenderEvents.PlayerAlphaEvent listener : listeners)
				alpha *= listener.getAlpha(player);
			
			return alpha;
		}
	}));
	
	@FunctionalInterface
	public interface PlayerAlphaEvent
	{
		float getAlpha(LivingEntity player);
	}
	
	public static final Event<RenderEvents.RenderPlayerEvent> BEFORE_RENDER_PLAYER_EVENT = EventFactory.createLoop(RenderEvents.RenderPlayerEvent.class);
	public static final Event<RenderEvents.RenderPlayerEvent> AFTER_RENDER_PLAYER_EVENT = EventFactory.createLoop(RenderEvents.RenderPlayerEvent.class);
	
	@FunctionalInterface
	public interface RenderPlayerEvent
	{
		void onRender(PlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer renderer);
	}
	
	public static final Event<RenderEvents.WorldRenderEvent> BEFORE_WORLD_RENDER_EVENT = EventFactory.createLoop(RenderEvents.WorldRenderEvent.class);
	public static final Event<RenderEvents.WorldRenderEvent> AFTER_WORLD_RENDER_EVENT = EventFactory.createLoop(RenderEvents.WorldRenderEvent.class);
	
	public interface WorldRenderEvent
	{
		void onRender(float tickDelta, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f1, Matrix4f matrix4f2, VertexConsumerProvider vertexConsumerProvider);
	}
	
	public static final Event<RenderEvents.OutlineRenderEvent> BEFORE_OUTLINE_RENDER_EVENT = EventFactory.createLoop(RenderEvents.OutlineRenderEvent.class);
	public static final Event<RenderEvents.OutlineRenderEvent> AFTER_OUTLINE_RENDER_EVENT = EventFactory.createLoop(RenderEvents.OutlineRenderEvent.class);
	
	public interface OutlineRenderEvent
	{
		void onRender(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ);
	}
}