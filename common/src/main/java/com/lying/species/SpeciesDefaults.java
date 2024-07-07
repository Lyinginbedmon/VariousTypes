package com.lying.species;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference.ModInfo;

import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpeciesDefaults
{
	private static final Map<Identifier, Supplier<Species>> DEFAULTS = new HashMap<>();
	
	public static final Supplier<Species> HUMAN = register(prefix("human"), () -> Species.Builder.of(prefix("human"))
			.setTypes(VTTypes.HUMAN.get(), VTTypes.LINN.get()).build());
	
	// Enderman
	public static final Supplier<Species> CRIOCH = register(prefix("crioch"), () -> Species.Builder.of(prefix("crioch"))
			.from(World.END)
			.setTypes(VTTypes.OTHALL.get())
			.addAbility(VTAbilities.TELEPORT.get()).build());
	
	// Piglin
	public static final Supplier<Species> ORKIN	= register(prefix("orkin"), () -> Species.Builder.of(prefix("orkin"))
			.from(World.NETHER)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.ORKIN.get()).build());
	
	// Lizardfolk
	public static final Supplier<Species> MUCKIE	= register(prefix("muckie"), () -> Species.Builder.of(prefix("muckie"))
			.power(1)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.REPTILIAN.get()).build());
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	private static Supplier<Species> register(Identifier registryName, Supplier<Species> species)
	{
		DEFAULTS.put(registryName, species);
		return species;
	}
	
	public static Collection<Supplier<Species>> defaultSpecies() { return DEFAULTS.values(); }
}