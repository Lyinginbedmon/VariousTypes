package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFastHeal.OperatingValuesFastHeal;
import com.lying.ability.AbilityRegeneration.OperatingValuesRegeneration;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityRegeneration extends Ability implements IComplexAbility<OperatingValuesRegeneration>
{
	public AbilityRegeneration(Identifier registryName, Category category)
	{
		super(registryName, category);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		OperatingValuesRegeneration values = OperatingValuesRegeneration.fromNbt(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.healAmount, VTUtils.ticksToSeconds(values.healRate), values.minimumFood));
	}
	
	public void registerEventHandlers()
	{
		TickEvent.PLAYER_POST.register(player -> 
		{
			if(!player.getWorld().isClient())
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					if(!(player.getHealth() < player.getMaxHealth() || sheet.<Float>elementValue(VTSheetElements.NONLETHAL) > 0F))
						return;
					if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
						return;
					
					OperatingValuesRegeneration values = OperatingValuesRegeneration.fromNbt(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName()).memory());
					if(player.age%values.healRate == 0 && player.getHungerManager().getFoodLevel() >= values.minimumFood)
						player.heal(values.healAmount);
				});
		});
		
		EntityEvent.LIVING_HURT.register((LivingEntity entity, DamageSource source, float amount) -> 
		{
			// TODO Prevent damage unless it has configured tag(s)
			return EventResult.pass();
		});
	}
	
	public OperatingValuesRegeneration memoryToValues(NbtCompound data) { return OperatingValuesRegeneration.fromNbt(data); }
	
	public static class OperatingValuesRegeneration extends OperatingValuesFastHeal
	{
		private static final Codec<OperatingValuesRegeneration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Rate").forGetter(OperatingValuesRegeneration::healRate), 
				Codec.INT.optionalFieldOf("Amount").forGetter(OperatingValuesRegeneration::healAmount),
				Codec.INT.optionalFieldOf("MinFood").forGetter(OperatingValuesRegeneration::minFood))
				.apply(instance, OperatingValuesRegeneration::new));
		
		/** Ticks between operations */
		private int healRate = Reference.Values.TICKS_PER_SECOND * 3;
		/** Amount healed each operation */
		private int healAmount = 1;
		/** Minimum player food amount to operate */
		private int minimumFood = 1;
		
		public OperatingValuesRegeneration(Optional<Integer> rateIn, Optional<Integer> amountIn, Optional<Integer> foodIn)
		{
			super(rateIn, amountIn, foodIn);
		}
		
		public static OperatingValuesRegeneration fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
