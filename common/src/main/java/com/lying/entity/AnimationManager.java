package com.lying.entity;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.Entity;

/** Finite State handler for mobs with multiple animations */
public class AnimationManager<T extends Entity>
{
	private final Map<Integer, Pair<AnimationState, Float>> STATE_MAP = new HashMap<>();
	private int ticksRunning = 0;
	private int currentAnim = -1;
	private int prevAnim = -1;
	
	public AnimationManager(float... animationsIn)
	{
		for(int i=0; i<animationsIn.length; i++)
			STATE_MAP.put(STATE_MAP.size(), Pair.of(new AnimationState(), animationsIn[i]));
	}
	
	@SafeVarargs
	public AnimationManager(Pair<Integer,Float>... entriesIn)
	{
		for(Pair<Integer, Float> entry : entriesIn)
			STATE_MAP.put(entry.getFirst(), Pair.of(new AnimationState(), entry.getSecond()));
	}
	
	public int size() { return STATE_MAP.size(); }
	
	public void start(int index, int age)
	{
		if(currentAnim >= 0)
			prevAnim = currentAnim;
		
		STATE_MAP.entrySet().forEach(entry -> 
		{
			if(entry.getKey() == index)
				entry.getValue().getFirst().startIfNotRunning(age);
			else
				entry.getValue().getFirst().stop();
		});
		currentAnim = index;
		ticksRunning = 0;
	}
	
	public int stopAll()
	{
		if(currentAnim >= 0)
			prevAnim = currentAnim;
		
		STATE_MAP.values().forEach(entry -> entry.getFirst().stop());
		return currentAnim = -1;
	}
	
	public Map<Integer, AnimationState> animations()
	{
		Map<Integer, AnimationState> anims = new HashMap<>();
		STATE_MAP.entrySet().forEach(entry -> anims.put(entry.getKey(), entry.getValue().getFirst()));
		return anims;
	}
	
	public float currentDuration() { return currentAnim == -1 ? 0 : STATE_MAP.get(currentAnim).getSecond(); }
	
	public int currentAnim() { return this.currentAnim; }
	
	public int lastAnim() { return this.prevAnim; }
	
	public int ticksRunning() { return currentAnim < 0 ? 0 : ticksRunning; }
	
	public void tick(T ent)
	{
		if(currentAnim >= 0)
		{
			onUpdateAnim(currentAnim, ++ticksRunning, ent);
			
			if(ticksRunning() >= (int)(currentDuration() * 20))
				stopAll();
		}
	}
	
	/** Called when the manager is ticked, used to provide additional effects like sound events */
	public void onUpdateAnim(int animation, int ticksRunning, T ent) { }
	
	@Nullable
	public AnimationState get(int index)
	{
		return !STATE_MAP.containsKey(index) ? null : STATE_MAP.get(index).getFirst();
	}
}
