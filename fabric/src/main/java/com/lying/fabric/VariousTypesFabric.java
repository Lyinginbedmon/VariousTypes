package com.lying.fabric;

import java.util.List;
import java.util.Optional;

import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.init.VTTypes;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.random.Random;

public final class VariousTypesFabric implements ModInitializer
{
    public void onInitialize()
    {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
    	
        // Run our common setup.
        VariousTypes.commonInit();
        
        VariousTypes.setGetSheetFunc(ent -> 
        {
        	// FIXME Temp method of retrieving character sheets per platform
        	if(ent.getType() == EntityType.PLAYER)
        	{
        		CharacterSheet sheet = new CharacterSheet(ent);
        		
        		Random rand = ent.getRandom();
        		TypeSet types = new TypeSet();
        		List<Supplier<Type>> supertypes = VTTypes.ofTier(Tier.SUPERTYPE);
        		List<Supplier<Type>> subtypes = VTTypes.ofTier(Tier.SUBTYPE);
        		types.add(supertypes.get(rand.nextInt(supertypes.size())).get());
        		for(int i=0; i<rand.nextBetween(1, 2); i++)
        			types.add(subtypes.get(rand.nextInt(subtypes.size())).get());
        		
        		sheet.setCustomTypes(types);
        		return Optional.of(sheet);
        	}
        	else
        		return Optional.empty();
        });
    }
}
