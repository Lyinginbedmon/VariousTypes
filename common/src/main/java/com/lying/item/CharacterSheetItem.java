package com.lying.item;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.reference.Reference;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.screen.CharacterSheetScreenHandler;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CharacterSheetItem extends Item
{
	public static final int USAGE_COOLDOWN = Reference.Values.TICKS_PER_SECOND / 2;
	
	public CharacterSheetItem(Settings settings)
	{
		super(settings);
	}
	
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack stackInHand = user.getStackInHand(hand);
		if(user.getItemCooldownManager().isCoolingDown(stackInHand.getItem()))
			return TypedActionResult.consume(stackInHand);
		
		user.getItemCooldownManager().set(stackInHand.getItem(), USAGE_COOLDOWN);
		Optional<CharacterSheet> opt = VariousTypes.getSheet(user);
		if(opt.isPresent())
		{
			if(!world.isClient())
				if(opt.get().timesCreated() == 0)
					user.openHandledScreen(new SimpleNamedScreenHandlerFactory((id, playerInventory, custom) -> new CharacterCreationScreenHandler(id, playerInventory.player), user.getDisplayName()));
				else
					MenuRegistry.openMenu((ServerPlayerEntity)user, new SimpleNamedScreenHandlerFactory((id, playerInv, custom) -> new CharacterSheetScreenHandler(id), user.getDisplayName()));
			return TypedActionResult.success(stackInHand, world.isClient());
		}
		else
		{
			if(world.isClient())
				user.sendMessage(Reference.ModInfo.translate("gui","no_character_sheet", user.getDisplayName()));
			return TypedActionResult.consume(stackInHand);
		}
	}
	
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
	{
		ItemStack stackInHand = user.getStackInHand(hand);
		if(user.getItemCooldownManager().isCoolingDown(stackInHand.getItem()))
			return ActionResult.CONSUME;
		
		user.getItemCooldownManager().set(stackInHand.getItem(), USAGE_COOLDOWN);
		Optional<CharacterSheet> opt = VariousTypes.getSheet(entity);
		if(opt.isPresent())
		{
			if(!user.getWorld().isClient())
				MenuRegistry.openMenu((ServerPlayerEntity)user, new SimpleNamedScreenHandlerFactory((id, playerInv, custom) -> new CharacterSheetScreenHandler(id), entity.getDisplayName()));
			return ActionResult.SUCCESS;
		}
		else
		{
			if(user.getWorld().isClient())
				user.sendMessage(Reference.ModInfo.translate("gui", "no_character_sheet", entity.getDisplayName()));
			return ActionResult.CONSUME;
		}
	}
}
