package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.joml.Vector3f;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFly;
import com.lying.ability.AbilityFly.ConfigFly;
import com.lying.ability.AbilityFly.WingStyle;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.AnimatedBipedEntityModel;
import com.lying.client.model.IBipedLikeModel;
import com.lying.client.model.IModelWithRoot;
import com.lying.client.model.WingsBatModel;
import com.lying.client.model.WingsBeetleModel;
import com.lying.client.model.WingsBirdModel;
import com.lying.client.model.WingsButterflyModel;
import com.lying.client.model.WingsDragonflyModel;
import com.lying.client.model.WingsElytraModel;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class WingsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M>
{
	private static Identifier[] WING_ABILITIES = null;
	private final Map<WingStyle, WingData<?>> wingsMap = new HashMap<>();
	private static final Function<AbilityInstance, ConfigFly> configGetter = inst -> ((AbilityFly)VTAbilities.FLY.get()).instanceToValues(inst);
	
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(context);
		populateWingsMap();
	}
	
	private void populateWingsMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		wingsMap.put(
				WingStyle.BUTTERFLY, 
				new WingData<>(
					new WingsButterflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BUTTERFLY)), 
					prefix("textures/entity/wings/butterfly.png"), 
					prefix("textures/entity/wings/butterfly_tinted.png")));
		wingsMap.put(
				WingStyle.ELYTRA, 
				new WingData<>(
					new WingsElytraModel<>(loader.getModelPart(VTModelLayerParts.WINGS_ELYTRA)),
					prefix("textures/entity/wings/elytra.png"),
					prefix("textures/entity/wings/elytra_tinted.png")));
		wingsMap.put(
				WingStyle.DRAGONFLY, 
				new TranslucentWingData<>(
					new WingsDragonflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGONFLY)),
					prefix("textures/entity/wings/dragonfly.png"),
					prefix("textures/entity/wings/dragonfly_tinted.png")));
		wingsMap.put(
				WingStyle.BAT, 
				new WingData<>(
					new WingsBatModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BAT)),
					prefix("textures/entity/wings/bat.png"),
					prefix("textures/entity/wings/bat_tinted.png")));
		wingsMap.put(
				WingStyle.BIRD,
				new WingData<>(
					new WingsBirdModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BIRD)),
					prefix("textures/entity/wings/bird.png"),
					prefix("textures/entity/wings/bird_tinted.png")));
		wingsMap.put(
				WingStyle.BEETLE,
				new OverlayWingData<>(
					new WingsBeetleModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BEETLE)),
					prefix("textures/entity/wings/beetle_overlay.png"),
					prefix("textures/entity/wings/beetle_overlay_tinted.png"),
					new TranslucentWingData<>(
						new WingsBeetleModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BEETLE)),
						prefix("textures/entity/wings/beetle.png"))));
	}
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		ItemStack chestplate = entity.getEquippedStack(EquipmentSlot.BODY);
		if(!chestplate.isEmpty() && chestplate.isOf(Items.ELYTRA))
			return;
		
		if(WING_ABILITIES == null)
			WING_ABILITIES = new Identifier[] {
				VTAbilities.FLY.get().registryName(),
				VTAbilities.COS_WINGS.get().registryName() };
		
		VariousTypes.getSheet(entity).ifPresent(sheet -> 
		{
			AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
			for(Identifier regName : WING_ABILITIES)
			{
				if(!abilities.hasAbility(regName))
					continue;
				
				ConfigFly config = configGetter.apply(abilities.get(regName));
				handleWingRendering(entity, config.type(), config.colour(), limbAngle, limbDistance, headYaw, headPitch, tickDelta, matrices, vertexConsumers, light);
			}
		});
	}
	
	protected void handleWingRendering(E entity, WingStyle style, Optional<Integer> colour, float limbAngle, float limbDistance, float headYaw, float headPitch, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		if(!wingsMap.containsKey(style))
			return;
		
		WingData<?> wingData = wingsMap.getOrDefault(style, null);
		if(wingData == null)
			return;
		
		wingData.prepareModel(entity, getContextModel(), limbAngle, limbDistance, tickDelta, headYaw, headPitch);
		
		float r = 1F, g = 1F, b = 1F;
		if(colour.isPresent())
		{
			int col = colour.get();
			Vector3f vec = VTUtilsClient.decimalToVector(col);
			r = vec.x;
			g = vec.y;
			b = vec.z;
		}
		
		wingData.renderFor(matrices, vertexConsumers, light, entity, colour.isPresent(), r, g, b);
	}
	
	private class WingData<T extends EntityModel<E>>
	{
		final Function<Identifier, RenderLayer> layerProvider;
		final Function<Boolean, Identifier> textureProvider;
		final T model;
		final float alpha;
		
		public WingData(T modelIn, Identifier colourTex, Identifier tintedTex)
		{
			this(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F);
		}
		
		protected WingData(T modelIn, Identifier colourTex, Identifier tintedTex, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn)
		{
			layerProvider = layerProviderIn;
			textureProvider = bool -> bool ? tintedTex : colourTex;
			model = modelIn;
			alpha = alphaIn;
		}
		
		@SuppressWarnings("unchecked")
		public void prepareModel(E entity, EntityModel<E> contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
		{
			model.animateModel(entity, limbAngle, limbDistance, tickDelta);
			model.setAngles(entity, limbAngle, limbDistance, (float)entity.age + tickDelta, headYaw, headPitch);
			
			if(model instanceof IBipedLikeModel)
			{
				IBipedLikeModel<E> bipedLike = (IBipedLikeModel<E>)model;
				if(contextModel instanceof BipedEntityModel)
					bipedLike.copyTransforms((BipedEntityModel<E>)contextModel);
				else if(contextModel instanceof AnimatedBipedEntityModel)
					bipedLike.copyTransforms((AnimatedBipedEntityModel<E>)contextModel);
			}
			else if(model instanceof IModelWithRoot)
			{
				IModelWithRoot rootModel = (IModelWithRoot)model;
				if(contextModel instanceof IModelWithRoot)
					rootModel.getRoot().copyTransform(((IModelWithRoot)contextModel).getRoot());
				else if(contextModel instanceof SinglePartEntityModel)
					rootModel.getRoot().copyTransform(((SinglePartEntityModel<E>)contextModel).getPart());
			}
		}
		
		public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
		{
			Identifier texture = textureProvider.apply(tinted);
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(layerProvider.apply(texture));
			model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
		}
	}
	
	/** Renders a semi-transparent wing model, usually for diaphanous insect wings */
	private class TranslucentWingData<T extends EntityModel<E>> extends WingData<T>
	{
		public TranslucentWingData(T modelIn, Identifier colourTex)
		{
			this(modelIn, colourTex, colourTex);
		}
		
		public TranslucentWingData(T modelIn, Identifier colourTex, Identifier tintedTex)
		{
			super(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityTranslucent(tex), 0.6F);
		}
	}
	
	/** Renders two wing models with distinct textures, with the sub-data never being tinted */
	private class OverlayWingData<T extends EntityModel<E>> extends WingData<T>
	{
		final WingData<T> underlayData;
		
		public OverlayWingData(T modelIn, Identifier colourTex, Identifier tintedTex, WingData<T> underlayData)
		{
			this(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F, underlayData);
		}
		
		public OverlayWingData(T modelIn, Identifier colourTex, Identifier tintedTex, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn, WingData<T> underlayDataIn)
		{
			super(modelIn, colourTex, tintedTex, layerProviderIn, alphaIn);
			underlayData = underlayDataIn;
		}
		
		public void prepareModel(E entity, EntityModel<E> contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
		{
			super.prepareModel(entity, contextModel, limbAngle, limbDistance, tickDelta, headYaw, headPitch);
			underlayData.prepareModel(entity, contextModel, limbAngle, limbDistance, tickDelta, headYaw, headPitch);
		}
		
		public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
		{
			underlayData.renderFor(matrixStack, vertexConsumerProvider, light, entity, tinted, 1F, 1F, 1F);
			super.renderFor(matrixStack, vertexConsumerProvider, light, entity, tinted, r, g, b);
		}
	}
}
