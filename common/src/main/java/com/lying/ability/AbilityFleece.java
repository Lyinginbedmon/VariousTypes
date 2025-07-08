package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFleece.ConfigFleece;
import com.lying.utility.LootBag;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityFleece extends ActivatedAbility implements IComplexAbility<ConfigFleece>
{
	public AbilityFleece(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigFleece values = ConfigFleece.fromNbt(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.foodCost, values.product.description()));
	}
	
	public boolean remappable() { return true; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return owner.getType() == EntityType.PLAYER && ((PlayerEntity)owner).getHungerManager().getFoodLevel() >= instanceToValues(instance).foodCost; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER) return;
		
		PlayerEntity player = (PlayerEntity)owner;
		ConfigFleece config = instanceToValues(instance);
		player.getHungerManager().add(-config.foodCost, 0);
		config.product.giveTo((ServerPlayerEntity)player);
	}
	
	public ConfigFleece memoryToValues(NbtCompound data) { return ConfigFleece.fromNbt(data); }
	
	public static class ConfigFleece
	{
		protected static final Codec<ConfigFleece> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Cost").forGetter(v -> Optional.of(v.foodCost)), 
				LootBag.CODEC.optionalFieldOf("Product").forGetter(v -> Optional.of(v.product)))
					.apply(instance, ConfigFleece::new));
		
		protected int foodCost;
		protected LootBag product;
		
		public ConfigFleece(Optional<Integer> costIn, Optional<LootBag> productIn)
		{
			foodCost = costIn.orElse(6);
			product = productIn.orElse(LootBag.ofItems(Items.WHITE_WOOL));
		}
		
		public static ConfigFleece fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
