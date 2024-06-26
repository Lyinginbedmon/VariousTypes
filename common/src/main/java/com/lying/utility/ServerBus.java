package com.lying.utility;

import org.jetbrains.annotations.Nullable;

import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class ServerBus
{
	public static final Event<GetTypesEvent> GET_TYPES_EVENT = EventFactory.createLoop(GetTypesEvent.class);
	
	@FunctionalInterface
	public interface GetTypesEvent
	{
		void affectTypes(LivingEntity entity, @Nullable RegistryKey<World> homeDimension, TypeSet types);
	}
	
	/**
	 * Called by CharacterSheet after actions are reconstructed to allow abilities to affect them.<br>
	 * This is called after Types are applied and after AbilityBreathing abilities are accounted for.
	 */
	public static final Event<RebuildActionsEvent> AFTER_REBUILD_ACTIONS_EVENT = EventFactory.createLoop(RebuildActionsEvent.class);
	
	@FunctionalInterface
	public interface RebuildActionsEvent
	{
		void affectActions(ActionHandler handler, AbilitySet abilities, LivingEntity owner);
	}
	
	public static void init()
	{
		GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			if(types.contains(VTTypes.NATIVE.get()) || types.contains(VTTypes.OTHAKIN.get()))
				return;
			
			// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
			types.add(home == null || home == entity.getWorld().getRegistryKey() ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get());
		});
	}
}