package com.lying;

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTItems;
import com.lying.init.VTSpecies;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;

import net.minecraft.entity.LivingEntity;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
    private static Function<LivingEntity, Optional<CharacterSheet>> getSheetFunc = (ent) -> Optional.empty();
    
    public static void commonInit()
    {
    	VTAbilities.init();
    	VTTypes.init();
    	VTSpecies.init();
    	VTItems.init();
    	ServerBus.init();
    }
    
    public static Optional<CharacterSheet> getSheet(LivingEntity entity)
    {
    	return getSheetFunc.apply(entity);
    }
    
    public static void setGetSheetFunc(Function<LivingEntity, Optional<CharacterSheet>> funcIn)
    {
    	getSheetFunc = funcIn;
    }
}