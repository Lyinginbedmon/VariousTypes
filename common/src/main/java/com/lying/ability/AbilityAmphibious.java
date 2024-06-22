package com.lying.ability;

import com.lying.init.VTAbilities;
import com.lying.utility.ServerBus;

import net.minecraft.fluid.Fluids;
import net.minecraft.util.Identifier;

public class AbilityAmphibious extends Ability
{
	public AbilityAmphibious(Identifier regName)
	{
		super(regName);
	}
	
	public void registerEventHandlers()
	{
		ServerBus.AFTER_REBUILD_ACTIONS_EVENT.register((handler,abilities,owner) -> 
		{
			// Adds the ability to breathe air after it may have been denied by other breathing abilities
			if(!handler.canBreathe(Fluids.EMPTY) && abilities.hasAbility(VTAbilities.AMPHIBIOUS.get().registryName()))
				handler.allowBreathe(Fluids.EMPTY);
		});
	}
}
