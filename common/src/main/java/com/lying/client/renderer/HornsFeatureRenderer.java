package com.lying.client.renderer;

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
					texture("horns/saiga.png"),
					texture("horns/saiga_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_RAM,
				AccessoryBasic.create(
					e -> ramHorns,
					texture("horns/ram.png"),
					texture("horns/ram_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_STAG,
				AccessoryBasic.create(
					e -> stagAntlers,
					texture("horns/stag.png"),
					texture("horns/stag_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_KIRIN,
				AccessoryBasic.create(
					e -> kirinHorns,
					texture("horns/kirin.png"),
					texture("horns/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_LIGHTNING,
				AccessoryLightning.create(e -> kirinHorns));
		addRendererMap(
				VTCosmetics.HORN_UNICORN,
				AccessoryBasic.create(
					e -> unicornHorn,
					texture("horns/unicorn.png"),
					texture("horns/unicorn_tinted.png")));
		addRendererMap(
				VTCosmetics.HORNS_DEVIL,
				AccessoryBasic.create(
					e -> devilHorns,
					texture("horns/devil.png"),
					texture("horns/devil_tinted.png")));
	}
}
