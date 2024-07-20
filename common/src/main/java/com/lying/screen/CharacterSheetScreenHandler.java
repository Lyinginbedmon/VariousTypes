package com.lying.screen;

import com.lying.init.VTScreenHandlerTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class CharacterSheetScreenHandler extends ScreenHandler
{
	public CharacterSheetScreenHandler(int syncId)
	{
		super(VTScreenHandlerTypes.CHARACTER_SCREEN_HANDLER.get(), syncId);
	}
	
	public ItemStack quickMove(PlayerEntity var1, int var2) { return ItemStack.EMPTY; }
	
	public boolean canUse(PlayerEntity var1) { return true; }
}
