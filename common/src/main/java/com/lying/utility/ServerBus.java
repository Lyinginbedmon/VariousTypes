package com.lying.utility;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilityInstance;
import com.lying.ability.IStatusEffectSpoofAbility;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementNonLethal;
import com.lying.event.LivingEvents;
import com.lying.event.Result;
import com.lying.event.SheetEvents;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.init.VTTypes;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.reference.Reference;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerBus
{
	public static void init()
	{
		characterSheetHandling();
		spoofAbilityHandling();
		
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
		LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.register((living, ticking) -> 
		{
			ItemStack chest = living.getEquippedStack(EquipmentSlot.CHEST);
			if(!chest.isEmpty() && chest.isOf(Items.ELYTRA))
				return EventResult.interrupt(ElytraItem.isUsable(chest));
			
			return EventResult.interruptDefault();
		});
		
		// Prevents any later handling from affecting damage necessary for stable gameplay
		EntityEvent.LIVING_HURT.register((LivingEntity entity, DamageSource source, float amount) -> 
			VTUtils.isDamageInviolable(source, entity) ? EventResult.pass() : LivingEvents.LIVING_HURT_EVENT.invoker().hurt(entity, source, amount));
	}
	
	/** Apply status effect spoof abilities in one central event listener */
	private static void spoofAbilityHandling()
	{
		// Causes hasEffect to return true if a spoof ability is providing the given effect
		LivingEvents.HAS_STATUS_EFFECT_EVENT.register((effect, living, abilities, truth) -> 
		{
			for(AbilityInstance inst : Ability.getAllOf(IStatusEffectSpoofAbility.class, living))
			{
				IStatusEffectSpoofAbility ability = (IStatusEffectSpoofAbility)inst.ability();
				if(ability.isAffectingStatus(effect, inst))
					return EventResult.interruptTrue();
			}
			return EventResult.pass();
		});
		
		// Forces getStatusEffect to return a spoofed instance
		LivingEvents.GET_STATUS_EFFECT_EVENT.register((effect, living, abilities, actual) -> 
		{
			for(AbilityInstance inst : Ability.getAllOf(IStatusEffectSpoofAbility.class, living))
			{
				IStatusEffectSpoofAbility ability = (IStatusEffectSpoofAbility)inst.ability();
				if(ability.isAffectingStatus(effect, inst))
					return Result.interrupt(ability.getSpoofed(effect, inst));
			}
			return Result.pass();
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
			SyncActionablesPacket.send(player, sheet.element(VTSheetElements.ACTIONABLES));
		}));
		
		// Update character sheet when changing dimension
		PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> VariousTypes.getSheet(player).ifPresent(sheet -> sheet.buildSheet()));
		
		// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
		SheetEvents.GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			if(types.contains(VTTypes.NATIVE.get()) || types.contains(VTTypes.OTHAKIN.get()))
				return;
			
			entity.ifPresent(ent -> types.add(home == null || home == ent.getWorld().getRegistryKey() ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get()));
		});
		
		// Attribute modifier handling
		SheetEvents.BEFORE_REBUILD_EVENT.register((living, abilities) -> abilities.abilities().forEach(inst -> inst.ability().removeAttributeModifiers(living, inst)));
		SheetEvents.AFTER_REBUILD_EVENT.register((living, abilities) -> abilities.abilities().forEach(inst -> inst.ability().applyAttributeModifiers(living, inst)));
	}
	
	/** Handles the down/up-grading of fatigue intensity */
	private static void handleFatigue(int amplifier, LivingEntity player)
	{
		if(!VariousTypes.config.fatigueEnabled() || player.age%Reference.Values.TICKS_PER_SECOND > 0 || amplifier < 0)
			return;
		
		// If the player already has a stronger instance of Fatigue, remove it so the weaker instance (if any) will take effect
		if(amplifier >= 0)
		{
			RegistryEntry<StatusEffect> fatigue = VTStatusEffects.getEntry(VTStatusEffects.FATIGUE);
			if(player.hasStatusEffect(fatigue) && player.getStatusEffect(fatigue).getAmplifier() > amplifier)
				player.removeStatusEffect(fatigue);
			
			player.addStatusEffect(new StatusEffectInstance(fatigue, Reference.Values.TICKS_PER_SECOND * 3, amplifier));
		}
	}
}