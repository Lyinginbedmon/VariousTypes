package com.lying.client.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class AnimationStateEngine
{
	private final Map<Integer, List<Integer>> stateTransitions = new HashMap<>();
	
	public AnimationStateEngine(Map<Integer, List<Integer>> statesIn)
	{
		statesIn.entrySet().forEach(entry -> stateTransitions.put(entry.getKey(), entry.getValue()));
	}
	
	public void addTransition(int fromState, int toState)
	{
		List<Integer> transitions = stateTransitions.containsKey(fromState) ? stateTransitions.get(fromState) : Lists.newArrayList();
		transitions.add(toState);
		stateTransitions.put(fromState, transitions);
	}
	
	/** If a transition has been registered, return it, otherwise return transition to idle */
	public List<Integer> getTransitions(int state)
	{
		List<Integer> transitions = stateTransitions.getOrDefault(state, List.of(0));
		return transitions.isEmpty() ? List.of(0) : transitions;
	}
}
