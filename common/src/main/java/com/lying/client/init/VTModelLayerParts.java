package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.model.EarsElfModel;
import com.lying.client.model.EarsPiglinModel;
import com.lying.client.model.GelatinousBipedEntityModel;
import com.lying.client.model.GillsAxolotlModel;
import com.lying.client.model.HornsHartebeestModel;
import com.lying.client.model.ModelFullbody;
import com.lying.client.model.NosePiglinModel;
import com.lying.client.model.TailDragonModel;
import com.lying.client.model.TailKirinModel;
import com.lying.client.model.wings.WingsAngelModel;
import com.lying.client.model.wings.WingsBatModel;
import com.lying.client.model.wings.WingsBeetleModel;
import com.lying.client.model.wings.WingsBirdModel;
import com.lying.client.model.wings.WingsButterflyModel;
import com.lying.client.model.wings.WingsDragonModel;
import com.lying.client.model.wings.WingsDragonflyModel;
import com.lying.client.model.wings.WingsElytraModel;
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
	
	public static final EntityModelLayer PLAYER_SLIME			= make("player", "slime", GelatinousBipedEntityModel::getTexturedModelData);
	
	public static final EntityModelLayer WINGS_BUTTERFLY		= make("player", "wings_butterfly", WingsButterflyModel::createBodyLayer);
	public static final EntityModelLayer WINGS_ELYTRA			= make("player", "wings_elytra", WingsElytraModel::createBodyLayer);
	public static final EntityModelLayer WINGS_DRAGONFLY		= make("player", "wings_dragonfly", WingsDragonflyModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BAT				= make("player", "wings_bat", WingsBatModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BIRD				= make("player", "wings_bird", WingsBirdModel::createBodyLayer);
	public static final EntityModelLayer WINGS_ANGEL			= make("player", "wings_angel", WingsAngelModel::createBodyLayer);
	public static final EntityModelLayer WINGS_BEETLE			= make("player", "wings_beetle", WingsBeetleModel::createBodyLayer);
	public static final EntityModelLayer WINGS_DRAGON			= make("player", "wings_dragon", WingsDragonModel::createBodyLayer);
	public static final EntityModelLayer WINGS_WITCH			= make("player", "wings_witch", WingsWitchModel::createBodyLayer);
	public static final EntityModelLayer WINGS_SKELETON			= make("player", "wings_skeleton", WingsSkeletonModel::createBodyLayer);
	
	public static final EntityModelLayer NOSE_PIG				= make("player", "nose_pig", NosePiglinModel::createPigNose);
	public static final EntityModelLayer NOSE_PIGLIN			= make("player", "nose_piglin", NosePiglinModel::createPiglinNose);
	
	public static final EntityModelLayer EARS_PIGLIN			= make("player", "ears_piglin", EarsPiglinModel::createBodyLayer);
	public static final EntityModelLayer GILLS_AXOLOTL			= make("player", "gills_axolotl", GillsAxolotlModel::createBodyLayer);
	public static final EntityModelLayer EARS_ELF				= make("player", "ears_elf", EarsElfModel::createBodyLayer);
	
	public static final EntityModelLayer HORNS_HARTEBEEST		= make("player", "horns_hartebeest", HornsHartebeestModel::createBodyLayer);
	
	public static final EntityModelLayer TAIL_DRAGON			= make("player", "tail_dragon", TailDragonModel::createBodyLayer);
	public static final EntityModelLayer TAIL_KIRIN				= make("player", "tail_kirin", TailKirinModel::createBodyLayer);
	
	public static final EntityModelLayer FULLBODY_SLIM			= make("player", "fullbody_slim", () -> ModelFullbody.createBodyLayer(true));
	public static final EntityModelLayer FULLBODY_WIDE			= make("player", "fullbody_wide", () -> ModelFullbody.createBodyLayer(false));
	
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
