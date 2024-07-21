package com.lying.client.utility;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.client.screen.DetailObject;
import com.lying.client.screen.DetailObject.Batch;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;

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
	
	public static <T extends Object> DetailObject listToDetail(List<T> set, Function<T,MutableText[]> provider)
	{
		List<Batch> entries = Lists.newArrayList();
		set.forEach(entry -> entries.add(new Batch(provider.apply(entry))));
		return new DetailObject(entries.toArray(new Batch[0]));
	}
}
