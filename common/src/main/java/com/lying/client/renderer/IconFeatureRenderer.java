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
	
//	protected boolean shouldRender(E entity) { return !VTCosmeticTypes.ICON.get().shouldBeHidden(entity); }
	
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
					prefix("eternal_flame_tinted")).withScale(2F));
		addRendererMap(
				VTCosmetics.ICON_CRYSTAL,
				new OverheadIconRenderer<>(
					prefix("crystal"),
					prefix("crystal_tinted")).withScale(2F));
		addRendererMap(
				VTCosmetics.ICON_GEM,
				new OverheadIconRenderer<>(
					prefix("gem"),
					prefix("gem_tinted")).withScale(2F));
		addRendererMap(
				VTCosmetics.ICON_CURRENCY,
				new OverheadIconRenderer<>(
					prefix("currency"),
					prefix("currency_tinted")).withScale(2F));
		addRendererMap(
				VTCosmetics.ICON_EXCLAMATION,
				new OverheadIconRenderer<>(
					prefix("exclamation"),
					prefix("exclamation_tinted")).withScale(3F));
		addRendererMap(
				VTCosmetics.ICON_QUESTION,
				new OverheadIconRenderer<>(
					prefix("question"),
					prefix("question_tinted")).withScale(2.5F));
		addRendererMap(
				VTCosmetics.ICON_SQUARE,
				new OverheadIconRenderer<>(
					prefix("square"),
					prefix("square_tinted")));
		addRendererMap(
				VTCosmetics.ICON_CIRCLE,
				new OverheadIconRenderer<>(
					prefix("circle"),
					prefix("circle_tinted")));
		addRendererMap(
				VTCosmetics.ICON_TRIANGLE,
				new OverheadIconRenderer<>(
					prefix("triangle"),
					prefix("triangle_tinted")));
		addRendererMap(
				VTCosmetics.ICON_HEXAGON,
				new OverheadIconRenderer<>(
					prefix("hexagon"),
					prefix("hexagon_tinted")));
	}
}
