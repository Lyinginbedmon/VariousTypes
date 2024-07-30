package com.lying.client.screen;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.client.VariousTypesClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

public class DetailObject
{
	private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
	
	private final int SPACING = 10;
	private final List<Batch> entries = Lists.newArrayList();
	
	public DetailObject(MutableText... entriesIn)
	{
		entries.add(new Batch(entriesIn));
	}
	
	public DetailObject(Batch... batches)
	{
		for(Batch batch : batches)
			entries.add(batch);
	}
	
	public void render(DrawContext context, int x, int y, int maxEntryWidth)
	{
		for(Batch batch : entries)
		{
			batch.render(context, x, y, maxEntryWidth);
			y += batch.backgroundHeight(maxEntryWidth) + SPACING;
		}
	}
	
	public int totalHeight(int maxEntryWidth)
	{
		int height = SPACING * (entries.size() - 1);
		for(Batch batch : entries)
			height += batch.backgroundHeight(maxEntryWidth);
		return height;
	}
	
	/**
	 * An individual rendered tooltip as part of a DetailObject
	 */
	public static class Batch
	{
		private static int PADDING = 5;
		private final List<Text> entries = Lists.newArrayList();
		
		public Batch(MutableText... entriesIn)
		{
			for(MutableText text : entriesIn)
				entries.add(text);
		}
		
		public int backgroundHeight(int maxEntryWidth) { return getConstrainedEntries(maxEntryWidth).size() * textRenderer.fontHeight + PADDING; }
		
		public static int backgroundWidth(List<TooltipComponent> entries, int maxEntryWidth, TextRenderer textRenderer)
		{
			int maxWidth = 0;
			for(TooltipComponent text : entries)
				if(text.getWidth(textRenderer) > maxWidth)
					maxWidth = text.getWidth(textRenderer);
			return maxWidth + PADDING;
		}
		
		private List<TooltipComponent> getConstrainedEntries(int maxEntryWidth)
		{
			List<OrderedText> wrapped = Lists.newArrayList();
			for(Text text : entries)
				wrapped.addAll(textRenderer.wrapLines(text, maxEntryWidth));
			return wrapped.stream().map(TooltipComponent::of).collect(Util.toArrayList());
		}
		
		public void render(DrawContext context, int x, int y, int maxEntryWidth)
		{
			List<TooltipComponent> entries = getConstrainedEntries(maxEntryWidth);
			int backgroundWidth = backgroundWidth(entries, maxEntryWidth, textRenderer);
			int backgroundHeight = backgroundHeight(maxEntryWidth);
			TooltipComponent comp;
			MatrixStack matrices = context.getMatrices();
			matrices.push();
				context.drawTooltip(textRenderer, Text.empty(), x + 0xFFFFFF, y);	// XXX Don't ask me why, but backgrounds fail to render promptly w/out this
				TooltipBackgroundRenderer.render(context, x - backgroundWidth / 2, y, backgroundWidth, backgroundHeight, 400);
				matrices.translate(0, 0, 400);
				int textY = y + (PADDING / 2);
				int textWidth = (backgroundWidth - PADDING);
				for(TooltipComponent text : entries)
				{
					comp = text;
					int textX = x;
					switch(VariousTypesClient.config.textAlignment())
					{
						case CENTRE:
							textX = x - text.getWidth(textRenderer) / 2;
							break;
						case RIGHT:
							textX = x + (textWidth / 2) - text.getWidth(textRenderer);
							break;
						default:
						case LEFT:
							textX = x - (textWidth / 2);
							break;
					}
					
					comp.drawText(textRenderer, textX, textY, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers());
					textY += textRenderer.fontHeight;
				}
			matrices.pop();
		}
	}
}