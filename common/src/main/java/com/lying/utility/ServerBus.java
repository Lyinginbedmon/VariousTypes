package com.lying.utility;

import com.lying.VariousTypes;
import com.lying.component.element.ElementNonLethal;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.init.VTTypes;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.reference.Reference;

import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;

public class ServerBus
{
	public static void init()
	{
		ServerEvents.SheetEvents.GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			if(types.contains(VTTypes.NATIVE.get()) || types.contains(VTTypes.OTHAKIN.get()))
				return;
			
			// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
			entity.ifPresent(ent -> types.add(home == null || home == ent.getWorld().getRegistryKey() ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get()));
		});
		
		PlayerEvent.PLAYER_JOIN.register((player) -> VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			SyncFatiguePacket.send(player, sheet.elementValue(VTSheetElements.NONLETHAL));
			SyncActionablesPacket.send(player, sheet.elementValue(VTSheetElements.ACTIONABLES));
		}));
		
		PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> VariousTypes.getSheet(player).ifPresent(sheet -> sheet.buildSheet()));
		TickEvent.PLAYER_POST.register(player -> VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			if(player.getWorld().isClient()) return;
			ElementNonLethal nonlethalDamage = sheet.element(VTSheetElements.NONLETHAL);
			float maxHealth = player.getMaxHealth();
			
			// Nonlethal damage naturally recedes at a rate of 1 point per minute, regardless of other sources of healing
			if(player.age%Reference.Values.TICKS_PER_MINUTE == 0 && nonlethalDamage.value() > 0)
				nonlethalDamage.accrue(-1F, maxHealth, player);
			
			double gradient = Math.pow(nonlethalDamage.value() / maxHealth, 3);
			handleFatigue((int)((gradient * 3) - 1), player);
		}));
	}
	
	private static void handleFatigue(int amplifier, LivingEntity player)
	{
		if(!VariousTypes.config.fatigueEnabled() || player.age%Reference.Values.TICKS_PER_SECOND > 0)
			return;
		
		// If the player already has a stronger instance of Fatigue, remove it so the weaker instance (if any) will take effect
		if(amplifier >= 0 && player.hasStatusEffect(VTStatusEffects.FATIGUE) && player.getStatusEffect(VTStatusEffects.FATIGUE).getAmplifier() > amplifier)
			player.removeStatusEffect(VTStatusEffects.FATIGUE);
		if(amplifier >= 0)
			player.addStatusEffect(new StatusEffectInstance(VTStatusEffects.FATIGUE, Reference.Values.TICKS_PER_SECOND * 3, amplifier));
	}
}