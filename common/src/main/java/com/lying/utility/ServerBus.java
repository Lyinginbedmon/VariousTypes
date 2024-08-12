package com.lying.utility;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementNonLethal;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.init.VTTypes;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.reference.Reference;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerBus
{
	public static void init()
	{
		characterSheetHandling();
		
		// Handle nonlethal damage and resulting fatigue
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
		
		// Implement custom fall-flying function (basically the same as Fabric natively, but cross-platform)
		ServerEvents.LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.register((living, ticking) -> 
		{
			ItemStack chest = living.getEquippedStack(EquipmentSlot.CHEST);
			if(!chest.isEmpty() && chest.isOf(Items.ELYTRA))
				return EventResult.interrupt(ElytraItem.isUsable(chest));
			
			return EventResult.interruptDefault();
		});
	}
	
	private static void characterSheetHandling()
	{
		// Ensure character sheet persistence
		PlayerEvent.PLAYER_CLONE.register((ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean wonGame) -> 
		{
			Optional<CharacterSheet> sheetOptA = VariousTypes.getSheet(oldPlayer);
			Optional<CharacterSheet> sheetOptB = VariousTypes.getSheet(newPlayer);
			
			if(sheetOptA.isPresent() != sheetOptB.isPresent()) return;
			else if(sheetOptA.isEmpty()) return;
			else
				sheetOptB.ifPresent(sheet -> 
				{
					sheet.clear();
					sheet.clone(sheetOptA.get(), true);
				});
		});
		
		// Sync player character sheet on join
		PlayerEvent.PLAYER_JOIN.register((player) -> VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			sheet.buildAndSync();
			SyncFatiguePacket.send(player, sheet.elementValue(VTSheetElements.NONLETHAL));
			SyncActionablesPacket.send(player, sheet.elementValue(VTSheetElements.ACTIONABLES));
		}));
		
		// Update character sheet when changing dimension
		PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> VariousTypes.getSheet(player).ifPresent(sheet -> sheet.buildSheet()));
		
		// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
		ServerEvents.SheetEvents.GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			if(types.contains(VTTypes.NATIVE.get()) || types.contains(VTTypes.OTHAKIN.get()))
				return;
			
			entity.ifPresent(ent -> types.add(home == null || home == ent.getWorld().getRegistryKey() ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get()));
		});
		
		// Attribute modifier handling
		ServerEvents.SheetEvents.BEFORE_REBUILD_EVENT.register((living, abilities) -> abilities.abilities().forEach(inst -> inst.ability().removeAttributeModifiers(living, inst)));
		ServerEvents.SheetEvents.AFTER_REBUILD_EVENT.register((living, abilities) -> abilities.abilities().forEach(inst -> inst.ability().applyAttributeModifiers(living, inst)));
	}
	
	private static void handleFatigue(int amplifier, LivingEntity player)
	{
		if(!VariousTypes.config.fatigueEnabled() || player.age%Reference.Values.TICKS_PER_SECOND > 0 || amplifier < 0)
			return;
		
		// If the player already has a stronger instance of Fatigue, remove it so the weaker instance (if any) will take effect
		if(amplifier >= 0)
		{
			if(player.hasStatusEffect(VTStatusEffects.FATIGUE) && player.getStatusEffect(VTStatusEffects.FATIGUE).getAmplifier() > amplifier)
				player.removeStatusEffect(VTStatusEffects.FATIGUE);
			
			player.addStatusEffect(new StatusEffectInstance(VTStatusEffects.FATIGUE, Reference.Values.TICKS_PER_SECOND * 3, amplifier));
		}
	}
}