package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.client.utility.EntityHighlights;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Inject(method = "hasOutline(Lnet/minecraft/entity/Entity;)Z", at = @At("RETURN"), cancellable = true)
	private void vt$hasOutline(Entity entity, final CallbackInfoReturnable<Boolean> ci)
	{
		if(!ci.getReturnValueZ() && EntityHighlights.isHighlighted(entity.getUuid()))
			ci.setReturnValue(true);
	}
}
