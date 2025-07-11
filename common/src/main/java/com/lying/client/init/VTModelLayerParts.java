package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.model.GelatinousBipedEntityModel;
import com.lying.client.model.ModelFullbody;
import com.lying.client.model.SimpleHornsModel;
import com.lying.client.model.SimpleNoseModel;
import com.lying.client.model.ears.EarsPiglinModel;
import com.lying.client.model.ears.EarsRabbitModel;
import com.lying.client.model.ears.GillsAxolotlModel;
import com.lying.client.model.ears.SimpleEarsModel;
import com.lying.client.model.tail.SimpleTailModel;
import com.lying.client.model.tail.TailAxolotlModel;
import com.lying.client.model.tail.TailDragonModel;
import com.lying.client.model.tail.TailKirinModel;
import com.lying.client.model.tail.TailRabbitModel;
import com.lying.client.model.tail.TailRatModel;
import com.lying.client.model.tail.TailScorpionModel;
import com.lying.client.model.tail.TailWhaleModel;
import com.lying.client.model.wings.MiscHaloModel;
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
import com.lying.reference.Reference;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class VTModelLayerParts
{
	private static final Map<EntityModelLayer, Supplier<TexturedModelData>> LAYERS = new HashMap<>();
	
	public static final EntityModelLayer ANIMATED_PLAYER		= make("animated_player", "player", () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, false));
	public static final EntityModelLayer ANIMATED_PLAYER_SLIM	= make("animated_player", "player_slim", () -> AnimatedPlayerEntityModel.createBodyLayer(Dilation.NONE, true));
	
	public static final EntityModelLayer PLAYER_SLIME			= make("slime", GelatinousBipedEntityModel::getTexturedModelData);
	
	public static final EntityModelLayer WINGS_BUTTERFLY		= make("wings_butterfly", WingsButterflyModel::createBodyLayer);
	public static final EntityModelLayer WINGS_ELYTRA			= make("wings_elytra", WingsElytraModel::createBodyLayer);
	public static final EntityModelLayer WINGS_DRAGONFLY		= make("wings_dragonfly", WingsDragonflyModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BAT				= make("wings_bat", WingsBatModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BIRD				= make("wings_bird", WingsBirdModel::createBodyLayer);
	public static final EntityModelLayer MISC_HALO				= make("wings_angel", MiscHaloModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BEETLE			= make("wings_beetle", WingsBeetleModel::createBodyLayer);
	public static final EntityModelLayer WINGS_DRAGON			= make("wings_dragon", WingsDragonModel::createBodyLayer);
	public static final EntityModelLayer WINGS_WITCH			= make("wings_witch", WingsWitchModel::createBodyLayer);
	public static final EntityModelLayer WINGS_SKELETON			= make("wings_skeleton", WingsSkeletonModel::createBodyLayer);
	public static final EntityModelLayer WINGS_ENERGY			= make("wings_energy", WingsEnergyModel::createBodyLayer);
	public static final EntityModelLayer WINGS_FAIRY_LOG		= make("wings_fairy_log", WingsFairyModel::createLogLayer);
	public static final EntityModelLayer WINGS_FAIRY_LEAF		= make("wings_fairy_leaf", WingsFairyModel::createLeafLayer);
	
	public static final EntityModelLayer NOSE_PIG				= make("nose_pig", SimpleNoseModel::createPigNose);
	public static final EntityModelLayer NOSE_PIGLIN			= make("nose_piglin", SimpleNoseModel::createPiglinNose);
	public static final EntityModelLayer NOSE_VILLAGER			= make("nose_villager", SimpleNoseModel::createVillagerNose);
	public static final EntityModelLayer NOSE_WITCH				= make("nose_witch", SimpleNoseModel::createWitchNose);
	
	public static final EntityModelLayer EARS_PIGLIN			= make("ears_piglin", EarsPiglinModel::createBodyLayer);
	public static final EntityModelLayer GILLS_AXOLOTL			= make("gills_axolotl", GillsAxolotlModel::createBodyLayer);
	public static final EntityModelLayer EARS_ELF				= make("ears_elf", SimpleEarsModel::createElfEars);
	public static final EntityModelLayer EARS_GOBLIN			= make("ears_goblin", SimpleEarsModel::createGoblinEars);
	public static final EntityModelLayer EARS_CAT				= make("ears_cat", SimpleEarsModel::createCatEars);
	public static final EntityModelLayer EARS_FOX				= make("ears_fox", SimpleEarsModel::createFoxEars);
	public static final EntityModelLayer EARS_WOLF				= make("ears_wolf", SimpleEarsModel::createWolfEars);
	public static final EntityModelLayer EARS_RABBIT			= make("ears_rabbit", EarsRabbitModel::createBodyLayer);
	
	public static final EntityModelLayer HORNS_HARTEBEEST		= make("horns_hartebeest", SimpleHornsModel::createSaigaHorns);
	public static final EntityModelLayer HORNS_RAM				= make("horns_ram", SimpleHornsModel::createRamHorns);
	public static final EntityModelLayer HORNS_STAG				= make("horns_stag", SimpleHornsModel::createStagHorns);
	public static final EntityModelLayer HORNS_KIRIN			= make("horns_kirin", SimpleHornsModel::createKirinHorns);
	public static final EntityModelLayer HORN_UNICORN			= make("horn_unicorn", SimpleHornsModel::createUnicornHorn);
	public static final EntityModelLayer HORNS_DEVIL			= make("horns_devil", SimpleHornsModel::createDevilHorns);
	
	public static final EntityModelLayer TAIL_DRAGON			= make("tail_dragon", TailDragonModel::createBodyLayer);
	public static final EntityModelLayer TAIL_KIRIN				= make("tail_kirin", TailKirinModel::createBodyLayer);
	public static final EntityModelLayer TAIL_RAT				= make("tail_rat", TailRatModel::createBodyLayer);
	public static final EntityModelLayer TAIL_FOX				= make("tail_fox", SimpleTailModel.Fox::createFoxTail);
	public static final EntityModelLayer TAIL_WOLF				= make("tail_wolf", SimpleTailModel.Wolf::createWolfTail);
	public static final EntityModelLayer TAIL_AXOLOTL			= make("tail_axolotl", TailAxolotlModel::createBodyLayer);
	public static final EntityModelLayer TAIL_WHALE				= make("tail_whale", TailWhaleModel::createBodyLayer);
	public static final EntityModelLayer TAIL_RABBIT			= make("tail_rabbit", TailRabbitModel::createBodyLayer);
	public static final EntityModelLayer TAIL_SCORPION			= make("tail_scorpion", TailScorpionModel::createBodyLayer);
	
	public static final EntityModelLayer FULLBODY_SLIM			= make("fullbody_slim", () -> ModelFullbody.createBodyLayer(true));
	public static final EntityModelLayer FULLBODY_WIDE			= make("fullbody_wide", () -> ModelFullbody.createBodyLayer(false));
	
	private static EntityModelLayer make(String name, Supplier<TexturedModelData> supplier)
	{
		return make("player", name, supplier);
	}
	
	/** Creates a new model layer and registers its supplier for generation */
	private static EntityModelLayer make(String id, String name, Supplier<TexturedModelData> supplier)
	{
		EntityModelLayer layer = new EntityModelLayer(Reference.ModInfo.prefix(id), name);
		LAYERS.put(layer, supplier);
		return layer;
	}
	
	public static void init(BiConsumer<EntityModelLayer, Supplier<TexturedModelData>> consumer)
	{
		LAYERS.entrySet().forEach(entry -> consumer.accept(entry.getKey(), entry.getValue()));
	}
}
