package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.NosePiglinModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.init.VTCosmetics;
import com.lying.utility.Cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.ItemTags;

public class NoseFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	public NoseFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(Cosmetic.Type.NOSE, context);
		populateRendererMap();
	}
	
	protected void populateRendererMap()
	{
		EntityModelLoader loader = MinecraftClient.getInstance().getEntityModelLoader();
		
		NosePiglinModel<E> piglinModel = new NosePiglinModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIGLIN));
		
		addRendererMap(
				VTCosmetics.NOSE_PIG,
				AccessoryBasic.create(
					new NosePiglinModel<>(loader.getModelPart(VTModelLayerParts.NOSE_PIG)),
					prefix("textures/entity/nose/pig.png"),
					prefix("textures/entity/nose/pig_tinted.png")));
		addRendererMap(
				VTCosmetics.NOSE_PIGLIN,
				AccessoryCompound.create(
					AccessoryBasic.create(
						piglinModel,
						prefix("textures/entity/nose/piglin.png"),
						prefix("textures/entity/nose/piglin_tinted.png")),
					AccessoryBasic.create(
						piglinModel, 
						prefix("textures/entity/nose/piglin_overlay.png")).untinted()));
		addRendererMap(
				VTCosmetics.NOSE_PIGZOMBIE,
				AccessoryCompound.create(
					AccessoryBasic.create(
						piglinModel,
						prefix("textures/entity/nose/zombified_piglin.png"),
						prefix("textures/entity/nose/zombified_piglin_tinted.png")),
					AccessoryBasic.create(
						piglinModel, 
						prefix("textures/entity/nose/zombified_piglin_overlay.png")).untinted()));
	}
	
	public boolean shouldRender(E entity) { return !entity.getEquippedStack(EquipmentSlot.HEAD).isIn(ItemTags.SKULLS); }
}
