package com.lying.init;

import com.lying.reference.Reference;
import com.lying.screen.AbilityMenuHandler;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.screen.CharacterSheetScreenHandler;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class VTScreenHandlerTypes
{
	public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.SCREEN_HANDLER);
	
	public static final RegistrySupplier<ScreenHandlerType<CharacterSheetScreenHandler>> CHARACTER_SCREEN_HANDLER = register("character_screen", new ScreenHandlerType<>((syncId, playerInventory) -> new CharacterSheetScreenHandler(syncId), FeatureFlags.VANILLA_FEATURES));
	public static final RegistrySupplier<ScreenHandlerType<CharacterCreationScreenHandler>> CREATION_SCREEN_HANDLER = register("creation_screen", new ScreenHandlerType<>((syncId, playerInventory) -> new CharacterCreationScreenHandler(syncId, playerInventory.player), FeatureFlags.VANILLA_FEATURES));
	public static final RegistrySupplier<ScreenHandlerType<AbilityMenuHandler>> ABILITY_MENU_HANDLER	= register("ability_menu", new ScreenHandlerType<>((syncId, playerInventory) -> new AbilityMenuHandler(syncId), FeatureFlags.VANILLA_FEATURES));
	
	private static <T extends ScreenHandler> RegistrySupplier<ScreenHandlerType<T>> register(String nameIn, ScreenHandlerType<T> typeIn)
	{
		return SCREEN_HANDLERS.register(Reference.ModInfo.prefix(nameIn), () -> typeIn);
	}
	
	public static void init()
	{
		SCREEN_HANDLERS.register();
	}
}
