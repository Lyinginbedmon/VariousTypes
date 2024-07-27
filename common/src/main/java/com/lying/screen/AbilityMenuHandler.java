package com.lying.screen;

import com.lying.init.VTScreenHandlerTypes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;

public class AbilityMenuHandler extends ScreenHandler
{
	public AbilityMenuHandler(int syncId)
	{
		super(VTScreenHandlerTypes.ABILITY_MENU_HANDLER.get(), syncId);
	}
	
	public ItemStack quickMove(PlayerEntity var1, int var2) { return ItemStack.EMPTY; }
	
	public boolean canUse(PlayerEntity var1) { return true; }
}
