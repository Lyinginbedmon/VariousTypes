package com.lying.client.renderer;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.ears.EarsPiglinModel;
import com.lying.client.model.ears.EarsRabbitModel;
import com.lying.client.model.ears.GillsAxolotlModel;
import com.lying.client.model.ears.SimpleEarsModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class EarsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private EntityModel<LivingEntity> piglinEars, axolotlGills, elfEars, goblinEars, catEars, foxEars, wolfEars, rabbitEars;
	
	public EarsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.EARS, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		piglinEars = new EarsPiglinModel<>(loader.getModelPart(VTModelLayerParts.EARS_PIGLIN));
		axolotlGills = new GillsAxolotlModel<>(loader.getModelPart(VTModelLayerParts.GILLS_AXOLOTL));
		elfEars = new SimpleEarsModel<>(loader.getModelPart(VTModelLayerParts.EARS_ELF));
		goblinEars = new SimpleEarsModel<>(loader.getModelPart(VTModelLayerParts.EARS_GOBLIN));
		catEars = new SimpleEarsModel<>(loader.getModelPart(VTModelLayerParts.EARS_CAT));
		foxEars = new SimpleEarsModel<>(loader.getModelPart(VTModelLayerParts.EARS_FOX));
		wolfEars = new SimpleEarsModel<>(loader.getModelPart(VTModelLayerParts.EARS_WOLF));
		rabbitEars = new EarsRabbitModel<>(loader.getModelPart(VTModelLayerParts.EARS_RABBIT));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.EARS_PIGLIN,
				AccessoryBasic.create(
					e -> piglinEars,
					texture("ears/piglin.png"),
					texture("ears/piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_PIGZOMBIE,
				AccessoryBasic.create(
					e -> piglinEars,
					texture("ears/zombified_piglin.png"),
					texture("ears/zombified_piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_AXOLOTL,
				AccessoryBasic.create(
					e -> axolotlGills,
					texture("ears/axolotl.png"),
					texture("ears/axolotl_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_ELF,
				AccessoryBasic.create(
					e -> elfEars,
					texture("ears/elf.png"),
					texture("ears/elf_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_GOBLIN,
				AccessoryBasic.create(
					e -> goblinEars,
					texture("ears/goblin.png"),
					texture("ears/goblin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_CAT,
				AccessoryBasic.create(
					e -> catEars,
					texture("ears/cat.png"),
					texture("ears/cat_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_FOX,
				AccessoryBasic.create(
					e -> foxEars,
					texture("ears/fox.png"),
					texture("ears/fox_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_WOLF,
				AccessoryBasic.create(
					e -> wolfEars,
					texture("ears/wolf.png"),
					texture("ears/wolf_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_RABBIT,
				AccessoryBasic.create(
					e -> rabbitEars,
					texture("ears/rabbit.png"),
					texture("ears/rabbit_tinted.png")));
	}
}
