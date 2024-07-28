package com.lying.mixin;

import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.ability.ToggledAbility;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementAbilitySet;
import com.lying.component.element.ElementActionHandler;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.init.VTTypes;
import com.lying.type.TypeSet;
import com.lying.utility.ServerEvents;
import com.lying.utility.ServerEvents.LivingEvents;
import com.lying.utility.ServerEvents.Result;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin
{
	@Shadow
	private Optional<BlockPos> climbingPos = Optional.empty();
	
	@Shadow
	private Map<RegistryEntry<StatusEffect>, StatusEffectInstance> activeStatusEffects = Maps.newHashMap();
	
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
	
	/** Functionally repurposed as "next air somewhere you can't breathe" */
	@Shadow
	public int getNextAirUnderwater(int air) { return air - 1; }
	
	/** Functionally repurposed as "next air somewhere you can breathe" */
	@Shadow
	public int getNextAirOnLand(int air) { return air + 4; }
	
	@Inject(method = "hasInvertedHealingAndHarm()Z", at = @At("HEAD"), cancellable = true)
	private void vt$hasInvertedHealingAndHarm(final CallbackInfoReturnable<Boolean> ci)
	{
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)(Object)this);
		if(sheet.isEmpty())
			return;
		else if(sheet.get().<TypeSet>element(VTSheetElements.TYPES).contains(VTTypes.UNDEAD.get()))
			ci.setReturnValue(true);
	}
	
	@Inject(method = "tickMovement()V", at = @At("RETURN"))
	private void vt$tickMovement(final CallbackInfo ci)
	{
		VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet -> 
		{
			if(sheet.<AbilitySet>element(VTSheetElements.ABILITES).hasAbility(VTAbilities.BURN_IN_SUN.get().registryName()))
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
		});
	}
	
	@Inject(method = "canBreatheInWater()Z", at = @At("HEAD"), cancellable = true)
	private void vt$canBreatheInWater(final CallbackInfoReturnable<Boolean> ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			if(ElementActionHandler.canBreathe(sheet, Fluids.WATER, StatusEffectUtil.hasWaterBreathing(living)))
				ci.setReturnValue(true);
		});
	}
	
	@Inject(method = "baseTick()V", at = @At("HEAD"))
	private void vt$baseTickHead(final CallbackInfo ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			((EntityMixin)(Object)living).shouldSkipAir = true;
			sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).tick(living);
		});
	}
	
	@Inject(method = "baseTick()V", at = @At("TAIL"))
	private void vt$baseTickTail(final CallbackInfo ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			((EntityMixin)(Object)living).shouldSkipAir = false;
			
			// Comprehensive breathing system, replaces vanilla air meter handling
			int air = getAir();
			if(!ElementActionHandler.canBreathe(sheet, fluidAtEyes().getFluid(), StatusEffectUtil.hasWaterBreathing(living)))
			{
				// Decline air meter
				air = getNextAirUnderwater(air);
				if(air == -20)
				{
					setAir(0);
					damage(getDamageSources().drown(), 2F);
				}
				else
					setAir(air);
			}
			// Restore air meter
			else if(air < getMaxAir())
				setAir(getNextAirOnLand(air));
		});
	}
	
	private FluidState fluidAtEyes()
	{
		return getWorld().getFluidState(BlockPos.ofFloored(getX(), getEyeY(), getZ()));
	}
	
	@Inject(method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$canHaveStatusEffect(StatusEffectInstance effect, final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet -> 
		{
			AbilitySet abilities = sheet.<AbilitySet>element(VTSheetElements.ABILITES);
			switch(LivingEvents.CAN_HAVE_STATUS_EFFECT_EVENT.invoker().shouldDenyStatusEffect(effect, abilities, ci.getReturnValue() ? Result.ALLOW : Result.DENY))
			{
				case Result.DENY:
					ci.setReturnValue(false);
					break;
				case Result.ALLOW:
					ci.setReturnValue(true);
					break;
				default:
				case Result.PASS:
					break;
			}
		});
	}
	
	@Inject(method = "isClimbing()Z", at = @At("TAIL"), cancellable = true)
	private void vt$isClimbing(final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
		{
			if(!isSpectator() && ToggledAbility.hasActive(ElementActionables.getActivated(sheet), VTAbilities.CLIMB.get().registryName()))
			{
				World world = getWorld();
				if(world.getBlockCollisions((LivingEntity)(Object)this, getBoundingBox().expand(0.2D, -0.1D, 0.2D)).iterator().hasNext())
				{
					climbingPos = Optional.of(getBlockPos());
					ci.setReturnValue(true);
				}
			};
		});
	}
	
	@Inject(method = "hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$hasEffect(RegistryEntry<StatusEffect> effect, final CallbackInfoReturnable<Boolean> ci)
	{
		final LivingEntity living = (LivingEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			final StatusEffectInstance actual = activeStatusEffects.get(effect);
			StatusEffectInstance spoofed = getSpoofedStatusEffect(living, effect);
			if(spoofed != null && !spoofed.equals(actual) && spoofed.getDuration() > 0)
				ci.setReturnValue(true);
		});
	}
	
	@Inject(method = "getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;", at = @At("TAIL"), cancellable = true)
	private void vt$getEffect(RegistryEntry<StatusEffect> effect, final CallbackInfoReturnable<StatusEffectInstance> ci)
	{
		final LivingEntity living = (LivingEntity)(Object)this;
		VariousTypes.getSheet(living).ifPresent(sheet -> 
		{
			StatusEffectInstance spoofed = getSpoofedStatusEffect(living, effect);
			if(spoofed != null)
				ci.setReturnValue(spoofed);
		});
	}
	
	@Nullable
	private StatusEffectInstance getSpoofedStatusEffect(LivingEntity living, RegistryEntry<StatusEffect> effect)
	{
		StatusEffectInstance spoofed = null;
		Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
		if(sheetOpt.isPresent())
		{
			CharacterSheet sheet = sheetOpt.get();
			final StatusEffectInstance actual = activeStatusEffects.get(effect);
			
			AbilitySet set = sheet.<ElementAbilitySet>element(VTSheetElements.ABILITES).copy();
			sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).abilities().forEach(inst -> set.set(inst));
			
			return ServerEvents.LivingEvents.GET_STATUS_EFFECT_EVENT.invoker().getStatusEffect(effect, (LivingEntity)(Object)this, set, actual);
		}
		return spoofed;
	}
}
