package com.lying.entity.ai;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilityPariah;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;

public class HatePariahsTask extends TrackTargetGoal
{
	protected final int reciprocalChance;
	protected Predicate<LivingEntity> targetPredicate = living -> living.isAlive() && (living.getType() != EntityType.PLAYER || !(((PlayerEntity)living).isCreative() || ((PlayerEntity)living).isSpectator()));
	@Nullable
	protected LivingEntity targetEntity;
	
	public HatePariahsTask(MobEntity mob, boolean checkVisibility, boolean checkNavigable)
	{
		this(mob, 10, checkVisibility, checkNavigable);
	}
	
	public HatePariahsTask(MobEntity mob, int reciprocalChance, boolean checkVisibility, boolean checkNavigable)
	{
		super(mob, checkVisibility, checkNavigable);
		this.reciprocalChance = ActiveTargetGoal.toGoalTicks(reciprocalChance);
		targetPredicate = targetPredicate.and(living -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isPresent())
			{
				CharacterSheet sheet = sheetOpt.get();
				for(AbilityInstance inst : sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).getAbilitiesOfType(VTAbilities.PARIAH.get().registryName()))
					if(AbilityPariah.includes(inst, this.mob.getType()))
						return true;
			}
			return false;
		});
		this.setControls(EnumSet.of(Goal.Control.TARGET));
	}

	@Override
	public boolean canStart()
	{
		if(this.reciprocalChance > 0 && this.mob.getRandom().nextInt(reciprocalChance) != 0)
			return false;
		findClosestTarget();
		return this.targetEntity != null;
	}
	
	public void start()
	{
		this.mob.setTarget(this.targetEntity);
		super.start();
	}
	
	protected Box getSearchBox(double distance) { return this.mob.getBoundingBox().expand(distance, 4, distance); }
	
	protected void findClosestTarget()
	{
		this.mob.getWorld().getEntitiesByClass(LivingEntity.class, getSearchBox(getFollowRange()), targetPredicate).stream().findFirst().ifPresent(living -> this.targetEntity = living);
	}
}
