package com.lying.ability;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.event.MiscEvents;
import com.lying.init.VTSheetElements;
import com.lying.network.HighlightBlockPacket;
import com.lying.network.HighlightEntityPacket;
import com.lying.reference.Reference;
import com.lying.utility.BlockHighlight;
import com.lying.utility.EntityHighlight;

import net.minecraft.entity.Entity;
import net.minecraft.registry.tag.GameEventTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AbilitySculksight extends ToggledAbility
{
	public AbilitySculksight(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public void registerEventHandlers()
	{
		MiscEvents.ON_VIBRATION_EVENT.register((gameEvent, entity, block, world) -> 
		{
			if(world.isClient())
				return;
			
			if(entity.isPresent())
			{
				if(entity.get().isSpectator())
					return;
				else if(gameEvent.isIn(GameEventTags.IGNORE_VIBRATIONS_SNEAKING) && entity.get().bypassesSteppingEffects())
					return;
				else if(entity.get().occludeVibrationSignals())
					return;
			}
			
			long currentTime = world.getTime();
			int duration = Reference.Values.TICKS_PER_SECOND * 5;
			EntityHighlight entityH = entity.isPresent() ? new EntityHighlight(entity.get().getUuid(), currentTime, duration) : null;
			BlockHighlight blockH = block.isPresent() ? new BlockHighlight(block.get(), currentTime, duration, 0x024050) : null;
			
			BlockPos pos = block.get();
			int range = gameEvent.value().notificationRadius();
			world.getPlayers().stream()
				.filter(Entity::isAlive)
				.filter(player -> !player.isSpectator())
				.filter(player -> player.squaredDistanceTo(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < (range * range))
				.filter(player -> entity.isEmpty() || entity.get() != player)
				.filter(player -> 
				{
					Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
					return sheetOpt.isPresent() && ToggledAbility.hasActive(sheetOpt.get().elementValue(VTSheetElements.ACTIONABLES), registryName());
				})
					.forEach(player -> 
					{
						if(blockH != null)
							HighlightBlockPacket.send((ServerPlayerEntity)player, blockH);
						
						if(entityH != null)
							HighlightEntityPacket.send((ServerPlayerEntity)player, entityH);
					});
		});
	}
}
