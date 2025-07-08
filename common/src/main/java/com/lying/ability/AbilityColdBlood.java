package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import org.joml.Math;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityColdBlood.ConfigColdBlood;
import com.lying.data.VTTags;
import com.lying.event.LivingEvents;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Precipitation;

public class AbilityColdBlood extends Ability implements IComplexAbility<ConfigColdBlood>
{
	public AbilityColdBlood(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigColdBlood values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				VTUtils.getEffectNames(values.effects)));
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.LIVING_MOVE_TICK_EVENT.register((living, sheetOpt) -> 
			sheetOpt.ifPresent(sheet -> 
			{
				World world = living.getEntityWorld();
				if(world.isClient() || world.getTime()%(Reference.Values.TICKS_PER_SECOND * 5) > 0) return;
				
				AbilityInstance inst = sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
				if(inst == null) return;
				
				ConfigColdBlood config = ConfigColdBlood.fromNbt(inst.memory());
				BlockPos pos = living.getBlockPos();
				if(config.activeIn(world.getBiome(pos), world.isRaining() && world.isSkyVisible(pos) && world.getFluidState(pos).isEmpty(), pos))
					config.effects.stream().filter(e -> living.canHaveStatusEffect(e)).forEach(e -> 
						living.addStatusEffect(new StatusEffectInstance(e.getEffectType(), e.getDuration(), e.getAmplifier(), e.isAmbient(), e.shouldShowParticles())));
			}));
	}
	
	public ConfigColdBlood memoryToValues(NbtCompound data) { return ConfigColdBlood.fromNbt(data); }
	
	public static class ConfigColdBlood
	{
		protected static final Codec<ConfigColdBlood> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			TagKey.codec(RegistryKeys.BIOME).listOf().optionalFieldOf("BiomeTags").forGetter(v -> Optional.of(v.biomeTags)),
			Temperature.CODEC.optionalFieldOf("Temperature").forGetter(v -> v.tempRange),
			Precipitation.CODEC.optionalFieldOf("Weather").forGetter(v -> v.weather),
			StatusEffectInstance.CODEC.listOf().optionalFieldOf("Effects").forGetter(v -> Optional.of(v.effects)))
				.apply(instance, ConfigColdBlood::new));
		
		private List<TagKey<Biome>> biomeTags = Lists.newArrayList();
		private Optional<Temperature> tempRange = Optional.empty();
		private Optional<Precipitation> weather = Optional.empty();
		
		private List<StatusEffectInstance> effects = Lists.newArrayList();
		
		public ConfigColdBlood(Optional<List<TagKey<Biome>>> tagsIn, Optional<Temperature> tempIn, Optional<Precipitation> weatherIn, Optional<List<StatusEffectInstance>> effectsIn)
		{
			tagsIn.ifPresentOrElse(val -> biomeTags.addAll(val), () -> biomeTags.add(VTTags.COLD_BIOMES));
			tempRange = tempIn;
			weather = weatherIn;
			
			effectsIn.ifPresentOrElse(val -> effects.addAll(val), () -> 
			{
				effects.add(new StatusEffectInstance(StatusEffects.SLOWNESS, Reference.Values.TICKS_PER_SECOND * 10));
				effects.add(new StatusEffectInstance(StatusEffects.WEAKNESS, Reference.Values.TICKS_PER_SECOND * 10));
			});
		}
		
		public boolean activeIn(RegistryEntry<Biome> biome, boolean isRaining, BlockPos position)
		{
			if(biomeTags.stream().anyMatch(tag -> biome.isIn(tag)))
				return true;
			
			Biome val = biome.value();
			if(tempRange.isPresent() && tempRange.get().isWithin(val.getTemperature()))
				return true;
			
			if(weather.isPresent())
			{
				Precipitation wet = weather.get();
				if((!isRaining && wet == Precipitation.NONE) || (isRaining && val.getPrecipitation(position) == wet))
					return true;
			}
			
			return false;
		}
		
		public record Temperature(float a, float b)
		{
			public static final Codec<Temperature> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.FLOAT.fieldOf("Min").forGetter(Temperature::min),
				Codec.FLOAT.fieldOf("Max").forGetter(Temperature::max)).apply(instance, Temperature::new));
			
			public float min() { return MathHelper.clamp(Math.min(a, b), -2F, 2F); }
			public float max() { return MathHelper.clamp(Math.max(a, b), -2F, 2F); }
			
			public boolean isWithin(float val) { return val >= min() && val <= max(); }
		}
		
		public static ConfigColdBlood fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
