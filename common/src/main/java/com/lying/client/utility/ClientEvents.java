package com.lying.client.utility;

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
		public static final Event<PlayerRenderEvent> PLAYER_RENDER_PERMISSION = EventFactory.createEventResult(PlayerRenderEvent.class);
		
		@FunctionalInterface
		public interface PlayerRenderEvent
		{
			EventResult shouldPlayerRender(PlayerEntity player);
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
