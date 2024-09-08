package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityBadBreath.ConfigBadBreath;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityBadBreath extends ActivatedAbility implements IComplexAbility<ConfigBadBreath>
{
	public AbilityBadBreath(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigBadBreath values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				(int)values.maxRadius, 
				VTUtils.getEffectNames(values.effects), 
				VTUtils.ticksToTime(values.duration)));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 20; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		ConfigBadBreath config = memoryToValues(instance.memory());
		AreaEffectCloudEntity cloud = new AreaEffectCloudEntity(owner.getWorld(), owner.getX(), owner.getY(), owner.getZ());
		cloud.setOwner(owner);
		cloud.setRadius(3F);
		cloud.setDuration(config.duration);
		cloud.setRadiusGrowth((config.maxRadius - cloud.getRadius()) / (float)cloud.getDuration());
		config.effects.forEach(effect -> cloud.addEffect(new StatusEffectInstance(effect)));
		owner.getWorld().spawnEntity(cloud);
	}
	
	public ConfigBadBreath memoryToValues(NbtCompound data) { return ConfigBadBreath.fromNbt(data); }
	
	public static class ConfigBadBreath
	{
		protected static final Codec<ConfigBadBreath> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				StatusEffectInstance.CODEC.listOf().optionalFieldOf("Effects").forGetter(v -> Optional.of(v.effects)),
				Codec.INT.optionalFieldOf("Duration").forGetter(v -> Optional.of(v.duration)),
				Codec.FLOAT.optionalFieldOf("MaxRadius").forGetter(v -> Optional.of(v.maxRadius)))
					.apply(instance, ConfigBadBreath::new));
		
		protected List<StatusEffectInstance> effects = Lists.newArrayList();
		protected int duration = 10 * Reference.Values.TICKS_PER_SECOND;
		protected float maxRadius = 3F;
		
		public ConfigBadBreath(Optional<List<StatusEffectInstance>> listIn, Optional<Integer> durationIn, Optional<Float> radiusIn)
		{
			listIn.ifPresentOrElse(val -> effects.addAll(val), () -> effects.add(new StatusEffectInstance(StatusEffects.POISON, 15 * Reference.Values.TICKS_PER_SECOND)));
			durationIn.ifPresent(val -> duration = val);
			radiusIn.ifPresent(val -> maxRadius = val);
		}
		
		public static ConfigBadBreath fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
