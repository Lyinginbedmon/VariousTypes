package com.lying.species;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lying.ability.AbilityDietRestriction;
import com.lying.ability.SingleAttributeAbility;
import com.lying.data.VTTags;
import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.LoreDisplay;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SpeciesDefaults
{
	private static final Map<Identifier, Supplier<Species>> DEFAULTS = new HashMap<>();
	
	// Human, default no-impact species
	public static final Supplier<Species> HUMAN = register(prefix("human"), () -> Species.Builder.of(prefix("human"))
			.texture(Species.BACKING_DEFAULT)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".human.desc"))
			.setTypes(VTTypes.HUMAN.get(), VTTypes.LINN.get()).build());
	
	public static final Supplier<Species> CRIOCH = register(prefix("crioch"), () -> Species.Builder.of(prefix("crioch"))
			.texture(Species.BACKING_END_CITY)
			.from(World.END)
			.setTypes(VTTypes.OTHALL.get())
			.addAbility(VTAbilities.TELEPORT.get())
			.addAbility(VTAbilities.PARIAH.get(), nbt -> 
			{
				NbtList list = new NbtList();
				list.add(NbtString.of(Registries.ENTITY_TYPE.getId(EntityType.ENDERMAN).toString()));
				nbt.put("Entities", list);
			}).build());
	
	public static final Supplier<Species> ORKIN	= register(prefix("orkin"), () -> Species.Builder.of(prefix("orkin"))
			.texture(Species.BACKING_BASTION)
			.from(World.NETHER)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.ORKIN.get())
			.addAbility(SingleAttributeAbility.Health.of(-4))
			.addAbility(VTAbilities.GOLDHEARTED.get(), VTAbilities.BERSERK.get()).build());
	
	public static final Supplier<Species> MERROW	= register(prefix("merrow"), () -> Species.Builder.of(prefix("merrow"))
			.texture(Species.BACKING_SHIPWRECK)
			.power(1)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.AQUATIC.get())
			.addAbility(VTAbilities.DEEP_BREATH.get(), VTAbilities.SWIM.get()).build());
	
	public static final Supplier<Species> DRAGAN	= register(prefix("dragan"), () -> Species.Builder.of(prefix("dragan"))
			.texture(Species.BACKING_END_ISLAND)
			.setTypes(VTTypes.DRAGON.get())
			.addAbility(AbilityDietRestriction.ofTags(List.of(ItemTags.MEAT), List.of()).setDisplay(new LoreDisplay(Reference.ModInfo.translate("ability", "carnivore")))).build());
	
	public static final Supplier<Species> VERDINE	= register(prefix("verdine"), () -> Species.Builder.of(prefix("verdine"))
			.texture(Species.BACKING_DEFAULT)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.VERDINE.get())
			.addAbility(AbilityDietRestriction.ofTags(List.of(VTTags.VEGETARIAN), List.of()))
			.addAbility(VTAbilities.PHOTOSYNTH.get()).build());
	
	private static Supplier<Species> register(Identifier registryName, Supplier<Species> species)
	{
		DEFAULTS.put(registryName, species);
		return species;
	}
	
	public static Collection<Supplier<Species>> defaultSpecies() { return DEFAULTS.values(); }
}