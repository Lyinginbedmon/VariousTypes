package com.lying.client.renderer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.joml.Vector3f;

import com.lying.client.renderer.accessory.IAccessoryRenderer;
import com.lying.client.utility.VTUtilsClient;
import com.lying.reference.Reference;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;
import com.lying.utility.VTUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public abstract class AbstractAccessoryFeature<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M>
{
	protected static final MinecraftClient mc = MinecraftClient.getInstance();
	private final Map<Identifier, IAccessoryRenderer<E,M>> rendererMap = new HashMap<>();
	private final Comparator<Cosmetic> SORTER = (a,b) -> 
	{
		int orderA = rendererMap.containsKey(a.registryName()) ? rendererMap.get(a.registryName()).renderOrder() : 0;
		int orderB = rendererMap.containsKey(b.registryName()) ? rendererMap.get(b.registryName()).renderOrder() : 0;
		return orderA < orderB ? -1 : orderA > orderB ? 1 : 0;
	};
	private final Supplier<CosmeticType> type;
	
	public AbstractAccessoryFeature(Supplier<CosmeticType> typeIn, FeatureRendererContext<E, M> context)
	{
		super(context);
		type = typeIn;
		
		createEntityModels(mc.getEntityModelLoader());
		populateRendererMap();
	}
	
	/** Instantiate entity models prior to use to reduce computational load */
	protected abstract void createEntityModels(EntityModelLoader loader);
	
	protected abstract void populateRendererMap();
	
	@SuppressWarnings("unchecked")
	protected void addRendererMap(Supplier<Cosmetic> style, IAccessoryRenderer<?,?> renderer)
	{
		rendererMap.put(style.get().registryName(), (IAccessoryRenderer<E,M>)renderer);
	}
	
	protected boolean shouldRender(E entity) { return !entity.isInvisible() && !type.get().shouldBeHidden(entity); }
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(!shouldRender(entity))
			return;
		
		VTUtils.getEntityCosmetics(entity, type.get()).stream().sorted(SORTER)
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
		
		renderer.renderFor(matrices, vertexConsumers, light, entity, tickDelta, colour.isPresent(), r, g, b);
	}
	
	protected static Identifier texture(String address)
	{
		return Reference.ModInfo.prefix("textures/entity/"+address);
	}
}
