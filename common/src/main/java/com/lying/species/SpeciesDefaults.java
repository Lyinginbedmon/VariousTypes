package com.lying.species;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpeciesDefaults
{
	private static final Map<Identifier, Supplier<Species>> DEFAULTS = new HashMap<>();
	
	// Human
	public static final Supplier<Species> HUMAN = register(prefix("human"), () -> Species.Builder.of(prefix("human"))
			.texture(Species.BACKING_DEFAULT)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".human.desc"))
			.setTypes(VTTypes.HUMAN.get(), VTTypes.LINN.get()).build());
	
	// Enderman
	public static final Supplier<Species> CRIOCH = register(prefix("crioch"), () -> Species.Builder.of(prefix("crioch"))
			.texture(Species.BACKING_END_CITY)
			.from(World.END)
			.setTypes(VTTypes.OTHALL.get())
			.addAbility(VTAbilities.TELEPORT.get()).build());
	
	// Piglin
	public static final Supplier<Species> ORKIN	= register(prefix("orkin"), () -> Species.Builder.of(prefix("orkin"))
			.texture(Species.BACKING_BASTION)
			.from(World.NETHER)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.ORKIN.get()).build());
	
	// Lizardfolk
	public static final Supplier<Species> MUCKIE	= register(prefix("muckie"), () -> Species.Builder.of(prefix("muckie"))
			.texture(Species.BACKING_SHIPWRECK)
			.power(1)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.REPTILIAN.get())
			.addAbility(VTAbilities.DEEP_BREATH.get()).build());
	
	private static Supplier<Species> register(Identifier registryName, Supplier<Species> species)
	{
		DEFAULTS.put(registryName, species);
		return species;
	}
	
	public static Collection<Supplier<Species>> defaultSpecies() { return DEFAULTS.values(); }
}