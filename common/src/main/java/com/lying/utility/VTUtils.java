package com.lying.utility;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.species.Species;
import com.lying.template.Template;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.random.Random;

public class VTUtils
{
	/** Generates a character sheet for the given entity with a random species and templates up to the given power level */
	public static CharacterSheet makeRandomSheet(LivingEntity ent, int power)
	{
		CharacterSheet sheet = new CharacterSheet(ent);
		
		Random rand = ent.getRandom();
		
		// Pick a random species
		List<Species> randomSpecies = Lists.newArrayList();
		randomSpecies.addAll(VTSpeciesRegistry.getAll());
		randomSpecies.removeIf(species -> species.power() > power);
		if(!randomSpecies.isEmpty())
			sheet.setSpecies(randomSpecies.get(rand.nextInt(randomSpecies.size())).registryName());
		
		// Pick a random arrangement of valid templates
		List<Template> candidates = getValidTemplatesFor(sheet, ent);
		candidates.removeIf(tem -> tem.power() > (power - sheet.power()));
		while(!candidates.isEmpty() && sheet.power() < power)
		{
			Template candidate = candidates.get(rand.nextInt(candidates.size()));
			if(candidate.validFor(sheet, ent))
			{
				sheet.addTemplate(candidate.registryName());
				break;
			}
			
			candidates = getValidTemplatesFor(sheet, ent);
			candidates.removeIf(tem -> tem.power() > (power - sheet.power()));
		}
		
		return sheet;
	}
	
	public static List<Template> getValidTemplatesFor(CharacterSheet sheet, LivingEntity owner)
	{
		List<Template> templates = Lists.newArrayList();
		templates.addAll(VTTemplateRegistry.instance().getAll());
		templates.removeIf(tem -> sheet.hasTemplate(tem.registryName()) || !tem.validFor(sheet, owner));
		return templates;
	}
}
