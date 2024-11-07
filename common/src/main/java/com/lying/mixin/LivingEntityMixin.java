package com.lying.mixin;

import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilitySet;
import com.lying.ability.IPhasingAbility;
import com.lying.ability.ITickingAbility;
import com.lying.ability.ToggledAbility;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionHandler;
import com.lying.component.element.ElementActionables;
import com.lying.component.element.ElementNonLethal;
import com.lying.event.LivingEvents;
import com.lying.event.Result;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.init.VTTypes;
import com.lying.type.TypeSet;
import com.lying.utility.InedibleFoodHelper;

import dev.architectury.event.EventResult;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(LivingEntity.class)
public class LivingEntityMixin extends EntityMixin
{
	private static final int FALL_FLYING_FLAG_INDEX = 7;
	
	@Shadow
	private Optional<BlockPos> climbingPos = Optional.empty();
	
	@Shadow
	private Map<RegistryEntry<StatusEffect>, StatusEffectInstance> activeStatusEffects = Maps.newHashMap();
	
	@Shadow
	protected float lastDamageTaken;
	
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
	
	@Shadow
	public boolean isFallFlying() { return false; }
	
	@Shadow
	public boolean hasStatusEffect(RegistryEntry<StatusEffect> entry) { return false; }
	
	@Shadow
	protected void applyDamage(DamageSource source, float amount) { }
	
	@Shadow
	public SoundEvent getEatSound(ItemStack stack) { return stack.getEatSound(); }
	
	@Inject(method = "hasInvertedHealingAndHarm()Z", at = @At("HEAD"), cancellable = true)
	private void vt$hasInvertedHealingAndHarm(final CallbackInfoReturnable<Boolean> ci)
	{
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)(Object)this);
		if(sheet.isEmpty())
			return;
		else if(sheet.get().<TypeSet>elementValue(VTSheetElements.TYPES).contains(VTTypes.UNDEAD.get()))
			ci.setReturnValue(true);
	}
	
	@Inject(method = "tickMovement()V", at = @At("RETURN"))
	private void vt$tickMovement(final CallbackInfo ci)
	{
		LivingEntity living = (LivingEntity)(Object)this;
		LivingEvents.LIVING_MOVE_TICK_EVENT.invoker().onLivingMoveTick(living, VariousTypes.getSheet(living));
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
			
			// Tick any actively-ticking abilities
			if(!living.getWorld().isClient())
				Ability.getAllOf(ITickingAbility.class, living).forEach(inst -> 
				{
					ITickingAbility ability = (ITickingAbility)inst.ability();
					if(ability.shouldTick(living, inst))
						ability.onTick(inst, sheet, living);
				});
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
			AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
			EventResult result = LivingEvents.CAN_HAVE_STATUS_EFFECT_EVENT.invoker().shouldDenyStatusEffect(effect, abilities); 
			if(result.isTrue())
				ci.setReturnValue(false);
			else if(result.isFalse())
				ci.setReturnValue(true);
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
		if(effect == StatusEffects.BLINDNESS && IPhasingAbility.isActivelyPhasing(living))
		{
			ci.setReturnValue(true);
			return;
		}
		
		VariousTypes.getSheet(living).ifPresent(sheet ->
		{
			EventResult result = LivingEvents.HAS_STATUS_EFFECT_EVENT.invoker().hasStatusEffect(effect, living, sheet.element(VTSheetElements.ABILITIES), ci.getReturnValue());
			if(!result.isEmpty())
				ci.setReturnValue(result.value());
		});
	}
	
	@Inject(method = "getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;", at = @At("TAIL"), cancellable = true)
	private void vt$getEffect(RegistryEntry<StatusEffect> effect, final CallbackInfoReturnable<StatusEffectInstance> ci)
	{
		final LivingEntity living = (LivingEntity)(Object)this;
		
		if(effect == StatusEffects.BLINDNESS && IPhasingAbility.isActivelyPhasing(living))
		{
			ci.setReturnValue(new StatusEffectInstance(StatusEffects.BLINDNESS, -1));
			return;
		}
		
		VariousTypes.getSheet(living).ifPresent(sheet ->
		{
			Result<StatusEffectInstance> result = LivingEvents.GET_STATUS_EFFECT_EVENT.invoker().getStatusEffect(effect, living, sheet.element(VTSheetElements.ABILITIES), ci.getReturnValue());
			if(!result.isEmpty())
				ci.setReturnValue(result.value());
		});
	}
	
	@Inject(method = "heal(F)V", at = @At("HEAD"), cancellable = true)
	private void vt$healNonlethalFirst(float amount, final CallbackInfo ci)
	{
		final LivingEntity living = (LivingEntity)(Object)this;
		Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
		if(sheetOpt.isEmpty()) return;
		
		// Heal nonlethal damage before restoring health
		ElementNonLethal nonlethal = sheetOpt.get().element(VTSheetElements.NONLETHAL);
		if(nonlethal.value() > 0F)
		{
			float healNon = Math.min(amount, nonlethal.value());
			nonlethal.accrue(-healNon, living.getMaxHealth(), living.getType() == EntityType.PLAYER ? (PlayerEntity)living : null);
			amount -= healNon;
			if(amount == 0F)
				ci.cancel();
		}
	}
	
	@Inject(method = "tickFallFlying()V", at = @At("HEAD"), cancellable = true)
	private void vt$tickFallFlying(final CallbackInfo ci)
	{
		final LivingEntity living = (LivingEntity)(Object)this;
		if(LivingEvents.CAN_FLY_EVENT.invoker().canCurrentlyFly(living) == EventResult.interruptFalse())
		{
			if(!getWorld().isClient())
				setFlag(FALL_FLYING_FLAG_INDEX, false);
			
			ci.cancel();
		}
		
		if(LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.invoker().passesElytraCheck(living, true) == EventResult.interruptTrue())
			ci.cancel();
	}
	
	@Inject(method = "getItemUseTime()I", at = @At("HEAD"))
	private void vt$getUseTimeHead(final CallbackInfoReturnable<Integer> ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.setPlayer((PlayerEntity)(Object)this);
	}
	
	@Inject(method = "getItemUseTime()I", at = @At("TAIL"))
	private void vt$getUseTimeTail(final CallbackInfoReturnable<Integer> ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.clearPlayer();
	}
	
	@Inject(method = "setCurrentHand(Lnet/minecraft/util/Hand;)V", at = @At("HEAD"))
	private void vt$setCurrentHandHead(Hand hand, final CallbackInfo ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.setPlayer((PlayerEntity)(Object)this);
	}
	
	@Inject(method = "setCurrentHand(Lnet/minecraft/util/Hand;)V", at = @At("TAIL"))
	private void vt$setCurrentHandTail(Hand hand, final CallbackInfo ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.clearPlayer();
	}
	
	@Inject(method = "shouldSpawnConsumptionEffects()Z", at = @At("HEAD"))
	private void vt$shouldConsumptionEffectsHead(final CallbackInfoReturnable<Boolean> ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.setPlayer((PlayerEntity)(Object)this);
	}
	
	@Inject(method = "shouldSpawnConsumptionEffects()Z", at = @At("TAIL"))
	private void vt$shouldConsumptionEffectsTail(final CallbackInfoReturnable<Boolean> ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.clearPlayer();
	}
	
	@Inject(method = "spawnConsumptionEffects(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"))
	private void vt$consumptionEffectsHead(ItemStack stack, int particleCount, final CallbackInfo ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.setPlayer((PlayerEntity)(Object)this);
	}
	
	@Inject(method = "spawnConsumptionEffects(Lnet/minecraft/item/ItemStack;I)V", at = @At("TAIL"))
	private void vt$consumptionEffectsTail(ItemStack stack, int particleCount, final CallbackInfo ci)
	{
		if(getType() == EntityType.PLAYER)
			InedibleFoodHelper.clearPlayer();
	}
	
	@Inject(method = "eatFood(Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", at = @At("TAIL"), cancellable = true)
	private void vt$eatFood(World world, ItemStack stack, final CallbackInfoReturnable<ItemStack> ci)
	{
		if(stack.get(DataComponentTypes.FOOD) == null && getType() == EntityType.PLAYER)
		{
			PlayerEntity player = (PlayerEntity)(Object)this;
			world.playSound(null, getX(), getY(), getZ(), getEatSound(stack), SoundCategory.NEUTRAL, 1.0f, 1.0f + (world.random.nextFloat() - world.random.nextFloat()) * 0.4f);
			stack.decrementUnlessCreative(1, player);
			emitGameEvent(GameEvent.EAT);
			ci.setReturnValue(stack);
		}
	}
	
	@Inject(method = "updatePotionVisibility()V", at = @At("TAIL"))
	private void vt$updatePotionVisibility(final CallbackInfo ci)
	{
		if(hasStatusEffect(VTStatusEffects.getEntry(VTStatusEffects.STEALTH)))
			setInvisible(true);
	}
}
