package com.lying.utility;

import com.lying.VariousTypes;
import com.lying.init.VTTypes;

import dev.architectury.event.events.common.PlayerEvent;

public class ServerBus
{
	public static void init()
	{
		ServerEvents.SheetEvents.GET_TYPES_EVENT.register((entity, home, types) -> 
		{
			if(types.contains(VTTypes.NATIVE.get()) || types.contains(VTTypes.OTHAKIN.get()))
				return;
			
			// Automatically add NATIVE or OTHAKIN depending on relation to home dimension
			entity.ifPresent(ent -> types.add(home == null || home == ent.getWorld().getRegistryKey() ? VTTypes.NATIVE.get() : VTTypes.OTHAKIN.get()));
		});
		
		PlayerEvent.CHANGE_DIMENSION.register((player, oldLevel, newLevel) -> VariousTypes.getSheet(player).ifPresent(sheet -> sheet.buildSheet()));
	}
}