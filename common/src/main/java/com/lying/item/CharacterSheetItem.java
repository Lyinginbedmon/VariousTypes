package com.lying.item;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.screen.CharacterSheetScreenHandler;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
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
			MenuRegistry.openMenu((ServerPlayerEntity)user, new SimpleNamedScreenHandlerFactory((id, playerInv, custom) -> new CharacterSheetScreenHandler(id), user.getDisplayName()));
			
//			CharacterSheet sheet = opt.get();
//			user.sendMessage(Text.literal("Power: "+sheet.power()));
//			if(sheet.hasASpecies())
//				user.sendMessage(Text.literal("Species: ").append(describeSpecies(sheet.getSpecies().get())));
//			if(!sheet.getAppliedTemplates().isEmpty())
//			{
//				user.sendMessage(Text.literal("Templates:"));
//				sheet.getAppliedTemplates().forEach(tem -> user.sendMessage(Text.literal(" * ").append(describeTemplate(tem))));
//			}
//			
//			if(!sheet.types().isEmpty())
//			{
//				user.sendMessage(Text.literal("Creature types:"));
//				sheet.types().forEach(type -> user.sendMessage(Text.literal(" * ").append(describeType(type, world.getRegistryManager()))));
//			}
//			
//			user.sendMessage(Text.literal("Actions:"));
//			Action.actions().forEach(action -> 
//			{
//				if(sheet.actions().can(action.get()))
//					user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.can_action", action.get().translate())));
//				else
//					user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.cannot_action", action.get().translate())));
//			});
//			if(sheet.actions().can(Action.BREATHE.get()))
//			{
//				user.sendMessage(Text.literal("Breathable fluids:"));
//				sheet.actions().canBreatheIn().forEach(fluid -> user.sendMessage(Text.literal(" ~").append(Text.literal(fluid.id().getPath()))));
//			}
//			
//			if(!sheet.abilities().isEmpty() && sheet.abilities().abilities().stream().anyMatch(inst -> !inst.ability().isHidden(inst)))
//			{
//				user.sendMessage(Text.literal("Abilities:"));
//				sheet.abilities().abilities().forEach(inst ->
//				{
//					if(!inst.ability().isHidden(inst))
//						user.sendMessage(Text.literal(" * ").append(inst.displayName(world.getRegistryManager())));
//				});
//			}
		}
		return TypedActionResult.success(user.getStackInHand(hand), world.isClient());
	}
}
