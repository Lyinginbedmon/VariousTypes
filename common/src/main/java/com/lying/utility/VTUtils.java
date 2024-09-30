package com.lying.utility;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collections;
import java.util.Comparator;
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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class VTUtils
{
	public static final Comparator<Text> TEXT_ALPHABETICAL = (a, b) -> stringComparator(a.getString(), b.getString());
	
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
	
	public static void playSound(Entity owner, SoundEvent sound, SoundCategory category, float pitch, float volume)
	{
		playSound(owner.getWorld(), RegistryEntry.of(sound), category, owner.getX(), owner.getEyeY(), owner.getZ(), pitch, volume, owner.getWorld().getRandom().nextLong());
	}
	
	public static void playSound(World world, RegistryEntry<SoundEvent> sound, SoundCategory category, double x, double y, double z, float pitch, float volume, long seed)
	{
		world.getPlayers().forEach(player -> ((ServerPlayerEntity)player).networkHandler.send(new PlaySoundS2CPacket(sound, category, x, y, z, pitch, volume, seed), null));
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
		if(inst.cooldown() > 0)
			tooltip.append(Reference.ModInfo.translate("gui","ability_cooldown", ticksToTime(inst.cooldown()))).append("\n");
		
		inst.tooltip().forEach(line -> tooltip.append(line.copy().formatted(Formatting.ITALIC, Formatting.GRAY).append("\n")));
		tooltip.append(Text.literal(inst.mapName().toString()).formatted(Formatting.DARK_GRAY));
		return inst.displayName().copy().styled(style -> style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)));
	}
	
	/** Converts game ticks into human-readable time in hours, minutes, and seconds */
	public static Text ticksToTime(int ticks)
	{
		int s = Math.floorDiv(ticks, Reference.Values.TICKS_PER_SECOND);
		int m = Math.floorDiv(s, 60);
		int h = Math.floorDiv(m, 60);
		
		s = s%60;
		m = m%60;
		
		MutableText time = Text.empty();
		Text prefix = Text.literal(" ");
		boolean prepend = false;
		if(h > 0)
		{
			time.append(translate("gui", "hours", h));
			prepend = true;
		}
		
		if(m > 0)
		{
			if(prepend) time.append(prefix);
			time.append(translate("gui", "minutes", m));
			prepend = true;
		}
		
		if(s > 0)
		{
			if(prepend) time.append(prefix);
			time.append(translate("gui", "seconds", s));
			prepend = true;
		}
		
		if(ticks%Reference.Values.TICKS_PER_SECOND > 0)
		{
			if(prepend) time.append(prefix);
			double excess = (double)(ticks%Reference.Values.TICKS_PER_SECOND) / (double)Reference.Values.TICKS_PER_SECOND;
			time.append(translate("gui", "milliseconds", excess * 1000));
		}
		
		return time;
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
	
	public static <T> MutableText tagListToString(List<TagKey<T>> set, String bridge)
	{
		return listToString(set, t -> Text.literal("#" + t.id().toString()), bridge);
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
	
	/** Returns true if the given damage is system damage necessary for stable gameplay */
	public static boolean isDamageInviolable(DamageSource source, Entity entity)
	{
		DamageSources types = entity.getWorld().getDamageSources();
		List<DamageSource> system = List.of(
				types.genericKill(), 
				types.outsideBorder(), 
				types.outOfWorld());
		return system.contains(source);
	}
	
	public static MutableText getEffectName(StatusEffectInstance inst)
	{
		MutableText text = inst.getEffectType().value().getName().copy();
		if(inst.getAmplifier() > 0 && inst.getAmplifier() <= 9)
			text.append(Text.literal(" ")).append(Text.translatable("enchantment.level." + (inst.getAmplifier() + 1)));
		return text;
	}
	
	public static Text getEffectNames(List<StatusEffectInstance> effects)
	{
		if(effects.isEmpty())
			return Text.empty();
		
		MutableText names = getEffectName(effects.get(0));
		if(effects.size() > 1)
			for(int i=1; i<effects.size(); i++)
				names.append(Text.literal(", ")).append(getEffectName(effects.get(i)));
		return names;
	}
}
