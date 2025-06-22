package com.lying.ability;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.network.SyncActionablesPacket;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface ITickingAbility
{
	/** Returns true if this ability needs to be ticked */
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance);
	
	/** Ticks the ability, only called server-side */
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner);
	
	/** Puts the ability on indefinite cooldown */
	public static void tryPutOnIndefiniteCooldown(Identifier mapName, LivingEntity owner)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.element(VTSheetElements.ACTIONABLES);
			actionables.putOnIndefiniteCooldown(mapName, true);
			if(owner.getType() == EntityType.PLAYER && !owner.getWorld().isClient())
				SyncActionablesPacket.send((ServerPlayerEntity)owner, actionables);
		});
	}
	
	/** Puts the ability on its actual cooldown */
	public static void tryPutOnCooldown(AbilityInstance instance, LivingEntity owner)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.element(VTSheetElements.ACTIONABLES);
			actionables.putOnCooldown(instance.mapName(), owner.getWorld().getTime(), instance.cooldown(), actionables.getCooldown(instance.mapName()) == null);
			if(owner.getType() == EntityType.PLAYER && !owner.getWorld().isClient())
				SyncActionablesPacket.send((ServerPlayerEntity)owner, actionables);
		});
	}
}
