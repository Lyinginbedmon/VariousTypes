package com.lying.utility;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.joml.Vector2i;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lying.ability.AbilityInstance;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleTemplates;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Type;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
		
		// Pick a random arrangement of valid templates
		ModuleTemplates templates = sheet.module(VTSheetModules.TEMPLATES);
		List<Template> candidates = getValidTemplatesFor(sheet, ent, Math.max(0, power - sheet.power()));
		while(!candidates.isEmpty())
		{
			Template candidate = candidates.get(rand.nextInt(candidates.size()));
			templates.add(candidate.registryName());
			candidates = getValidTemplatesFor(sheet, ent, Math.max(0, power - sheet.power()));
		}
		
		return sheet;
	}
	
	public static List<Template> getValidTemplatesFor(CharacterSheet sheet, LivingEntity owner, int powerLimit)
	{
		boolean ignoreConditions = owner.getType() == EntityType.PLAYER && ((PlayerEntity)owner).isCreative();
		ModuleTemplates existing = sheet.module(VTSheetModules.TEMPLATES);
		int maxPower = powerLimit >= 0 ? powerLimit : Integer.MAX_VALUE;
		List<Template> templates = Lists.newArrayList();
		VTTemplateRegistry.instance().getAll().forEach(tem -> 
		{
			if(tem.power() > maxPower)
				return;
			else if(existing.contains(tem.registryName()))
				return;
			else if(!tem.validFor(sheet, owner) && !ignoreConditions)
				return;
			else
				templates.add(tem);
		});
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
	
	public static Text describeType(Type type)
	{
		return describe(type.displayName(), type.listID(), Optional.empty());
	}
	
	public static Text describeAbility(AbilityInstance inst)
	{
		MutableText tooltip = Text.empty().append(inst.displayName().copy().formatted(Formatting.BOLD)).append("\n");
		MutableText registry = Text.literal(inst.mapName().toString()).formatted(Formatting.DARK_GRAY);
		if(inst.cooldown() > 0)
			tooltip.append(Reference.ModInfo.translate("gui","ability_cooldown", ticksToSeconds(inst.cooldown()))).append("\n");
		
		if(inst.description().isPresent())
			tooltip.append(inst.description().get().copy().formatted(Formatting.ITALIC, Formatting.GRAY)).append("\n").append(registry);
		else
			tooltip.append(registry);
		return inst.displayName().copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));
	}
	
	public static Object ticksToSeconds(int ticks)
	{
		double seconds = (double)ticks / (double)Reference.Values.TICKS_PER_SECOND;
		if(seconds%1 == 0)
			return (int)seconds;
		else
			return seconds;
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
