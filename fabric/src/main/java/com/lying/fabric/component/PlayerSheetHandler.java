package com.lying.fabric.component;

import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.RespawnableComponent;

import com.lying.component.CharacterSheet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;

public class PlayerSheetHandler extends CharacterSheet implements AutoSyncedComponent, RespawnableComponent<PlayerSheetHandler>
{
	public PlayerSheetHandler(PlayerEntity entity)
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
	
	public boolean shouldCopyForRespawn(boolean lossless, boolean keepInventory, boolean sameCharacter) { return true; }
}
