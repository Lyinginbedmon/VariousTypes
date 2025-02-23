package com.lying.entity;

import com.lying.utility.PlayerPose;

import net.minecraft.entity.AnimationState;

public interface AccessoryAnimationInterface
{
	/** Starts the given pose animation, stopping all others */
	void startAnimation(PlayerPose pose);
	
	/** Retrieves the state of the given animation */
	AnimationState getAnimation(PlayerPose pose);
	
	/** Returns true if the given animation is currently running */
	default boolean isRunning(PlayerPose pose) { return getAnimation(pose).isRunning(); }
	
	default PlayerPose currentlyRunning()
	{
		for(PlayerPose pose : PlayerPose.values())
			if(isRunning(pose))
				return pose;
		
		startAnimation(PlayerPose.STANDING);
		return PlayerPose.STANDING;
	}
	
	/** Returns true if any PlayerPose animation is currently running */
	default boolean isAnimating()
	{
		for(PlayerPose pose : PlayerPose.values())
			if(isRunning(pose))
				return true;
		return false;
	}
	
	/** Returns the idle animation state, which is always running */
	AnimationState getIdleAnimation();
	
	/** Flags for powered vs idle flight animation and notifies clients if necessary */
	void setPoweredFlight(boolean bool);
	
	boolean isPoweredFlying();
}
