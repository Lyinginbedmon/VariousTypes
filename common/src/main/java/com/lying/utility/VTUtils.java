package com.lying.utility;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2i;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleTemplates;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Type;

import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
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
		randomSpecies.addAll(VTSpeciesRegistry.instance().getAll());
		randomSpecies.removeIf(species -> species.power() > power);
		if(!randomSpecies.isEmpty())
			sheet.module(VTSheetModules.SPECIES).set(randomSpecies.get(rand.nextInt(randomSpecies.size())).registryName());
		
		if(sheet.power() < power)
		{
			ModuleTemplates templates = sheet.module(VTSheetModules.TEMPLATES);
			// Pick a random arrangement of valid templates
			List<Template> candidates = getValidTemplatesFor(sheet, ent);
			candidates.removeIf(tem -> tem.power() > (power - sheet.power()));
			
			while(!candidates.isEmpty() && sheet.power() < power)
			{
				Template candidate = candidates.get(rand.nextInt(candidates.size()));
				if(candidate.validFor(sheet, ent))
				{
					templates.add(candidate.registryName());
					break;
				}
				
				candidates = getValidTemplatesFor(sheet, ent);
				candidates.removeIf(tem -> tem.power() > (power - sheet.power()));
			}
		}
		
		return sheet;
	}
	
	public static List<Template> getValidTemplatesFor(CharacterSheet sheet, LivingEntity owner)
	{
		List<Template> templates = Lists.newArrayList();
		templates.addAll(VTTemplateRegistry.instance().getAll());
		templates.removeIf(tem -> ModuleTemplates.hasTemplate(sheet, tem.registryName()) || !tem.validFor(sheet, owner));
		return templates;
	}
	
	public static int stringComparator(String name1, String name2)
	{
		List<String> names = Lists.newArrayList(name1, name2);
		Collections.sort(names);
		int ind1 = names.indexOf(name1);
		int ind2 = names.indexOf(name2);
		return ind1 > ind2 ? 1 : ind1 < ind2 ? -1 : 0;
	}
	
	public static Text describeSpecies(Species spec)
	{
		return describe(spec.displayName(), spec.registryName(), spec.display().description());
	}
	
	public static Text describeTemplate(Template spec)
	{
		return describe(spec.displayName(), spec.registryName(), spec.display().description());
	}
	
	public static Text describeType(Type type, DynamicRegistryManager manager)
	{
		return describe(type.displayName(manager), type.listID(), Optional.empty());
	}
	
	private static Text describe(Text display, Identifier regName, Optional<Text> desc)
	{
		return display.copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, makeTooltip(display, regName, desc))));
	}
	
	public static Text makeTooltip(Text display, Identifier regName, Optional<Text> desc)
	{
		MutableText tooltip = Text.empty().append(display.copy().formatted(Formatting.BOLD)).append("\n");
		MutableText registry = Text.literal(regName.toString()).formatted(Formatting.DARK_GRAY);
		if(desc.isPresent())
			tooltip.append(desc.get().copy().formatted(Formatting.ITALIC, Formatting.GRAY)).append("\n").append(registry);
		else
			tooltip.append(registry);
		return tooltip;
	}
	
	public static <T extends Object> MutableText listToString(List<T> set, Function<T,Text> getter, String bridge)
	{
		MutableText string = Text.empty();
		for(int i=0; i<set.size(); i++)
		{
			string.append(getter.apply(set.get(i)));
			if(i < set.size() - 1)
				string.append(bridge);
		}
		return string;
	}
	
	/** Rotates a 2D vector clockwise */
	public static Vector2i rotateDegrees2D(Vector2i vec, double degrees)
	{
		double rads = Math.toRadians(degrees);
		double cos = Math.cos(rads);
		double sin = Math.sin(rads);
		return new Vector2i((int)(vec.x * cos - vec.y * sin), (int)(vec.x * sin + vec.y * cos));
	}
}
