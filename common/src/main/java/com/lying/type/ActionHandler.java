package com.lying.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilityInstance;
import com.lying.data.VTTags;
import com.lying.init.VTAbilities;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ActionHandler
{
	/** The set of actions most commonly available to creatures */
	public static final ActionHandler STANDARD_SET = ActionHandler.of(Action.EAT.get(), Action.BREATHE.get(), Action.SLEEP.get(), Action.REGEN.get()).allowBreathe(VTTags.AIR);
	public static final ActionHandler REGEN_ONLY = ActionHandler.of(Action.REGEN.get());
	public static final ActionHandler NONE = new ActionHandler();
	
	/** List of operations to apply when combining action handlers */
	private final List<Operation> operations = Lists.newArrayList();
	
	/** Unique list of actions */
	private final Map<Identifier, Action> activeActions = new HashMap<>();
	private final List<TagKey<Fluid>> breathableFluids = Lists.newArrayList();
	
	private boolean isDirty = false;
	
	private ActionHandler() { }
	
	public ActionHandler copy()
	{
		ActionHandler handler = new ActionHandler();
		handler.operations.addAll(operations);
		handler.activeActions.putAll(activeActions);
		handler.breathableFluids.addAll(breathableFluids);
		return handler;
	}
	
	public void clear()
	{
		operations.clear();
		activeActions.clear();
		breathableFluids.clear();
		isDirty = false;
	}
	
	public static ActionHandler of(Action... actionsIn)
	{
		ActionHandler handler = new ActionHandler();
		for(Action action : actionsIn)
		{
			handler.activate(action);
			handler.operations.add(Operation.add(action));
		}
		return handler;
	}
	
	public void markDirty() { isDirty = true; }
	
	/** Returns true if this handler needs to be recalculated in a CharacterSheet */
	public boolean isDirty() { return isDirty; }
	
	public ActionHandler add(Operation operation)
	{
		operations.add(operation);
		return this;
	}
	
	public Collection<Action> active() { return activeActions.values(); }
	
	public void activate(Action action)
	{
		activeActions.put(action.registryName(), action);
	}
	
	public void deactivate(Action action)
	{
		activeActions.remove(action.registryName());
	}
	
	public boolean can(Action action) { return activeActions.containsKey(action.registryName()); }
	
	public boolean cannot(Action action) { return !can(action); }
	
	public Collection<TagKey<Fluid>> canBreatheIn() { return breathableFluids; }
	
	@SuppressWarnings("deprecation")
	public boolean canBreathe(Fluid fluid)
	{
		if(!can(Action.BREATHE.get()))
			return true;
		
		if(fluid == null || fluid == Fluids.EMPTY)
			return breathableFluids.isEmpty() || breathableFluids.contains(VTTags.AIR);
		else
			return breathableFluids.stream().anyMatch(tag -> fluid.isIn(tag));
	}
	
	public boolean canBreatheIn(TagKey<Fluid> tag) { return breathableFluids.contains(tag); }
	
	public boolean isSuffocating(FluidState submergedIn)
	{
		return canBreathe(submergedIn.getFluid());
	}
	
	public ActionHandler allowBreathe(TagKey<Fluid> fluid)
	{
		if(!breathableFluids.contains(fluid))
			breathableFluids.add(fluid);
		return this;
	}
	
	public ActionHandler denyBreathe(TagKey<Fluid> fluid)
	{
		if(breathableFluids.contains(fluid))
			breathableFluids.remove(fluid);
		return this;
	}
	
	public void stack(ActionHandler handler, TypeSet types)
	{
		operations.forEach(op -> op.apply(handler, types));
	}
	
	/** Returns a list of ability instances corresponding to breathing any breathable fluids in this handler */
	public List<AbilityInstance> abilities()
	{
		List<AbilityInstance> abilities = Lists.newArrayList();
		if(!can(Action.BREATHE.get()) || breathableFluids.isEmpty())
			return abilities;
		
		breathableFluids.forEach(fluid -> abilities.add(VTAbilities.BREATHE_FLUID.get().instance(AbilitySource.TYPE, AbilityBreathing.of(fluid))));
		return abilities;
	}
	
	public static class Operation
	{
		private Predicate<TypeSet> conditional = Predicates.alwaysTrue();
		private Mode mode = Mode.ADD;
		private final Action action;
		
		private Operation(Action actionIn)
		{
			action = actionIn;
		}
		
		private Operation(Predicate<TypeSet> conditionIn, Mode modeIn, Action actionIn)
		{
			this(actionIn);
			conditional = conditionIn;
			mode = modeIn;
		}
		
		public static Operation add(Action action) { return add(Predicates.alwaysTrue(), action); }
		
		public static Operation add(Predicate<TypeSet> condition, Action action) { return new Operation(condition, Mode.ADD, action); }
		
		public static Operation remove(Action action) { return remove(Predicates.alwaysTrue(), action); }
		
		public static Operation remove(Predicate<TypeSet> condition, Action action) { return new Operation(condition, Mode.REMOVE, action); }
		
		public void apply(ActionHandler handler, TypeSet types)
		{
			if(conditional.apply(types))
				switch(mode)
				{
					case ADD:
						handler.activate(action);
						break;
					case REMOVE:
						handler.deactivate(action);
						break;
				}
		}
		
		private static enum Mode
		{
			ADD,
			REMOVE;
		}
	}
}
