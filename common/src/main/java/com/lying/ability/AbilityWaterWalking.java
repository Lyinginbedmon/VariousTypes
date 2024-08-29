package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.data.VTTags;
import com.lying.utility.VTUtils;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class AbilityWaterWalking extends Ability implements IBlockCollisionAbility
{
	public AbilityWaterWalking(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.listToString(getFluidList(instance.memory()), tag -> Text.literal(tag.id().toString()), ", ")));
	}
	
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance)
	{
		return getFluidList(instance.memory()).stream().anyMatch(tag -> state.getFluidState().isIn(tag) || state.getFluidState().isEmpty() && tag == VTTags.AIR) && !state.isIn(VTTags.UNPHASEABLE);
	}
	
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance)
	{
		/** Ignore if the entity is sneaking, swimming, below the block, or the block is part of a vertical stack of the same block (ie. not the surface) */
		if(living.isSneaking() || living.isSwimming() || pos.getY() >= living.getBlockY() || living.getWorld().getBlockState(pos.up()).getBlock() == state.getBlock())
			return Optional.empty();
		
		return isApplicableTo(state, living, instance) ? Optional.of(VoxelShapes.fullCube()) : Optional.empty();
	}
	
	public static List<TagKey<Fluid>> getFluidList(NbtCompound memory)
	{
		return AbilityHelper.getTags(memory, "Fluids", RegistryKeys.FLUID, () -> List.of(FluidTags.WATER));
	}
}
