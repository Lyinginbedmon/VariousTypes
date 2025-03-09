package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.client.VariousTypesClient;
import com.lying.client.renderer.IconSpriteManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Shadow
	public TextureManager getTextureManager() { return null; }
	
	@Inject(method = "<init>(Lnet/minecraft/client/RunArgs;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/PaintingManager;<init>(Lnet/minecraft/client/texture/TextureManager;)V"))
	private void vt$Constructor(RunArgs args, final CallbackInfo ci)
	{
		IconSpriteManager.init(getTextureManager());
	}
	
	@Inject(method = "hasOutline(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private void vt$hasOutline(Entity entity, final CallbackInfoReturnable<Boolean> ci)
	{
		if(!ci.getReturnValueZ() && VariousTypesClient.ENTITY_HIGHLIGHTS.isHighlighted(entity.getUuid()))
			ci.setReturnValue(true);
	}
}
