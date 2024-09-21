package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.VariousTypes;
import com.lying.client.event.RenderEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
	@Shadow
	private MinecraftClient client;
	
	@Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("HEAD"))
	private void vt$renderHead(DrawContext context, float tickDelta, final CallbackInfo ci)
	{
		RenderEvents.BEFORE_HUD_RENDER_EVENT.invoker().render(client, client.player, VariousTypes.getSheet(client.player), tickDelta, context);
	}
	
	@Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("TAIL"))
	private void vt$renderTail(DrawContext context, float tickDelta, final CallbackInfo ci)
	{
		RenderEvents.AFTER_HUD_RENDER_EVENT.invoker().render(client, client.player, VariousTypes.getSheet(client.player), tickDelta, context);
	}
}
