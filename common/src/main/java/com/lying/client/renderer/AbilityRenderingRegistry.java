package com.lying.client.renderer;

import java.util.HashMap;
import java.util.Map;

import com.lying.ability.AbilityInstance;
import com.lying.init.VTAbilities;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
public class AbilityRenderingRegistry
{
	private static final Map<Identifier, AbilityRenderFunc> REGISTRY = new HashMap<>();
	
	public static void register(Identifier registryName, AbilityRenderFunc func)
	{
		REGISTRY.put(registryName, func);
	}
	
	public static void init()
	{
		register(VTAbilities.INTANGIBLE.get().registryName(), new AbilityRenderFunc() 
		{
			public void beforeRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
			{
				
			}
			
			public void afterRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
			{
				
			}
		});
	}
	
	public static void doPreRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
	{
		if(REGISTRY.containsKey(instance.ability().registryName()))
			REGISTRY.get(instance.ability().registryName()).beforeRender(player, instance, matrices, vertexProvider, renderer, yaw, tickDelta, light);
	}
	
	public static void doPostRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
	{
		if(REGISTRY.containsKey(instance.ability().registryName()))
			REGISTRY.get(instance.ability().registryName()).afterRender(player, instance, matrices, vertexProvider, renderer, yaw, tickDelta, light);
	}
	
	public static interface AbilityRenderFunc
	{
		void beforeRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light);
		void afterRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light);
	}
}
