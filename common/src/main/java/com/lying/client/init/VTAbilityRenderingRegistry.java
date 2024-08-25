package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.lying.ability.AbilityInstance;
import com.lying.client.model.GelatinousBipedEntityModel;
import com.lying.client.renderer.AbilityRenderFunc;
import com.lying.client.utility.ClientEvents;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTAbilities;
import com.lying.reference.Reference;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
public class VTAbilityRenderingRegistry
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
			public float getAlpha(LivingEntity player, AbilityInstance instance) { return 0.5F; }
		});
		register(VTAbilities.GELATINOUS.get().registryName(), new AbilityRenderFunc() 
		{
			private static final MinecraftClient mc = MinecraftClient.getInstance();
			private static final Identifier TEXTURE = Reference.ModInfo.prefix("textures/entity/gelatinous.png");
			private static EntityModel<AbstractClientPlayerEntity> MODEL = null;
			
			public Vector3f getColor(LivingEntity player, AbilityInstance instance) { return VTUtilsClient.decimalToVector(getColor(instance.memory())); }
			public float getAlpha(LivingEntity player, AbilityInstance instance) { return instance.memory().contains("Opacity", NbtElement.FLOAT_TYPE) ? instance.memory().getFloat("Opacity") : 0.5F; }
			
			public void beforeRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
			{
				if(player.isInvisible() || player.isSpectator())
					return;
				
				if(MODEL == null)
					MODEL = new GelatinousBipedEntityModel<AbstractClientPlayerEntity>(mc.getEntityModelLoader().getModelPart(VTModelLayerParts.PLAYER_SLIME));
				
				Vector3f color = ClientEvents.Rendering.GET_PLAYER_COLOR_EVENT.invoker().getColor(player);
				AbilityRenderFunc.renderModel(
					MODEL, 
					TEXTURE, player, matrices, vertexProvider, tickDelta, light, color.x(), color.y(), color.z(), 1F);
			}
			
			private static int getColor(NbtCompound memory) { return memory.contains("Color", NbtElement.INT_TYPE) ? memory.getInt("Color") : 0x7BC35C; }
		});
	}
	
	public static Vector3f doColorMods(LivingEntity player, AbilityInstance instance)
	{
		if(REGISTRY.containsKey(instance.ability().registryName()))
			return REGISTRY.get(instance.ability().registryName()).getColor(player, instance);
		return new Vector3f(1F, 1F, 1F);
	}
	
	public static float doAlphaMods(LivingEntity player, AbilityInstance instance)
	{
		if(REGISTRY.containsKey(instance.ability().registryName()))
			return REGISTRY.get(instance.ability().registryName()).getAlpha(player, instance);
		return 1F;
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
}
