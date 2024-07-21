package com.lying.client.screen;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.client.screen.TemplateListWidget.TemplateEntry;
import com.lying.template.Template;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;

public class TemplateListWidget extends ElementListWidget<TemplateEntry>
{
	public TemplateListWidget(MinecraftClient client, int width, int height, int y)
	{
		super(client, width, height, y, 25);
	}
	
	public void addEntry(Template spec)
	{
		addEntry(new TemplateEntry(spec));
	}
	
	public void clear() { clearEntries(); }

	public static class TemplateEntry extends ElementListWidget.Entry<TemplateEntry>
	{
		private final Template template;
		
		public TemplateEntry(Template templateIn)
		{
			template = templateIn;
		}
		
		public List<? extends Element> children()
		{
			// TODO Auto-generated method stub
			return Lists.newArrayList();
		}

		@Override
		public List<? extends Selectable> selectableChildren()
		{
			// TODO Auto-generated method stub
			return Lists.newArrayList();
		}

		@Override
		public void render(DrawContext var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, boolean var9, float var10)
		{
			// TODO Auto-generated method stub
			
		}
	}
}
