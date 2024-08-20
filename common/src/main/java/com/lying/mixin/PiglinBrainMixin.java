package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin
{
	@Inject(method = "wearsGoldArmor(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
	private static void vt$wearsGoldArmor(LivingEntity living, final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			if(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.GOLDHEARTED.get().registryName()))
				ci.setReturnValue(true);
		});
	}
}
