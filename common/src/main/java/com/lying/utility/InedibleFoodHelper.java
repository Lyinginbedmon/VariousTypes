package com.lying.utility;

import org.jetbrains.annotations.Nullable;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

/** Mainly used to pass information from LivingEntity to Item when handling inedible food items */
public class InedibleFoodHelper
{
	private static PlayerEntity checkingPlayer = null; 
	public static final FoodComponent INEDIBLE_STANDIN = FoodComponents.APPLE;
	
	public static void setPlayer(PlayerEntity player) { checkingPlayer = player; }
	
	public static void clearPlayer() { checkingPlayer = null; }
	
	@Nullable
	public static PlayerEntity checkingPlayer() { return checkingPlayer; }
	
	public static int getUseTicks(ItemStack stack, int vanilla)
	{
		if(vanilla > 0)
			return vanilla;
		
		FoodComponent comp = stack.get(DataComponentTypes.FOOD);
		if(comp != null)
			return comp.getEatTicks();
		
		// Item is ordinarily inedible, check if we can eat it
		if(checkingPlayer != null && ServerEvents.PlayerEvents.CAN_EAT_EVENT.invoker().canEat(InedibleFoodHelper.checkingPlayer(), stack).isTrue())
			return INEDIBLE_STANDIN.getEatTicks();
		
		return vanilla;
	}
	
	public static UseAction getUseAction(ItemStack stack, UseAction vanilla)
	{
		if(vanilla != UseAction.NONE)
			return vanilla;

		FoodComponent comp = stack.get(DataComponentTypes.FOOD);
		if(comp != null)
			return UseAction.EAT;

		// Item is ordinarily inedible, check if we can eat it
		if(checkingPlayer != null && ServerEvents.PlayerEvents.CAN_EAT_EVENT.invoker().canEat(InedibleFoodHelper.checkingPlayer(), stack).isTrue())
			return UseAction.EAT;
		
		return vanilla;
	}
}
