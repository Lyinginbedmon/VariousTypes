package com.lying.event;

import java.util.Optional;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class MiscEvents
{
	public static final Event<VibrationEvent> ON_VIBRATION_EVENT = EventFactory.createLoop(VibrationEvent.class);
	
	public interface VibrationEvent
	{
		void onVibration(RegistryEntry<GameEvent> gameEvent, Optional<Entity> entity, BlockPos block, World world);
	}
}
