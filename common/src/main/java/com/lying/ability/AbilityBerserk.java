package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.UUID;

import com.lying.VariousTypes;
import com.lying.ability.AbilityBerserk.ConfigBerserk;
import com.lying.component.CharacterSheet;
import com.lying.init.VTParticleTypes;
import com.lying.init.VTSoundEvents;
import com.lying.init.VTStatusEffects;
import com.lying.network.ParentedParticlePacket;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
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
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigBerserk values = memoryToValues(instance.memory());
		StatusEffectInstance buff = values.getBuff(manager);
		StatusEffectInstance debuff = values.getDebuff(manager);
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				Text.translatable(buff.getTranslationKey()),
				VTUtils.ticksToTime(values.buffTime), 
				Text.translatable(debuff.getTranslationKey()),
				VTUtils.ticksToTime(values.debuffTime)));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_MINUTE * 5; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		startTicking(instance, owner);
		owner.addStatusEffect(memoryToValues(instance.memory()).getBuff(owner.getRegistryManager()));
	}
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return !shouldTick(owner, instance); }
	
	public boolean shouldTick(final LivingEntity owner, final AbilityInstance instance) { return getCurrentTick(instance, owner.getWorld().getTime()) >= 0; }
	
	public void onTick(AbilityInstance instance, final CharacterSheet sheet, final LivingEntity owner)
	{
		ConfigBerserk values = memoryToValues(instance.memory());
		ServerWorld world = (ServerWorld)owner.getWorld();
		int tick;
		switch(tick = getCurrentTick(instance, world.getTime()))
		{
			case 0:
				ITickingAbility.tryPutOnCooldown(instance, owner);
				owner.addStatusEffect(values.getDebuff(owner.getRegistryManager()));
				break;
			case 1:
			default:
				if(tick%5 > 0 || owner.getType() != EntityType.PLAYER) return;
				Random rand = owner.getRandom();
				UUID id = owner.getUuid();
				world.getPlayers().stream().forEach(p -> ParentedParticlePacket.send(
						p, id, 
						VTParticleTypes.RAGE.get(), 
						new Vec3d(
								owner.getParticleX(0.5D) - owner.getX(), 
								owner.getHeight() * (0.5D + rand.nextDouble() * 0.5D), 
								owner.getParticleZ(0.5D) - owner.getZ()), 
						Vec3d.ZERO));
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
				Registries.STATUS_EFFECT.getEntryCodec().optionalFieldOf("Buff").forGetter(c -> c.buff),
				Codec.INT.optionalFieldOf("BuffTime").forGetter(c -> Optional.of(c.buffTime)),
				Registries.STATUS_EFFECT.getEntryCodec().optionalFieldOf("Debuff").forGetter(c -> c.debuff), 
				Codec.INT.optionalFieldOf("DebuffTime").forGetter(c -> Optional.of(c.debuffTime)))
					.apply(instance, ConfigBerserk::new));
		
		protected int buffTime = 12 * Reference.Values.TICKS_PER_SECOND;
		protected int debuffTime = 15 * Reference.Values.TICKS_PER_SECOND;
		
		protected Optional<RegistryEntry<StatusEffect>> buff = Optional.empty(), debuff = Optional.empty();
		
		public ConfigBerserk(Optional<RegistryEntry<StatusEffect>> buffIn, Optional<Integer> buffTimeIn, Optional<RegistryEntry<StatusEffect>> debuffIn, Optional<Integer> debuffTimeIn)
		{
			buffTimeIn.ifPresent(val -> buffTime = val);
			debuffTimeIn.ifPresent(val -> debuffTime = val);
		}
		
		protected Optional<Integer> buffTime(){ return Optional.of(buffTime); }
		protected Optional<Integer> debuffTime(){ return Optional.of(debuffTime); }
		
		public StatusEffectInstance getBuff(DynamicRegistryManager manager)
		{
			return 
				buff.isPresent() ? 
					new StatusEffectInstance(buff.get(), buffTime) : 
					new StatusEffectInstance(VTStatusEffects.getEntry(manager, VTStatusEffects.RAGE), buffTime);
		}
		
		public StatusEffectInstance getDebuff(DynamicRegistryManager manager)
		{
			return 
				debuff.isPresent() ? 
					new StatusEffectInstance(debuff.get(), debuffTime) : 
					new StatusEffectInstance(VTStatusEffects.getEntry(manager, VTStatusEffects.LETHARGY), debuffTime);
		}
		
		public static ConfigBerserk fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
