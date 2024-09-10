package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joml.Vector3f;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFly;
import com.lying.ability.AbilityFly.ConfigFly;
import com.lying.ability.AbilityFly.WingType;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.AnimatedBipedEntityModel;
import com.lying.client.model.WingsButterflyModel;
import com.lying.client.utility.VTUtilsClient;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class WingsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M>
{
	private final Map<WingType, WingData<?>> wingsMap = new HashMap<>();
	
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(context);
		populateWingsMap();
	}
	
	private void populateWingsMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		wingsMap.put(
				WingType.BUTTERFLY, 
				new WingData<>(
					new WingsButterflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BUTTERFLY)), 
					prefix("textures/entity/wings/butterfly.png"), 
					prefix("textures/entity/wings/butterfly_tinted.png")));
		wingsMap.put(
				WingType.ELYTRA, 
				new ElytraWingData(loader));
	}
	
	@SuppressWarnings("unchecked")
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(entity);
		if(sheetOpt.isEmpty())
			return;
		
		Identifier regName = VTAbilities.FLY.get().registryName();
		AbilitySet abilities = sheetOpt.get().elementValue(VTSheetElements.ABILITIES);
		if(!abilities.hasAbility(regName))
			return;
		
		AbilityInstance inst = abilities.get(regName);
		ConfigFly config = ((AbilityFly)inst.ability()).instanceToValues(inst);
		if(config.type() == WingType.NONE)
			return;
		
		WingData<?> wingData = wingsMap.getOrDefault(config.type(), null);
		if(wingData == null)
			return;
		
		M contextModel = getContextModel();
		EntityModel<E> wingModel = wingData.model;
		
		if(wingModel instanceof BipedEntityModel)
			if(contextModel instanceof BipedEntityModel)
				((BipedEntityModel<E>)contextModel).copyBipedStateTo((BipedEntityModel<E>)wingModel);
			else if(contextModel instanceof AnimatedBipedEntityModel)
				((AnimatedBipedEntityModel<E>)contextModel).copyTransformsTo((BipedEntityModel<E>)wingModel);
		
		wingModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
		wingModel.setAngles(entity, limbAngle, limbDistance, (float)entity.age + tickDelta, headYaw, headPitch);
		
		float r = 1F, g = 1F, b = 1F;
		if(config.colour().isPresent())
		{
			int colour = config.colour().get();
			Vector3f vec = VTUtilsClient.decimalToVector(colour);
			r = vec.x;
			g = vec.y;
			b = vec.z;
		}
		
		wingData.renderFor(matrices, vertexConsumers, light, entity, config.colour().isPresent(), r, g, b);
	}
	
	public EntityModel<E> getModel(WingType type) { return wingsMap.get(type).model; }
	
	public Identifier getTexture(WingType type, boolean tinted) { return tinted ? wingsMap.get(type).tintedTexture : wingsMap.get(type).colourTexture; }
	
	private class WingData<T extends EntityModel<E>>
	{
		T model;
		Identifier colourTexture;
		Identifier tintedTexture;
		
		public WingData(T modelIn, Identifier texAIn, Identifier texBIn)
		{
			model = modelIn;
			colourTexture = texAIn;
			tintedTexture = texBIn;
		}
		
		public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
		{
			renderModel(model, tinted ? tintedTexture : colourTexture, matrixStack, vertexConsumerProvider, light, entity, r, g, b);
		}
	}
	
	public class ElytraWingData extends WingData<ElytraEntityModel<E>>
	{
		public ElytraWingData(EntityModelLoader loader)
		{
			super(new ElytraEntityModel<>(loader.getModelPart(EntityModelLayers.ELYTRA)), new Identifier("textures/entity/elytra.png"), prefix("textures/entity/wings/elytra_tinted.png"));
		}
		
		public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, boolean tinted, float r, float g, float b)
		{
			matrixStack.push();
				matrixStack.translate(0F, 0F, 0.125F);
				VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumerProvider, RenderLayer.getArmorCutoutNoCull(tinted ? tintedTexture : colourTexture), false, false);
				model.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0f);
			matrixStack.pop();
		}
	}
}
