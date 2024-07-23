package com.lying.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ElementListWidget;

public abstract class GuiAbstractList<E extends GuiAbstractList.ListEntry<E>> extends ElementListWidget<E>
{
	protected GuiAbstractList(MinecraftClient client, int width, int height, int y, int itemHeight)
	{
		super(client, width, height, y, itemHeight);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(!this.active)
			return false;
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public int entryCount() { return getEntryCount(); }
	
	public boolean isEmpty() { return entryCount() == 0; }
	
	public void clear() { clearEntries(); }
	
	public void setPosition(int x, int y)
	{
		super.setPosition(x, y);
		for(int i=0; i<getEntryCount(); i++)
			children().get(i).updatePosition(getRowLeft(), getRowTop(i));
	}
	
	public static abstract class ListEntry<E extends ListEntry<E>> extends ElementListWidget.Entry<E>
	{
		public abstract void updatePosition(int x, int y);
	}
}
