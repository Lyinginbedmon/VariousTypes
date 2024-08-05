package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.ability.IBlockCollisionAbility;
import com.lying.init.VTSheetElements;

import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

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
			for(AbilityInstance inst : sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).getAbilitiesOfClass(IBlockCollisionAbility.class))
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
}
