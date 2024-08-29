package com.lying.fabric.component;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import com.lying.component.CharacterSheet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class MobSheetHandler extends CharacterSheet implements AutoSyncedComponent
{
	public MobSheetHandler(LivingEntity entity)
	{
		super(entity);
	}
	
	public void writeToNbt(NbtCompound tag, WrapperLookup registryLookup)
	{
		super.writeSheetToNbt(tag);
	}
	
	public void readFromNbt(NbtCompound tag, WrapperLookup registryLookup)
	{
		super.readSheetFromNbt(tag);
	}
	
	public int timesCreated() { return 1; }
	
	public void incEdits() { }
	
	public void markDirty() { }
}
