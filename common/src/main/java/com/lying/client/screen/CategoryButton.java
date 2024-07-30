package com.lying.client.screen;

import com.lying.ability.Ability.Category;
import com.lying.mixin.IDrawContextInvoker;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CategoryButton extends ButtonWidget
{
	private final Category category;
	private final int iconRenderSize;
	
	public CategoryButton(int x, int y, Category category, PressAction onPress)
	{
		super(x, y, 18, 18, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
		this.category = category;
		this.iconRenderSize = getWidth() - 4;
	}
	
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.renderWidget(context, mouseX, mouseY, delta);
		float v = this.active ? 1F : 0.5F;
		
		int iconX = getX() + (getWidth() - iconRenderSize) / 2;
		int iconY = getY() + (getHeight() - iconRenderSize) / 2;
		((IDrawContextInvoker)context).drawTexRGBA(category.icon(), iconX, iconX + iconRenderSize, iconY, iconY + iconRenderSize, 0, 0F, 1F, 0F, 1F, v, v, v, 1F);
	}
}
