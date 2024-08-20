package com.lying.ability;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilityType;
import com.lying.init.VTSheetElements;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;

/**
 * An ability that can modify block collision behaviours, whether that be making them more or less passable<br>
 * This interface primarily handles making collision boxes MORE solid, otherwise use IPhasingAbility
 */
public interface IBlockCollisionAbility
{
	/** Returns whether this ability could apply to the given blockstate, absent any positional context */
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance);
	
	/** Returns an optional containing the modified collision shape, if any */
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance);
	
	public static Collection<AbilityInstance> getCollisionAbilities(LivingEntity living)
	{
		Map<Identifier, AbilityInstance> abilityMap = new HashMap<>();
		
		VariousTypes.getSheet((LivingEntity)living).ifPresent(sheet -> 
		{
			// Collect all passive abilities from main ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).getAbilitiesOfClass(IBlockCollisionAbility.class).stream().filter(inst -> inst.ability().type() == AbilityType.PASSIVE).forEach(inst -> abilityMap.put(inst.mapName(), inst));
			
			// Collect all activated abilities from actionable ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ACTIONABLES).getAbilitiesOfClass(IBlockCollisionAbility.class).forEach(inst -> abilityMap.put(inst.mapName(), inst));
		});
		
		return abilityMap.isEmpty() ? Lists.newArrayList() : abilityMap.values();
	}
}
