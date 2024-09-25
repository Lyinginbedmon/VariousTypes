package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySunblind.ConfigSunblind;
import com.lying.event.LivingEvents;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class AbilitySunblind extends Ability implements IComplexAbility<ConfigSunblind>
{
	public AbilitySunblind(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigSunblind values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				VTUtils.getEffectNames(values.debuffs)));
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.LIVING_MOVE_TICK_EVENT.register((living, sheetOpt) -> 
		{
			if(living.isInvisible() || !isAffectedByDaylight(living) || sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return;
			
			AbilityInstance inst = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
			ConfigSunblind config = ((AbilitySunblind)inst.ability()).instanceToValues(inst);
			config.debuffs.forEach(debuff -> 
				living.addStatusEffect(new StatusEffectInstance(debuff.getEffectType(), Reference.Values.TICKS_PER_SECOND * 10, debuff.getAmplifier(), true, false)));
		});
	}
	
	@SuppressWarnings("deprecation")
	private static boolean isAffectedByDaylight(Entity ent)
	{
		if(ent.getWorld().isDay() && !ent.getWorld().isClient())
		{
			float f = ent.getBrightnessAtEyes();
			BlockPos pos = BlockPos.ofFloored(ent.getX(), ent.getEyeY(), ent.getZ());
			return f > 0.5F && ent.getWorld().isSkyVisible(pos) && ent.getWorld().getRandom().nextFloat() * 30F < (f - 0.4F) * 2F;
		}
		return false;
	}
	
	public ConfigSunblind memoryToValues(NbtCompound nbt) { return ConfigSunblind.fromNbt(nbt); }
	
	public static class ConfigSunblind
	{
		protected static final Codec<ConfigSunblind> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				StatusEffectInstance.CODEC.listOf().optionalFieldOf("Debuffs").forGetter(v -> Optional.of(v.debuffs)))
					.apply(instance, ConfigSunblind::new));
		
		protected List<StatusEffectInstance> debuffs;
		
		public ConfigSunblind(Optional<List<StatusEffectInstance>> debuffsIn)
		{
			debuffs = debuffsIn.orElse(List.of(new StatusEffectInstance(VTStatusEffects.getEntry(VTStatusEffects.DAZZLED), 0, 0)));
		}
		
		public static ConfigSunblind fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}