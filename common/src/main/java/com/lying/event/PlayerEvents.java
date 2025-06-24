package com.lying.event;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.lying.utility.VTUtils;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEvents
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
	
	public static final Event<CanUnlockRecipeEvent> CAN_UNLOCK_RECIPE_EVENT = EventFactory.createEventResult(CanUnlockRecipeEvent.class);
	
	public interface CanUnlockRecipeEvent
	{
		EventResult canPlayerUnlockRecipeEvent(RecipeEntry<?> recipe, PlayerEntity player);
	}
	
	/** Called by ServerPlayerEntity before opening a handled screen */
	public static final Event<CanUseScreenEvent> CAN_USE_SCREEN_EVENT = EventFactory.createEventResult(CanUseScreenEvent.class);
	
	@FunctionalInterface
	public interface CanUseScreenEvent
	{
		EventResult canPlayerUseScreen(PlayerEntity player, ScreenHandlerType<?> screen);
	}
	
	/** Called by Item before testing for eating an item, return true to allow it (even inedible items) or false to deny it outright */
	public static final Event<CanEatItemEvent> CAN_EAT_EVENT = EventFactory.createEventResult(CanEatItemEvent.class);
	
	@FunctionalInterface
	public interface CanEatItemEvent
	{
		EventResult canEat(PlayerEntity user, ItemStack stack);
	}
	
	public static final Event<PlayerTakeDamageEvent> MODIFY_DAMAGE_TAKEN_EVENT = EventFactory.of(listeners -> (PlayerTakeDamageEvent)Proxy.newProxyInstance(EventFactory.class.getClassLoader(), new Class[]{PlayerTakeDamageEvent.class}, new AbstractInvocationHandler()
	{
		protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable
		{
			PlayerEntity player = (PlayerEntity)args[0];
			DamageSource source = (DamageSource)args[1];
			if(VTUtils.isDamageInviolable(source, player))
				return 1F;
			
			float result = 1F;
			for(PlayerTakeDamageEvent listener : listeners)
				result *= listener.getDamageModifier(player, source, result);
			
			return result;
		}
	}));
	
	@FunctionalInterface
	public interface PlayerTakeDamageEvent
	{
		/** Returns a multiplier, usually 0F or higher, or 1F if this source does not modifer the damage */
		float getDamageModifier(PlayerEntity living, DamageSource source, float amount);
	}
	
	public static final Event<LivingDropsEvent> PLAYER_DROPS_EVENT = EventFactory.createLoop(LivingDropsEvent.class);
	
	public interface LivingDropsEvent
	{
		void onLivingDrops(LivingEntity living, DamageSource source);
	}
}