package com.lying;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTItems;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.ServerBus;

import net.minecraft.entity.LivingEntity;

public class VariousTypes
{
    public static final Logger LOGGER = LoggerFactory.getLogger(Reference.ModInfo.MOD_ID);
    
    private static Optional<Function<LivingEntity, Optional<CharacterSheet>>> getSheetFunc = Optional.empty();
    private static Optional<BiConsumer<LivingEntity,CharacterSheet>> setSheetFunc = Optional.empty();
    
    public static void commonInit()
    {
    	VTAbilities.init();
    	VTTypes.init();
    	VTSpeciesRegistry.init();
    	VTTemplateRegistry.init();
    	VTItems.init();
    	ServerBus.init();
    }
    
    public static Optional<CharacterSheet> getSheet(LivingEntity entity)
    {
    	return getSheetFunc.isPresent() ? getSheetFunc.get().apply(entity) : Optional.empty();
    }
    
    public static void setSheet(LivingEntity entity, CharacterSheet sheet)
    {
    	setSheetFunc.ifPresent(func -> func.accept(entity, sheet));
    }
    
    public static void getSheetFunc(Function<LivingEntity, Optional<CharacterSheet>> funcIn)
    {
    	getSheetFunc = Optional.of(funcIn);
    }
    
    public static void setSheetFunc(BiConsumer<LivingEntity,CharacterSheet> funcIn)
    {
    	setSheetFunc = Optional.of(funcIn);
    }
}