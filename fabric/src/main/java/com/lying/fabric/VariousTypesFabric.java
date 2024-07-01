package com.lying.fabric;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.species.Species;
import com.lying.species.SpeciesRegistry;
import com.lying.template.Template;
import com.lying.template.TemplateRegistry;

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
        		List<Supplier<Species>> randomSpecies = Lists.newArrayList();
        		randomSpecies.addAll(SpeciesRegistry.defaultSpecies());
        		sheet.setSpecies(randomSpecies.get(rand.nextInt(randomSpecies.size())).get());
        		
        		List<Supplier<Template>> randomTemplates = Lists.newArrayList();
        		randomTemplates.addAll(TemplateRegistry.defaultTemplates());
        		for(int i=0; i<rand.nextBetween(1, 3); i++)
        		{
        			List<Supplier<Template>> candidates = Lists.newArrayList();
        			candidates.addAll(randomTemplates);
        			
        			while(!candidates.isEmpty())
        			{
        				Supplier<Template> candidate = candidates.get(rand.nextInt(candidates.size()));
        				if(candidate.get().validFor(sheet, ent))
        				{
        					sheet.addTemplate(candidate.get());
        					break;
        				}
        				candidates.remove(candidate);
        			}
        		}
        		
        		return Optional.of(sheet);
        	}
        	else
        		return Optional.empty();
        });
    }
}
