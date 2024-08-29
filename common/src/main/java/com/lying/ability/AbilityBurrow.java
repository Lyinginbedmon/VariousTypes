package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.element.ElementSpecialPose;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.utility.VTUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
	
	@SafeVarargs
	public static AbilityInstance of(TagKey<Block>... tags)
	{
		NbtList list = new NbtList();
		for(TagKey<Block> tag : tags)
			list.add(NbtString.of(tag.id().toString()));
		
		return VTAbilities.BURROW.get().instance(AbilitySource.MISC, nbt -> nbt.put("Blocks", list));
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		MutableText names = VTUtils.tagListToString(getBurrowables(instance.memory()), ", ");
		return Optional.of(translate("ability",registryName().getPath()+".desc", names));
	}
	
	protected void onActivation(LivingEntity owner, AbilityInstance instance)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			sheet.<ElementSpecialPose>element(VTSheetElements.SPECIAL_POSE).set(EntityPose.SWIMMING, (PlayerEntity)owner);
			owner.setPose(EntityPose.SWIMMING);
		});
	}
	
	protected void onDeactivation(LivingEntity owner, AbilityInstance instance)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementSpecialPose specPose = sheet.<ElementSpecialPose>element(VTSheetElements.SPECIAL_POSE);
			if(specPose.value().isPresent() && specPose.value().get() == EntityPose.SWIMMING)
			{
				specPose.clear((PlayerEntity)owner);
				owner.setPose(EntityPose.STANDING);
			}
		});
	}
	
	public boolean isApplicableTo(BlockState state, LivingEntity living, AbilityInstance instance)
	{
		return isActive(instance) && AbilityBurrow.getBurrowables(instance.memory()).stream().anyMatch(tag -> state.isIn(tag));
	}
	
	public Optional<VoxelShape> getCollisionFor(BlockState state, BlockPos pos, LivingEntity living, AbilityInstance instance)
	{
		return isApplicableTo(state, living, instance) && pos.getY() > living.getWorld().getBottomY() && (living.isSneaking() || pos.getY() >= living.getY()) ? Optional.of(VoxelShapes.empty()) : Optional.empty();
	}
	
	public static List<TagKey<Block>> getBurrowables(NbtCompound memory)
	{
		return AbilityHelper.getTags(memory, "Blocks", RegistryKeys.BLOCK, () -> List.of(BlockTags.DIRT));
	}
}
