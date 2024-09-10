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
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class RenderEvents
{
	public static final Event<AddFeatureRenderersEvent> ADD_FEATURE_RENDERERS_EVENT = EventFactory.createLoop(AddFeatureRenderersEvent.class);
	
	@FunctionalInterface
	public interface AddFeatureRenderersEvent
	{
		void append(EntityRenderDispatcher dispatcher);
	}
	
	public static final Event<RenderPermissionEvent> PLAYER_RENDER_PERMISSION = EventFactory.createEventResult(RenderPermissionEvent.class);
	
	@FunctionalInterface
	public interface RenderPermissionEvent
	{
		EventResult shouldPlayerRender(PlayerEntity player);
	}
	
	public static final Event<PlayerColorEvent> GET_PLAYER_COLOR_EVENT = EventFactory.of(listeners -> (PlayerColorEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{PlayerColorEvent.class}, new AbstractInvocationHandler()
	{
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			PlayerEntity player = (PlayerEntity)args[0];
			float red = 1F;
			float green = 1F;
			float blue = 1F;
			
			for(PlayerColorEvent listener : listeners)
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
	
	public static final Event<PlayerAlphaEvent> GET_PLAYER_ALPHA_EVENT = EventFactory.of(listeners -> (PlayerAlphaEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{PlayerAlphaEvent.class}, new AbstractInvocationHandler()
	{
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			LivingEntity player = (LivingEntity)args[0];
			float alpha = 1F;
			
			for(PlayerAlphaEvent listener : listeners)
				alpha *= listener.getAlpha(player);
			
			return alpha;
		}
	}));
	
	@FunctionalInterface
	public interface PlayerAlphaEvent
	{
		float getAlpha(LivingEntity player);
	}
	
	public static final Event<RenderPlayerEvent> BEFORE_RENDER_PLAYER_EVENT = EventFactory.createLoop(RenderPlayerEvent.class);
	public static final Event<RenderPlayerEvent> AFTER_RENDER_PLAYER_EVENT = EventFactory.createLoop(RenderPlayerEvent.class);
	
	@FunctionalInterface
	public interface RenderPlayerEvent
	{
		void onRender(PlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer renderer);
	}
	
	public static final Event<WorldRenderEvent> BEFORE_WORLD_RENDER_EVENT = EventFactory.createLoop(WorldRenderEvent.class);
	public static final Event<WorldRenderEvent> AFTER_WORLD_RENDER_EVENT = EventFactory.createLoop(WorldRenderEvent.class);
	
	public interface WorldRenderEvent
	{
		void onRender(float tickDelta, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f1, Matrix4f matrix4f2, VertexConsumerProvider vertexConsumerProvider);
	}
	
	public static final Event<OutlineRenderEvent> BEFORE_OUTLINE_RENDER_EVENT = EventFactory.createLoop(OutlineRenderEvent.class);
	public static final Event<OutlineRenderEvent> AFTER_OUTLINE_RENDER_EVENT = EventFactory.createLoop(OutlineRenderEvent.class);
	
	public interface OutlineRenderEvent
	{
		void onRender(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ);
	}
}