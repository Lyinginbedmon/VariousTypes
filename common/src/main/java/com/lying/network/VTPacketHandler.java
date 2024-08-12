package com.lying.network;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.screen.AbilityMenuHandler;
import com.lying.utility.ServerEvents;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class VTPacketHandler
{
	public static final Identifier FINISH_CHARACTER_ID	= prefix("c2s_finish_character_creation");
	public static final Identifier OPEN_ABILITY_MENU_ID	= prefix("c2s_open_ability_menu");
	public static final Identifier SET_FAVOURITE_ABILITY_ID	= prefix("c2s_set_favourite_ability");
	public static final Identifier ACTIVATE_ABILITY_ID	= prefix("c2s_activate_ability");
	public static final Identifier PLAYER_FLYING_INPUT_ID	= prefix("c2s_player_flying_input");
	
	public static final Identifier SYNC_ACTIONABLES_ID	= prefix("s2c_sync_actionables");
	public static final Identifier SYNC_FATIGUE_ID	= prefix("s2c_sync_fatigue");
	public static final Identifier SYNC_POSE_ID		= prefix("s2c_sync_pose");
	
	public static void initServer()
	{
		if(Platform.getEnv() == EnvType.SERVER)
		{
			NetworkManager.registerS2CPayloadType(SyncActionablesPacket.PACKET_TYPE, SyncActionablesPacket.PACKET_CODEC);
			NetworkManager.registerS2CPayloadType(SyncFatiguePacket.PACKET_TYPE, SyncFatiguePacket.PACKET_CODEC);
			NetworkManager.registerS2CPayloadType(SyncPosePacket.PACKET_TYPE, SyncPosePacket.PACKET_CODEC);
		}
		
    	NetworkManager.registerReceiver(NetworkManager.c2s(), FinishCharacterCreationPacket.PACKET_TYPE, FinishCharacterCreationPacket.PACKET_CODEC, (value, context) -> 
    	{
    		VariousTypes.getSheet(context.getPlayer()).ifPresent(sheet -> 
    		{
    			sheet.clear();
    			
    			if(value.speciesId() != null)
    				sheet.module(VTSheetModules.SPECIES).set(value.speciesId());
    			
    			sheet.module(VTSheetModules.TEMPLATES).set(value.templateIds().toArray(new Identifier[0]));
    			
    			sheet.buildSheet();
    			sheet.incEdits();
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.c2s(), OpenAbilityMenuPacket.PACKET_TYPE, OpenAbilityMenuPacket.PACKET_CODEC, (value, context) -> 
    		MenuRegistry.openMenu((ServerPlayerEntity)context.getPlayer(), new SimpleNamedScreenHandlerFactory((id, playerInv, custom) -> new AbilityMenuHandler(id), context.getPlayer().getDisplayName())));
    	NetworkManager.registerReceiver(NetworkManager.c2s(), SetFavouriteAbilityPacket.PACKET_TYPE, SetFavouriteAbilityPacket.PACKET_CODEC, (value, context) -> 
    	{
    		ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
    		VariousTypes.getSheet(player).ifPresent(sheet -> 
    		{
        		Optional<Identifier> map = value.contents();
    			if(sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).setFavourite(value.slot(), map.isPresent() ? map.get() : null))
    				sheet.markDirty();
    		});
    	});
    	NetworkManager.registerReceiver(NetworkManager.c2s(), ActivateAbilityPacket.PACKET_TYPE, ActivateAbilityPacket.PACKET_CODEC, (value, context) -> 
    		VariousTypes.getSheet(context.getPlayer()).ifPresent(sheet -> sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).enactActionable(context.getPlayer(), value.mapName())));
    	NetworkManager.registerReceiver(NetworkManager.c2s(), PlayerFlightInputPacket.PACKET_TYPE, PlayerFlightInputPacket.PACKET_CODEC, (value, context) -> 
    	{
    		ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
    		if(player.isFallFlying())
    			ServerEvents.LivingEvents.PLAYER_FLIGHT_INPUT_EVENT.invoker().onPlayerInput((ServerPlayerEntity)context.getPlayer(), value.forward(), value.strafing(), value.jumping(), value.sneaking());
    	});
	}
}
