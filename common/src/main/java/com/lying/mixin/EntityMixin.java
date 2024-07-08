package com.lying.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.type.Action;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

@Mixin(Entity.class)
public class EntityMixin
{
	@Shadow
	public Random random;
	
	@Shadow
	public World getWorld() { return null; }
	
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
			ci.setReturnValue(!sheet.get().hasAction(Action.BREATHE.get()));
		if(source == sources.starve())
			ci.setReturnValue(!sheet.get().hasAction(Action.EAT.get()));
	}
}
