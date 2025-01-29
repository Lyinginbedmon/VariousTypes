package com.lying.entity;

import com.lying.utility.PlayerPose;

import net.minecraft.entity.AnimationState;

public interface PlayerEntityInterface
{
	void startAnimation(PlayerPose pose);
	
	AnimationState getAnimation(PlayerPose poseIn);
}
