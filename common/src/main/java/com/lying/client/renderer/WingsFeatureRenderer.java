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
import com.lying.client.model.WingsAngelModel;
import com.lying.client.model.WingsBatModel;
import com.lying.client.model.WingsBeetleModel;
import com.lying.client.model.WingsBirdModel;
import com.lying.client.model.WingsButterflyModel;
import com.lying.client.model.WingsDragonModel;
import com.lying.client.model.WingsDragonflyModel;
import com.lying.client.model.WingsElytraModel;
import com.lying.client.renderer.wings.CompoundWingData;
import com.lying.client.renderer.wings.GlowWingData;
import com.lying.client.renderer.wings.TranslucentWingData;
import com.lying.client.renderer.wings.WingData;
import com.lying.client.renderer.wings.WingRenderer;
import com.lying.client.utility.VTUtilsClient;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class WingsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends FeatureRenderer<E, M>
{
	private static Identifier[] WING_ABILITIES = null;
	private final Map<WingStyle, WingRenderer<E,M>> wingsMap = new HashMap<>();
	private static final Function<AbilityInstance, ConfigFly> configGetter = inst -> ((AbilityFly)VTAbilities.FLY.get()).instanceToValues(inst);
	
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(context);
		populateWingsMap();
	}
	
	private void populateWingsMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		WingsBeetleModel<E> beetleModel = new WingsBeetleModel<E>(loader.getModelPart(VTModelLayerParts.WINGS_BEETLE));
		WingsAngelModel<E> angelModel = new WingsAngelModel<E>(loader.getModelPart(VTModelLayerParts.WINGS_ANGEL));
		
		addWingModel(
				WingStyle.ELYTRA, 
				WingData.create(
					new WingsElytraModel<>(loader.getModelPart(VTModelLayerParts.WINGS_ELYTRA)),
					prefix("textures/entity/wings/elytra.png"),
					prefix("textures/entity/wings/elytra_tinted.png")));
		addWingModel(
				WingStyle.BUTTERFLY, 
				WingData.create(
					new WingsButterflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BUTTERFLY)), 
					prefix("textures/entity/wings/butterfly.png"), 
					prefix("textures/entity/wings/butterfly_tinted.png")));
		addWingModel(
				WingStyle.DRAGONFLY, 
				TranslucentWingData.create(
					new WingsDragonflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGONFLY)),
					prefix("textures/entity/wings/dragonfly.png"),
					prefix("textures/entity/wings/dragonfly_tinted.png")));
		addWingModel(
				WingStyle.BAT, 
				WingData.create(
					new WingsBatModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BAT)),
					prefix("textures/entity/wings/bat.png"),
					prefix("textures/entity/wings/bat_tinted.png")));
		addWingModel(
				WingStyle.BIRD,
				WingData.create(
					new WingsBirdModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BIRD)),
					prefix("textures/entity/wings/bird.png"),
					prefix("textures/entity/wings/bird_tinted.png")));
		addWingModel(
				WingStyle.BEETLE,
				CompoundWingData.create(
					TranslucentWingData.create(
						beetleModel,
						prefix("textures/entity/wings/beetle.png")).untinted(),
					WingData.create(
						beetleModel,
						prefix("textures/entity/wings/beetle_overlay.png"),
						prefix("textures/entity/wings/beetle_overlay_tinted.png"))));
		addWingModel(
				WingStyle.ANGEL, 
				CompoundWingData.create(
					WingData.create(
						angelModel, 
						prefix("textures/entity/wings/angel.png"), 
						prefix("textures/entity/wings/angel_tinted.png")),
					WingData.create(
						angelModel, 
						prefix("textures/entity/wings/angel_halo.png")).untinted(),
					GlowWingData.create(
						angelModel,
						prefix("textures/entity/wings/angel_glow.png")).untinted()));
		addWingModel(
				WingStyle.DRAGON,
				WingData.create(
					new WingsDragonModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGON)),
					prefix("textures/entity/wings/dragon.png"),
					prefix("textures/entity/wings/dragon_tinted.png")));
	}
	
	@SuppressWarnings("unchecked")
	private void addWingModel(WingStyle style, WingRenderer<?,?> renderer)
	{
		wingsMap.put(style, (WingRenderer<E,M>)renderer);
	}
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, E entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(entity.isInvisible())
			return;
		
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
		
		WingRenderer<E,M> wingData = (WingRenderer<E,M>)wingsMap.getOrDefault(style, null);
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
}
