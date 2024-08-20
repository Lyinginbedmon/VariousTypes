package com.lying.init;

import java.util.function.Supplier;

import com.lying.entity.AnimatedPlayerEntity;
import com.lying.entity.ShakenBlockEntity;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public class VTEntityTypes
{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.ENTITY_TYPE);

	public static final RegistrySupplier<EntityType<AnimatedPlayerEntity>> ANIMATED_PLAYER	= register("animated_player", () -> 
	{
		EntityType.Builder<AnimatedPlayerEntity> builder = EntityType.Builder.<AnimatedPlayerEntity>create(AnimatedPlayerEntity::new, SpawnGroup.MISC).dimensions(0.6F, 1.8F);
		return builder.build("animated_player");
	});
	
	public static final RegistrySupplier<EntityType<ShakenBlockEntity>> SHAKEN_BLOCK	= register("shaken_block", () -> 
	{
		EntityType.Builder<ShakenBlockEntity> builder = EntityType.Builder.<ShakenBlockEntity>create(ShakenBlockEntity::new, SpawnGroup.MISC).dimensions(1F, 1F);
		return builder.build("shaken_block");
	});
	
	private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, Supplier<EntityType<T>> entry)
	{
		return ENTITY_TYPES.register(Reference.ModInfo.prefix(name), entry);
	}
	
	public static void init()
	{
		ENTITY_TYPES.register();
	}
}