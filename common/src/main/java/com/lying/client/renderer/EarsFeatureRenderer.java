package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

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
					prefix("textures/entity/ears/piglin.png"),
					prefix("textures/entity/ears/piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_PIGZOMBIE,
				AccessoryBasic.create(
					e -> piglinEars,
					prefix("textures/entity/ears/zombified_piglin.png"),
					prefix("textures/entity/ears/zombified_piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_AXOLOTL,
				AccessoryBasic.create(
					e -> axolotlGills,
					prefix("textures/entity/ears/axolotl.png"),
					prefix("textures/entity/ears/axolotl_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_ELF,
				AccessoryBasic.create(
					e -> elfEars,
					prefix("textures/entity/ears/elf.png"),
					prefix("textures/entity/ears/elf_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_GOBLIN,
				AccessoryBasic.create(
					e -> goblinEars,
					prefix("textures/entity/ears/goblin.png"),
					prefix("textures/entity/ears/goblin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_CAT,
				AccessoryBasic.create(
					e -> catEars,
					prefix("textures/entity/ears/cat.png"),
					prefix("textures/entity/ears/cat_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_FOX,
				AccessoryBasic.create(
					e -> foxEars,
					prefix("textures/entity/ears/fox.png"),
					prefix("textures/entity/ears/fox_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_WOLF,
				AccessoryBasic.create(
					e -> wolfEars,
					prefix("textures/entity/ears/wolf.png"),
					prefix("textures/entity/ears/wolf_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_RABBIT,
				AccessoryBasic.create(
					e -> rabbitEars,
					prefix("textures/entity/ears/rabbit.png"),
					prefix("textures/entity/ears/rabbit_tinted.png")));
	}
}
