package com.lying.init;

import java.util.function.Supplier;

import com.lying.item.CharacterSheetItem;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;

public class VTItems
{
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.ITEM);
	
	public static final RegistrySupplier<Item> SHEET	= register("character_sheet", () -> new CharacterSheetItem(new Item.Settings()));
	
	private static RegistrySupplier<Item> register(String nameIn, Supplier<Item> supplierIn)
	{
		return ITEMS.register(Reference.ModInfo.prefix(nameIn), supplierIn);
	}
	
	public static void init()
	{
		ITEMS.register();
	}
}
