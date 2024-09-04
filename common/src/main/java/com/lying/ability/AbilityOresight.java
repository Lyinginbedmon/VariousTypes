package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityOresight.ConfigOresight;
import com.lying.data.VTTags;
import com.lying.network.HighlightBlockPacket;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
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
		return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.tagListToString(values.blockTags, ", "), values.radius));
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 30; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER || owner.getWorld().isClient()) return;
		
		ConfigOresight config = instanceToValues(instance);
		BlockPos pos = owner.getBlockPos().down();
		World world = owner.getWorld();
		int radius = 6;
		for(int x=-radius; x<radius; x++)
			for(int z=-radius; z<radius; z++)
				for(int y=-radius; y<radius; y++)
				{
					BlockPos position = pos.add(x, y, z);
					if(position.getY() < world.getBottomY() || position.getY() > 256 || world.isAir(position))
						continue;
					else if(config.blockTags.stream().anyMatch(tag -> world.getBlockState(position).isIn(tag)))
						HighlightBlockPacket.send((ServerPlayerEntity)owner, position);
				}
	}
	
	public ConfigOresight memoryToValues(NbtCompound data) { return ConfigOresight.fromNbt(data); }
	
	public static class ConfigOresight
	{
		protected static final Codec<ConfigOresight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).listOf().optionalFieldOf("Blocks").forGetter(v -> Optional.of(v.blockTags)),
				Codec.INT.optionalFieldOf("Radius").forGetter(v -> Optional.of(v.radius)))
					.apply(instance, ConfigOresight::new));
		
		protected List<TagKey<Block>> blockTags;
		protected int radius;
		
		public ConfigOresight(Optional<List<TagKey<Block>>> blockIn, Optional<Integer> radiusIn)
		{
			blockTags = blockIn.orElse(List.of(VTTags.ORES));
			radius = radiusIn.orElse(4);
		}
		
		public static ConfigOresight fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
