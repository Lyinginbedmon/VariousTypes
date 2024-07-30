package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

@Mixin(DrawContext.class)
public interface IDrawContextInvoker
{
	@Invoker("drawTexturedQuad")
	public void drawTexRGBA(Identifier texture, int x1, int x2, int y1, int y2, int z, float u1, float u2, float v1, float v2, float red, float green, float blue, float alpha);
}
