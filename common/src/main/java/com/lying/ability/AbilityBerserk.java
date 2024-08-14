package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityBerserk.OperatingValuesBerserk;
import com.lying.component.CharacterSheet;
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
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class AbilityBerserk extends ActivatedAbility implements ITickingAbility, IComplexAbility<OperatingValuesBerserk>
{
	private static final String TIME = "Time";
	
	public AbilityBerserk(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		OperatingValuesBerserk values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.ticksToSeconds(values.buffTime), VTUtils.ticksToSeconds(values.debuffTime)));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_MINUTE * 5; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
		
		OperatingValuesBerserk values = memoryToValues(instance.memory());
		owner.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, values.buffTime, 1));
		owner.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, values.buffTime, 8));
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance); }
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance) { return getCurrentTick(instance, owner.getWorld().getTime()) >= 0; }
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		OperatingValuesBerserk values = memoryToValues(instance.memory());
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
				for(int i=0; i<2; i++)
				{
					double x = owner.getX() + ((rand.nextDouble() - 0.5) * owner.getWidth());
					double y = owner.getRandomBodyY() - 0.25;
					double z = owner.getZ() + ((rand.nextDouble() - 0.5) * owner.getWidth());
					
					double velX = (rand.nextDouble() - 0.5) * 2;
					double velY = -rand.nextDouble();
					double velZ = (rand.nextDouble() - 0.5) * 2;
					world.spawnParticles(DustParticleEffect.DEFAULT, x, y, z, 1, velX, velY, velZ, 1);
				}
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
	
	public OperatingValuesBerserk memoryToValues(NbtCompound data) { return OperatingValuesBerserk.fromNbt(data); }
	
	public static class OperatingValuesBerserk
	{
		protected static final Codec<OperatingValuesBerserk> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("BuffTime").forGetter(OperatingValuesBerserk::buffTime), 
				Codec.INT.optionalFieldOf("DebuffTime").forGetter(OperatingValuesBerserk::debuffTime))
					.apply(instance, OperatingValuesBerserk::new));
		
		protected int buffTime = 12 * Reference.Values.TICKS_PER_SECOND;
		protected int debuffTime = 15 * Reference.Values.TICKS_PER_SECOND;
		
		public OperatingValuesBerserk(Optional<Integer> rateIn, Optional<Integer> amountIn)
		{
			rateIn.ifPresent(val -> buffTime = val);
			amountIn.ifPresent(val -> debuffTime = val);
		}
		
		protected Optional<Integer> buffTime(){ return Optional.of(buffTime); }
		protected Optional<Integer> debuffTime(){ return Optional.of(debuffTime); }
		
		public static OperatingValuesBerserk fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
