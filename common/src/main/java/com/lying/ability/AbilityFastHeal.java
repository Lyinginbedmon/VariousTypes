package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.function.Function;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFastHeal.ConfigFastHeal;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSoundEvents;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityFastHeal extends Ability implements IComplexAbility<ConfigFastHeal>
{
	public AbilityFastHeal(Identifier registryName, Category category)
	{
		super(registryName, category);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigFastHeal values = ConfigFastHeal.fromNbt(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.healAmount, VTUtils.ticksToTime(values.healRate), values.minimumFood));
	}
	
	public ConfigFastHeal memoryToValues(NbtCompound data) { return ConfigFastHeal.fromNbt(data); }
	
	public void registerEventHandlers()
	{
		TickEvent.PLAYER_POST.register(player -> processFastHealing(player, registryName(), this::instanceToValues));
	}
	
	public static void processFastHealing(PlayerEntity owner, Identifier registryName, Function<AbilityInstance,ConfigFastHeal> converter)
	{
		if(owner.getWorld().isClient()) return;
		
		ServerPlayerEntity player = (ServerPlayerEntity)owner;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			if(player.getHealth() >= player.getMaxHealth() && sheet.<Float>elementValue(VTSheetElements.NONLETHAL) <= 0F)
				return;
			if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName))
				return;
			
			ConfigFastHeal values = converter.apply(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName));
			if(player.age%values.healRate == 0 && player.getHungerManager().getFoodLevel() >= values.minimumFood)
			{
				player.heal(values.healAmount);
				// FIXME Add heart particle?
				VTUtils.playSoundFor(player, VTSoundEvents.FAST_HEAL.get(), SoundCategory.PLAYERS, 0.5F + player.getRandom().nextFloat() * 0.5F, 0.5F);
			}
		});
	}
	
	/** Used to convert instance memory NBT to organised operational variables */
	public static class ConfigFastHeal
	{
		protected static final Codec<ConfigFastHeal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Rate").forGetter(v -> Optional.of(v.healRate)), 
				Codec.INT.optionalFieldOf("Amount").forGetter(v -> Optional.of(v.healAmount)),
				Codec.INT.optionalFieldOf("MinFood").forGetter(v -> Optional.of(v.minimumFood)))
					.apply(instance, ConfigFastHeal::new));
		
		/** Ticks between operations */
		protected final int healRate;
		/** Amount healed each operation */
		protected final int healAmount;
		/** Minimum player food amount to operate */
		protected final int minimumFood;
		
		public ConfigFastHeal(Optional<Integer> rateIn, Optional<Integer> amountIn, Optional<Integer> foodIn)
		{
			healRate = rateIn.orElse(Reference.Values.TICKS_PER_SECOND * 3);
			healAmount = amountIn.orElse(1);
			minimumFood = foodIn.orElse(1);
		}
		
		public static ConfigFastHeal fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
