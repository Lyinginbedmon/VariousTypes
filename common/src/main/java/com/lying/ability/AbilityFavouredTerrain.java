package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityFavouredTerrain.ConfigFavouredTerrain;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class AbilityFavouredTerrain extends Ability implements IComplexAbility<ConfigFavouredTerrain>
{
	public AbilityFavouredTerrain(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigFavouredTerrain values = memoryToValues(instance.memory());
		Text effects = VTUtils.getEffectNames(values.buffs);
		if(values.isBlank())
			return Optional.of(translate("ability", registryName().getPath()+".desc", effects));
		
		boolean biomeTag = values.hasBiomeTag();
		boolean blockTag = values.hasBlockTag();
		
		Text biomeList = biomeTag ? VTUtils.tagListToString(values.biomeTags, ", ") : Text.empty();
		Text blockList = blockTag ?VTUtils.tagListToString(values.blockTags, ", ") : Text.empty();
		int threshold = (int)(values.threshold * 100);
		
		if(!biomeTag && blockTag)
			return Optional.of(translate("ability", registryName().getPath()+".desc_block", effects, threshold, blockList));
		else if(biomeTag && !blockTag)
			return Optional.of(translate("ability", registryName().getPath()+".desc_biome", effects, threshold, biomeList));
		else
			return Optional.of(translate("ability", registryName().getPath()+".desc_both", effects, threshold, blockList, biomeList));
	}
	
	public void registerEventHandlers()
	{
		TickEvent.PLAYER_PRE.register(player -> 
		{
			World world = player.getWorld();
			if(world.isClient() || player.age%Reference.Values.TICKS_PER_MINUTE > 0) return;
			
			VariousTypes.getSheet(player).ifPresent(sheet -> 
			{
				if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName())) return;
				AbilityInstance instance = sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
				ConfigFavouredTerrain config = ConfigFavouredTerrain.fromNbt(instance.memory());
				
				boolean criteriaMet;
				if(!config.isBlank())
				{
					// Shortcut to save processing if we have a biome tag requirement
					if(config.hasBiomeTag() && !config.matchesBiome(world.getBiome(player.getBlockPos())))
						criteriaMet = false;
					else
					{
						int sides = 0;
						float total = 0F;
						for(Direction dir : Direction.values())
						{
							float result = traceBlocks(player.getEyePos(), dir, world, player, config);
							if(result < 0F)
								continue;
							sides++;
							total += result;
						}
						
						criteriaMet = (total / sides) > config.threshold;
					}
				}
				else
					criteriaMet = true;
				
				if(criteriaMet)
					config.buffs.forEach(buff -> 
						player.addStatusEffect(new StatusEffectInstance(buff.getEffectType(), Reference.Values.TICKS_PER_SECOND * 90, buff.getAmplifier(), true, false)));
			});
		});
	}
	
	private static float traceBlocks(Vec3d eyePos, Direction dir, World world, Entity entity, ConfigFavouredTerrain config)
	{
		Vec3i directionMask = dir.getVector();
		Vec3i dirVec = directionMask.multiply(6);
		Vec3d endPos = eyePos.add(dirVec.getX(), dirVec.getY(), dirVec.getZ());
		HitResult result = world.raycast(new RaycastContext(eyePos, endPos, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity));
		if(result.getType() != HitResult.Type.BLOCK)
			return -1F;
		
		BlockPos origin = ((BlockHitResult)result).getBlockPos();
		
		Vec3i longMask, latMask;
		switch(dir.getAxis())
		{
			case X:
				longMask = new Vec3i(0, 0, 1);
				latMask = new Vec3i(0, 1, 0);
				break;
			default:
			case Y:
				longMask = new Vec3i(1, 0, 0);
				latMask = new Vec3i(0, 0, 1);
				break;
			case Z:
				longMask = new Vec3i(1, 0, 0);
				latMask = new Vec3i(0, 1, 0);
				break;
		}
		
		float pass = 0, fail = 0;
		for(int x=-2; x<=2; x++)
			for(int z=-2; z<=2; z++)
				for(int i=1; i>=-1; i--)
				{
					BlockPos floor = origin.add(longMask.multiply(x)).add(latMask.multiply(z)).add(directionMask.multiply(i));
					switch(validateBlock(floor, dir.getOpposite(), world, config))
					{
						case FAIL:
							fail++;
							break;
						case IGNORE:
							break;
						case PASS:
							pass++;
							break;
					}
				}
		return pass / (pass + fail);
	}
	
	private static CheckResult validateBlock(BlockPos pos, Direction offset, World world, ConfigFavouredTerrain config)
	{
		if(world.isAir(pos) || world.getBlockState(pos.offset(offset)).getCollisionShape(world, pos.offset(offset)) == VoxelShapes.fullCube())
			return CheckResult.IGNORE;
		
		return config.matchesBlock(world.getBlockState(pos)) && config.matchesBiome(world.getBiome(pos)) ? CheckResult.PASS : CheckResult.FAIL;
	}
	
	private static enum CheckResult
	{
		FAIL,
		PASS,
		IGNORE;
	}
	
	public ConfigFavouredTerrain memoryToValues(NbtCompound data) { return ConfigFavouredTerrain.fromNbt(data); }
	
	public static class ConfigFavouredTerrain
	{
		protected static final Codec<ConfigFavouredTerrain> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).listOf().optionalFieldOf("Blocks").forGetter(v -> Optional.of(v.blockTags)),
				TagKey.codec(RegistryKeys.BIOME).listOf().optionalFieldOf("Biomes").forGetter(v -> Optional.of(v.biomeTags)),
				Codec.FLOAT.optionalFieldOf("Threshold").forGetter(v -> Optional.of(v.threshold)),
				StatusEffectInstance.CODEC.listOf().optionalFieldOf("Buffs").forGetter(v -> Optional.of(v.buffs)))
					.apply(instance, ConfigFavouredTerrain::new));
		
		protected List<TagKey<Block>> blockTags;
		protected List<TagKey<Biome>> biomeTags;
		protected float threshold;
		
		protected List<StatusEffectInstance> buffs;
		
		public ConfigFavouredTerrain(Optional<List<TagKey<Block>>> blockIn, Optional<List<TagKey<Biome>>> biomeIn, Optional<Float> thresholdIn, Optional<List<StatusEffectInstance>> buffsIn)
		{
			blockTags = blockIn.orElse(List.of(BlockTags.BASE_STONE_OVERWORLD));
			biomeTags = biomeIn.orElse(Lists.newArrayList());
			threshold = thresholdIn.orElse(0.6F);
			
			buffs = buffsIn.orElse(List.of(new StatusEffectInstance(StatusEffects.SPEED), new StatusEffectInstance(StatusEffects.HASTE)));
		}
		
		public boolean isBlank()
		{
			return !hasBiomeTag() && !hasBlockTag();
		}
		
		public boolean hasBiomeTag() { return !biomeTags.isEmpty(); }
		
		public boolean matchesBiome(RegistryEntry<Biome> biomeIn)
		{
			return !hasBiomeTag() || biomeTags.stream().anyMatch(tag -> biomeIn.isIn(tag));
		}
		
		public boolean hasBlockTag() { return !blockTags.isEmpty(); }
		
		public boolean matchesBlock(BlockState stateIn)
		{
			return !hasBlockTag() || blockTags.stream().anyMatch(tag -> stateIn.isIn(tag));
		}
		
		public static ConfigFavouredTerrain fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}