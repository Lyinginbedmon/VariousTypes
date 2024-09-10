package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.event.LivingEvents;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(Block.class)
public class BlockMixin
{
	@Inject(method = "onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
	public void vt$onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity, final CallbackInfo ci)
	{
		if(!(entity instanceof LivingEntity))
			return;
		
		LivingEvents.ON_STEP_ON_BLOCK_EVENT.invoker().onBlockSteppedOn((LivingEntity)entity, state, pos, world);
	}
}
