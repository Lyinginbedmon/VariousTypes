package com.lying.client.renderer;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.SimpleNoseModel;
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
	private EntityModel<LivingEntity> piglinNose, pigNose, villagerNose, witchNose;
	
	public NoseFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.NOSE, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		piglinNose = new SimpleNoseModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIGLIN));
		pigNose = new SimpleNoseModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIG));
		villagerNose = new SimpleNoseModel<>(loader.getModelPart(VTModelLayerParts.NOSE_VILLAGER));
		witchNose = new SimpleNoseModel<>(loader.getModelPart(VTModelLayerParts.NOSE_WITCH));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.NOSE_PIG,
				AccessoryBasic.create(
					e -> pigNose,
					texture("nose/pig.png"),
					texture("nose/pig_tinted.png")));
		addRendererMap(
				VTCosmetics.NOSE_PIGLIN,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> piglinNose,
						texture("nose/piglin.png"),
						texture("nose/piglin_tinted.png")),
					AccessoryBasic.create(
						e -> piglinNose, 
						texture("nose/piglin_overlay.png")).untinted()));
		addRendererMap(
				VTCosmetics.NOSE_PIGZOMBIE,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> piglinNose,
						texture("nose/zombified_piglin.png"),
						texture("nose/zombified_piglin_tinted.png")),
					AccessoryBasic.create(
						e -> piglinNose, 
						texture("nose/zombified_piglin_overlay.png")).untinted()));
		addRendererMap(
				VTCosmetics.NOSE_VILLAGER,
				AccessoryBasic.create(
					e -> villagerNose,
					texture("nose/villager.png"),
					texture("nose/villager_tinted.png")));
		addRendererMap(
				VTCosmetics.NOSE_WITCH,
				AccessoryBasic.create(
					e -> witchNose,
					texture("nose/witch.png"),
					texture("nose/witch_tinted.png")));
	}
}
