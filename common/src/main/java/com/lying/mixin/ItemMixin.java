package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.utility.InedibleFoodHelper;
import com.lying.utility.ServerEvents;

import dev.architectury.event.EventResult;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

@Mixin(Item.class)
public class ItemMixin
{
	@Inject(method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;", at = @At("HEAD"), cancellable = true)
	private void vt$useCanEat(World world, PlayerEntity user, Hand hand, final CallbackInfoReturnable<TypedActionResult<ItemStack>> ci)
	{
		ItemStack stack = user.getStackInHand(hand);
		EventResult result = ServerEvents.PlayerEvents.CAN_EAT_EVENT.invoker().canEat(user, stack);
		if(result.interruptsFurtherEvaluation())
		{
			if(result.isTrue())
			{
				FoodComponent comp = stack.get(DataComponentTypes.FOOD);
				if(!user.canConsume(comp == null ? false : comp.canAlwaysEat()))
					return;
				
				user.setCurrentHand(hand);
				ci.setReturnValue(TypedActionResult.success(stack));
			}
			else
				ci.setReturnValue(TypedActionResult.pass(stack));
		}
	}
	
	@Inject(method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I", at = @At("TAIL"), cancellable = true)
	private void vt$inedibleUseTime(ItemStack stack, final CallbackInfoReturnable<Integer> ci)
	{
		ci.setReturnValue(InedibleFoodHelper.getUseTicks(stack, ci.getReturnValue()));
	}
	
	@Inject(method = "getUseAction(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/util/UseAction;", at = @At("TAIL"), cancellable = true)
	private void vt$inedibleUseAction(ItemStack stack, final CallbackInfoReturnable<UseAction> ci)
	{
		ci.setReturnValue(InedibleFoodHelper.getUseAction(stack, ci.getReturnValue()));
	}
	
	@Inject(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;", at = @At("TAIL"), cancellable = true)
	private void vt$finishEating(ItemStack stack, World world, LivingEntity user, final CallbackInfoReturnable<ItemStack> ci)
	{
		if(user.getType() != EntityType.PLAYER) return;
		PlayerEntity player = (PlayerEntity)user;
		EventResult result = ServerEvents.PlayerEvents.CAN_EAT_EVENT.invoker().canEat(player, stack);
		if(result.interruptsFurtherEvaluation())
			if(result.isTrue())
			{
				FoodComponent comp = stack.get(DataComponentTypes.FOOD);
				if(player.canConsume(comp == null ? false : comp.canAlwaysEat()))
					ci.setReturnValue(player.eatFood(world, stack));
			}
			else
				ci.setReturnValue(stack);
	}
}
