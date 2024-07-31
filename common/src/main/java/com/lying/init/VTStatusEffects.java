package com.lying.init;

import java.util.function.Supplier;

import com.lying.effect.FatigueStatusEffect;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.RegistryKeys;

public class VTStatusEffects
{
	public static final DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.STATUS_EFFECT);
	public static final Registrar<StatusEffect> REGISTRAR = STATUS_EFFECTS.getRegistrar();
	
	public static final RegistrySupplier<StatusEffect> FATIGUE	= register("fatigue", FatigueStatusEffect::new);
	
	private static RegistrySupplier<StatusEffect> register(String nameIn, Supplier<StatusEffect> supplierIn)
	{
		return REGISTRAR.register(Reference.ModInfo.prefix(nameIn), supplierIn);
	}
	
	public static void init()
	{
		STATUS_EFFECTS.register();
	}
}
