package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityDietRestriction.ConfigDiet;
import com.lying.component.CharacterSheet;
import com.lying.data.VTTags;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.utility.ServerEvents;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityDietRestriction extends Ability implements IComplexAbility<ConfigDiet>
{
	public AbilityDietRestriction(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigDiet values = memoryToValues(instance.memory());
		boolean allows = values.allows();
		boolean denies = values.denies();
		
		Text allowList = allows ? VTUtils.tagListToString(values.allowedTags, ", ") : Text.empty();
		Text denyList = denies ?VTUtils.tagListToString(values.deniedTags, ", ") : Text.empty();
		if(!allows && denies)
			return Optional.of(translate("ability", registryName().getPath()+".desc_deny", denyList));
		else if(allows && !denies)
			return Optional.of(translate("ability", registryName().getPath()+".desc_allow", allowList));
		else if(allows && denies)
			return Optional.of(translate("ability", registryName().getPath()+".desc_both", allowList, denyList));
		else
			return Optional.of(translate("ability", registryName().getPath()+".desc"));
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.PlayerEvents.CAN_EAT_EVENT.register((player, stack) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty()) return EventResult.pass();
			
			CharacterSheet sheet = sheetOpt.get();
			ConfigDiet values = memoryToValues(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(VTAbilities.HERBIVORE.get().registryName()).memory());
			if(values.isBlank()) return EventResult.pass();
			
			if(values.denies() && values.deniedTags.stream().anyMatch(tag -> stack.isIn(tag)))
				return EventResult.interruptFalse();
			
			if(values.allows() && values.allowedTags.stream().anyMatch(tag -> stack.isIn(tag)))
				return EventResult.interruptTrue();
			
			return EventResult.pass();
		});
	}
	
	public ConfigDiet memoryToValues(NbtCompound data) { return ConfigDiet.fromNbt(data); }
	
	public static class ConfigDiet
	{
		protected static final Codec<ConfigDiet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.ITEM).listOf().optionalFieldOf("Allowed").forGetter(v -> Optional.of(v.allowedTags)),
				TagKey.codec(RegistryKeys.ITEM).listOf().optionalFieldOf("Denied").forGetter(v -> Optional.of(v.deniedTags)))
					.apply(instance, ConfigDiet::new));
		
		protected List<TagKey<Item>> allowedTags = Lists.newArrayList();
		protected List<TagKey<Item>> deniedTags = Lists.newArrayList();
		
		public ConfigDiet(Optional<List<TagKey<Item>>> allowIn, Optional<List<TagKey<Item>>> denyIn)
		{
			allowIn.ifPresentOrElse(val -> allowedTags.addAll(val), () -> allowedTags.add(VTTags.VEGETARIAN));
			denyIn.ifPresentOrElse(val -> deniedTags.addAll(val), () -> deniedTags.addAll(List.of(ItemTags.MEAT, ItemTags.FISHES)));
		}
		
		public boolean isBlank() { return !allows() && !denies(); }
		public boolean allows() { return !allowedTags.isEmpty(); }
		public boolean denies() { return !deniedTags.isEmpty(); }
		
		public static ConfigDiet fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
