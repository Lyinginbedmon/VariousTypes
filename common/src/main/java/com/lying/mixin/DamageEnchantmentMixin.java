package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.lying.VariousTypes;
import com.lying.init.VTTypes;

import net.minecraft.enchantment.DamageEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.TagKey;

@Mixin(DamageEnchantment.class)
public class DamageEnchantmentMixin
{
	@Shadow
	private Optional<TagKey<EntityType<?>>> applicableEntities;
	
	@Inject(method = "onTargetDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;I)V", at = @At("HEAD"), cancellable = true)
	private void vt$onTargetDamage(LivingEntity user, Entity target, int level, final CallbackInfo ci)
	{
		LivingEntity living;
		if(level > 0 && applicableEntities.isPresent() && target instanceof LivingEntity && applicableEntities.get() == EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS)
			VariousTypes.getSheet(living = (LivingEntity)target).ifPresent(sheet -> 
			{
				if(!sheet.getTypes().contains(VTTypes.ARTHROPOD.get()))
					return;
				
				int i = 20 + user.getRandom().nextInt(10 * level);
				living.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, i, 3));
			});
	}
}
