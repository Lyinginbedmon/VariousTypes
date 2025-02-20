package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.model.GelatinousBipedEntityModel;
import com.lying.client.model.WingsAngelModel;
import com.lying.client.model.WingsBatModel;
import com.lying.client.model.WingsBeetleModel;
import com.lying.client.model.WingsBirdModel;
import com.lying.client.model.WingsButterflyModel;
import com.lying.client.model.WingsDragonModel;
import com.lying.client.model.WingsDragonflyModel;
import com.lying.client.model.WingsElytraModel;
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
