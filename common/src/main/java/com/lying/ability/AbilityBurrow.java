package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.utility.VTUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class AbilityBurrow extends ToggledAbility implements IPhasingAbility
{
	public AbilityBurrow(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		MutableText names = VTUtils.listToString(getBurrowables(instance.memory()), tag -> Text.literal(tag.id().toString()), ", ");
		return Optional.of(translate("ability",registryName().getPath()+".desc", names));
	}
	
	protected void onActivation(LivingEntity owner, AbilityInstance instance)
	{
		// FIXME Establish means to alter default pose away from EntityPose.STANDING
	}
	
	protected void onDeactivation(LivingEntity owner, AbilityInstance instance)
	{
		
	}
	
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance)
	{
		return ((ToggledAbility)instance.ability()).isActive(instance) && getBurrowables(instance.memory()).stream().anyMatch(tag -> state.isIn(tag));
	}
	
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance)
	{
		return isApplicableTo(state, living, instance) && pos.getY() > living.getWorld().getBottomY() && (living.isSneaking() || pos.getY() >= living.getY()) ? Optional.of(VoxelShapes.empty()) : Optional.empty();
	}
	
	public static List<TagKey<Block>> getBurrowables(NbtCompound memory)
	{
		List<TagKey<Block>> tags = Lists.newArrayList();
		
		if(memory.contains("Blocks", NbtElement.LIST_TYPE))
			for(NbtElement element : memory.getList("Blocks", NbtElement.STRING_TYPE))
				tags.add(TagKey.of(RegistryKeys.BLOCK, new Identifier(element.asString())));
		else
			tags.add(BlockTags.DIRT);
		
		return tags;
	}
}
