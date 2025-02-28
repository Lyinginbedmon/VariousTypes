package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.EarsElfModel;
import com.lying.client.model.EarsPiglinModel;
import com.lying.client.model.GillsAxolotlModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class EarsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public EarsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.EARS, context);
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		EarsPiglinModel<E> piglinModel = new EarsPiglinModel<>(loader.getModelPart(VTModelLayerParts.EARS_PIGLIN));
		
		addRendererMap(
				VTCosmetics.EARS_PIGLIN,
				AccessoryBasic.create(
					piglinModel,
					prefix("textures/entity/ears/piglin.png"),
					prefix("textures/entity/ears/piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_PIGZOMBIE,
				AccessoryBasic.create(
					piglinModel,
					prefix("textures/entity/ears/zombified_piglin.png"),
					prefix("textures/entity/ears/zombified_piglin_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_AXOLOTL,
				AccessoryBasic.create(
					new GillsAxolotlModel<>(loader.getModelPart(VTModelLayerParts.GILLS_AXOLOTL)),
					prefix("textures/entity/ears/axolotl.png"),
					prefix("textures/entity/ears/axolotl_tinted.png")));
		addRendererMap(
				VTCosmetics.EARS_ELF,
				AccessoryBasic.create(
					new EarsElfModel<>(loader.getModelPart(VTModelLayerParts.EARS_ELF)),
					prefix("textures/entity/ears/elf.png"),
					prefix("textures/entity/ears/elf_tinted.png")));
	}
}
