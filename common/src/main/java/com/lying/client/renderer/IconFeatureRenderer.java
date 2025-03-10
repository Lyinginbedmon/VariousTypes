package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.renderer.accessory.OverheadIconRenderer;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;

import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class IconFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public IconFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.ICON, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader) { }
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.ICON_ASTRAL_EYE,
				new OverheadIconRenderer<>(
					prefix("astral_eye"),
					prefix("astral_eye_tinted")));
		addRendererMap(
				VTCosmetics.ICON_DIVINE_CROWN,
				new OverheadIconRenderer<>(
					prefix("divine_crown"),
					prefix("divine_crown_tinted")));
		addRendererMap(
				VTCosmetics.ICON_ETERNAL_FLAME,
				new OverheadIconRenderer<>(
					prefix("eternal_flame"),
					prefix("eternal_flame_tinted")));
	}
}
