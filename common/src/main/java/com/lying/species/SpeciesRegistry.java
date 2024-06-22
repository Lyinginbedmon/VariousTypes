package com.lying.species;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference.ModInfo;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpeciesRegistry
{
	private static final Map<Identifier, Species> SPECIES = new HashMap<>();
	
	private static final Map<Identifier, Supplier<Species>> DEFAULTS = new HashMap<>();
	
	public static final Supplier<Species> HUMAN = register(prefix("human"), () -> Species.Builder.of(prefix("human"))
			.setTypes(VTTypes.HUMAN.get(), VTTypes.LINN).build());
	
	// Enderman
	public static final Supplier<Species> CRIOCH = register(prefix("crioch"), () -> Species.Builder.of(prefix("crioch"))
			.from(World.END)
			.setTypes(VTTypes.OTHALL.get())
			.addAbility(VTAbilities.TELEPORT.get()).build());
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	private static Supplier<Species> register(Identifier registryName, Supplier<Species> species)
	{
		DEFAULTS.put(registryName, species);
		return species;
	}
	
	@Nullable
	public static Species get(Identifier registryName)
	{
		return SPECIES.getOrDefault(registryName, HUMAN.get());
	}
}