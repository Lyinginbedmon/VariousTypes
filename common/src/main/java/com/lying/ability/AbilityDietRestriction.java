package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityDietRestriction.ConfigDiet;
import com.lying.component.CharacterSheet;
import com.lying.data.VTTags;
import com.lying.event.PlayerEvents;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
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
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
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
	
	public static AbilityInstance ofTags(@Nullable List<TagKey<Item>> allow, @Nullable List<TagKey<Item>> deny)
	{
		ConfigDiet config = new ConfigDiet(allow == null ? Optional.empty() : Optional.of(allow), deny == null ? Optional.empty() : Optional.of(deny));
		AbilityInstance inst = VTAbilities.HERBIVORE.get().instance();
		inst.setMemory(config.toNbt());
		return inst;
	}
	
	public void registerEventHandlers()
	{
		PlayerEvents.CAN_EAT_EVENT.register((player, stack) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.HERBIVORE.get().registryName())) return EventResult.pass();
			
			CharacterSheet sheet = sheetOpt.get();
			ConfigDiet values = memoryToValues(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(VTAbilities.HERBIVORE.get().registryName()).memory());
			if(values.isBlank())
				return EventResult.pass();
			
			if(values.allows() && values.allowedTags.stream().anyMatch(tag -> stack.isIn(tag)))
				return EventResult.interruptTrue();
			
			if(values.denies() && values.deniedTags.stream().anyMatch(tag -> stack.isIn(tag)))
				return EventResult.interruptFalse();
			
			return EventResult.pass();
		});
	}
	
	public ConfigDiet memoryToValues(NbtCompound data) { return ConfigDiet.fromNbt(data); }
	
	public static class ConfigDiet
	{
		protected static final Codec<ConfigDiet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.ITEM).listOf().optionalFieldOf("Allowed").forGetter(v -> v.allowedTags.isEmpty() ? Optional.empty() : Optional.of(v.allowedTags)),
				TagKey.codec(RegistryKeys.ITEM).listOf().optionalFieldOf("Denied").forGetter(v -> v.deniedTags.isEmpty() ? Optional.empty() : Optional.of(v.deniedTags)))
					.apply(instance, ConfigDiet::new));
		
		protected List<TagKey<Item>> allowedTags = Lists.newArrayList();
		protected List<TagKey<Item>> deniedTags = Lists.newArrayList();
		
		public ConfigDiet(Optional<List<TagKey<Item>>> allowIn, Optional<List<TagKey<Item>>> denyIn)
		{
			allowIn.ifPresentOrElse(val -> allowedTags.addAll(val), () -> allowedTags.add(VTTags.VEGETARIAN));
			denyIn.ifPresentOrElse(val -> deniedTags.addAll(val), () -> deniedTags.addAll(List.of(ItemTags.MEAT, ItemTags.FISHES)));
		}
		
		public Codec<ConfigDiet> codec() { return CODEC; }
		
		public boolean isBlank() { return !allows() && !denies(); }
		public boolean allows() { return !allowedTags.isEmpty(); }
		public boolean denies() { return !deniedTags.isEmpty(); }
		
		public NbtCompound toNbt() { return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(); }
		
		public static ConfigDiet fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
