package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class ActivatedAbility extends Ability
{
	public ActivatedAbility(Identifier regName)
	{
		super(regName);
	}
	
	public AbilityType type() { return AbilityType.ACTIVATED; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return true; }
	
	public final void trigger(LivingEntity owner, AbilityInstance instance)
	{
		if(!instance.isReadOnly() && canTrigger(owner, instance))
		{
			if(owner.getType() == EntityType.PLAYER)
				((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.success", instance.displayName()), true);
			activate(owner, instance);
		}
		else if(owner.getType() == EntityType.PLAYER)
			((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.failed"), true);
	}
	
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
}