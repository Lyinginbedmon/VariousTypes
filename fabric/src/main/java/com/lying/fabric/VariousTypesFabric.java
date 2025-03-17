package com.lying.fabric;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.fabric.component.VTComponents;
import com.lying.init.VTEntityTypes;
import com.lying.utility.XPlatHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public final class VariousTypesFabric implements ModInitializer
{
	public void onInitialize()
	{
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		
		// Run our common setup.
		VariousTypes.commonInit();
    	
    	FabricDefaultAttributeRegistry.register(VTEntityTypes.ANIMATED_PLAYER.get(), PlayerEntity.createPlayerAttributes());
		
		VariousTypes.setPlatHandler(new XPlatHandler() 
		{
			public Optional<CharacterSheet> getSheet(@NotNull LivingEntity entity)
			{
				if(!VariousTypes.SHEETED_ENTITIES.get().contains(entity.getType()))
					return Optional.empty();
				else if(entity.getType() == EntityType.PLAYER)
					return Optional.of(VTComponents.CHARACTER_SHEET_PLAYER.get(entity));
				else if(entity.getType() == VTEntityTypes.ANIMATED_PLAYER.get())
					return Optional.of(VTComponents.CHARACTER_SHEET_MOB.get(entity));
				return Optional.empty();
			}
			
			public void setSheet(LivingEntity entity, CharacterSheet sheet)
			{
				if(VariousTypes.SHEETED_ENTITIES.get().contains(entity.getType()))
					if(entity.getType() == EntityType.PLAYER)
						VTComponents.CHARACTER_SHEET_PLAYER.sync(entity);
					else
						getSheet(entity).ifPresent(sheetB -> sheetB.readSheetFromNbt(sheet.writeSheetToNbt(new NbtCompound())));
			}
		});
	}
}
