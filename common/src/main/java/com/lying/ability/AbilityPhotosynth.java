package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.function.BiPredicate;

import com.lying.VariousTypes;
import com.lying.ability.AbilityPhotosynth.ConfigPhotosynth;
import com.lying.init.VTParticleTypes;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSoundEvents;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class AbilityPhotosynth extends Ability implements IComplexAbility<ConfigPhotosynth>
{
	private static final BiPredicate<World, BlockPos> GOOD_SKY = (world, position) -> 
		world.isDay() && 
		!world.isRaining() && 
		world.isSkyVisible(position);
	private static final BiPredicate<PlayerEntity, BlockPos> GOOD_PLAYER = (player, position) -> 
		player.isOnGround() && 
		(player.getHungerManager().getFoodLevel() < 20 || player.getHungerManager().getSaturationLevel() < 20) && 
		(position == null || position.withY(player.getBlockY()).getSquaredDistance(player.getBlockPos()) == 0D);
	
	public AbilityPhotosynth(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigPhotosynth values = instanceToValues(instance);
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.food, values.saturation, VTUtils.ticksToTime(values.rate)));
	}
	
	public void registerEventHandlers()
	{
		// Restart tracking whenever we take damage
		EntityEvent.LIVING_HURT.register((living,source,amount) -> 
		{
			VariousTypes.getSheet(living).ifPresent(sheet -> 
			{
				AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
				if(!abilities.hasAbility(registryName())) return;
				
				AbilityInstance inst = abilities.get(registryName());
				ConfigPhotosynth config = ((AbilityPhotosynth)inst.ability()).instanceToValues(inst);
				if(config.isTracking())
				{
					config.startTime = Optional.of(living.getWorld().getTime());
					inst.setMemory(config.toNbt());
				}
			});
			return EventResult.pass();
		});
		TickEvent.PLAYER_POST.register(player -> VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			World world = player.getWorld();
			if(world.isClient()) return;
			
			AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
			if(!abilities.hasAbility(registryName())) return;
			
			AbilityInstance inst = abilities.get(registryName());
			ConfigPhotosynth config = ((AbilityPhotosynth)inst.ability()).instanceToValues(inst);
			BlockPos playerPos = player.getBlockPos();
			if(config.isTracking())
			{
				BlockPos pos = config.position.get();
				// Stop tracking if it starts raining, we can no longer see the sky, or we have moved horizontally
				if(!GOOD_PLAYER.test(player, pos) || !GOOD_SKY.test(world, playerPos))
				{
					config.stopTracking();
					inst.setMemory(config.toNbt());
					return;
				}
				
				int time = (int)(world.getTime() - config.startTime.get());
				HungerManager manager = player.getHungerManager();
				if(time > 0 && time%config.rate == 0 && (manager.getFoodLevel() < 20 || manager.getSaturationLevel() < 20))
				{
					boolean emit = false;
					if(manager.getFoodLevel() < 20 && config.food > 0)
					{
						manager.add(config.food, 0F);
						emit = true;
					}
					else if(manager.getSaturationLevel() < 20 && config.saturation > 0)
					{
						manager.add(0, (float)config.saturation);
						emit = true;
					}
					
					if(emit)
					{
						Random rand = player.getRandom();
						VTUtils.playSound(player, VTSoundEvents.PHOTOSYNTH_HEAL.get(), SoundCategory.PLAYERS, 0.5F + rand.nextFloat() * 0.5F, 0.3F);
						for(int i=5; i>0; --i)
							VTUtils.spawnParticles((ServerWorld)world, VTParticleTypes.LEAF.get(), new Vec3d(player.getX(), player.getRandomBodyY() * 0.75D, player.getZ()), Vec3d.ZERO);
					}
				}
			}
			else if(player.age%Reference.Values.TICKS_PER_SECOND == 0)
				if(GOOD_PLAYER.test(player, null) && GOOD_SKY.test(world, playerPos))
				{
					config.startTracking(playerPos, world.getTime());
					inst.setMemory(config.toNbt());
					return;
				}
		}));
	}
	
	public ConfigPhotosynth memoryToValues(NbtCompound data) { return ConfigPhotosynth.fromNbt(data); }
	
	public static class ConfigPhotosynth
	{
		protected static final Codec<ConfigPhotosynth> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Food").forGetter(v -> v.food == 0 ? Optional.empty() : Optional.of(v.food)),
				Codec.INT.optionalFieldOf("Saturation").forGetter(v -> v.saturation == 0 ? Optional.empty() : Optional.of(v.saturation)),
				Codec.INT.optionalFieldOf("Rate").forGetter(v -> v.rate == 0 ? Optional.empty() : Optional.of(v.rate)),
				Codec.LONG.optionalFieldOf("Start").forGetter(v -> v.startTime),
				BlockPos.CODEC.optionalFieldOf("Pos").forGetter(v -> v.position))
					.apply(instance, ConfigPhotosynth::new));
		
		protected int food, saturation, rate;
		protected Optional<Long> startTime;
		protected Optional<BlockPos> position;
		
		public ConfigPhotosynth(Optional<Integer> foodIn, Optional<Integer> saturationIn, Optional<Integer> rateIn, Optional<Long> startIn, Optional<BlockPos> posIn)
		{
			food = foodIn.orElse(2);
			saturation = saturationIn.orElse(0);
			rate = rateIn.orElse(Reference.Values.TICKS_PER_SECOND * 5);
			
			startTime = startIn;
			position = posIn;
		}
		
		public boolean isTracking() { return startTime.isPresent() && position.isPresent(); }
		
		public ConfigPhotosynth startTracking(BlockPos pos, Long time)
		{
			if(food == 0 && saturation == 0)
				return this;
			
			startTime = Optional.of(time);
			position = Optional.of(pos);
			return this;
		}
		
		public ConfigPhotosynth stopTracking()
		{
			startTime = Optional.empty();
			position = Optional.empty();
			return this;
		}
		
		public NbtCompound toNbt()
		{
			return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static ConfigPhotosynth fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
