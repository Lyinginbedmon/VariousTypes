package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.NosePiglinModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class NoseFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private EntityModel<LivingEntity> piglinNose, pigNose;
	
	public NoseFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.NOSE, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		piglinNose = new NosePiglinModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIGLIN));
		pigNose = new NosePiglinModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIG));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.NOSE_PIG,
				AccessoryBasic.create(
					e -> pigNose,
					prefix("textures/entity/nose/pig.png"),
					prefix("textures/entity/nose/pig_tinted.png")));
		addRendererMap(
				VTCosmetics.NOSE_PIGLIN,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> piglinNose,
						prefix("textures/entity/nose/piglin.png"),
						prefix("textures/entity/nose/piglin_tinted.png")),
					AccessoryBasic.create(
						e -> piglinNose, 
						prefix("textures/entity/nose/piglin_overlay.png")).untinted()));
		addRendererMap(
				VTCosmetics.NOSE_PIGZOMBIE,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> piglinNose,
						prefix("textures/entity/nose/zombified_piglin.png"),
						prefix("textures/entity/nose/zombified_piglin_tinted.png")),
					AccessoryBasic.create(
						e -> piglinNose, 
						prefix("textures/entity/nose/zombified_piglin_overlay.png")).untinted()));
	}
}
