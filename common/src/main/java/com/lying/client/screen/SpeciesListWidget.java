package com.lying.client.screen;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.client.screen.SpeciesListWidget.SpeciesEntry;
import com.lying.species.Species;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;

public class SpeciesListWidget extends ElementListWidget<SpeciesEntry>
{
	public SpeciesListWidget(MinecraftClient client, int width, int height, int y)
	{
		super(client, width, height, y, 25);
	}
	
	public void addEntry(Species spec)
	{
		addEntry(new SpeciesEntry(spec));
	}

	public static class SpeciesEntry extends ElementListWidget.Entry<SpeciesEntry>
	{
		private final Species species;
		
		public SpeciesEntry(Species speciesIn)
		{
			species = speciesIn;
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
