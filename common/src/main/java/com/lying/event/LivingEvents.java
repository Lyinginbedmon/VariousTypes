package com.lying.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.lying.ability.AbilitySet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent.LivingHurt;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;

public class LivingEvents
{
	public static final Event<LivingEvents.CanHaveStatusEffectEvent> CAN_HAVE_STATUS_EFFECT_EVENT = EventFactory.createLoop(LivingEvents.CanHaveStatusEffectEvent.class);
	
	@FunctionalInterface
	public interface CanHaveStatusEffectEvent
	{
		EventResult shouldDenyStatusEffect(StatusEffectInstance effect, AbilitySet abilities);
	}
	
	public static final Event<LivingEvents.GetMaxAirEvent> GET_MAX_AIR_EVENT = EventFactory.createLoop(LivingEvents.GetMaxAirEvent.class);
	
	@FunctionalInterface
	public interface GetMaxAirEvent
	{
		int maxAir(AbilitySet abilities, int maxAir);
	}
	
	/** Called by LivingEntityMixin when checking if the given effect is present */
	public static final Event<LivingEvents.HasStatusEffectEvent> HAS_STATUS_EFFECT_EVENT = EventFactory.createEventResult(LivingEvents.HasStatusEffectEvent.class);
	
	@FunctionalInterface
	public interface HasStatusEffectEvent
	{
		EventResult hasStatusEffect(final RegistryEntry<StatusEffect> effect, final LivingEntity entity, final AbilitySet abilities, final boolean truth);
	}
	
	/** Called by LivingEntityMixin when attempting to retrieve a status effect */
	public static final Event<LivingEvents.GetStatusEffectEvent> GET_STATUS_EFFECT_EVENT = EventFactory.of(listeners -> (LivingEvents.GetStatusEffectEvent) Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{LivingEvents.GetStatusEffectEvent.class}, new AbstractInvocationHandler()
	{
		@SuppressWarnings("unchecked")
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			for(LivingEvents.GetStatusEffectEvent listener : listeners)
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
	
	public static final Event<LivingEvents.CanFly> CAN_FLY_EVENT = EventFactory.createEventResult(LivingEvents.CanFly.class);
	
	@FunctionalInterface
	public interface CanFly
	{
		EventResult canCurrentlyFly(LivingEntity entity);
	}
	
	public static final Event<LivingEvents.CustomElytraCheck> CUSTOM_ELYTRA_CHECK_EVENT = EventFactory.createEventResult(LivingEvents.CustomElytraCheck.class);
	
	@FunctionalInterface
	public interface CustomElytraCheck
	{
		EventResult passesElytraCheck(LivingEntity entity, boolean ticking);
	}
	
	public static final Event<LivingEvents.LivingFallEvent> ON_FALL_EVENT = EventFactory.createLoop(LivingEvents.LivingFallEvent.class);
	
	@FunctionalInterface
	public interface LivingFallEvent
	{
		void onLivingFall(LivingEntity living, float fallDistance, boolean onGround, BlockState stateLandedOn, BlockPos landedPosition);
	}
	
	/** If true, prevents slowdown being applied by the given blockstate to the given living entity */
	public static final Event<LivingEvents.LivingSlowEvent> IGNORE_SLOW_EVENT = EventFactory.createEventResult(LivingEvents.LivingSlowEvent.class);
	
	@FunctionalInterface
	public interface LivingSlowEvent
	{
		EventResult shouldIgnoreSlowingFrom(LivingEntity living, BlockState state);
	}
	
	/** Identical to the native LIVING_HURT event, but excludes void and /kill damage events */
	public static final Event<LivingHurt> LIVING_HURT_EVENT = EventFactory.createEventResult();
}