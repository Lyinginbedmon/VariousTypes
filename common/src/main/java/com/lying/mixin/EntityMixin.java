package com.lying.mixin;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.ability.IPhasingAbility;
import com.lying.ability.ToggledAbility;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementAbilitySet;
import com.lying.component.element.ElementActionables;
import com.lying.entity.AccessoryAnimationInterface;
import com.lying.event.LivingEvents;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.utility.PlayerPose;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

@Mixin(Entity.class)
public class EntityMixin
{
	/** Set to true if calls to setAir should be ignored */
	protected boolean shouldSkipAir = false;
	
	@Shadow
	public int age;
	
	@Shadow
	public Random random;
	
	@Shadow
	public boolean horizontalCollision;
	
	@Shadow
	public float fallDistance;
	
	@Shadow
	public int timeUntilRegen;
	
	@Shadow
	public UUID getUuid() { return null; }
	
	@Shadow
	public boolean isSpectator() { return false; }
	
	@Shadow
	public World getWorld() { return null; }
	
	@Shadow
	public boolean hasVehicle() { return false; }
	
	@Shadow
	public Box getBoundingBox() { return Box.of(Vec3d.ZERO, 1D, 1D, 1D); }
	
	@Shadow
	public BlockPos getBlockPos() { return BlockPos.ORIGIN; }
	
	@Shadow
	public double getX() { return 0D; }
	
	@Shadow
	public double getEyeY() { return 0D; }
	
	@Shadow
	public double getY() { return 0D; }
	
	@Shadow
	public double getZ() { return 0D; }
	
	@Shadow
	public float getYaw() { return 0F; }
	
	@Shadow
	public boolean isAlive() { return true; }
	
	@Shadow
	public void setOnFireFor(int seconds) { }
	
	@Shadow
	public boolean isSubmergedIn(TagKey<Fluid> tag) { return false; }
	
	@Shadow
	public int getAir() { return 20; }
	
	@Shadow
	public int getMaxAir() { return 20; }
	
	@Shadow
	public void setAir(int airIn) { }
	
	@Shadow
	public DamageSources getDamageSources() { return null; }
	
	@Shadow
	public EntityPose getPose() { return EntityPose.STANDING; }
	
	@Shadow
	public void setPose(EntityPose poseIn) { }
	
	@Shadow
	public boolean isOnGround() { return false; }
	
	@Shadow
	public boolean isTouchingWater() { return false; }
	
	@Shadow
	protected void setFlag(int index, boolean value) { }
	
	@Shadow
	public EntityType<?> getType() { return null; }
	
	@Shadow
	public void emitGameEvent(RegistryEntry<GameEvent> event) { }
	
	@Shadow
	public void setInvisible(boolean bool) { }
	
	@Shadow
	public Text getName() { return Text.empty(); }
	
	@Shadow
	public DynamicRegistryManager getRegistryManager() { return null; }
	
	@Inject(method = "isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$isInvulnerableTo(DamageSource source, final CallbackInfoReturnable<Boolean> ci)
	{
		// Note: Set return value to TRUE to PREVENT damage of the given type to the entity
		
		Entity ent = (Entity)(Object)this;
		if(!(ent instanceof LivingEntity))
			return;
		
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)ent);
		if(sheet.isEmpty())
			return;
		
		DamageSources sources = getDamageSources();
		// Can't drown if you don't need to breathe
		if(source == sources.drown() && !sheet.get().<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.BREATHE.get()))
			ci.setReturnValue(true);
		// Can't starve if you don't need to eat
		else if(source == sources.starve() && !sheet.get().<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.EAT.get()))
			ci.setReturnValue(true);
		// Can't suffocate if you can move through solid blocks
		else if(source == sources.inWall() && IPhasingAbility.isActivelyPhasing((LivingEntity)ent))
			ci.setReturnValue(true);
	}
	
	@Inject(method = "setAir(I)V", at = @At("HEAD"), cancellable = true)
	private void vt$setAir(int air, final CallbackInfo ci)
	{
		if(shouldSkipAir)
			ci.cancel();
	}
	
	@Inject(method = "getMaxAir()I", at = @At("TAIL"), cancellable = true)
	private void vt$getMaxAir(final CallbackInfoReturnable<Integer> ci)
	{
		Entity ent = (Entity)(Object)this;
		if(!(ent instanceof LivingEntity) || getWorld() == null)
			return;
		
		VariousTypes.getSheet((LivingEntity)ent).ifPresent(sheet -> ci.setReturnValue(LivingEvents.GET_MAX_AIR_EVENT.invoker().maxAir(sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES), ci.getReturnValueI())));
	}
	
	@Inject(method = "canClimb(Lnet/minecraft/block/BlockState;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$canClimb(BlockState state, final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity && !state.isAir())
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(ToggledAbility.hasActive(ElementActionables.getActivated(sheet), VTAbilities.CLIMB.get().registryName()))
					ci.setReturnValue(true);
			});
	}
	
	@Inject(method = "isInvisible()Z", at = @At("TAIL"), cancellable = true)
	private void vt$isInvis(final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity)
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(sheet.<ElementAbilitySet>element(VTSheetElements.ABILITIES).hasAbility(VTAbilities.INVISIBILITY.get().registryName()))
					ci.setReturnValue(true);
			});
	}
	
	@Inject(method = "isCollidable()Z", at = @At("TAIL"), cancellable = true)
	private void vt$isCollidable(final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity)
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(sheet.<ElementAbilitySet>element(VTSheetElements.ABILITIES).hasAbility(VTAbilities.INDOMITABLE.get().registryName()))
					ci.setReturnValue(true);
			});
	}
	
	@Inject(method = "isPushable()Z", at = @At("TAIL"), cancellable = true)
	private void vt$isPushable(final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity)
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(sheet.<ElementAbilitySet>element(VTSheetElements.ABILITIES).hasAbility(VTAbilities.INDOMITABLE.get().registryName()))
					ci.setReturnValue(false);
			});
	}
	
	@Inject(method = "bypassesSteppingEffects()Z", at = @At("TAIL"), cancellable = true)
	private void vt$bypassesSteppingEffects(final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity)
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(sheet.<ElementAbilitySet>element(VTSheetElements.ABILITIES).hasAbility(VTAbilities.INTANGIBLE.get().registryName()))
					ci.setReturnValue(true);
			});
	}
	
	@Inject(method = "fall(DZLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
	private void vt$fall(double heightDifference, boolean onGround, BlockState stateLandedOn, BlockPos landedPosition, final CallbackInfo ci)
	{
		if(this.fallDistance > 0F && onGround && (Entity)(Object)this instanceof LivingEntity && !getWorld().isClient())
			LivingEvents.ON_FALL_EVENT.invoker().onLivingFall((LivingEntity)(Object)this, this.fallDistance, onGround, stateLandedOn, landedPosition);
	}
	
	@Inject(method = "slowMovement(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Vec3d;)V", at = @At("HEAD"), cancellable = true)
	private void vt$ignoresSlowdown(BlockState state, Vec3d slow, final CallbackInfo ci)
	{
		if((Entity)(Object)this instanceof LivingEntity)
			if(LivingEvents.IGNORE_SLOW_EVENT.invoker().shouldIgnoreSlowingFrom((LivingEntity)(Object)this, state).isTrue())
				ci.cancel();
	}
	
	@Inject(method = "move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V", at = @At("TAIL"))
	private void vt$move(MovementType type, Vec3d move, final CallbackInfo ci)
	{
		if((Entity)(Object)this instanceof LivingEntity)
		{
			LivingEntity living = (LivingEntity)(Object)this;
			LivingEvents.LIVING_MOVE_EVENT.invoker().onLivingMove(living, type, move, VariousTypes.getSheet(living));
		}
	}
	
	private boolean isClientPlayer() { return getType() == EntityType.PLAYER && getWorld().isClient(); }
	
	@Inject(method = "setPose(Lnet/minecraft/entity/EntityPose;)V", at = @At("TAIL"))
	private void vt$setPose(EntityPose poseIn, final CallbackInfo ci)
	{
		if(isClientPlayer())
		{
			PlayerEntity player = (PlayerEntity)(Object)this;
			PlayerPose pose = PlayerPose.getPoseFromPlayer(player, poseIn);
			((AccessoryAnimationInterface)player).startAnimation(pose);
		}
	}
	
	@Inject(method = "startRiding(Lnet/minecraft/entity/Entity;Z)Z", at = @At("TAIL"))
	private void vt$startRiding(Entity entity, boolean force, final CallbackInfoReturnable<Boolean> ci)
	{
		if(isClientPlayer() && ci.getReturnValueZ())
		{
			PlayerEntity player = (PlayerEntity)(Object)this;
			AccessoryAnimationInterface animator = (AccessoryAnimationInterface)player;
			if(animator.currentlyRunning() != PlayerPose.SITTING)
				animator.startAnimation(PlayerPose.SITTING);
		}
	}
	
	@Inject(method = "dismountVehicle()V", at = @At("TAIL"))
	private void vt$dismountVehicle(final CallbackInfo ci)
	{
		if(isClientPlayer())
		{
			PlayerEntity player = (PlayerEntity)(Object)this;
			AccessoryAnimationInterface animator = (AccessoryAnimationInterface)player;
			PlayerPose pose = animator.currentlyRunning();
			if(pose == PlayerPose.SITTING && !hasVehicle())
				animator.startAnimation(PlayerPose.STANDING);
		}
	}
}
