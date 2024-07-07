package com.lying.item;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.type.Action;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CharacterSheetItem extends Item
{
	public CharacterSheetItem(Settings settings)
	{
		super(settings);
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		Optional<CharacterSheet> opt = VariousTypes.getSheet(user);
		if(opt.isPresent() && !world.isClient())
		{
			user.sendMessage(Text.literal("Retrieved character sheet"));
			CharacterSheet sheet = opt.get();
			user.sendMessage(Text.literal("Power: "+sheet.power()));
			if(sheet.hasSpecies())
				user.sendMessage(Text.literal("Species: ").append(Text.literal(sheet.getSpecies().get().display().title().getString())));
			if(!sheet.getAppliedTemplates().isEmpty())
			{
				user.sendMessage(Text.literal("Templates:"));
				sheet.getAppliedTemplates().forEach(tem -> user.sendMessage(Text.literal(" * ").append(Text.literal(tem.display().title().getString()))));
			}
			
			if(!sheet.getTypes().isEmpty())
			{
				user.sendMessage(Text.literal("Creature types:"));
				sheet.getTypes().forEach(type -> user.sendMessage(Text.literal(" * ").append(type.displayName(world.getRegistryManager()))));
			}
			
			user.sendMessage(Text.literal("Actions:"));
			Action.actions().forEach(action -> 
			{
				if(sheet.getActions().can(action.get()))
					user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.can_action", action.get().translate())));
				else
					user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.cannot_action", action.get().translate())));
			});
			if(sheet.getActions().can(Action.BREATHE.get()))
			{
				user.sendMessage(Text.literal("Breathable fluids:"));
				sheet.getActions().canBreatheIn().forEach(fluid -> user.sendMessage(Text.literal(" ~").append(Text.literal(fluid.id().getPath()))));
			}
			
			if(!sheet.getAbilities().isEmpty())
			{
				user.sendMessage(Text.literal("Abilities:"));
				sheet.getAbilities().abilities().forEach(inst ->
				{
					if(!inst.ability().isHidden(inst))
						user.sendMessage(Text.literal(" * ").append(inst.displayName(world.getRegistryManager())));
				});
			}
		}
		return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
	}
}
