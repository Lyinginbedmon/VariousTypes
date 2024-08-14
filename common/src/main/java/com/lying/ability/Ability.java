package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

/** A distinct gameplay-modifying property */
public class Ability
{
	public static final Codec<Ability> CODEC = Identifier.CODEC.comapFlatMap(id -> 
			{
				Ability ability = VTAbilities.get(id);
				if(ability != null)
					return DataResult.success(ability);
				else
					return DataResult.error(() -> "Not a registered ability: '"+String.valueOf(id) + "'");
			}, Ability::registryName).stable();
	
	private final Identifier registryName;
	private final Category category;
	
	private final Map<RegistryEntry<EntityAttribute>, Function<AbilityInstance,EntityAttributeModifier>> attributeModifiers = new HashMap<>();
	private boolean attributesLoaded = false;
	
	public Ability(Identifier regName, Category catIn)
	{
		registryName = regName;
		category = catIn;
	}
	
	/** Returns a collection of all abilities of the given class from all sources in the owner's character sheet */
	public static Collection<AbilityInstance> getAllOf(Class<?> classIn, LivingEntity owner)
	{
		Map<Identifier, AbilityInstance> abilityMap = new HashMap<>();
		
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			// Collect all passive abilities from main ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ABILITES).getAbilitiesOfClass(classIn).stream().
				filter(inst -> inst.ability().type() == AbilityType.PASSIVE).
					forEach(inst -> abilityMap.put(inst.mapName(), inst));
			
			// Collect all activated abilities from actionable ability set
			sheet.<AbilitySet>elementValue(VTSheetElements.ACTIONABLES).getAbilitiesOfClass(classIn).
					forEach(inst -> abilityMap.put(inst.mapName(), inst));
		});
		
		return abilityMap.isEmpty() ? Lists.newArrayList() : abilityMap.values();
	}
	
	/** Returns true if this ability can be given a custom map name in its instance memory */
	protected boolean remappable() { return false; }
	
	/** Returns what type of ability this is */
	public AbilityType type() { return AbilityType.PASSIVE; }
	
	public Category category() { return category; }
	
	public Identifier iconTexture() { return category.icon(); }
	
	public final AbilityInstance instance() { return instance(AbilitySource.MISC); }
	
	/** Returns a blank instance of this ability from the given source */
	public final AbilityInstance instance(AbilitySource source) { return new AbilityInstance(this, source); }
	
	/** Returns an instance of this ability from the given source, with modified memory data */
	public final AbilityInstance instance(AbilitySource source, Consumer<NbtCompound> dataModifier) { return new AbilityInstance(this, source, dataModifier); }
	
	/** The unique registry name of abilities of this type */
	public Identifier registryName() { return registryName; }
	
	public Identifier nameInAbilityMap(AbilityInstance instance)
	{
		if(remappable() && instance.memory().contains("MapName", NbtElement.STRING_TYPE))
			return new Identifier(instance.memory().getString("MapName"));
		
		return mapName(instance);
	}
	
	protected Identifier mapName(AbilityInstance instance) { return registryName(); }
	
	public boolean isHidden(AbilityInstance instance) { return false; }
	
	/** Sets the initial values of any necessary memory values */
	protected NbtCompound initialiseNBT(NbtCompound data) { return data; }
	
	public Text displayName(AbilityInstance instance) { return translate("ability",registryName.getPath()); }
	
	public Optional<Text> description(AbilityInstance instance) { return Optional.of(translate("ability", registryName.getPath()+".desc")); }
	
	/** Registers any event handlers needed by this ability to operate. Called during initialisation. */
	public void registerEventHandlers() { }
	
	/** Returns a collection of ability instances imparted by having this ability */
	public Collection<AbilityInstance> getSubAbilities(AbilityInstance instance) { return Collections.emptyList(); };
	
	public final void generateModifiers()
	{
		if(attributesLoaded) return;
		
		attributeModifiers.clear();
		generateAttributeModifiers();
		
		attributesLoaded = true;
	}
	
	protected void generateAttributeModifiers() { }
	
	protected final void addAttributeModifier(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier modifier) { attributeModifiers.put(attribute, (inst) -> modifier); }
	protected final void addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Function<AbilityInstance,EntityAttributeModifier> func) { attributeModifiers.put(attribute, func); }
	
	public final void applyAttributeModifiers(LivingEntity living, AbilityInstance instance)
	{
		generateAttributeModifiers();
		attributeModifiers.entrySet().forEach(entry -> 
		{
			EntityAttributeModifier modifier = entry.getValue().apply(instance);
			EntityAttributeInstance attribute = living.getAttributeInstance(entry.getKey());
			if(attribute.getModifier(modifier.uuid()) == null)
			{
				attribute.addPersistentModifier(modifier);
			}
		});
	}
	
	public final void removeAttributeModifiers(LivingEntity living, AbilityInstance instance)
	{
		generateAttributeModifiers();
		attributeModifiers.entrySet().forEach(entry -> living.getAttributeInstance(entry.getKey()).removeModifier(entry.getValue().apply(instance).uuid()));
	}
	
	public static enum AbilityType
	{
		PASSIVE(Integer.MAX_VALUE),
		ACTIVATED(0),
		TOGGLED(1);
		
		public final int displayOrder;
		
		private AbilityType(int orderIn)
		{
			displayOrder = orderIn;
		}
		
		public Text translate() { return Reference.ModInfo.translate("gui", "ability_"+name().toLowerCase()); }
		
		public static int compare(AbilityType a, AbilityType b) { return (int)Math.signum(a.displayOrder - b.displayOrder); }
	}
	
	/**
	 * Where a specific ability instance originates<br>
	 * This determines which ability is retained if two or more share the same map name
	 */
	public static enum AbilitySource implements StringIdentifiable
	{
		MISC(Integer.MAX_VALUE),
		TYPE(4),
		SPECIES(3),
		TEMPLATE(2),
		CUSTOM(-1);
		
		@SuppressWarnings("deprecation")
		public static final StringIdentifiable.EnumCodec<AbilitySource> CODEC = StringIdentifiable.createCodec(AbilitySource::values);
		
		private final int priority;
		
		private AbilitySource(int priorityIn)
		{
			priority = priorityIn;
		}
		
		public String asString() { return name().toLowerCase(); }
		
		public static AbilitySource fromName(String name)
		{
			for(AbilitySource source : values())
				if(source.asString().equalsIgnoreCase(name))
					return source;
			return MISC;
		}
		
		public boolean overrules(AbilitySource other) { return other.priority >= priority; }
	}
	
	public static enum Category
	{
		OFFENSE,
		DEFENSE,
		UTILITY;
		
		public Identifier icon() { return Reference.ModInfo.prefix("textures/gui/ability_"+name().toLowerCase()+".png"); }
	}
}