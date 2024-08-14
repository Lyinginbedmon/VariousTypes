package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public abstract class ActivatedAbility extends Ability
{
	public ActivatedAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public AbilityType type() { return AbilityType.ACTIVATED; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return true; }
	
	public final boolean trigger(LivingEntity owner, AbilityInstance instance)
	{
		boolean isServerPlayer = owner.getType() == EntityType.PLAYER && !owner.getWorld().isClient();
		if(!instance.isReadOnly() && canTrigger(owner, instance))
		{
			if(!owner.getWorld().isClient())
				activate(owner, instance);
			
			putAbilityOnCooldown(owner, instance.mapName());
			
			if(isServerPlayer)
				((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.success", instance.displayName()), true);
			return true;
		}
		else if(isServerPlayer)
			((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.failed", instance.displayName()), true);
		return false;
	}
	
	/** Called server side when a player activates an ability.<br>Not responsible for cooldowns. */
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND; }
	
	/** Puts the ability on cooldown, if it isn't already */
	public static void putAbilityOnCooldown(LivingEntity owner, Identifier mapName)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			AbilityInstance inst = actionables.get(mapName);
			if(inst.cooldown() > 0 && actionables.getCooldown(mapName) == null)
			{
				actionables.putOnCooldown(mapName, owner.getEntityWorld().getTime(), inst.cooldown());
				sheet.markDirty();
			}
		});
	}
}