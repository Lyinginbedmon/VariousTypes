package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityFaeskin.ConfigFaeskin;
import com.lying.data.VTTags;
import com.lying.event.LivingEvents;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityFaeskin extends Ability implements IComplexAbility<ConfigFaeskin>
{
	public AbilityFaeskin(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigFaeskin values = instanceToValues(instance);
		boolean blocks = !values.badBlocks.isEmpty();
		boolean items = !values.badItems.isEmpty();
		
		Text blockList = blocks ? VTUtils.tagListToString(values.badBlocks, ", ") : Text.empty();
		Text itemList = items ? VTUtils.tagListToString(values.badItems, ", ") : Text.empty();
		if(!blocks && items)
			return Optional.of(translate("ability", registryName().getPath()+".desc_items", itemList));
		else if(blocks && !items)
			return Optional.of(translate("ability", registryName().getPath()+".desc_blocks", blockList));
		else if(blocks && items)
			return Optional.of(translate("ability", registryName().getPath()+".desc_both", blockList, itemList));
		else
			return Optional.of(translate("ability", registryName().getPath()+".desc"));
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.ON_STEP_ON_BLOCK_EVENT.register((living, state, pos, world) -> 
		{
			if(living.getWorld().isClient())
				return;
			
			VariousTypes.getSheet(living).ifPresent(sheet -> 
			{
				AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
				if(!abilities.hasAbility(registryName()))
					return;
				
				ConfigFaeskin config = instanceToValues(abilities.get(registryName()));
				if(config.badBlocks.stream().anyMatch(tag -> state.isIn(tag)))
					config.applyPenalties(living);
			});
		});
		
		LivingEvents.LIVING_MOVE_EVENT.register((living, type, move, sheetOpt) -> 
		{
			if(living.getWorld().isClient())
				return;
			
			sheetOpt.ifPresent(sheet -> 
			{
				AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
				if(!abilities.hasAbility(registryName()))
					return;
				
				ConfigFaeskin config = instanceToValues(abilities.get(registryName()));
				for(ItemStack stack : living.getEquippedItems())
					if(!stack.isEmpty() && config.badItems.stream().anyMatch(tag -> stack.isIn(tag)))
					{
						config.applyPenalties(living);
						break;
					}
			});
		});
	}
	
	public ConfigFaeskin memoryToValues(NbtCompound data) { return ConfigFaeskin.fromNbt(data); }
	
	public static class ConfigFaeskin
	{
		protected static final Codec<ConfigFaeskin> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.BLOCK).listOf().optionalFieldOf("Blocks").forGetter(v -> Optional.of(v.badBlocks)), 
				TagKey.codec(RegistryKeys.ITEM).listOf().optionalFieldOf("Items").forGetter(v -> Optional.of(v.badItems)),
				Codec.INT.optionalFieldOf("Damage").forGetter(v -> v.damage),
				StatusEffectInstance.CODEC.listOf().optionalFieldOf("Effects").forGetter(v -> v.effects))
					.apply(instance, ConfigFaeskin::new));
		
		protected List<TagKey<Block>> badBlocks;
		protected List<TagKey<Item>> badItems;
		
		protected Optional<Integer> damage;
		protected Optional<List<StatusEffectInstance>> effects;
		
		private ConfigFaeskin(Optional<List<TagKey<Block>>> blocksIn, Optional<List<TagKey<Item>>> itemsIn, Optional<Integer> damageIn, Optional<List<StatusEffectInstance>> effectsIn)
		{
			badBlocks = blocksIn.orElse(List.of(VTTags.SILVER_BLOCK));
			badItems = itemsIn.orElse(List.of(VTTags.SILVER_ITEM));
			
			damage = damageIn.isPresent() ? damageIn : Optional.of(1);
			effects = effectsIn;
		}
		
		public boolean penalisesItem(ItemStack itemIn) { return badItems.stream().anyMatch(tag -> itemIn.isIn(tag)); }
		
		public void applyPenalties(LivingEntity living)
		{
			if(damage.isPresent())
				living.damage(living.getWorld().getDamageSources().magic(), damage.get());
			
			if(effects.isPresent() && !effects.get().isEmpty())
				effects.get().forEach(effect -> living.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), Reference.Values.TICKS_PER_SECOND * 5, effect.getAmplifier(), true, false)));
		}
		
		public static ConfigFaeskin fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
