package com.lying.neoforge.component;

import org.jetbrains.annotations.UnknownNullability;

import com.lying.component.CharacterSheet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class PlayerSheetHandler extends CharacterSheet implements INBTSerializable<NbtCompound>
{
	public PlayerSheetHandler()
	{
		super(null);
	}
	
	public @UnknownNullability NbtCompound serializeNBT(WrapperLookup provider)
	{
		return super.writeSheetToNbt(new NbtCompound());
	}
	
	public void deserializeNBT(WrapperLookup provider, NbtCompound nbt)
	{
		super.readSheetFromNbt(nbt);
	}
}
