package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.wings.WingsAngelModel;
import com.lying.client.model.wings.WingsBatModel;
import com.lying.client.model.wings.WingsBeetleModel;
import com.lying.client.model.wings.WingsBirdModel;
import com.lying.client.model.wings.WingsButterflyModel;
import com.lying.client.model.wings.WingsDragonModel;
import com.lying.client.model.wings.WingsDragonflyModel;
import com.lying.client.model.wings.WingsElytraModel;
import com.lying.client.model.wings.WingsSkeletonModel;
import com.lying.client.model.wings.WingsWitchModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.client.renderer.accessory.AccessoryEndPortal;
import com.lying.client.renderer.accessory.AccessoryGlowing;
import com.lying.client.renderer.accessory.AccessoryTranslucent;
import com.lying.init.VTCosmetics;
import com.lying.utility.Cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;

public class WingsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(Cosmetic.Type.WINGS, context);
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		WingsBeetleModel<E> beetleModel = new WingsBeetleModel<E>(loader.getModelPart(VTModelLayerParts.WINGS_BEETLE));
		WingsAngelModel<E> angelModel = new WingsAngelModel<E>(loader.getModelPart(VTModelLayerParts.WINGS_ANGEL));
		WingsDragonModel<E> dragonModel = new WingsDragonModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGON));
		
		addRendererMap(
				VTCosmetics.WINGS_ELYTRA, 
				AccessoryBasic.create(
					new WingsElytraModel<>(loader.getModelPart(VTModelLayerParts.WINGS_ELYTRA)),
					prefix("textures/entity/wings/elytra.png"),
					prefix("textures/entity/wings/elytra_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BUTTERFLY, 
				AccessoryBasic.create(
					new WingsButterflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BUTTERFLY)), 
					prefix("textures/entity/wings/butterfly.png"), 
					prefix("textures/entity/wings/butterfly_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_DRAGONFLY, 
				AccessoryTranslucent.create(
					new WingsDragonflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGONFLY)),
					prefix("textures/entity/wings/dragonfly.png"),
					prefix("textures/entity/wings/dragonfly_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BAT, 
				AccessoryBasic.create(
					new WingsBatModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BAT)),
					prefix("textures/entity/wings/bat.png"),
					prefix("textures/entity/wings/bat_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BIRD,
				AccessoryBasic.create(
					new WingsBirdModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BIRD)),
					prefix("textures/entity/wings/bird.png"),
					prefix("textures/entity/wings/bird_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BEETLE,
				AccessoryCompound.create(
					AccessoryTranslucent.create(
						beetleModel,
						prefix("textures/entity/wings/beetle.png")).untinted(),
					AccessoryBasic.create(
						beetleModel,
						prefix("textures/entity/wings/beetle_overlay.png"),
						prefix("textures/entity/wings/beetle_overlay_tinted.png"))));
		addRendererMap(
				VTCosmetics.WINGS_WITCH, 
				AccessoryEndPortal.create(
					new WingsWitchModel<>(loader.getModelPart(VTModelLayerParts.WINGS_WITCH))).untinted());
		addRendererMap(
				VTCosmetics.WINGS_ANGEL, 
				AccessoryCompound.create(
					AccessoryBasic.create(
						angelModel, 
						prefix("textures/entity/wings/angel.png"), 
						prefix("textures/entity/wings/angel_tinted.png")),
					AccessoryBasic.create(
						angelModel, 
						prefix("textures/entity/wings/angel_halo.png")).untinted(),
					AccessoryGlowing.create(
						angelModel,
						prefix("textures/entity/wings/angel_glow.png")).untinted()));
		addRendererMap(
				VTCosmetics.WINGS_SKELETON,
				AccessoryBasic.create(
					new WingsSkeletonModel<>(loader.getModelPart(VTModelLayerParts.WINGS_SKELETON)), 
					prefix("textures/entity/wings/skeleton.png"),
					prefix("textures/entity/wings/skeleton_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_DRAGON,
				AccessoryCompound.create(
					AccessoryBasic.create(
						dragonModel,
						prefix("textures/entity/wings/dragon.png"),
						prefix("textures/entity/wings/dragon_tinted.png")),
					AccessoryBasic.create(
						dragonModel,
						prefix("textures/entity/wings/dragon_overlay.png")).untinted()));
	}
	
	public boolean shouldRender(E entity) { return super.shouldRender(entity) && !entity.getEquippedStack(EquipmentSlot.BODY).isOf(Items.ELYTRA); }
}
