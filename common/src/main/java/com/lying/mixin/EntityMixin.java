package com.lying.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.Entity;
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
}
