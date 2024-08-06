package com.lying.fabric;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.fabric.component.VTComponents;
import com.lying.utility.XPlatHandler;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

public final class VariousTypesFabric implements ModInitializer
{
	public void onInitialize()
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		// Run our common setup.
		VariousTypes.commonInit();
		
		VariousTypes.setPlatHandler(new XPlatHandler() 
		{
			public Optional<CharacterSheet> getSheet(@NotNull LivingEntity entity)
			{
				return
						entity.getType() != EntityType.PLAYER ?
							Optional.empty() :
							Optional.of(VTComponents.CHARACTER_SHEET.get(entity));
			}
			
			public void setSheet(LivingEntity entity, CharacterSheet sheet)
			{
				if(entity.getType() == EntityType.PLAYER)
					VTComponents.CHARACTER_SHEET.sync(entity);
			}
		});
	}
}
