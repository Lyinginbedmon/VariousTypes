package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityThunderstep.ConfigThunderstep;
import com.lying.component.CharacterSheet;
import com.lying.event.LivingEvents;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbilityThunderstep extends ActivatedAbility implements IComplexAbility<ConfigThunderstep>
{
	public AbilityThunderstep(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigThunderstep values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc_"+(values.needsThundering ? "thunder" : "no_thunder"), 
				(int)values.minRange, 
				(int)values.maxRange));
	}

	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 30; }

	public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
	{
		if(!owner.getWorld().isSkyVisible(owner.getBlockPos())) return false;
		
		ConfigThunderstep config = memoryToValues(instance.memory());
		if(!owner.getWorld().isThundering() && config.needsThundering) return false;
		
		HitResult trace = owner.raycast(config.maxRange, 1F, true);
		if(trace.getType() == HitResult.Type.MISS || trace.squaredDistanceTo(owner) < config.minSquared())
			return false;
		
		Vec3d destination = trace.getPos();
		return owner.getWorld().isSkyVisible(BlockPos.ofFloored(destination.x, destination.y, destination.z));
	}

	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(!canTrigger(owner, instance))
			return;
		
		ConfigThunderstep config = memoryToValues(instance.memory());
		HitResult trace = owner.raycast(config.maxRange, 1F, true);
		World world = owner.getWorld();
		Vec3d destination = trace.getPos();
		if(destination == null || !world.isSkyVisible(BlockPos.ofFloored(destination.x, destination.y, destination.z)))
			return;
		
		owner.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 5 * Reference.Values.TICKS_PER_SECOND, 0, true, false));
		world.spawnEntity(makeAt(owner.getPos(), world));
		owner.teleport(destination.getX(), destination.getY(), destination.getZ());
		world.spawnEntity(makeAt(destination, world));
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.LIVING_HURT_EVENT.register((LivingEntity entity, DamageSource source, float amount) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(entity);
			if(sheetOpt.isEmpty()) return EventResult.pass();
			
			CharacterSheet sheet = sheetOpt.get();
			if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return EventResult.pass();
			
			return source == entity.getWorld().getDamageSources().lightningBolt() ? EventResult.interruptFalse() : EventResult.pass();
		});
	}

	private static LightningEntity makeAt(Vec3d pos, World world)
	{
        LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
        lightningEntity.refreshPositionAfterTeleport(pos.getX(), pos.getY(), pos.getZ());
        return lightningEntity;
	}
	
	public ConfigThunderstep memoryToValues(NbtCompound data) { return ConfigThunderstep.fromNbt(data); }
	
	public static class ConfigThunderstep
	{
		protected static final Codec<ConfigThunderstep> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.DOUBLE.optionalFieldOf("RangeMax").forGetter(ConfigThunderstep::rangeMax), 
				Codec.DOUBLE.optionalFieldOf("RangeMin").forGetter(ConfigThunderstep::rangeMin),
				Codec.BOOL.optionalFieldOf("NeedsThundering").forGetter(ConfigThunderstep::needsThunder))
					.apply(instance, ConfigThunderstep::new));
		
		protected double maxRange = 128;
		protected double minRange = 32;
		protected boolean needsThundering = true;
		
		public ConfigThunderstep(Optional<Double> maxIn, Optional<Double> minIn, Optional<Boolean> needsThunderIn)
		{
			needsThunderIn.ifPresent(val -> needsThundering = val);
			maxIn.ifPresent(val -> maxRange = val);
			minIn.ifPresent(val -> minRange = val);
			if(maxRange < minRange)
			{
				double d = maxRange;
				maxRange = minRange;
				minRange = d;
			}
		}
		
		protected Optional<Double> rangeMax(){ return Optional.of(maxRange); }
		protected Optional<Double> rangeMin(){ return Optional.of(minRange); }
		protected Optional<Boolean> needsThunder() { return Optional.of(needsThundering); }
		
		public double minSquared() { return minRange * minRange; }
		
		public static ConfigThunderstep fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}