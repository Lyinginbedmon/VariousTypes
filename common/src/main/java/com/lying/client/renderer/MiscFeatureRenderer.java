package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.ModelFullbody;
import com.lying.client.renderer.accessory.AccessoryGlowing;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class MiscFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private final boolean isSlimModel;
	
	public MiscFeatureRenderer(FeatureRendererContext<E, M> context, boolean slim)
	{
		super(VTCosmeticTypes.MISC, context);
		isSlimModel = slim;
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		ModelFullbody<E> fullbodyModel = new ModelFullbody<>(loader.getModelPart(isSlimModel ? VTModelLayerParts.FULLBODY_SLIM : VTModelLayerParts.FULLBODY_WIDE));
		
		addRendererMap(
				VTCosmetics.MISC_GLOW_SPOTS,
				AccessoryGlowing.create(
					fullbodyModel, 
					prefix(isSlimModel ? "textures/entity/verdine_spots_slim.png" : "textures/entity/verdine_spots_wide.png"),
					prefix(isSlimModel ? "textures/entity/verdine_spots_tinted_slim.png": "textures/entity/verdine_spots_tinted_slim.png")));
	}
}
