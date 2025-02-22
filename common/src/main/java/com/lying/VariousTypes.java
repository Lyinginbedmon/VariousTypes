package com.lying;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.command.VTCommands;
import com.lying.component.CharacterSheet;
import com.lying.config.ServerConfig;
import com.lying.init.VTAbilities;
import com.lying.init.VTCosmetics;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTItems;
import com.lying.init.VTParticles;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTStatusEffects;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.network.VTPacketHandler;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;
import com.lying.utility.XPlatHandler;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    public static final Supplier<List<EntityType<? extends LivingEntity>>> SHEETED_ENTITIES = () -> List.of(EntityType.PLAYER, VTEntityTypes.ANIMATED_PLAYER.get());
    
    public static ServerConfig config;
    
    /** Cross-platform handler for vital functions that aren't handled directly by Architectury */
    private static XPlatHandler HANDLER = new XPlatHandler() 
    {
		public Optional<CharacterSheet> getSheet(LivingEntity entity) { return Optional.empty(); }
		
		public void setSheet(LivingEntity entity, CharacterSheet sheet) { }
    };
    
	public static void commonInit()
    {
    	config = new ServerConfig("config/VariousTypesServer.cfg");
    	config.read();
    	
    	VTSheetElements.init();
    	VTSheetModules.init();
    	
    	VTEntityTypes.init();
    	VTCosmetics.init();
    	VTAbilities.init();
    	VTTypes.init();
    	VTSpeciesRegistry.init();
    	VTTemplateRegistry.init();
    	VTScreenHandlerTypes.init();
    	VTItems.init();
    	VTStatusEffects.init();
    	VTCommands.init();
    	VTParticles.init();
    	ServerBus.init();
    	
    	VTPacketHandler.initServer();
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