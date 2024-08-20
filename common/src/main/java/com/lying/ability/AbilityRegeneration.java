package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFastHeal.OperatingValuesFastHeal;
import com.lying.ability.AbilityRegeneration.OperatingValuesRegeneration;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementNonLethal;
import com.lying.init.VTSheetElements;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
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
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(entity);
			if(sheetOpt.isEmpty()) return EventResult.pass();
			
			CharacterSheet sheet = sheetOpt.get();
			if(!sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return EventResult.pass();
			
			OperatingValuesRegeneration values = OperatingValuesRegeneration.fromNbt(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName()).memory());
			if(!values.bypassedBy(source))
			{
				sheet.<ElementNonLethal>element(VTSheetElements.NONLETHAL).accrue(amount, entity.getMaxHealth(), entity.getType() == EntityType.PLAYER ? (PlayerEntity)entity : null);
				return EventResult.interruptFalse();
			}
			return EventResult.pass();
		});
	}
	
	public OperatingValuesRegeneration memoryToValues(NbtCompound data) { return OperatingValuesRegeneration.fromNbt(data); }
	
	public static class OperatingValuesRegeneration extends OperatingValuesFastHeal
	{
		private static final Codec<OperatingValuesRegeneration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Rate").forGetter(OperatingValuesRegeneration::healRate), 
				Codec.INT.optionalFieldOf("Amount").forGetter(OperatingValuesRegeneration::healAmount),
				Codec.INT.optionalFieldOf("MinFood").forGetter(OperatingValuesRegeneration::minFood),
				TagKey.codec(RegistryKeys.DAMAGE_TYPE).listOf().optionalFieldOf("DamageTags").forGetter(v -> Optional.of(v.tags)))
				.apply(instance, OperatingValuesRegeneration::new));
		
		/** Damage types that bypass the conversion to nonlethal damage */
		private final List<TagKey<DamageType>> tags;
		
		public OperatingValuesRegeneration(Optional<Integer> rateIn, Optional<Integer> amountIn, Optional<Integer> foodIn, Optional<List<TagKey<DamageType>>> tagsIn)
		{
			super(rateIn, amountIn, foodIn);
			tags = tagsIn.orElse(List.of(DamageTypeTags.IS_FIRE));
		}
		
		public static OperatingValuesRegeneration fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
		
		public boolean bypassedBy(DamageSource source) { return tags.stream().anyMatch(tag -> source.isIn(tag)); }
	}
}
