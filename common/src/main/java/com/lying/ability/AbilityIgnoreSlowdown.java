package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.data.VTTags;
import com.lying.init.VTSheetElements;
import com.lying.utility.ServerEvents;
import com.lying.utility.VTUtils;

import dev.architectury.event.EventResult;
import net.minecraft.block.Block;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityIgnoreSlowdown extends Ability
{
	public AbilityIgnoreSlowdown(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability",registryName().getPath()+".desc", VTUtils.tagListToString(getTags(instance.memory()), ", ")));
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.LivingEvents.IGNORE_SLOW_EVENT.register((living, state) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isPresent() && sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				if(getTags(sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName()).memory()).stream().anyMatch(tag -> state.isIn(tag)))
					return EventResult.interruptTrue();
			
			return EventResult.pass();
		});
	}
	
	public static List<TagKey<Block>> getTags(NbtCompound memory)
	{
		return AbilityHelper.getTags(memory, "Blocks", RegistryKeys.BLOCK, () -> List.of(VTTags.WEBS));
	}
}