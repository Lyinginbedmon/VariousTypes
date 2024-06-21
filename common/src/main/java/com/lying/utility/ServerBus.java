package com.lying.utility;

import com.lying.init.VTTypes;
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
		void affectTypes(LivingEntity entity, RegistryKey<World> homeDimension, TypeSet types);
	}
	
	public static void init()
	{
		GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
			types.add(entity.getWorld().getRegistryKey() == home ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get());
		});
	}
}