package com.lying.init;

import java.util.function.Supplier;

import com.lying.VariousTypes;
import com.lying.entity.AnimatedPlayerEntity;
import com.lying.entity.EmitterEntity;
import com.lying.entity.PortalEntity;
import com.lying.entity.ShakenBlockEntity;
import com.lying.entity.SmokeCloudEntity;
import com.lying.entity.ThrownBlockEntity;
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
	private static int tally;
	
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
	
	public static final RegistrySupplier<EntityType<ThrownBlockEntity>> THROWN_BLOCK	= register("thrown_block", () -> 
	{
		EntityType.Builder<ThrownBlockEntity> builder = EntityType.Builder.<ThrownBlockEntity>create(ThrownBlockEntity::new, SpawnGroup.MISC).dimensions(1F, 1F);
		return builder.build("thrown_block");
	});
	
	public static final RegistrySupplier<EntityType<EmitterEntity>> EMITTER				= register("emitter", () -> 
	{
		EntityType.Builder<EmitterEntity> builder = EntityType.Builder.create(EmitterEntity::new, SpawnGroup.MISC).dimensions(0.1F, 0.1F);
		return builder.build("emitter");
	});
	
	public static final RegistrySupplier<EntityType<SmokeCloudEntity>> THICK_SMOKE_CLOUD	= register("thick_smoke_cloud", () -> 
	{
		EntityType.Builder<SmokeCloudEntity> builder = EntityType.Builder.create(SmokeCloudEntity::new, SpawnGroup.MISC).dimensions(1F, 1F);
		return builder.build("thick_smoke_cloud");
	});
	
	public static final RegistrySupplier<EntityType<PortalEntity>> PORTAL			= register("portal", () -> 
	{
		EntityType.Builder<PortalEntity> builder = EntityType.Builder.create(PortalEntity::new, SpawnGroup.MISC).dimensions(1F, 2F);
		return builder.build("portal");
	});
	
	private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, Supplier<EntityType<T>> entry)
	{
		tally++;
		return ENTITY_TYPES.register(Reference.ModInfo.prefix(name), entry);
	}
	
	public static void init()
	{
		ENTITY_TYPES.register();
		VariousTypes.LOGGER.info(" # Registered {} entity types", tally);
	}
}