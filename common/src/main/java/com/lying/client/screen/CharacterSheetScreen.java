package com.lying.client.screen;

import com.lying.VariousTypes;
import com.lying.screen.CharacterSheetScreenHandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class CharacterSheetScreen extends CharacterSheetDisplayScreen<CharacterSheetScreenHandler>
{
	public CharacterSheetScreen(CharacterSheetScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(VariousTypes.getSheet(inventory.player).get(), handler, inventory, title);
	}
}
