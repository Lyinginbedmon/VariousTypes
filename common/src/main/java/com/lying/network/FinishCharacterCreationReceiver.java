package com.lying.network;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;

import dev.architectury.networking.NetworkManager.NetworkReceiver;
import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class FinishCharacterCreationReceiver implements NetworkReceiver<RegistryByteBuf>
{
	public void receive(RegistryByteBuf value, PacketContext context)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
		
		Identifier speciesId = value.readIdentifier();
		if(speciesId.equals(new Identifier("debug:no_species")))
			speciesId = null;
		
		List<Identifier> templateIds = Lists.newArrayList();
		for(int i=0; i<value.readInt(); i++)
			templateIds.add(value.readIdentifier());
		
		applyCreation(player, speciesId, templateIds);
	}
	
	private void applyCreation(PlayerEntity player, Identifier speciesId, List<Identifier> templateIds)
	{
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			sheet.clear(false);
			
			if(speciesId != null)
				sheet.setSpecies(speciesId);
			
			templateIds.forEach(tem -> sheet.addTemplate(tem));
			
			sheet.buildSheet();
		});
	}
}
