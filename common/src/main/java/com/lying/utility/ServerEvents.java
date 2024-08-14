package com.lying.utility;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.reflect.AbstractInvocationHandler;
import com.lying.ability.AbilitySet;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
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
			EventResult shouldDenyStatusEffect(StatusEffectInstance effect, AbilitySet abilities);
		}
		
		public static final Event<GetMaxAirEvent> GET_MAX_AIR_EVENT = EventFactory.createLoop(GetMaxAirEvent.class);
		
		@FunctionalInterface
		public interface GetMaxAirEvent
		{
			int maxAir(AbilitySet abilities, int maxAir);
		}
		
		/** Called by LivingEntityMixin when checking if the given effect is present */
		public static final Event<HasStatusEffectEvent> HAS_STATUS_EFFECT_EVENT = EventFactory.createEventResult(HasStatusEffectEvent.class);
		
		@FunctionalInterface
		public interface HasStatusEffectEvent
		{
			EventResult hasStatusEffect(final RegistryEntry<StatusEffect> effect, final LivingEntity entity, final AbilitySet abilities, final boolean truth);
		}
		
		/** Called by LivingEntityMixin when attempting to retrieve a status effect */
		public static final Event<GetStatusEffectEvent> GET_STATUS_EFFECT_EVENT = EventFactory.of(listeners -> (GetStatusEffectEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{GetStatusEffectEvent.class}, new AbstractInvocationHandler()
		{
			@SuppressWarnings("unchecked")
			protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
			{
				for(GetStatusEffectEvent listener : listeners)
				{
					Result<StatusEffectInstance> result = listener.getStatusEffect((RegistryEntry<StatusEffect>)args[0], (LivingEntity)args[1], (AbilitySet)args[2], (StatusEffectInstance)args[3]);
					if(result.interruptsFurtherEvaluation())
						return result;
				}
				return Result.pass();
			}
		}));
		
		@FunctionalInterface
		public interface GetStatusEffectEvent
		{
			Result<StatusEffectInstance> getStatusEffect(final RegistryEntry<StatusEffect> effect, final LivingEntity entity, final AbilitySet abilities, final StatusEffectInstance actual);
		}
		
		public static final Event<CanFly> CAN_FLY_EVENT = EventFactory.createEventResult(CanFly.class);
		
		@FunctionalInterface
		public interface CanFly
		{
			EventResult canCurrentlyFly(LivingEntity entity);
		}
		
		public static final Event<CustomElytraCheck> CUSTOM_ELYTRA_CHECK_EVENT = EventFactory.createEventResult(CustomElytraCheck.class);
		
		@FunctionalInterface
		public interface CustomElytraCheck
		{
			EventResult passesElytraCheck(LivingEntity entity, boolean ticking);
		}
	}
	
	public static class PlayerEvents
	{
		/** Fired when the server receives a PlayerFlightInput packet from a flying player */
		public static final Event<PlayerInput> PLAYER_FLIGHT_INPUT_EVENT = EventFactory.createLoop(PlayerInput.class);
		
		@FunctionalInterface
		public interface PlayerInput
		{
			void onPlayerInput(ServerPlayerEntity player, float forward, float strafe, boolean jump, boolean sneak);
		}
		
		/** Called by ExperienceOrbEntity when determining contact with or attraction to a player */
		public static final Event<CanCollectXPEvent> CAN_COLLECT_XP_EVENT = EventFactory.createEventResult(CanCollectXPEvent.class);
		
		@FunctionalInterface
		public interface CanCollectXPEvent
		{
			EventResult canPlayerCollectExperienceOrbs(ExperienceOrbEntity orb, PlayerEntity player);
		}
		
		/** Called by ServerPlayerEntity before opening a handled screen */
		public static final Event<CanUseScreenEvent> CAN_USE_SCREEN_EVENT = EventFactory.createEventResult(CanUseScreenEvent.class);
		
		@FunctionalInterface
		public interface CanUseScreenEvent
		{
			EventResult canPlayerUseScreen(PlayerEntity player, ScreenHandlerType<?> screen);
		}
	}
	
	public static class Result<T extends Object>
	{
		@Nullable
		private final T resultValue;
		@NotNull
		private final boolean shouldInterrupt;
		
		public Result(T value, boolean interrupt)
		{
			resultValue = value;
			shouldInterrupt = interrupt;
		}
		
		public static <T extends Object> Result<T> interrupt(T value) { return new Result<>(value, true); }
		public static <T extends Object> Result<T> pass() { return new Result<>(null, false); }
		
		public boolean interruptsFurtherEvaluation() { return shouldInterrupt; }
		
		public T value() { return resultValue; }
		
		public boolean isEmpty() { return resultValue == null; }
	}
}
