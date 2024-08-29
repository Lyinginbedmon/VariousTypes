package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.utility.InedibleFoodHelper;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.ItemStack;

@Mixin(HungerManager.class)
public class HungerManagerMixin
{
	@Shadow
	private void addInternal(int nutrition, float saturation) { }
	
	@Inject(method = "eat(Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void vt$eatInedible(ItemStack stack, final CallbackInfo ci)
	{
		if(stack.get(DataComponentTypes.FOOD) != null) return;
		
		/**
		 * If we somehow try to eat something inedible, we treat it as equivalent to an apple.
		 * This is because we presume inedible items are filtered out to begin with in {@link Item.use}
		 */
		FoodComponent food = InedibleFoodHelper.INEDIBLE_STANDIN;
		addInternal(food.nutrition(), food.saturation());
	}
}
