package com.lying.client.utility;

import com.lying.client.renderer.VertexConsumerProviderWrapped;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
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
		
		public static final Event<PlayerColorEvent> MODIFY_PLAYER_COLOR_EVENT = EventFactory.createLoop(PlayerColorEvent.class);
		
		@FunctionalInterface
		public interface PlayerColorEvent
		{
			void modifyColor(PlayerEntity player, VertexConsumerProviderWrapped vertexConsumerProvider);
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
