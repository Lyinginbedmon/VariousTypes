package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityOresight.ConfigOresight;
import com.lying.data.VTTags;
import com.lying.network.HighlightBlockPacket;
import com.lying.reference.Reference;
import com.lying.utility.BlockHighlight;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbilityOresight extends ActivatedAbility implements IComplexAbility<ConfigOresight>
{
	public AbilityOresight(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigOresight values = instanceToValues(instance);
		return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.tagListToString(values.blockTags, ", "), values.radius, VTUtils.ticksToTime(values.duration)));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 30; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER || owner.getWorld().isClient()) return;
		
		ServerPlayerEntity player = (ServerPlayerEntity)owner;
		ConfigOresight config = instanceToValues(instance);
		BlockPos pos = BlockPos.ofFloored(player.getX(), player.getEyeY(), player.getZ());
		World world = player.getWorld();
		int radius = config.radius;
		int minY = Math.max(world.getBottomY(), pos.getY() - radius);
		int maxY = Math.min(256, pos.getY() + radius);
		
		List<BlockPos> ores = Lists.newArrayList();
		for(int x=-radius; x<radius; x++)
			for(int z=-radius; z<radius; z++)
				for(int y=minY; y<=maxY; y++)
				{
					BlockPos position = pos.add(x, 0, z).withY(y);
					if(world.isAir(position))
						continue;
					
					BlockState state = world.getBlockState(position);
					if(config.blockTags.stream().anyMatch(tag -> state.isIn(tag)))
						ores.add(position);
				}
		
		long time = world.getTime();
		int duration = config.duration;
		HighlightBlockPacket.send(player, ores.stream().map(position -> new BlockHighlight((BlockPos)position, time, duration, 0xFFFFFF)).collect(Collectors.toList()));
		player.sendMessage(Text.translatable("gui.vartypes.oresight_tally", ores.size()), false);
	}
	
	public ConfigOresight memoryToValues(NbtCompound data) { return ConfigOresight.fromNbt(data); }
	
	public static class ConfigOresight
	{
		protected static final Codec<ConfigOresight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).listOf().optionalFieldOf("Blocks").forGetter(v -> Optional.of(v.blockTags)),
				Codec.INT.optionalFieldOf("Radius").forGetter(v -> Optional.of(v.radius)),
				Codec.INT.optionalFieldOf("Duration").forGetter(v -> Optional.of(v.duration)))
					.apply(instance, ConfigOresight::new));
		
		protected List<TagKey<Block>> blockTags;
		protected int radius;
		protected int duration;
		
		public ConfigOresight(Optional<List<TagKey<Block>>> blockIn, Optional<Integer> radiusIn, Optional<Integer> durationIn)
		{
			blockTags = blockIn.orElse(List.of(VTTags.ORES));
			radius = radiusIn.orElse(4);
			duration = durationIn.orElse(Reference.Values.TICKS_PER_SECOND * 15);
		}
		
		public static ConfigOresight fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
