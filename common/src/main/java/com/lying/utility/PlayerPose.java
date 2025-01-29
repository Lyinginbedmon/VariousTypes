package com.lying.utility;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;

import net.minecraft.entity.EntityPose;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;

public enum PlayerPose
{
	STANDING(1, EntityPose.STANDING),
	CROUCHING(EntityPose.CROUCHING),
	RUNNING(0, EntityPose.STANDING, p -> p.shouldSpawnSprintingParticles()),
	SITTING(EntityPose.SITTING),
	SWIMMING_IDLE(1, EntityPose.SWIMMING),
	SWIMMING_POWERED(0, EntityPose.SWIMMING, p -> p.hasStatusEffect(StatusEffects.DOLPHINS_GRACE)),
	FLYING_IDLE(1, EntityPose.FALL_FLYING),
	FLYING_POWERED(0, EntityPose.FALL_FLYING, Predicates.alwaysFalse()),	// TODO Detect when under powered flight
	SLEEPING(EntityPose.SLEEPING),
	DYING(EntityPose.DYING);
	
	private final int index;
	private final Optional<EntityPose> parentPose;
	private final Predicate<PlayerEntity> conditions;
	
	private static int compare(PlayerPose a, PlayerPose b)
	{
		if(a.index != b.index)
			return a.index < b.index ? -1 : a.index > b.index ? 1 : 0;
		return a.ordinal() < b.ordinal() ? -1 : a.ordinal() > b.ordinal() ? 1 : 0;
	}
	
	private PlayerPose(EntityPose parent) { this(0, parent, Predicates.alwaysTrue()); }
	
	private PlayerPose(int indexIn, EntityPose parent) { this(indexIn, parent, Predicates.alwaysTrue()); }
	
	private PlayerPose(int indexIn, EntityPose parent, Predicate<PlayerEntity> conditionIn)
	{
		index = indexIn;
		parentPose = parent == null ? Optional.empty() : Optional.of(parent);
		conditions = conditionIn;
	}
	
	public static PlayerPose getPoseFromPlayer(PlayerEntity player, EntityPose poseIn)
	{
		List<PlayerPose> poses = Lists.newArrayList();
		for(PlayerPose pose : values())
			if(pose.parentPose.isEmpty() || pose.parentPose.get().equals(poseIn))
				poses.add(pose);
		
		return poses.stream().filter(p -> p.conditions.test(player)).sorted(PlayerPose::compare).findFirst().orElse(STANDING);
	}
}