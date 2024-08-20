package com.lying.client.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.joml.Vector3f;

import com.google.common.reflect.AbstractInvocationHandler;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class ClientEvents
{
	public static class Rendering
	{
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
	}
}
