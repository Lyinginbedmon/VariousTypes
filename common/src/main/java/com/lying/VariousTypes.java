package com.lying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
    public static void commonInit()
    {
    	VTAbilities.init();
    	VTTypes.init();
    	
    	ServerBus.init();
    }
}