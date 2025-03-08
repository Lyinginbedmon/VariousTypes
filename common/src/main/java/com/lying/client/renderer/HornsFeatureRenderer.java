package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.SimpleHornsModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryLightning;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class HornsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private EntityModel<LivingEntity> dragonHorns, ramHorns, stagAntlers, kirinHorns, unicornHorn, devilHorns;
	
	public HornsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.HORNS, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		dragonHorns = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_HARTEBEEST));
		ramHorns = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_RAM));
		stagAntlers = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_STAG));
		kirinHorns = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_KIRIN));
		unicornHorn = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORN_UNICORN));
		devilHorns = new SimpleHornsModel<>(loader.getModelPart(VTModelLayerParts.HORNS_DEVIL));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.HORNS_SAIGA,
				AccessoryBasic.create(
					e -> dragonHorns,
					prefix("textures/entity/horns/saiga.png"),
					prefix("textures/entity/horns/saiga_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_RAM,
				AccessoryBasic.create(
					e -> ramHorns,
					prefix("textures/entity/horns/ram.png"),
					prefix("textures/entity/horns/ram_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_STAG,
				AccessoryBasic.create(
					e -> stagAntlers,
					prefix("textures/entity/horns/stag.png"),
					prefix("textures/entity/horns/stag_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_KIRIN,
				AccessoryBasic.create(
					e -> kirinHorns,
					prefix("textures/entity/horns/kirin.png"),
					prefix("textures/entity/horns/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_LIGHTNING,
				AccessoryLightning.create(e -> kirinHorns));
		addRendererMap(
				VTCosmetics.HORN_UNICORN,
				AccessoryBasic.create(
					e -> unicornHorn,
					prefix("textures/entity/horns/unicorn.png"),
					prefix("textures/entity/horns/unicorn_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_DEVIL,
				AccessoryBasic.create(
					e -> devilHorns,
					prefix("textures/entity/horns/devil.png"),
					prefix("textures/entity/horns/devil_tinted.png")));
	}
}
