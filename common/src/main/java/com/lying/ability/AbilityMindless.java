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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityMindless extends Ability
{
	public AbilityMindless(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability",registryName().getPath()+".desc", VTUtils.tagListToString(getTags(instance.memory()), ", ")));
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.PlayerEvents.CAN_COLLECT_XP_EVENT.register((orb,player) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isPresent() && sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return EventResult.interruptFalse();
			return EventResult.pass();
		});
		
		ServerEvents.PlayerEvents.CAN_USE_SCREEN_EVENT.register((player,screen) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isPresent() && sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
			{
				AbilityInstance inst = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
				if(getTags(inst.memory()).stream().anyMatch(tag -> VTTags.isScreenIn(screen, tag)))
					return EventResult.interruptFalse();
			}
			
			return EventResult.pass();
		});
	}
	
	public static List<TagKey<ScreenHandlerType<?>>> getTags(NbtCompound memory)
	{
		return AbilityHelper.getTags(memory, "Menus", RegistryKeys.SCREEN_HANDLER, () -> List.of(VTTags.CRAFTING_MENU));
	}
}