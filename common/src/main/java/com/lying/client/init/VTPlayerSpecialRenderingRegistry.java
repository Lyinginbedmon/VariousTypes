package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.lying.client.event.RenderEvents;
import com.lying.client.model.GelatinousBipedEntityModel;
import com.lying.client.renderer.PlayerSpecialRenderFunc;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTCosmetics;
import com.lying.reference.Reference;
import com.lying.utility.Cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
public class VTPlayerSpecialRenderingRegistry
{
	private static final Map<Identifier, PlayerSpecialRenderFunc> REGISTRY = new HashMap<>();
	
	public static void register(Identifier registryName, PlayerSpecialRenderFunc func)
	{
		REGISTRY.put(registryName, func);
	}
	
	public static void init()
	{
		register(VTCosmetics.MISC_GHOSTLY.get().registryName(), new PlayerSpecialRenderFunc() 
		{
			public float getAlpha(LivingEntity player, Cosmetic instance) { return 0.5F; }
		});
		register(VTCosmetics.MISC_GELATINOUS.get().registryName(), new PlayerSpecialRenderFunc() 
		{
			private static final MinecraftClient mc = MinecraftClient.getInstance();
			private static final Identifier TEXTURE = Reference.ModInfo.prefix("textures/entity/gelatinous.png");
			private static EntityModel<AbstractClientPlayerEntity> MODEL = null;
			
			public Vector3f getColor(LivingEntity player, Cosmetic instance) { return VTUtilsClient.decimalToVector(instance.color().orElse(0x7BC35C)); }
			public float getAlpha(LivingEntity player, Cosmetic instance) { return instance.alpha().orElse(0.5F); }
			
			public void beforeRender(PlayerEntity player, Cosmetic instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
			{
				if(player.isInvisible() || player.isSpectator())
					return;
				
				if(MODEL == null)
					MODEL = new GelatinousBipedEntityModel<AbstractClientPlayerEntity>(mc.getEntityModelLoader().getModelPart(VTModelLayerParts.PLAYER_SLIME));
				
				Vector3f color = RenderEvents.GET_PLAYER_COLOR_EVENT.invoker().getColor(player);
				PlayerSpecialRenderFunc.renderModel(
					MODEL, 
					TEXTURE, player, matrices, vertexProvider, tickDelta, light, color.x(), color.y(), color.z(), 1F);
			}
		});
	}
	
	public static Vector3f doColorMods(LivingEntity player, Cosmetic instance)
	{
		if(REGISTRY.containsKey(instance.registryName()))
			return REGISTRY.get(instance.registryName()).getColor(player, instance);
		return new Vector3f(1F, 1F, 1F);
	}
	
	public static float doAlphaMods(LivingEntity player, Cosmetic instance)
	{
		if(REGISTRY.containsKey(instance.registryName()))
			return REGISTRY.get(instance.registryName()).getAlpha(player, instance);
		return 1F;
	}
	
	public static void doPreRender(PlayerEntity player, Cosmetic instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
	{
		if(REGISTRY.containsKey(instance.registryName()))
			REGISTRY.get(instance.registryName()).beforeRender(player, instance, matrices, vertexProvider, renderer, yaw, tickDelta, light);
	}
	
	public static void doPostRender(PlayerEntity player, Cosmetic instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
	{
		if(REGISTRY.containsKey(instance.registryName()))
			REGISTRY.get(instance.registryName()).afterRender(player, instance, matrices, vertexProvider, renderer, yaw, tickDelta, light);
	}
}
