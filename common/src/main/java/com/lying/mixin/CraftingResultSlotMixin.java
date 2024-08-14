package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.utility.ServerEvents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;

@Mixin(Slot.class)
public class CraftingResultSlotMixin
{
	@Shadow
	public ItemStack getStack() { return ItemStack.EMPTY; }
	
	@Inject(method = "canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$canTakeItems(PlayerEntity playerEntity, final CallbackInfoReturnable<Boolean> ci)
	{
		// FIXME Ensure function in multiplayer contexts
		if((Object)this instanceof CraftingResultSlot && ci.getReturnValueZ())
			if(ServerEvents.PlayerEvents.CAN_CRAFT_EVENT.invoker().canPlayerCraft(playerEntity, getStack()).isFalse())
				ci.setReturnValue(false);
	}
}
