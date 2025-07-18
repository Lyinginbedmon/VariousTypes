package com.lying.client.renderer;

import java.util.function.Supplier;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.wings.WingsBatModel;
import com.lying.client.model.wings.WingsBeetleModel;
import com.lying.client.model.wings.WingsBirdModel;
import com.lying.client.model.wings.WingsButterflyModel;
import com.lying.client.model.wings.WingsDragonModel;
import com.lying.client.model.wings.WingsDragonflyModel;
import com.lying.client.model.wings.WingsElytraModel;
import com.lying.client.model.wings.WingsEnergyModel;
import com.lying.client.model.wings.WingsFairyModel;
import com.lying.client.model.wings.WingsSkeletonModel;
import com.lying.client.model.wings.WingsWitchModel;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryBlockTexture;
import com.lying.client.renderer.accessory.AccessoryCompound;
import com.lying.client.renderer.accessory.AccessoryEndPortal;
import com.lying.client.renderer.accessory.AccessoryGlowing;
import com.lying.client.renderer.accessory.AccessoryTranslucent;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;
import com.lying.utility.Cosmetic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.entity.LivingEntity;

public class WingsFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private EntityModel<LivingEntity> 
		elytraWings, 
		butterflyWings, 
		dragonflyWings, 
		batWings, 
		birdWings, 
		beetleWings, 
		witchWings, 
		dragonWings, 
		skeletonWings,
		energyWings,
		fairyWingsLog, fairyWingsLeaf;
	
	public WingsFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.WINGS, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		elytraWings = new WingsElytraModel<>(loader.getModelPart(VTModelLayerParts.WINGS_ELYTRA));
		butterflyWings = new WingsButterflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BUTTERFLY));
		dragonflyWings = new WingsDragonflyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGONFLY));
		batWings = new WingsBatModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BAT));
		birdWings = new WingsBirdModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BIRD));
		witchWings = new WingsWitchModel<>(loader.getModelPart(VTModelLayerParts.WINGS_WITCH));
		skeletonWings = new WingsSkeletonModel<>(loader.getModelPart(VTModelLayerParts.WINGS_SKELETON));
		beetleWings = new WingsBeetleModel<>(loader.getModelPart(VTModelLayerParts.WINGS_BEETLE));
		dragonWings = new WingsDragonModel<>(loader.getModelPart(VTModelLayerParts.WINGS_DRAGON));
		energyWings = new WingsEnergyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_ENERGY));
		fairyWingsLog = new WingsFairyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_FAIRY_LOG));
		fairyWingsLeaf = new WingsFairyModel<>(loader.getModelPart(VTModelLayerParts.WINGS_FAIRY_LEAF));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.WINGS_ELYTRA, 
				AccessoryBasic.create(
					e -> elytraWings,
					texture("wings/elytra.png"),
					texture("wings/elytra_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BUTTERFLY, 
				AccessoryBasic.create(
					e -> butterflyWings, 
					texture("wings/butterfly.png"), 
					texture("wings/butterfly_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_DRAGONFLY, 
				AccessoryTranslucent.create(
					e -> dragonflyWings,
					texture("wings/dragonfly.png"),
					texture("wings/dragonfly_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BAT, 
				AccessoryBasic.create(
					e -> batWings,
					texture("wings/bat.png"),
					texture("wings/bat_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BIRD,
				AccessoryBasic.create(
					e -> birdWings,
					texture("wings/bird.png"),
					texture("wings/bird_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_BEETLE,
				AccessoryCompound.create(
					AccessoryTranslucent.create(
						e -> beetleWings,
						texture("wings/beetle.png")).untinted(),
					AccessoryBasic.create(
						e -> beetleWings,
						texture("wings/beetle_overlay.png"),
						texture("wings/beetle_overlay_tinted.png"))));
		addRendererMap(
				VTCosmetics.WINGS_WITCH, 
				AccessoryEndPortal.create(
					e -> witchWings).untinted());
		addRendererMap(
				VTCosmetics.WINGS_ANGEL, 
				AccessoryBasic.create(
					e -> witchWings, 
					texture("wings/angel.png"), 
					texture("wings/angel_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_SKELETON,
				AccessoryBasic.create(
					e -> skeletonWings, 
					texture("wings/skeleton.png"),
					texture("wings/skeleton_tinted.png")));
		addRendererMap(
				VTCosmetics.WINGS_DRAGON,
				AccessoryCompound.create(
					AccessoryBasic.create(
						e -> dragonWings,
						texture("wings/dragon.png"),
						texture("wings/dragon_tinted.png")),
					AccessoryBasic.create(
						e -> dragonWings,
						texture("wings/dragon_overlay.png")).untinted()));
		addRendererMap(
				VTCosmetics.WINGS_ENERGY,
				AccessoryCompound.create(
					AccessoryBasic.create(
							e -> energyWings, 
							texture("wings/energy_overlay.png")).untinted(),
					AccessoryGlowing.create(
							e -> energyWings, 
							texture("wings/energy_glow.png"), 
							texture("wings/energy_glow_tinted.png"))));
		
		BlockColorProvider leafColor = (state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
		BlockColorProvider spruceColor = (state, world, pos, tintIndex) -> FoliageColors.getSpruceColor();
		BlockColorProvider birchColor = (state, world, pos, tintIndex) -> FoliageColors.getBirchColor();
		registerFairyWing(VTCosmetics.WINGS_FAIRY_OAK, Blocks.OAK_LOG, Blocks.OAK_LEAVES, leafColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_BIRCH, Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, birchColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_SPRUCE, Blocks.SPRUCE_LOG, Blocks.SPRUCE_LEAVES, spruceColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_JUNGLE, Blocks.JUNGLE_LOG, Blocks.JUNGLE_LEAVES, leafColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_ACACIA, Blocks.ACACIA_LOG, Blocks.ACACIA_LEAVES, leafColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_CHERRY, Blocks.CHERRY_LOG, Blocks.CHERRY_LEAVES);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_DARK_OAK, Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_LEAVES, leafColor);
		registerFairyWing(VTCosmetics.WINGS_FAIRY_MANGROVE, Blocks.MANGROVE_LOG, Blocks.MANGROVE_LEAVES, leafColor);
	}
	
	private void registerFairyWing(Supplier<Cosmetic> cosmetic, Block log, Block leaf)
	{
		addRendererMap(
				cosmetic,
				AccessoryCompound.create(
					AccessoryBlockTexture.create(
						e -> fairyWingsLog,
						() -> log).untinted(),
					AccessoryBlockTexture.create(
						e -> fairyWingsLeaf, 
						() -> leaf).untinted()));
	}
	
	private void registerFairyWing(Supplier<Cosmetic> cosmetic, Block log, Block leaf, BlockColorProvider tint)
	{
		addRendererMap(
				cosmetic,
				AccessoryCompound.create(
					AccessoryBlockTexture.create(
						e -> fairyWingsLog,
						() -> log).untinted(),
					AccessoryBlockTexture.create(
						e -> fairyWingsLeaf, 
						() -> leaf).tintFunc(tint)));
	}
}
