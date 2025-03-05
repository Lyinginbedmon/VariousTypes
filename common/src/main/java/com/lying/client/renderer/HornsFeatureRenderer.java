package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.SimpleHornsModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryLightning;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class HornsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public HornsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.HORNS, context);
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		SimpleHornsModel<E> kirinModel = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_KIRIN));
		
		addRendererMap(
				VTCosmetics.HORNS_HARTEBEEST,
				AccessoryBasic.create(
					new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_HARTEBEEST)),
					prefix("textures/entity/horns/hartebeest.png"),
					prefix("textures/entity/horns/hartebeest_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_RAM,
				AccessoryBasic.create(
					new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_RAM)),
					prefix("textures/entity/horns/ram.png"),
					prefix("textures/entity/horns/ram_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_STAG,
				AccessoryBasic.create(
					new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_STAG)),
					prefix("textures/entity/horns/stag.png"),
					prefix("textures/entity/horns/stag_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_KIRIN,
				AccessoryBasic.create(
						kirinModel,
					prefix("textures/entity/horns/kirin.png"),
					prefix("textures/entity/horns/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_LIGHTNING,
				AccessoryLightning.create(kirinModel));
	}
}
