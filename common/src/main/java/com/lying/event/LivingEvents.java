package com.lying.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Optional;

import com.google.common.reflect.AbstractInvocationHandler;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent.LivingHurt;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LivingEvents
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
	
	/** Fired whenever the entity lands from a fall */
	public static final Event<LivingFallEvent> ON_FALL_EVENT = EventFactory.createLoop(LivingFallEvent.class);
	
	@FunctionalInterface
	public interface LivingFallEvent
	{
		void onLivingFall(LivingEntity living, float fallDistance, boolean onGround, BlockState stateLandedOn, BlockPos landedPosition);
	}
	
	/** If true, prevents slowdown being applied by the given blockstate to the given living entity */
	public static final Event<LivingSlowEvent> IGNORE_SLOW_EVENT = EventFactory.createEventResult(LivingSlowEvent.class);
	
	@FunctionalInterface
	public interface LivingSlowEvent
	{
		EventResult shouldIgnoreSlowingFrom(LivingEntity living, BlockState state);
	}
	
	/** Identical to the native LIVING_HURT event, but excludes void and /kill damage events */
	public static final Event<LivingHurt> LIVING_HURT_EVENT = EventFactory.createEventResult();
	
	public static final Event<LivingSteppedOnEvent> ON_STEP_ON_BLOCK_EVENT = EventFactory.createLoop(LivingSteppedOnEvent.class);
	
	public interface LivingSteppedOnEvent
	{
		void onBlockSteppedOn(LivingEntity living, BlockState state, BlockPos pos, World world);
	}
	
	public static final Event<LivingMoveTickEvent> LIVING_MOVE_TICK_EVENT = EventFactory.createLoop(LivingMoveTickEvent.class);
	
	public interface LivingMoveTickEvent
	{
		void onLivingMoveTick(LivingEntity living, Optional<CharacterSheet> sheetOpt);
	}
	
	public static final Event<LivingMoveEvent> LIVING_MOVE_EVENT = EventFactory.createLoop(LivingMoveEvent.class);
	
	public interface LivingMoveEvent
	{
		void onLivingMove(LivingEntity living, MovementType type, Vec3d move, Optional<CharacterSheet> sheetOpt);
	}
}