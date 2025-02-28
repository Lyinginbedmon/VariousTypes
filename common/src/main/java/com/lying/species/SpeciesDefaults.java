package com.lying.species;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.lying.ability.AbilityCosmetics;
import com.lying.ability.AbilityDietRestriction;
import com.lying.ability.SingleAttributeAbility;
import com.lying.data.VTTags;
import com.lying.init.VTAbilities;
import com.lying.init.VTCosmetics;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.utility.LootBag;
import com.lying.utility.LoreDisplay;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
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
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".crioch.desc"))
			.from(World.END)
			.setTypes(VTTypes.OTHALL.get())
			.addAbility(AbilityCosmetics.of(false, VTCosmetics.EARS_GOBLIN.get(), VTCosmetics.TAIL_RAT.get()).withMapName(prefix("goblin_appearance")))
			.addAbility(VTAbilities.TELEPORT.get())
			.addAbility(VTAbilities.PARIAH.get(), nbt -> 
			{
				NbtList list = new NbtList();
				list.add(NbtString.of(Registries.ENTITY_TYPE.getId(EntityType.ENDERMAN).toString()));
				nbt.put("Entities", list);
			}).build());
	
	@SuppressWarnings("rawtypes")
	public static final Supplier<Species> ORKIN	= register(prefix("orkin"), () -> Species.Builder.of(prefix("orkin"))
			.texture(Species.BACKING_BASTION)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".orkin.desc"))
			.from(World.NETHER)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.ORKIN.get())
			.addAbility(AbilityCosmetics.of(false, VTCosmetics.EARS_PIGLIN.get(), VTCosmetics.NOSE_PIGLIN.get()).withMapName(prefix("orkin_appearance")))
			.addAbility(SingleAttributeAbility.Health.of(-4))
			.addAbility(VTAbilities.GOLDHEARTED.get(), VTAbilities.BERSERK.get())
			.startingLoot(LootBag.ofItems(Items.CROSSBOW, Items.GOLDEN_SWORD).withCustomTable(LootTable.builder().pool(LootPool.builder().rolls(UniformLootNumberProvider.create(1.0f, 2.0f)).with((LootPoolEntry.Builder<?>)((Object)((LeafEntry.Builder)ItemEntry.builder(Items.ARROW).weight(30)).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0f, 7.0f)))))).build())).build());
	
	public static final Supplier<Species> MERROW	= register(prefix("merrow"), () -> Species.Builder.of(prefix("merrow"))
			.texture(Species.BACKING_SHIPWRECK)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".merrow.desc"))
			.power(1)
			.setTypes(VTTypes.HUMAN.get(), VTTypes.AQUATIC.get())
			.addAbility(AbilityCosmetics.of(false, VTCosmetics.EARS_AXOLOTL.get()).withMapName(prefix("merfolk_appearance")))
			.addAbility(VTAbilities.DEEP_BREATH.get(), VTAbilities.SWIM.get()).build());
	
	public static final Supplier<Species> DRAGAN	= register(prefix("dragan"), () -> Species.Builder.of(prefix("dragan"))
			.texture(Species.BACKING_END_ISLAND)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".dragan.desc"))
			.power(3)
			.setTypes(VTTypes.DRAGON.get())
			.addAbility(AbilityCosmetics.of(false, VTCosmetics.HORNS_HARTEBEEST.get(), VTCosmetics.TAIL_DRAGON.get()).withMapName(prefix("draconic_appearance")))
			.addAbility(SingleAttributeAbility.Scale.of(0.1F))
			.addAbility(SingleAttributeAbility.Damage.of(2))
			.addAbility(AbilityDietRestriction.ofTags(List.of(ItemTags.MEAT), List.of()).setDisplay(new LoreDisplay(Reference.ModInfo.translate("ability", "carnivore")))).build());
	
	public static final Supplier<Species> VERDINE	= register(prefix("verdine"), () -> Species.Builder.of(prefix("verdine"))
			.texture(Species.BACKING_DEFAULT)
			.description(Text.translatable("species."+Reference.ModInfo.MOD_ID+".verdine.desc"))
			.setTypes(VTTypes.HUMAN.get(), VTTypes.VERDINE.get())
			.addAbility(AbilityCosmetics.of(false, VTCosmetics.EARS_ELF.get(), VTCosmetics.MISC_GLOW_SPOTS.get()).withMapName(prefix("fae_appearance")))
			.addAbility(AbilityDietRestriction.ofTags(List.of(VTTags.VEGETARIAN), List.of()))
			.addAbility(VTAbilities.PHOTOSYNTH.get()).build());
	
	private static Supplier<Species> register(Identifier registryName, Supplier<Species> species)
	{
		DEFAULTS.put(registryName, species);
		return species;
	}
	
	public static Collection<Supplier<Species>> defaultSpecies() { return DEFAULTS.values(); }
}