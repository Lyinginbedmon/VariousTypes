package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.HornsHartebeestModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
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
		
		addRendererMap(
				VTCosmetics.HORNS_HARTEBEEST,
				AccessoryBasic.create(
					new HornsHartebeestModel<>(loader.getModelPart(VTModelLayerParts.HORNS_HARTEBEEST)),
					prefix("textures/entity/horns/hartebeest.png"),
					prefix("textures/entity/horns/hartebeest_tinted.png")));
	}
}
