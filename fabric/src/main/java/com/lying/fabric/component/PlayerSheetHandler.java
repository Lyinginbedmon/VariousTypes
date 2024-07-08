package com.lying.fabric.component;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import com.lying.component.CharacterSheet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class PlayerSheetHandler extends CharacterSheet implements AutoSyncedComponent
{
	public PlayerSheetHandler(LivingEntity entity)
	{
		super(entity);
	}
	
	public void writeToNbt(NbtCompound tag, WrapperLookup registryLookup)
	{
		super.writeSheetToNbt(tag, registryLookup);
	}
	
	public void readFromNbt(NbtCompound tag, WrapperLookup registryLookup)
	{
		super.readSheetFromNbt(tag);
	}
}
