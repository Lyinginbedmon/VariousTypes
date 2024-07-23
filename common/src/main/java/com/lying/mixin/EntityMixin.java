package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.ability.ToggledAbility;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementAbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.utility.ServerEvents;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class EntityMixin
{
	/** Set to true if calls to setAir should be ignored */
	protected boolean shouldSkipAir = false;
	
	@Shadow
	public Random random;
	
	@Shadow
	public boolean horizontalCollision;
	
	@Shadow
	public boolean isSpectator() { return false; }
	
	@Shadow
	public World getWorld() { return null; }
	
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
	
	@Inject(method = "isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$isInvulnerableTo(DamageSource source, final CallbackInfoReturnable<Boolean> ci)
	{
		Entity ent = (Entity)(Object)this;
		if(!(ent instanceof LivingEntity))
			return;
		
		Optional<CharacterSheet> sheet = VariousTypes.getSheet((LivingEntity)ent);
		if(sheet.isEmpty())
			return;
		
		DamageSources sources = getDamageSources();
		if(source == sources.drown())
			ci.setReturnValue(!sheet.get().<ActionHandler>element(VTSheetElements.ACTIONS).can(Action.BREATHE.get()));
		if(source == sources.starve())
			ci.setReturnValue(!sheet.get().<ActionHandler>element(VTSheetElements.ACTIONS).can(Action.EAT.get()));
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
		
		VariousTypes.getSheet((LivingEntity)ent).ifPresent(sheet -> ci.setReturnValue(ServerEvents.LivingEvents.GET_MAX_AIR_EVENT.invoker().maxAir(sheet.<AbilitySet>element(VTSheetElements.ABILITES), ci.getReturnValueI())));
	}
	
	@Inject(method = "canClimb(Lnet/minecraft/block/BlockState;)Z", at = @At("TAIL"), cancellable = true)
	private void vt$canClimb(BlockState state, final CallbackInfoReturnable<Boolean> ci)
	{
		if((Entity)(Object) this instanceof LivingEntity && !state.isAir())
			VariousTypes.getSheet((LivingEntity)(Object)this).ifPresent(sheet ->
			{
				if(ToggledAbility.hasActive(ElementAbilitySet.getActivated(sheet), VTAbilities.CLIMB.get().registryName()))
					ci.setReturnValue(true);
			});
	}
}
