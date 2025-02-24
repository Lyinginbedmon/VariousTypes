package com.lying.client.renderer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import com.google.common.base.Supplier;
import com.lying.client.renderer.accessory.IAccessoryRenderer;
import com.lying.client.utility.VTUtilsClient;
import com.lying.utility.Cosmetic;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class AbstractAccessoryFeature<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M>
{
	private final Map<Identifier, IAccessoryRenderer<E,M>> rendererMap = new HashMap<>();
	private final Cosmetic.Type type;
	
	public AbstractAccessoryFeature(Cosmetic.Type typeIn, FeatureRendererContext<E, M> context)
	{
		super(context);
		type = typeIn;
		populateRendererMap();
	}
	
	protected abstract void populateRendererMap();
	
	@SuppressWarnings("unchecked")
	protected void addRendererMap(Supplier<Cosmetic> style, IAccessoryRenderer<?,?> renderer)
	{
		rendererMap.put(style.get().registryName(), (IAccessoryRenderer<E,M>)renderer);
	}
	
	protected boolean shouldRender(E entity) { return !entity.isInvisible(); }
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(!shouldRender(entity))
			return;
		
		VTUtilsClient.getEntityCosmetics(entity, type)
			.forEach(cosmetic -> handleAccessoryRendering(entity, cosmetic, limbAngle, limbDistance, headYaw, headPitch, tickDelta, matrices, vertexConsumers, light));
	}
	
	protected void handleAccessoryRendering(E entity, final Cosmetic cosmetic, float limbAngle, float limbDistance, float headYaw, float headPitch, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		if(!rendererMap.containsKey(cosmetic.registryName()))
			return;
		
		IAccessoryRenderer<E,M> renderer = (IAccessoryRenderer<E,M>)rendererMap.getOrDefault(cosmetic.registryName(), null);
		if(renderer == null)
			return;
		
		renderer.prepareModel(entity, getContextModel(), limbAngle, limbDistance, tickDelta, headYaw, headPitch);
		
		Optional<Integer> colour = cosmetic.color();
		float r = 1F, g = 1F, b = 1F;
		if(colour.isPresent())
		{
			int col = colour.get();
			Vector3f vec = VTUtilsClient.decimalToVector(col);
			r = vec.x;
			g = vec.y;
			b = vec.z;
		}
		
		renderer.renderFor(matrices, vertexConsumers, light, entity, colour.isPresent(), r, g, b);
	}
}
