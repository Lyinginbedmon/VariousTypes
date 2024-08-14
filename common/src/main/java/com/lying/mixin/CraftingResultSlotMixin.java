package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.CraftingResultSlot;
import net.minecraft.screen.slot.Slot;

@Mixin(Slot.class)
public class CraftingResultSlotMixin
{
	@Inject(method = "canTakeItems(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$canTakeItems(PlayerEntity playerEntity, final CallbackInfoReturnable<Boolean> ci)
	{
		if((Object)this instanceof CraftingResultSlot && ci.getReturnValueZ())
			VariousTypes.getSheet(playerEntity).ifPresent(sheet -> 
			{
				if(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).hasAbility(VTAbilities.MINDLESS.get().registryName()))
					ci.setReturnValue(false);
			});
	}
}
