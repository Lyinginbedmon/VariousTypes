package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.tail.TailAxolotlModel;
import com.lying.client.model.tail.TailDragonModel;
import com.lying.client.model.tail.TailFoxModel;
import com.lying.client.model.tail.TailKirinModel;
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
	private EntityModel<LivingEntity> dragonTail, kirinTail, ratTail, foxTail, axolotlTail;
	
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
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.TAIL_DRAGON,
				AccessoryBasic.create(
					e -> dragonTail,
					prefix("textures/entity/tail/dragon.png"),
					prefix("textures/entity/tail/dragon_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_KIRIN,
				AccessoryBasic.create(
					e -> kirinTail,
					prefix("textures/entity/tail/kirin.png"),
					prefix("textures/entity/tail/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_RAT,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> ratTail,
						prefix("textures/entity/tail/rat_overlay.png")).untinted(),
					AccessoryBasic.create(
						e -> ratTail, 
						prefix("textures/entity/tail/rat.png"), 
						prefix("textures/entity/tail/rat_tinted.png"))));
		addRendererMap(
				VTCosmetics.TAIL_FOX,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> foxTail,
						prefix("textures/entity/tail/fox_overlay.png")).untinted(),
					AccessoryBasic.create(
						e -> foxTail, 
						prefix("textures/entity/tail/fox.png"), 
						prefix("textures/entity/tail/fox_tinted.png"))));
		addRendererMap(
				VTCosmetics.TAIL_AXOLOTL,
				AccessoryBasic.create(
					e -> axolotlTail,
					prefix("textures/entity/tail/axolotl.png"),
					prefix("textures/entity/tail/axolotl_tinted.png")));
	}
}
