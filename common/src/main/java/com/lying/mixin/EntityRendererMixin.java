package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.client.utility.ClientEvents;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
	@Inject(method = "getShadowRadius(Lnet/minecraft/entity/Entity;)F", at = @At("HEAD"), cancellable = true)
	private void vt$getShadowRadius(Entity entity, final CallbackInfoReturnable<Float> ci)
	{
		if(entity.getType() == EntityType.PLAYER && ClientEvents.Rendering.PLAYER_RENDER_PERMISSION.invoker().shouldPlayerRender((PlayerEntity)entity).isFalse())
			ci.setReturnValue(0F);
	}
}
