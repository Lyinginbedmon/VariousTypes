package com.lying.client.renderer;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.tail.TailAxolotlModel;
import com.lying.client.model.tail.TailDragonModel;
import com.lying.client.model.tail.TailFoxModel;
import com.lying.client.model.tail.TailKirinModel;
import com.lying.client.model.tail.TailRabbitModel;
import com.lying.client.model.tail.TailRatModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class TailFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private EntityModel<LivingEntity> dragonTail, kirinTail, ratTail, foxTail, axolotlTail, rabbitTail;
	
	public TailFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.TAIL, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		dragonTail = new TailDragonModel<>(loader.getModelPart(VTModelLayerParts.TAIL_DRAGON));
		kirinTail = new TailKirinModel<>(loader.getModelPart(VTModelLayerParts.TAIL_KIRIN));
		ratTail = new TailRatModel<>(loader.getModelPart(VTModelLayerParts.TAIL_RAT));
		foxTail = new TailFoxModel<>(loader.getModelPart(VTModelLayerParts.TAIL_FOX));
		axolotlTail = new TailAxolotlModel<>(loader.getModelPart(VTModelLayerParts.TAIL_AXOLOTL));
		rabbitTail = new TailRabbitModel<>(loader.getModelPart(VTModelLayerParts.TAIL_RABBIT));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.TAIL_DRAGON,
				AccessoryBasic.create(
					e -> dragonTail,
					texture("tail/dragon.png"),
					texture("tail/dragon_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_KIRIN,
				AccessoryBasic.create(
					e -> kirinTail,
					texture("tail/kirin.png"),
					texture("tail/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_RAT,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> ratTail,
						texture("tail/rat_overlay.png")).untinted(),
					AccessoryBasic.create(
						e -> ratTail, 
						texture("tail/rat.png"), 
						texture("tail/rat_tinted.png"))));
		addRendererMap(
				VTCosmetics.TAIL_FOX,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> foxTail,
						texture("tail/fox_overlay.png")).untinted(),
					AccessoryBasic.create(
						e -> foxTail, 
						texture("tail/fox.png"), 
						texture("tail/fox_tinted.png"))));
		addRendererMap(
				VTCosmetics.TAIL_AXOLOTL,
				AccessoryBasic.create(
					e -> axolotlTail,
					texture("tail/axolotl.png"),
					texture("tail/axolotl_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_RABBIT,
				AccessoryBasic.create(
					e -> rabbitTail,
					texture("tail/rabbit.png"),
					texture("tail/rabbit_tinted.png")));
	}
}
