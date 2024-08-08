package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.ability.IPhasingAbility;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	
	@Inject(method = "getInWallBlockState(Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;", at = @At("HEAD"), cancellable = true)
	private static void vt$getInWallBlockState(final PlayerEntity player, final CallbackInfoReturnable<BlockState> ci)
	{
		if(IPhasingAbility.isActivelyPhasing(mc.player, (state, world, mutable) -> state.getRenderType() == BlockRenderType.INVISIBLE || !state.shouldBlockVision(world, mutable)))
			ci.setReturnValue(null);
	}
}
