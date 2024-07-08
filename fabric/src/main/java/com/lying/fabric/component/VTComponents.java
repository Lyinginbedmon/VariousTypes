package com.lying.fabric.component;

import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

import com.lying.reference.Reference;

import net.minecraft.entity.player.PlayerEntity;

public class VTComponents implements EntityComponentInitializer
{
	public static final ComponentKey<PlayerSheetHandler> CHARACTER_SHEET = ComponentRegistry.getOrCreate(Reference.ModInfo.prefix("character_sheet"), PlayerSheetHandler.class);
	
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
	{
		registry.registerFor(PlayerEntity.class, CHARACTER_SHEET, PlayerSheetHandler::new);
	}
}
