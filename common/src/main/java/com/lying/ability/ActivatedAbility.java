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
	
	public final void trigger(LivingEntity owner, AbilityInstance instance)
	{
		boolean isServerPlayer = owner.getType() == EntityType.PLAYER && !owner.getWorld().isClient();
		if(!instance.isReadOnly() && canTrigger(owner, instance))
		{
			if(!owner.getWorld().isClient())
				activate(owner, instance);
			
			putAbilityOnCooldown(owner, instance.mapName());
			
			if(isServerPlayer)
				((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.success", instance.displayName()), true);
		}
		else if(isServerPlayer)
			((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.failed", instance.displayName()), true);
	}
	
	/** Called server side when a player activates an ability.<br>Not responsible for cooldowns. */
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND; }
	
	public static void putAbilityOnCooldown(LivingEntity owner, Identifier mapName)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			AbilityInstance inst = actionables.get(mapName);
			if(inst.cooldown() > 0)
			{
				actionables.putOnCooldown(mapName, owner.getEntityWorld().getTime(), inst.cooldown());
				sheet.markDirty();
			}
		});
	}
	
	/** Called both client and server side when a player tries to activate an ability */
	public static void tryTriggerAbility(PlayerEntity player, Identifier mapName)
	{
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			if(actionables.hasAbilityInstance(mapName))
			{
				AbilityInstance inst = actionables.get(mapName);
				if(!actionables.isAvailable(mapName))
					player.sendMessage(translate("gui", "activated_ability.failed", inst.displayName()), true);
				else
					((ActivatedAbility)inst.ability()).trigger(player, inst);
			}
		});
	}
}