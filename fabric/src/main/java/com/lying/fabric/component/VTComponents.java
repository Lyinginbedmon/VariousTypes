package com.lying.fabric.component;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import com.lying.entity.AnimatedPlayerEntity;
import com.lying.reference.Reference;

public class VTComponents implements EntityComponentInitializer
{
	public static final ComponentKey<PlayerSheetHandler> CHARACTER_SHEET_PLAYER = ComponentRegistry.getOrCreate(Reference.ModInfo.prefix("character_sheet"), PlayerSheetHandler.class);
	public static final ComponentKey<MobSheetHandler> CHARACTER_SHEET_MOB = ComponentRegistry.getOrCreate(Reference.ModInfo.prefix("mob_character_sheet"), MobSheetHandler.class);
	
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
	{
		registry.registerForPlayers(CHARACTER_SHEET_PLAYER, PlayerSheetHandler::new);
		registry.registerFor(AnimatedPlayerEntity.class, CHARACTER_SHEET_MOB, MobSheetHandler::new);
	}
}
