package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.ability.IBlockCollisionAbility;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(AbstractBlockState.class)
public class AbstractBlockStateMixin
{
	@Inject(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private void vt$getCollisionShape(BlockView world, BlockPos pos, ShapeContext context, final CallbackInfoReturnable<VoxelShape> ci)
	{
		if(!(context instanceof EntityShapeContext))
			return;
		
		EntityShapeContext entityContext = (EntityShapeContext)context;
		if(!(entityContext.getEntity() instanceof LivingEntity))
			return;
		
		LivingEntity living = (LivingEntity)entityContext.getEntity();
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			for(AbilityInstance inst : IBlockCollisionAbility.getCollisionAbilities(living))
			{
				Optional<VoxelShape> result = ((IBlockCollisionAbility)inst.ability()).getCollisionFor((BlockState)(Object)this, pos, living, inst);
				if(result.isPresent())
				{
					ci.setReturnValue(result.get());
					return;
				}
			}
		});
	}
	
	@Inject(method = "onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
	private void vt$onEntityCollision(World world, BlockPos pos, Entity entity, final CallbackInfo ci)
	{
		if(entity instanceof LivingEntity)
			VariousTypes.getSheet((LivingEntity)entity).ifPresent(sheet -> 
			{
				if(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).hasAbility(VTAbilities.INTANGIBLE.get().registryName()))
					ci.cancel();
			});
	}
}
