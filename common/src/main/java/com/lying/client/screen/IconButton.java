package com.lying.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class IconButton extends ButtonWidget
{
	private final Identifier texture, hoveredTexture;
	
	public IconButton(int x, int y, int width, int height, PressAction onPress, Identifier textureIn, Identifier textureHoveredIn)
	{
		super(x, y, width, height, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
		texture = textureIn;
		hoveredTexture = textureHoveredIn;
	}
	
	public void appendClickableNarrations(NarrationMessageBuilder var1)
	{
		this.appendDefaultNarrations(var1);
	}
	
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		float brightness = this.active ? 1F : 0.2F;
		context.drawTexturedQuad(isHovered() && this.active ? hoveredTexture : texture, getX(), getRight(), getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
	}
}