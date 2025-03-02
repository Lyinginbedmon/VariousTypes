package com.lying.component.element;

import org.jetbrains.annotations.Nullable;

import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.network.SyncFatiguePacket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

// TODO Add HUD display of current nonlethal damage total
public class ElementNonLethal implements ISheetElement<Float>
{
	private float nonlethalDamage = 0F;
	
	public SheetElement<?> registry() { return VTSheetElements.NONLETHAL; }
	
	public Float value() { return nonlethalDamage; }
	
	public void set(float amount, @Nullable PlayerEntity player)
	{
		nonlethalDamage = amount;
		if(player != null && !player.getWorld().isClient())
			SyncFatiguePacket.send((ServerPlayerEntity)player, nonlethalDamage);
	}
	
	public void accrue(float amount, float maxHealth, @Nullable PlayerEntity player)
	{
		nonlethalDamage = Math.clamp(nonlethalDamage + amount, 0F, maxHealth);
		if(player != null && !player.getWorld().isClient())
			SyncFatiguePacket.send((ServerPlayerEntity)player, nonlethalDamage);
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.putFloat("Nonlethal", nonlethalDamage);
		return nbt;
	}
	
	public void readFromNbt(NbtCompound nbt)
	{
		nonlethalDamage = nbt.getFloat("Nonlethal");
	}
}
