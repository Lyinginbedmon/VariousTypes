package com.lying;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.command.VTCommands;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTItems;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.network.ActivateAbilityReceiver;
import com.lying.network.FinishCharacterCreationReceiver;
import com.lying.network.OpenAbilityMenuReceiver;
import com.lying.network.SetFavouriteAbilityReceiver;
import com.lying.network.VTPacketHandler;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;
import com.lying.utility.XPlatHandler;

import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.LivingEntity;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
    // FIXME Replace with server config value
    public static int POWER = 5;
    
    /** Cross-platform handler for vital functions that aren't handled directly by Architectury */
    private static XPlatHandler HANDLER = new XPlatHandler() 
    {
		public Optional<CharacterSheet> getSheet(LivingEntity entity) { return Optional.empty(); }
		
		public void setSheet(LivingEntity entity, CharacterSheet sheet) { }
    };
    
    @SuppressWarnings("removal")
	public static void commonInit()
    {
    	VTSheetElements.init();
    	VTSheetModules.init();
    	
    	VTAbilities.init();
    	VTTypes.init();
    	VTSpeciesRegistry.init();
    	VTTemplateRegistry.init();
    	VTScreenHandlerTypes.init();
    	VTItems.init();
    	VTCommands.init();
    	ServerBus.init();
    	
    	NetworkManager.registerReceiver(NetworkManager.Side.C2S, VTPacketHandler.FINISH_CHARACTER_ID, new FinishCharacterCreationReceiver());
    	NetworkManager.registerReceiver(NetworkManager.Side.C2S, VTPacketHandler.OPEN_ABILITY_MENU_ID, new OpenAbilityMenuReceiver());
    	NetworkManager.registerReceiver(NetworkManager.Side.C2S, VTPacketHandler.SET_FAVOURITE_ABILITY_ID, new SetFavouriteAbilityReceiver());
    	NetworkManager.registerReceiver(NetworkManager.Side.C2S, VTPacketHandler.ACTIVATE_ABILITY_ID, new ActivateAbilityReceiver());
    }
    
    public static void setPlatHandler(XPlatHandler handler) { HANDLER = handler; }
    
    public static Optional<CharacterSheet> getSheet(@Nullable LivingEntity entity)
    {
    	Optional<CharacterSheet> sheetOpt = Optional.empty();
    	if(entity != null)
	    	try
	    	{
	    		sheetOpt = HANDLER.getSheet(entity);
	    	}
    		catch(Exception e) { }
    	return sheetOpt;
    }
    
    public static void setSheet(LivingEntity entity, CharacterSheet sheet)
    {
    	if(entity != null && sheet != null)
    		HANDLER.setSheet(entity, sheet);
    }
    
    public static void syncSheet(LivingEntity entity)
    {
    	if(entity != null)
    		HANDLER.syncSheet(entity);
    }
}