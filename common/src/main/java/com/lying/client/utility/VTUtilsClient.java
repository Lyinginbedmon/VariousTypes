package com.lying.client.utility;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;

public class VTUtilsClient
{
	public static void renderDemoEntity(@Nullable PlayerEntity owner, DrawContext context, int mouseX, int mouseY, int renderX, int renderY)
	{
		// TODO Exchange for animated biped model using same player skin
		if(owner == null)
			return;
		
		int displayWidth = 200;
		int displayHeight = 200;
		int size = 80;
		InventoryScreen.drawEntity(context, renderX - (displayWidth / 2), renderY - (displayHeight / 2), renderX + (displayWidth / 2), renderY + (displayHeight / 2), size, 0.0625f, mouseX, mouseY, owner);
	}
}
