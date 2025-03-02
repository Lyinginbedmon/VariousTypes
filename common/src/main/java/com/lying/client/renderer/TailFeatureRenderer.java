package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.tail.TailDragonModel;
import com.lying.client.model.tail.TailKirinModel;
import com.lying.client.model.tail.TailRatModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class TailFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public TailFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.TAIL, context);
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		TailRatModel<E> ratModel = new TailRatModel<>(loader.getModelPart(VTModelLayerParts.TAIL_RAT));
		
		addRendererMap(
				VTCosmetics.TAIL_DRAGON,
				AccessoryBasic.create(
					new TailDragonModel<>(loader.getModelPart(VTModelLayerParts.TAIL_DRAGON)),
					prefix("textures/entity/tail/dragon.png"),
					prefix("textures/entity/tail/dragon_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_KIRIN,
				AccessoryBasic.create(
					new TailKirinModel<>(loader.getModelPart(VTModelLayerParts.TAIL_KIRIN)),
					prefix("textures/entity/tail/kirin.png"),
					prefix("textures/entity/tail/kirin_tinted.png")));
		addRendererMap(
				VTCosmetics.TAIL_RAT,
				AccessoryCompound.create(
					AccessoryBasic.create(
						ratModel,
						prefix("textures/entity/tail/rat_overlay.png")).untinted(),
					AccessoryBasic.create(
						ratModel, 
						prefix("textures/entity/tail/rat.png"), 
						prefix("textures/entity/tail/rat_tinted.png"))));
	}
}
