package com.lying.init;

import java.util.function.Supplier;

import com.lying.client.event.RenderEvents;
import com.lying.effect.DazzledStatusEffect;
import com.lying.effect.FatigueStatusEffect;
import com.lying.effect.StealthStatusEffect;
import com.lying.reference.Reference;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class VTStatusEffects
{
	public static final DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.STATUS_EFFECT);
	public static final Registrar<StatusEffect> REGISTRAR = STATUS_EFFECTS.getRegistrar();
	
	public static final DeferredSupplier<StatusEffect> ANCHORED	= register("anchored", () -> new FlagStatusEffect(StatusEffectCategory.HARMFUL, -1));
	public static final DeferredSupplier<StatusEffect> DAZZLED	= register("dazzled", DazzledStatusEffect::new);
	public static final DeferredSupplier<StatusEffect> FATIGUE	= register("fatigue", FatigueStatusEffect::new);
	public static final DeferredSupplier<StatusEffect> STEALTH	= register("stealth", StealthStatusEffect::new);
	
	private static RegistrySupplier<StatusEffect> register(String nameIn, Supplier<StatusEffect> supplierIn)
	{
		return REGISTRAR.register(Reference.ModInfo.prefix(nameIn), supplierIn);
	}
	
	public static void init()
	{
		STATUS_EFFECTS.register();
		registerEffectEventHandling();
	}
	
	public static RegistryEntry<StatusEffect> getEntry(Supplier<StatusEffect> effect)
	{
		return Registries.STATUS_EFFECT.getEntry(effect.get());
	}
	
	private static void registerEffectEventHandling()
	{
		RenderEvents.PLAYER_RENDER_PERMISSION.register((player) -> player.hasStatusEffect(getEntry(STEALTH)) ? EventResult.interruptFalse() : EventResult.pass());
		EntityEvent.LIVING_HURT.register((entity, source, amount) -> 
		{
			RegistryEntry<StatusEffect> stealth = getEntry(STEALTH);
			for(Entity ent : new Entity[] {entity, source.getSource()})
				if(ent != null && ent.isAlive() && ent instanceof LivingEntity && ((LivingEntity)ent).hasStatusEffect(stealth))
					((LivingEntity)ent).removeStatusEffect(stealth);
			
			return EventResult.pass();
		});
	}
	
	private static class FlagStatusEffect extends StatusEffect
	{
		public FlagStatusEffect(StatusEffectCategory category, int color)
		{
			super(category, color);
		}
	}
}
