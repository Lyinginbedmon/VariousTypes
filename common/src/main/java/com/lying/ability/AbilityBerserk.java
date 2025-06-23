package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityBerserk.ConfigBerserk;
import com.lying.component.CharacterSheet;
import com.lying.init.VTParticleTypes;
import com.lying.init.VTSoundEvents;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class AbilityBerserk extends ActivatedAbility implements ITickingAbility, IComplexAbility<ConfigBerserk>
{
	private static final String TIME = "Time";
	
	public AbilityBerserk(Identifier regName, Category catIn)
	{
		super(regName, catIn);
		this.soundSettings = new ActivationSoundSettings(
				i -> VTSoundEvents.BERSERK_ACTIVATE.get(), 
				1F, 
				true);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigBerserk values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.ticksToTime(values.buffTime), VTUtils.ticksToTime(values.debuffTime)));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_MINUTE * 5; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
		
		ConfigBerserk values = memoryToValues(instance.memory());
		owner.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, values.buffTime, 1));
		owner.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, values.buffTime, 8));
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance); }
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance) { return getCurrentTick(instance, owner.getWorld().getTime()) >= 0; }
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		ConfigBerserk values = memoryToValues(instance.memory());
		ServerWorld world = (ServerWorld)owner.getWorld();
		switch(getCurrentTick(instance, world.getTime()))
		{
			case 0:
				ITickingAbility.tryPutOnCooldown(instance, owner);
				
				owner.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, values.debuffTime));
				owner.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, values.debuffTime));
				break;
			case 1:
			default:
				Random rand = owner.getRandom();
				for(int i=2; i>0; --i)
					VTUtils.spawnParticles(
							world,
							VTParticleTypes.RAGE.get(), 
							owner.getPos().add(owner.getParticleX(0.5D), rand.nextDouble() * (owner.getHeight() - 0.25D), owner.getParticleZ(0.5D)),
							Vec3d.ZERO);
				break;
		}
	}
	
	private void startTicking(AbilityInstance instance, LivingEntity owner)
	{
		long start = owner.getWorld().getTime();
		NbtCompound memory = instance.memory();
		memory.putLong(TIME, start + memoryToValues(instance.memory()).buffTime);
		instance.setMemory(memory);
		
		ITickingAbility.tryPutOnIndefiniteCooldown(instance.mapName(), owner);
	}
	
	private static int getCurrentTick(AbilityInstance instance, long currentTime)
	{
		long finish = instance.memory().contains(TIME, NbtElement.LONG_TYPE) ? instance.memory().getLong(TIME) : currentTime - 1;
		if(finish < currentTime)
		{
			NbtCompound memory = instance.memory();
			memory.remove(TIME);
			instance.setMemory(memory);
		}
		return (int)(finish - currentTime);
	}
	
	public ConfigBerserk memoryToValues(NbtCompound data) { return ConfigBerserk.fromNbt(data); }
	
	public static class ConfigBerserk
	{
		protected static final Codec<ConfigBerserk> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("BuffTime").forGetter(ConfigBerserk::buffTime), 
				Codec.INT.optionalFieldOf("DebuffTime").forGetter(ConfigBerserk::debuffTime))
					.apply(instance, ConfigBerserk::new));
		
		protected int buffTime = 12 * Reference.Values.TICKS_PER_SECOND;
		protected int debuffTime = 15 * Reference.Values.TICKS_PER_SECOND;
		
		public ConfigBerserk(Optional<Integer> rateIn, Optional<Integer> amountIn)
		{
			rateIn.ifPresent(val -> buffTime = val);
			amountIn.ifPresent(val -> debuffTime = val);
		}
		
		protected Optional<Integer> buffTime(){ return Optional.of(buffTime); }
		protected Optional<Integer> debuffTime(){ return Optional.of(debuffTime); }
		
		public static ConfigBerserk fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
