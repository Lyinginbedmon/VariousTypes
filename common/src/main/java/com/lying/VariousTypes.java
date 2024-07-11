package com.lying;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.command.VTCommands;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTItems;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;
import com.lying.utility.XPlatHandler;

import net.minecraft.entity.LivingEntity;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
    /** Cross-platform handler for vital functions that aren't handled directly by Architectury */
    private static XPlatHandler HANDLER = new XPlatHandler() 
    {
		public Optional<CharacterSheet> getSheet(LivingEntity entity) { return Optional.empty(); }
		
		public void setSheet(LivingEntity entity, CharacterSheet sheet) { }
    };
    
    public static void commonInit()
    {
    	VTAbilities.init();
    	VTTypes.init();
    	VTSpeciesRegistry.init();
    	VTTemplateRegistry.init();
    	VTScreenHandlerTypes.init();
    	VTItems.init();
    	VTCommands.init();
    	ServerBus.init();
    }
    
    public static void setPlatHandler(XPlatHandler handler) { HANDLER = handler; }
    
    public static Optional<CharacterSheet> getSheet(LivingEntity entity)
    {
    	return HANDLER.getSheet(entity);
    }
    
    public static void setSheet(LivingEntity entity, CharacterSheet sheet)
    {
    	HANDLER.setSheet(entity, sheet);
    }
}