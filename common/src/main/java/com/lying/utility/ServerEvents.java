package com.lying.utility;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.ability.AbilitySet;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class ServerEvents
{
	public static class SheetEvents
	{
		public static final Event<GetTypesEvent> GET_TYPES_EVENT = EventFactory.createLoop(GetTypesEvent.class);
		
		@FunctionalInterface
		public interface GetTypesEvent
		{
			void affectTypes(Optional<LivingEntity> entity, @Nullable RegistryKey<World> homeDimension, TypeSet types);
		}
		
		/**
		 * Called by CharacterSheet after actions are reconstructed to allow abilities to affect them.<br>
		 * This is called after Types are applied and after AbilityBreathing abilities are accounted for.
		 */
		public static final Event<RebuildActionsEvent> AFTER_REBUILD_ACTIONS_EVENT = EventFactory.createLoop(RebuildActionsEvent.class);
		
		@FunctionalInterface
		public interface RebuildActionsEvent
		{
			void affectActions(ActionHandler handler, AbilitySet abilities, Optional<LivingEntity> owner);
		}
		
		public static final Event<SheetRebuildEvent> BEFORE_REBUILD_EVENT = EventFactory.createLoop(SheetRebuildEvent.class);
		
		public static final Event<SheetRebuildEvent> AFTER_REBUILD_EVENT = EventFactory.createLoop(SheetRebuildEvent.class);
		
		@FunctionalInterface
		public interface SheetRebuildEvent
		{
			void process(LivingEntity owner, AbilitySet abilities);
		}
	}
	
	public static class LivingEvents
	{
		public static final Event<CanHaveStatusEffectEvent> CAN_HAVE_STATUS_EFFECT_EVENT = EventFactory.createLoop(CanHaveStatusEffectEvent.class);
		
		@FunctionalInterface
		public interface CanHaveStatusEffectEvent
		{
			Result shouldDenyStatusEffect(StatusEffectInstance effect, AbilitySet abilities, Result result);
		}
		
		public static final Event<GetMaxAirEvent> GET_MAX_AIR_EVENT = EventFactory.createLoop(GetMaxAirEvent.class);
		
		@FunctionalInterface
		public interface GetMaxAirEvent
		{
			int maxAir(AbilitySet abilities, int maxAir);
		}
		
		public static final Event<GetStatusEffectEvent> GET_STATUS_EFFECT_EVENT = EventFactory.createLoop(GetStatusEffectEvent.class);
		/** Result of the latest call to GET_STATUS_EFFECT_EVENT */	// TODO Replace this with something more contained
		public static StatusEffectInstance GetStatusEffectEventResult = null;
		
		@FunctionalInterface
		public interface GetStatusEffectEvent
		{
			void getStatusEffect(final RegistryEntry<StatusEffect> effect, final LivingEntity entity, final AbilitySet abilities, final StatusEffectInstance actual);
		}
	}
	
	public static enum Result
	{
		PASS,
		DENY,
		ALLOW;
	}
}
