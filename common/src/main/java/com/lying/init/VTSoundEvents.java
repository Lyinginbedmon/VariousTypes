package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import com.lying.VariousTypes;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class VTSoundEvents
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS 	= DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.SOUND_EVENT);
	private static int tally;
	
	public static final RegistrySupplier<SoundEvent> SHEET_RANDOMISE	= register("randomise_sheet");
	
	public static final RegistrySupplier<SoundEvent> ABILITY_ACTIVATED	= register("ability_activated");
	public static final RegistrySupplier<SoundEvent> ABILITY_TOGGLE_ON	= register("ability_toggle_on");
	public static final RegistrySupplier<SoundEvent> ABILITY_TOGGLE_OFF	= register("ability_toggle_off");
	
	public static final RegistrySupplier<SoundEvent> PORTAL_CRACKLE		= register("portal_crackle");
	public static final RegistrySupplier<SoundEvent> PORTAL_OPEN		= register("portal_open");
	public static final RegistrySupplier<SoundEvent> PORTAL_CLOSE		= register("portal_close");
	
	public static final RegistrySupplier<SoundEvent> BERSERK_ACTIVATE	= register("berserk_activated");
	public static final RegistrySupplier<SoundEvent> FAST_HEAL			= register("fast_heal");
	public static final RegistrySupplier<SoundEvent> PHOTOSYNTH_HEAL	= register("photosynth_heal");
	public static final RegistrySupplier<SoundEvent> QUAKE_ACTIVATE		= register("quake_activated");
	public static final RegistrySupplier<SoundEvent> QUAKE_IMPACT		= register("quake_impact");
	
	private static RegistrySupplier<SoundEvent> register(String name) { return register(prefix(name)); }
	
	private static RegistrySupplier<SoundEvent> register(Identifier name)
	{
		tally++;
		return SOUND_EVENTS.register(name, () -> SoundEvent.of(name));
	}
	
	public static void init()
	{
		SOUND_EVENTS.register();
		VariousTypes.LOGGER.info(" # Registered {} custom sounds", tally);
	}
}
