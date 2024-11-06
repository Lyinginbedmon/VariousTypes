package com.lying.init;

import java.util.function.Supplier;

import com.lying.effect.DazzledStatusEffect;
import com.lying.effect.FatigueStatusEffect;
import com.lying.effect.StealthStatusEffect;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class VTStatusEffects
{
	public static final DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.STATUS_EFFECT);
	public static final Registrar<StatusEffect> REGISTRAR = STATUS_EFFECTS.getRegistrar();
	
	public static final DeferredSupplier<StatusEffect> FATIGUE	= register("fatigue", FatigueStatusEffect::new);
	public static final DeferredSupplier<StatusEffect> DAZZLED	= register("dazzled", DazzledStatusEffect::new);
	public static final DeferredSupplier<StatusEffect> ANCHORED	= register("anchored", () -> new FlagStatusEffect(StatusEffectCategory.HARMFUL, -1));
	public static final DeferredSupplier<StatusEffect> STEALTH	= register("stealth", StealthStatusEffect::new);
	
	private static RegistrySupplier<StatusEffect> register(String nameIn, Supplier<StatusEffect> supplierIn)
	{
		return REGISTRAR.register(Reference.ModInfo.prefix(nameIn), supplierIn);
	}
	
	public static void init()
	{
		STATUS_EFFECTS.register();
	}
	
	public static RegistryEntry<StatusEffect> getEntry(Supplier<StatusEffect> effect)
	{
		return Registries.STATUS_EFFECT.getEntry(effect.get());
	}
	
	private static class FlagStatusEffect extends StatusEffect
	{
		public FlagStatusEffect(StatusEffectCategory category, int color)
		{
			super(category, color);
		}
	}
}
