package com.lying.client.screen;

import com.lying.mixin.IDrawContextInvoker;
import com.lying.reference.Reference;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class InspectButton extends ButtonWidget
{
	private static final Identifier TEXTURE = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/inspect.png");
	private static final Identifier TEXTURE_HOVERED = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/inspect_hovered.png");
	
	public InspectButton(int x, int y, int width, int height, PressAction onPress)
	{
		super(x, y, width, height, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
	}
	
	public void appendClickableNarrations(NarrationMessageBuilder var1)
	{
		this.appendDefaultNarrations(var1);
	}
	
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		float brightness = this.active ? 1F : 0.2F;
		((IDrawContextInvoker)context).drawTexRGBA(isHovered() && this.active ? TEXTURE_HOVERED : TEXTURE, getX(), getRight(), getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
	}
}
