package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.network.PlayerFlightInputPacket;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends LivingEntityMixin
{
	@Inject(method = "tickMovement()V", at = @At("HEAD"), cancellable = true)
	private void vt$tickFallFlying(final CallbackInfo ci)
	{
		final ClientPlayerEntity living = (ClientPlayerEntity)(Object)this;
		if(isFallFlying() && living.isMainPlayer())
			PlayerFlightInputPacket.send(living.input.movementForward, living.input.movementSideways, living.input.jumping, living.input.sneaking);
	}
	
	@Inject(method = "getRecipeBook()Lnet/minecraft/client/recipebook/ClientRecipeBook;", at = @At("HEAD"), cancellable = true)
	private void vt$getRecipeBook(final CallbackInfoReturnable<ClientRecipeBook> ci)
	{
		final ClientPlayerEntity living = (ClientPlayerEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			if(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.FORGETFUL.get().registryName()))
				ci.setReturnValue(new ClientRecipeBook());
		});
	}
}
