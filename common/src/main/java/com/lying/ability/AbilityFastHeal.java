package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.events.common.TickEvent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityFastHeal extends Ability implements IComplexAbility<com.lying.ability.AbilityFastHeal.OperatingValuesFastHeal>
{
	public AbilityFastHeal(Identifier registryName, Category category)
	{
		super(registryName, category);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		OperatingValuesFastHeal values = OperatingValuesFastHeal.fromNbt(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.healAmount, VTUtils.ticksToSeconds(values.healRate), values.minimumFood));
	}
	
	public OperatingValuesFastHeal memoryToValues(NbtCompound data) { return OperatingValuesFastHeal.fromNbt(data); }
	
	public void registerEventHandlers()
	{
		TickEvent.PLAYER_POST.register(player -> 
		{
			if(!player.getWorld().isClient())
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					if(!(player.getHealth() < player.getMaxHealth() || sheet.<Float>elementValue(VTSheetElements.NONLETHAL) > 0F))
						return;
					if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).hasAbility(registryName()))
						return;
					
					OperatingValuesFastHeal values = OperatingValuesFastHeal.fromNbt(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).get(registryName()).memory());
					if(player.age%values.healRate == 0 && player.getHungerManager().getFoodLevel() >= values.minimumFood)
						player.heal(values.healAmount);
				});
		});
	}
	
	/** Used to convert instance memory NBT to organised operational variables */
	public static class OperatingValuesFastHeal
	{
		protected static final Codec<OperatingValuesFastHeal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Rate").forGetter(OperatingValuesFastHeal::healRate), 
				Codec.INT.optionalFieldOf("Amount").forGetter(OperatingValuesFastHeal::healAmount),
				Codec.INT.optionalFieldOf("MinFood").forGetter(OperatingValuesFastHeal::minFood))
					.apply(instance, OperatingValuesFastHeal::new));
		
		/** Ticks between operations */
		protected int healRate = Reference.Values.TICKS_PER_SECOND * 3;
		/** Amount healed each operation */
		protected int healAmount = 1;
		/** Minimum player food amount to operate */
		protected int minimumFood = 1;
		
		public OperatingValuesFastHeal(Optional<Integer> rateIn, Optional<Integer> amountIn, Optional<Integer> foodIn)
		{
			rateIn.ifPresent(val -> healRate = val);
			amountIn.ifPresent(val -> healAmount = val);
			foodIn.ifPresent(val -> minimumFood = val);
		}
		
		protected Optional<Integer> healRate(){ return Optional.of(healRate); }
		protected Optional<Integer> healAmount(){ return Optional.of(healAmount); }
		protected Optional<Integer> minFood(){ return Optional.of(minimumFood); }
		
		public static OperatingValuesFastHeal fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
