package com.lying.client.renderer;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector3f;

import com.lying.ability.AbilityInstance;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.GelatinousPlayerEntityModel;
import com.lying.client.utility.ClientEvents;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTAbilities;
import com.lying.reference.Reference;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

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
			public float getAlpha(PlayerEntity player, AbilityInstance instance) { return 0.5F; }
		});
		register(VTAbilities.GELATINOUS.get().registryName(), new AbilityRenderFunc() 
		{
			private static final MinecraftClient mc = MinecraftClient.getInstance();
			private static final Identifier TEXTURE = Reference.ModInfo.prefix("textures/entity/gelatinous.png");
			private static EntityModel<AbstractClientPlayerEntity> MODEL = null;
			
			public Vector3f getColor(PlayerEntity player, AbilityInstance instance) { return VTUtilsClient.decimalToVector(getColor(instance.memory())); }
			public float getAlpha(PlayerEntity player, AbilityInstance instance) { return instance.memory().contains("Opacity", NbtElement.FLOAT_TYPE) ? instance.memory().getFloat("Opacity") : 0.5F; }
			
			public void beforeRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light)
			{
				if(player.isInvisible() || player.isSpectator())
					return;
				
				if(MODEL == null)
					MODEL = new GelatinousPlayerEntityModel<AbstractClientPlayerEntity>(mc.getEntityModelLoader().getModelPart(VTModelLayerParts.PLAYER_SLIME));
				
				Vector3f color = ClientEvents.Rendering.GET_PLAYER_COLOR_EVENT.invoker().getColor(player);
				AbilityRenderFunc.renderModel(
					MODEL, 
					TEXTURE, player, matrices, vertexProvider, tickDelta, light, color.x(), color.y(), color.z(), 1F);
			}
			
			private static int getColor(NbtCompound memory) { return memory.contains("Color", NbtElement.INT_TYPE) ? memory.getInt("Color") : 0x7BC35C; }
		});
	}
	
	public static Vector3f doColorMods(PlayerEntity player, AbilityInstance instance)
	{
		if(REGISTRY.containsKey(instance.ability().registryName()))
			return REGISTRY.get(instance.ability().registryName()).getColor(player, instance);
		return new Vector3f(1F, 1F, 1F);
	}
	
	public static float doAlphaMods(PlayerEntity player, AbilityInstance instance)
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
	
	public static interface AbilityRenderFunc
	{
		default Vector3f getColor(PlayerEntity player, AbilityInstance instance) { return new Vector3f(1F, 1F, 1F); }
		default float getAlpha(PlayerEntity player, AbilityInstance instance) { return 1F; }
		default void beforeRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light) { }
		default void afterRender(PlayerEntity player, AbilityInstance instance, MatrixStack matrices, VertexConsumerProvider vertexProvider, PlayerEntityRenderer renderer, float yaw, float tickDelta, int light) { }
		
		public static void renderModel(EntityModel<AbstractClientPlayerEntity> model, Identifier texture, PlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexProvider, float tickDelta, int light, float red, float green, float blue, float alpha)
		{
			VertexConsumerProvider provider = vertexProvider instanceof VertexConsumerProviderWrapped ? ((VertexConsumerProviderWrapped)vertexProvider).internal() : vertexProvider;
			VertexConsumer consumer = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(texture));
			
			model.handSwingProgress = player.getHandSwingProgress(tickDelta);
			model.riding = player.hasVehicle();
			model.child = player.isBaby();
			
			if(model instanceof BipedEntityModel)
				((BipedEntityModel<?>)model).sneaking = player.isInSneakingPose();
			
			float n = (float)player.age + tickDelta;
			float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
			float headYaw = MathHelper.lerpAngleDegrees(tickDelta, player.prevHeadYaw, player.headYaw);
			float k = MathHelper.wrapDegrees(headYaw - bodyYaw);
			float scale = player.getScale();
			
			matrices.push();
				matrices.scale(scale, scale, scale);
				setupTransforms(player, matrices, n, bodyYaw, tickDelta, scale);
				matrices.scale(-1F, -1F, 1F);
				float g = 0.9375F;
				matrices.scale(g, g, g);
				matrices.translate(0F, -1.501F, 0F);
				float m = MathHelper.lerp(tickDelta, player.prevPitch, player.getPitch());
				if(LivingEntityRenderer.shouldFlipUpsideDown(player))
				{
					m *= -1F;
					k *= -1F;
				}
				float p = 0F;
				float o = 0F;
				if(!player.hasVehicle() && player.isAlive())
				{
					o = Math.min(1F, player.limbAnimator.getSpeed(tickDelta));
					p = player.limbAnimator.getPos(tickDelta) * (player.isBaby() ? 3F : 1F);
				}
				model.animateModel((AbstractClientPlayerEntity)player, p, o, tickDelta);
				model.setAngles((AbstractClientPlayerEntity)player, p, o, n, k, m);
				model.render(matrices, consumer, light, LivingEntityRenderer.getOverlay(player, 0F), red, green, blue, alpha);
			matrices.pop();
		}
		
		private static void setupTransforms(PlayerEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) 
		{
			if(entity.isFrozen())
				bodyYaw += (float)(Math.cos((double)((LivingEntity)entity).age * 3.25) * Math.PI * (double)0.4f);
			
			if(!((Entity)entity).isInPose(EntityPose.SLEEPING))
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - bodyYaw));
			
			if(((LivingEntity)entity).deathTime > 0)
			{
				float f = ((float)((LivingEntity)entity).deathTime + tickDelta - 1.0f) / 20.0f * 1.6f;
				if((f = MathHelper.sqrt(f)) > 1.0f)
					f = 1.0f;
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(f * 90F));
			}
			else if(((LivingEntity)entity).isUsingRiptide())
			{
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0f - ((Entity)entity).getPitch()));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(((float)((LivingEntity)entity).age + tickDelta) * -75.0f));
			}
			else if(((Entity)entity).isInPose(EntityPose.SLEEPING))
			{
				Direction direction = ((LivingEntity)entity).getSleepingDirection();
				float g;
				switch(direction)
				{
					case SOUTH:	g = 90F; break;
					case WEST:	g = 0F; break;
					case NORTH:	g = 270F; break;
					case EAST:	g = 180F; break;
					default:
						g = bodyYaw;
						break;
				}
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90F));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0f));
			} else if (LivingEntityRenderer.shouldFlipUpsideDown(entity)) {
				matrices.translate(0.0f, (((Entity)entity).getHeight() + 0.1f) / scale, 0.0f);
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
			}
		}
	}
}
