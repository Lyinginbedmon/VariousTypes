package com.lying.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilityType;
import com.lying.data.VTTags;
import com.lying.init.VTSheetElements;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/** A variant of {@link IBlockCollisionAbility} that specifically deals with moving through otherwise-impassable blocks */
public interface IPhasingAbility extends IBlockCollisionAbility
{
	/** Checks the player's abilities to find at least one IPhasingAbility that is applicable to the blockstate at their eyes */
	public static boolean isPhasingAtEyes(LivingEntity living)
	{
		return isPhasingAtPos(living, living.getBlockPos().add(0, (int)living.getEyeHeight(living.getPose()), 0));
	}
	
	public static Collection<AbilityInstance> getPhasingAbilities(LivingEntity living)
	{
		Map<Identifier, AbilityInstance> abilityMap = new HashMap<>();
		
		VariousTypes.getSheet((LivingEntity)living).ifPresent(sheet -> 
		{
			// Collect all passive abilities from main ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).getAbilitiesOfClass(IPhasingAbility.class).stream().
				filter(inst -> inst.ability().type() == AbilityType.PASSIVE).
					forEach(inst -> abilityMap.put(inst.mapName(), inst));
			
			// Collect all activated abilities from actionable ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ACTIONABLES).getAbilitiesOfClass(IPhasingAbility.class).
					forEach(inst -> abilityMap.put(inst.mapName(), inst));
		});
		
		return abilityMap.isEmpty() ? Lists.newArrayList() : abilityMap.values();
	}
	
	/** Checks if the player can phase through the block at the given position */
	public static boolean isPhasingAtPos(LivingEntity living, BlockPos pos)
	{
		final BlockState state = living.getWorld().getBlockState(pos);
		return !cannotEverBePhased(state) && getPhasingAbilities(living).stream().anyMatch(inst -> ((IPhasingAbility)inst.ability()).isApplicableTo(state, (LivingEntity)living, inst));
	}
	
	/** Returns true if the block should never be considered phaseable due to datapack tags */
	private static boolean cannotEverBePhased(BlockState state) { return state.isIn(VTTags.UNPHASEABLE) || state.isIn(BlockTags.WITHER_IMMUNE); }
	
	/** Returns true if the player has any phasing ability */
	public static boolean isPhasingAtAll(LivingEntity living) { return !getPhasingAbilities(living).isEmpty(); }
	
	/** Returns true if the given entity is phasing through any block within their bounding box */
	public static boolean isActivelyPhasing(LivingEntity living)
	{
		return isActivelyPhasing(living, (state, world, mutable) -> state.isAir() || state.getBlock() instanceof FluidBlock);
	}
	
	/** Returns true if the given entity is phasing through any block within their bounding box */
	public static boolean isActivelyPhasing(LivingEntity living, PhaseExcluder ignoreIf)
	{
		World world = living.getWorld();
		
		// Generate list of occupied block positions
		List<BlockPos> positions = Lists.newArrayList();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = 0; i < 8; ++i)
        {
            double d = living.getX() + (double)(((float)((i >> 0) % 2) - 0.5f) * living.getWidth() * 0.8f);
            double e = living.getEyeY() + (double)(((float)((i >> 1) % 2) - 0.5f) * 0.1f * living.getScale());
            double f = living.getZ() + (double)(((float)((i >> 2) % 2) - 0.5f) * living.getWidth() * 0.8f);
            mutable.set(d, e, f);
            BlockState blockState = world.getBlockState(mutable);
            if(ignoreIf.shouldExcludeIf(blockState, world, mutable))
            	continue;
            positions.add(mutable);
        }
        
        // Check if any occupied block position is applicable to any block collision ability the player has
		for(AbilityInstance inst : getPhasingAbilities(living))
		{
			IBlockCollisionAbility ability = (IPhasingAbility)inst.ability();
			if(positions.stream().anyMatch(pos -> !cannotEverBePhased(world.getBlockState(pos)) && ability.isApplicableTo(world.getBlockState(pos), living, inst)))
				return true;
		}
		return false;
	}
	
	@FunctionalInterface
	public static interface PhaseExcluder
	{
		public boolean shouldExcludeIf(BlockState state, World world, BlockPos.Mutable mutable);
	}
}
