package com.lying.mixin;

import java.util.Optional;
import java.util.OptionalInt;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionHandler;
import com.lying.entity.PlayerXPInterface;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.init.VTTypes;
import com.lying.type.Action;
import com.lying.type.TypeSet;
import com.mojang.datafixers.util.Either;

import dev.architectury.event.EventResult;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin extends LivingEntityMixin implements PlayerXPInterface
{
	@Shadow
	public int totalExperience;
	
	@Shadow
	public int experienceLevel;
	
	@Shadow
	public float experienceProgress;
	
	@Shadow
	protected int enchantmentTableSeed;
	
	/** Stores what the player's Actual XP total should be, including any XP accrued whilst manipulated */
	protected OptionalInt originalXP = OptionalInt.empty();
	
	@Shadow
	protected boolean canChangeIntoPose(EntityPose pose) { return false; }
	
	@Shadow
	public void startFallFlying() { }
	
	@Shadow
	public PlayerInventory getInventory() { return null; }
	
	public boolean xpIsManipulated() { return originalXP.isPresent(); }
	
	public void addStoredXP(int xpIn)
	{
		if(!xpIsManipulated()) return;
		setTotalXP(MathHelper.clamp(originalXP.getAsInt() + xpIn, 0, Integer.MAX_VALUE));
	}
	
	public void addStoredLevel(int levelIn)
	{
		if(!xpIsManipulated()) return;
		
		for(int i=0; i<levelIn; i++)
		{
			int level = PlayerXPInterface.xpToLevel(originalXP.getAsInt());
			int nextLevel = PlayerXPInterface.getXPToNextLevel(level);
			addStoredXP(nextLevel);
		}
	}
	
	public void storeCurrentXP()
	{
		originalXP = OptionalInt.of(totalExperience);
	}
	
	public void restoreActualXP()
	{
		totalExperience = originalXP.orElse(0);
		experienceLevel = PlayerXPInterface.xpToLevel(totalExperience);
		recalculateProgressBar();
		
		originalXP = OptionalInt.empty();
	}
	
	private void setTotalXP(int xpIn)
	{
		if(!xpIsManipulated()) return;
		
		originalXP = OptionalInt.of(xpIn);
		recalculateProgressBar();
	}
	
	@Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
	public void vt$writeCustomDataToNbt(NbtCompound nbt, final CallbackInfo ci)
	{
		originalXP.ifPresent(xp -> nbt.putInt("XPOriginal", xp));
	}
	
	@Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
	public void vt$readCustomDataFromNbt(NbtCompound nbt, final CallbackInfo ci)
	{
		if(nbt.contains("XPOriginal", NbtElement.INT_TYPE))
			originalXP = OptionalInt.of(nbt.getInt("XPOriginal"));
	}
	
	@Inject(method = "tick()V", at = @At("HEAD"))
	private void vt$tick(final CallbackInfo ci)
	{
		if(getWorld().isClient()) return;
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			if(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(VTAbilities.OMNISCIENT.get().registryName()))
			{
				if(xpIsManipulated()) return;
				
				storeCurrentXP();
				experienceLevel = 9999;
				totalExperience = PlayerXPInterface.getTotalXPForLevel(experienceLevel + 1) - 1;
				recalculateProgressBar();
			}
			else if(xpIsManipulated())
				restoreActualXP();
		});
	}
	
	private void recalculateProgressBar()
	{
		int excessXP = totalExperience - PlayerXPInterface.getTotalXPForLevel(experienceLevel);
		int toNext = PlayerXPInterface.getXPToNextLevel(experienceLevel);
		experienceProgress = (float)(excessXP) / (float)toNext;
		// FIXME Resolve excess texture rendered as part of progress bar
		
		if(!getWorld().isClient())
			((ServerPlayerEntity)(Object)this).networkHandler.send(new ExperienceBarUpdateS2CPacket(this.experienceProgress, this.totalExperience, this.experienceLevel), null);
	}
	
	@Inject(method = "addExperience(I)V", at = @At("HEAD"), cancellable = true)
	private void vt$addExperience(int experience, final CallbackInfo ci)
	{
		if(xpIsManipulated())
		{
			ci.cancel();
			if(experience <= 0)
				return;
			else
				addStoredXP(experience);
		}
	}
	
	@Inject(method = "addExperienceLevels(I)V", at = @At("HEAD"), cancellable = true)
	private void vt$addExperienceLevels(int levels, final CallbackInfo ci)
	{
		if(xpIsManipulated())
		{
			ci.cancel();
			if(levels < 0)
				return;
			else
				addStoredLevel(levels);
		}
	}
	
	@Inject(method = "applyEnchantmentCosts(Lnet/minecraft/item/ItemStack;I)V", at = @At("HEAD"), cancellable = true)
	private void vt$applyEnchantmentCosts(ItemStack enchantedItem, int experienceLevels, final CallbackInfo ci)
	{
		if(xpIsManipulated())
		{
			this.enchantmentTableSeed = this.random.nextInt();
			ci.cancel();
		}
	}
	
	@Inject(method = "canFoodHeal()Z", at = @At("TAIL"), cancellable = true)
	private void vt$canFoodHeal(final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents natural health regeneration if the player does not have that action
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.REGEN.get()))
				ci.setReturnValue(false);
		});
	}
	
	@Inject(method = "canResetTimeBySleeping()Z", at = @At("HEAD"), cancellable = true)
	private void vt$canResetTime(final CallbackInfoReturnable<Boolean> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			/**
			 * Automatically includes players that can't sleep in the list of sleeping players.
			 * This prevents situations where a server population wants to skip night but can't due to unsleeping players.
			 */
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				ci.setReturnValue(true);
		});
	}
	
	@Inject(method = "trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;", at = @At("HEAD"), cancellable = true)
	private void vt$trySleep(BlockPos pos, final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents natural health regeneration if the player does not have that action
			if(!sheet.<ElementActionHandler>element(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				ci.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
		});
	}
	
	@Inject(method = "updatePose()V", at = @At("HEAD"), cancellable = true)
	private void vt$updatePose(final CallbackInfo ci)
	{
		if(!canChangeIntoPose(EntityPose.SWIMMING))
			return;
		
		PlayerEntity player = (PlayerEntity)(Object)this;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
			sheet.<Optional<EntityPose>>elementValue(VTSheetElements.SPECIAL_POSE).ifPresent(pose -> 
			{
				if(getPose() != pose)
					setPose(pose);
				ci.cancel();
			}));
	}
	
	@Inject(method = "checkFallFlying()Z", at = @At("HEAD"), cancellable = true)
	private void vt$checkFallFlying(final CallbackInfoReturnable<Boolean> ci)
	{
		PlayerEntity player = (PlayerEntity)(Object)this;
		if(LivingEvents.CAN_FLY_EVENT.invoker().canCurrentlyFly(player) == EventResult.interruptFalse())
		{
			ci.setReturnValue(false);
			return;
		}
		
		if(!isOnGround() && !isFallFlying() && !isTouchingWater() && !hasStatusEffect(StatusEffects.LEVITATION) && LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.invoker().passesElytraCheck(player, false) == EventResult.interruptTrue())
		{
			startFallFlying();
			ci.setReturnValue(true);
		}
	}
	
	@Inject(method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At("HEAD"), cancellable = true)
	private void vt$applyDamage(final DamageSource source, float amount, final CallbackInfo ci)
	{
		final PlayerEntity player = (PlayerEntity)(Object)this;
		
		amount = Math.max(0, PlayerEvents.MODIFY_DAMAGE_TAKEN_EVENT.invoker().getModifiedDamage(player, source, amount));
		
		/**
		 * Applies Smite and Bane of Arthropods bonus damage to players with the UNDEAD or ARTHROPOD supertypes<br>
		 * This doesn't perfectly replicate the same effect as for mobs, but it's as close as we can get.<br>
		 * Note that since is applied AFTER damage is modified, these enchantments can bypass normal invulnerabilities.
		 */
		if(source.isOf(DamageTypes.MOB_ATTACK) || source.isOf(DamageTypes.PLAYER_ATTACK))
		{
			LivingEntity originator = (LivingEntity)source.getAttacker();
			ItemStack heldStack = originator.getEquippedStack(EquipmentSlot.MAINHAND);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty()) return;
			
			TypeSet types = sheetOpt.get().elementValue(VTSheetElements.TYPES);
			// XXX Find some approach to check without using hard-coded enchantments?
			if(types.contains(VTTypes.UNDEAD.get()))
				amount += 2.5F * EnchantmentHelper.getLevel(Enchantments.SMITE, heldStack);
			
			if(types.contains(VTTypes.ARTHROPOD.get()))
				amount += 2.5F * EnchantmentHelper.getLevel(Enchantments.BANE_OF_ARTHROPODS, heldStack);
		}
		
		if(amount <= 0)
			ci.cancel();
	}
}
