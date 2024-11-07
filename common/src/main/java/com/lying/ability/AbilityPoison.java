package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityPoison.ConfigPoison;
import com.lying.init.VTAbilities;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityPoison extends AbilityOnMeleeHit implements IComplexAbility<ConfigPoison>
{
	public AbilityPoison(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public static AbilityInstance withEffects(List<StatusEffectInstance> list)
	{
		AbilityInstance inst = VTAbilities.POISON.get().instance();
		inst.setMemory((NbtCompound)ConfigPoison.CODEC.encodeStart(NbtOps.INSTANCE, new ConfigPoison(Optional.of(list))).getOrThrow());
		return inst;
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigPoison values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				VTUtils.getEffectNames(values.effects)));
	}
	
	public void onMeleeHit(LivingEntity victim, LivingEntity attacker, AbilityInstance inst)
	{
		ConfigPoison.fromNbt(inst.memory()).effects.stream().filter(e -> victim.canHaveStatusEffect(e)).forEach(e -> 
			victim.addStatusEffect(new StatusEffectInstance(e.getEffectType(), e.getDuration(), e.getAmplifier(), e.isAmbient(), e.shouldShowParticles()), attacker));
	}
	
	public ConfigPoison memoryToValues(NbtCompound data) { return ConfigPoison.fromNbt(data); }

	public static class ConfigPoison
	{
		protected static final Codec<ConfigPoison> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			StatusEffectInstance.CODEC.listOf().optionalFieldOf("Effects").forGetter(v -> Optional.of(v.effects)))
				.apply(instance, ConfigPoison::new));
			
		protected List<StatusEffectInstance> effects = Lists.newArrayList();
		
		public ConfigPoison(Optional<List<StatusEffectInstance>> listIn)
		{
			listIn.ifPresentOrElse(val -> effects.addAll(val), () -> effects.add(new StatusEffectInstance(StatusEffects.POISON, 15 * Reference.Values.TICKS_PER_SECOND)));
		}
		
		public static ConfigPoison fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
