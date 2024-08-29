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
import net.minecraft.text.MutableText;
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
				values.getEffectNames(), 
				VTUtils.ticksToSeconds(values.duration)));
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
				StatusEffectInstance.CODEC.listOf().optionalFieldOf("Effects").forGetter(ConfigBadBreath::effectsList),
				Codec.INT.optionalFieldOf("Duration").forGetter(ConfigBadBreath::duration),
				Codec.FLOAT.optionalFieldOf("MaxRadius").forGetter(ConfigBadBreath::maxRadius))
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
		
		protected Optional<List<StatusEffectInstance>> effectsList(){ return Optional.of(effects); }
		protected Optional<Integer> duration() { return Optional.of(duration); }
		protected Optional<Float> maxRadius() { return Optional.of(maxRadius); }
		
		public Text getEffectNames()
		{
			if(effects.isEmpty())
				return Text.empty();
			
			MutableText names = getEffectName(effects.get(0));
			if(effects.size() > 1)
				for(int i=1; i<effects.size(); i++)
					names.append(Text.literal(", ")).append(getEffectName(effects.get(i)));
			return names;
		}
		
		private MutableText getEffectName(StatusEffectInstance inst)
		{
			MutableText text = inst.getEffectType().value().getName().copy();
			if(inst.getAmplifier() > 0 && inst.getAmplifier() <= 9)
				text.append(Text.literal(" ")).append(Text.translatable("enchantment.level." + (inst.getAmplifier() + 1)));
			return text;
		}
		
		public static ConfigBadBreath fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
