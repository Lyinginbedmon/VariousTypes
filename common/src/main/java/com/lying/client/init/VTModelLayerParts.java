package com.lying.client.init;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.model.GelatinousBipedEntityModel;
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
