package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin
{
	@Shadow
	public ItemStack getEquippedStack(EquipmentSlot slot) { return ItemStack.EMPTY; }
	
	@Shadow
	public void processEquippedStack(ItemStack stack) { }
	
	@Shadow
	public void onEquipStack(EquipmentSlot slot, ItemStack oldStack, ItemStack newStack) { }
	
	@Shadow
	public void equipStack(EquipmentSlot slot, ItemStack newStack) { }
	
	@Shadow
	public void sendEquipmentBreakStatus(EquipmentSlot slot) { }
	
	@Shadow
	public boolean damage(DamageSource source, float amount) { return false; }
	
	@Shadow
	public int getNextAirUnderwater(int air) { return air - 1; }
	
	@Shadow
	public int getNextAirOnLand(int air) { return air + 4; }
	
	@Inject(method = "hasInvertedHealingAndHarm()Z", at = @At("HEAD"), cancellable = true)
	private void vt$hasInvertedHealingAndHarm(final CallbackInfoReturnable<Boolean> ci)
	{
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)(Object)this);
		if(sheet.isEmpty())
			return;
		else if(sheet.get().getTypes().contains(VTTypes.UNDEAD.get()))
			ci.setReturnValue(true);
	}
	
	@Inject(method = "tickMovement()V", at = @At("RETURN"))
	private void vt$tickMovement(final CallbackInfo ci)
	{
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)(Object)this);
		if(sheet.isPresent() && sheet.get().getAbilities().hasAbility(VTAbilities.BURN_IN_SUN.get().registryName()))
		{
			ItemStack helmet = getEquippedStack(EquipmentSlot.HEAD);
			if(!helmet.isEmpty())
			{
				if(helmet.isDamageable())
				{
					helmet.setDamage(helmet.getDamage() + random.nextInt(2));
					if(helmet.getDamage() >= helmet.getMaxDamage())
					{
						sendEquipmentBreakStatus(EquipmentSlot.HEAD);
						equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
					}
				}
			}
			else
				setOnFireFor(8);
		}
	}
	
	@Inject(method = "canBreatheInWater()Z", at = @At("HEAD"), cancellable = true)
	private void vt$canBreatheInWater(final CallbackInfoReturnable<Boolean> ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		Optional<CharacterSheet> sheet = VariousTypes.getSheet(living);
		if(sheet.isPresent() && sheet.get().isAbleToBreathe(Fluids.WATER, StatusEffectUtil.hasWaterBreathing(living)))
			ci.setReturnValue(true);
	}
	
	@Inject(method = "baseTick()V", at = @At("TAIL"))
	private void vt$baseTick(final CallbackInfo ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		Optional<CharacterSheet> sheet = VariousTypes.getSheet(living);
		if(sheet.isEmpty())
			return;
		
		// FIXME Prevent baseTick from stalling air loss w/ original breath handling
		if(!sheet.get().isAbleToBreathe(fluidAtEyes().getFluid(), StatusEffectUtil.hasWaterBreathing(living)))
		{
			// Decline air meter
			setAir(getNextAirUnderwater(getAir()));
			if(getAir() == -20)
			{
				setAir(0);
				damage(getDamageSources().drown(), 2F);
			}
		}
		else if(getAir() < getMaxAir())
		{
			// Restore air meter
			setAir(getNextAirOnLand(getAir()));
		}
	}
	
	private FluidState fluidAtEyes()
	{
		return getWorld().getFluidState(BlockPos.ofFloored(getX(), getEyeY(), getZ()));
	}
}
