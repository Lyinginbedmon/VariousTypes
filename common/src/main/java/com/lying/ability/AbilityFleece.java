package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFleece.ConfigFleece;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityFleece extends ActivatedAbility implements IComplexAbility<ConfigFleece>
{
	public AbilityFleece(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigFleece values = ConfigFleece.fromNbt(instance.memory());
		Text productName = values.product.getName();
		if(values.product.getCount() > 1)
			productName = productName.copy().append(Text.literal(" x")).append(Text.literal(String.valueOf(values.product.getCount())));
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.foodCost, productName));
	}
	
	public boolean remappable() { return true; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return owner.getType() == EntityType.PLAYER && ((PlayerEntity)owner).getHungerManager().getFoodLevel() >= instanceToValues(instance).foodCost; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER) return;
		
		PlayerEntity player = (PlayerEntity)owner;
		ConfigFleece config = instanceToValues(instance);
		player.getHungerManager().add(-config.foodCost, 0);
		player.giveItemStack(config.product.copy());
	}
	
	public ConfigFleece memoryToValues(NbtCompound data) { return ConfigFleece.fromNbt(data); }
	
	public static class ConfigFleece
	{
		protected static final Codec<ConfigFleece> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Cost").forGetter(v -> Optional.of(v.foodCost)), 
				ItemStack.CODEC.optionalFieldOf("Product").forGetter(v -> Optional.of(v.product)))
					.apply(instance, ConfigFleece::new));
		
		protected int foodCost;
		protected ItemStack product;
		
		public ConfigFleece(Optional<Integer> rateIn, Optional<ItemStack> amountIn)
		{
			rateIn.ifPresentOrElse(val -> foodCost = val, () -> foodCost = 6);
			amountIn.ifPresentOrElse(val -> product = val.copy(), () -> product = new ItemStack(Blocks.WHITE_WOOL));
		}
		
		public static ConfigFleece fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
