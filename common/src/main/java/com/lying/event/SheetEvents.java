package com.lying.event;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.ability.AbilitySet;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class SheetEvents
{
	public static final Event<SheetEvents.GetTypesEvent> GET_TYPES_EVENT = EventFactory.createLoop(SheetEvents.GetTypesEvent.class);
	
	@FunctionalInterface
	public interface GetTypesEvent
	{
		void affectTypes(Optional<LivingEntity> entity, @Nullable RegistryKey<World> homeDimension, TypeSet types);
	}
	
	/**
	 * Called by CharacterSheet after actions are reconstructed to allow abilities to affect them.<br>
	 * This is called after Types are applied and after AbilityBreathing abilities are accounted for.
	 */
	public static final Event<SheetEvents.RebuildActionsEvent> AFTER_REBUILD_ACTIONS_EVENT = EventFactory.createLoop(SheetEvents.RebuildActionsEvent.class);
	
	@FunctionalInterface
	public interface RebuildActionsEvent
	{
		void affectActions(ActionHandler handler, AbilitySet abilities, Optional<LivingEntity> owner);
	}
	
	public static final Event<SheetEvents.SheetRebuildEvent> BEFORE_REBUILD_EVENT = EventFactory.createLoop(SheetEvents.SheetRebuildEvent.class);
	
	public static final Event<SheetEvents.SheetRebuildEvent> AFTER_REBUILD_EVENT = EventFactory.createLoop(SheetEvents.SheetRebuildEvent.class);
	
	@FunctionalInterface
	public interface SheetRebuildEvent
	{
		void process(LivingEntity owner, AbilitySet abilities);
	}
}