package com.lying.fabric;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.fabric.component.VTComponents;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;

public final class VariousTypesFabric implements ModInitializer
{
	public void onInitialize()
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		// Run our common setup.
		VariousTypes.commonInit();
		
		// Populate helper functions
		VariousTypes.getSheetFunc(ent -> ent.getType() != EntityType.PLAYER ? Optional.empty() : Optional.of(VTComponents.CHARACTER_SHEET.get(ent)));
		VariousTypes.setSheetFunc((ent,sheet) -> VariousTypes.getSheet(ent).ifPresent(sheet2 -> sheet2.clone(sheet)));
	}
}
