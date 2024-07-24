package com.lying.client.utility;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;

/** State handler for mobs with multiple animations */
public class AnimationManager<T extends Entity>
{
	private final List<AnimationState> states = Lists.newArrayList();
	private final List<Animation> animations = Lists.newArrayList();
	private int ticksRunning = 0;
	private int currentAnim = -1;
	
	public AnimationManager(Animation... animationsIn)
	{
		for(int i=0; i<animationsIn.length; i++)
		{
			states.add(new AnimationState());
			animations.add(animationsIn[i]);
		}
	}
	
	public int size() { return states.size(); }
	
	public void start(int index, int age)
	{
		for(int i=0; i<states.size(); i++)
		{
			AnimationState state = states.get(i);
			if(i == index)
			{
				state.startIfNotRunning(age);
				currentAnim = index;
			}
			else
				state.stop();
		}
		ticksRunning = 0;
	}
	
	public int stopAll()
	{
		states.forEach(state -> state.stop());
		return currentAnim = -1;
	}
	
	public List<Animation> animations() { return this.animations; }
	
	public int currentAnim() { return this.currentAnim; }
	
	public int ticksRunning() { return currentAnim < 0 ? 0 : ticksRunning; }
	
	public void tick(T ent)
	{
		if(currentAnim >= 0)
		{
			onUpdateAnim(currentAnim, ++ticksRunning, ent);
			
			if(ticksRunning() >= (int)(animations.get(currentAnim).lengthInSeconds() * 20))
				stopAll();
		}
	}
	
	/** Called when the manager is ticked, used to provide additional effects like sound events */
	public void onUpdateAnim(int animation, int ticksRunning, T ent) { }
	
	@Nullable
	public AnimationState get(int index)
	{
		return index < 0 || index >= states.size() ? null : states.get(index);
	}
}
