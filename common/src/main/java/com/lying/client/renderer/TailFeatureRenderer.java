package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.TailDragonModel;
import com.lying.client.model.TailKirinModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.init.VTCosmetics;
import com.lying.utility.Cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class TailFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public TailFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(Cosmetic.Type.TAIL, context);
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
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
	}
}
