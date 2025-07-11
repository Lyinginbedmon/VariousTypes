package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.lying.VariousTypes;
import com.lying.item.CharacterSheetItem;
import com.lying.reference.Reference;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class VTItems
{
	public static final DeferredRegister<Item> ITEMS		= DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.ITEM);
	public static final DeferredRegister<ItemGroup> TABS	= DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.ITEM_GROUP);
	private static int tally;
	
	public static final RegistrySupplier<ItemGroup> VT_TAB = TABS.register(Reference.ModInfo.MOD_ID, () -> CreativeTabRegistry.create(
			Text.translatable("itemGroup."+Reference.ModInfo.MOD_ID+".item_group"), 
			() -> new ItemStack(VTItems.SHEET.get())));
	
	public static final RegistrySupplier<Item> SHEET	= register("character_sheet", settings -> new CharacterSheetItem(settings));
	
	public static final RegistrySupplier<Item> SMOKE	= registerBlock("smoke", VTBlocks.SMOKE);
	
	private static RegistrySupplier<Item> register(String nameIn, Function<Item.Settings, Item> supplierIn)
	{
		return register(prefix(nameIn), supplierIn);
	}
	
	private static RegistrySupplier<Item> register(Identifier nameIn, Function<Item.Settings, Item> supplierIn)
	{
		tally++;
		return ITEMS.register(nameIn, () -> supplierIn.apply(new Item.Settings().arch$tab(VT_TAB)));
	}
	
	private static RegistrySupplier<Item> registerBlock(String nameIn, RegistrySupplier<Block> blockIn)
	{
		return registerBlockNoItem(nameIn, blockIn, UnaryOperator.identity());
	}
	
	private static RegistrySupplier<Item> registerBlockNoItem(String nameIn, RegistrySupplier<Block> blockIn, UnaryOperator<Item.Settings> settingsOp)
	{
		return register(nameIn, settings -> new BlockItem(blockIn.get(), settingsOp.apply(settings)));
	}
	
	public static void init()
	{
		TABS.register();
		ITEMS.register();
		VariousTypes.LOGGER.info(" # Registered {} items", tally);
	}
}
