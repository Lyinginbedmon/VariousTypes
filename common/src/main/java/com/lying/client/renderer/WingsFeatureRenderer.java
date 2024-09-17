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
import com.lying.client.model.WingsButterflyModel;
import com.lying.client.model.WingsDragonflyModel;
import com.lying.client.model.WingsElytraModel;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
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
	
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context, boolean isPlayer)
	{
		super(context);
		populateWingsMap(isPlayer);
	}
	
	private void populateWingsMap(boolean isPlayer)
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
		wingsMap.put(WingStyle.DRAGONFLY, 
				new WingData<>(
					new WingsDragonflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGONFLY)),
					prefix("textures/entity/wings/dragonfly.png"),
					prefix("textures/entity/wings/dragonfly_tinted.png")));
	}
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		ItemStack chestplate = entity.getEquippedStack(EquipmentSlot.BODY);
		if(!chestplate.isEmpty() && chestplate.isOf(Items.ELYTRA))
			return;
		
		if(WING_ABILITIES == null)
			WING_ABILITIES = new Identifier[] { VTAbilities.FLY.get().registryName(), VTAbilities.COS_WINGS.get().registryName() };
		
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
	
	@SuppressWarnings("unchecked")
	protected void handleWingRendering(E entity, WingStyle style, Optional<Integer> colour, float limbAngle, float limbDistance, float headYaw, float headPitch, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		if(!wingsMap.containsKey(style))
			return;
		
		WingData<?> wingData = wingsMap.getOrDefault(style, null);
		if(wingData == null)
			return;
		
		M contextModel = getContextModel();
		EntityModel<E> wingModel = wingData.model;
		
		wingModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
		wingModel.setAngles(entity, limbAngle, limbDistance, (float)entity.age + tickDelta, headYaw, headPitch);
		
		if(wingModel instanceof IBipedLikeModel)
		{
			IBipedLikeModel<E> bipedLike = (IBipedLikeModel<E>)wingModel;
			if(contextModel instanceof BipedEntityModel)
				bipedLike.copyTransforms((BipedEntityModel<E>)contextModel);
			else if(contextModel instanceof AnimatedBipedEntityModel)
				bipedLike.copyTransforms((AnimatedBipedEntityModel<E>)contextModel);
		}
		else if(wingModel instanceof IModelWithRoot)
		{
			IModelWithRoot rootModel = (IModelWithRoot)wingModel;
			if(contextModel instanceof IModelWithRoot)
				rootModel.getRoot().copyTransform(((IModelWithRoot)contextModel).getRoot());
			else if(contextModel instanceof SinglePartEntityModel)
				rootModel.getRoot().copyTransform(((SinglePartEntityModel<E>)contextModel).getPart());
		}
		
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
	
	public EntityModel<E> getModel(WingStyle type) { return wingsMap.get(type).model; }
	
	public Identifier getTexture(WingStyle type, boolean tinted) { return tinted ? wingsMap.get(type).tintedTexture : wingsMap.get(type).colourTexture; }
	
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
}
