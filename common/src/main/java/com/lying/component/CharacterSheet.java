package com.lying.component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;
import com.lying.utility.ServerBus;

import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** General data management class for handling species & templates of a specific entity */
public class CharacterSheet
{
	private static final Identifier DEFAULT_SPECIES = Reference.ModInfo.prefix("human");
	private static final RegistryKey<World> DEFAULT_HOME = World.OVERWORLD;
	protected Optional<LivingEntity> owner = Optional.empty();
	
	private TypeSet customTypes = new TypeSet();
	private AbilitySet customAbilities = new AbilitySet();
	
	private RegistryKey<World> home = DEFAULT_HOME;
	
	// Species and templates are stored as IDs so we can retain them even if the datapack changes
	private Identifier speciesID = DEFAULT_SPECIES;
	private List<Identifier> templateIDs = Lists.newArrayList();
	
	// These values represent the information the character is currently working on
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	private ActionHandler actions = ActionHandler.STANDARD_SET.copy();
	
	public CharacterSheet(@Nullable LivingEntity ownerIn)
	{
		if(ownerIn != null)
			owner = Optional.of(ownerIn);
	}
	
	/** Creates a duplicate of this character sheet for the given entity */
	public CharacterSheet copy(@Nullable LivingEntity ownerIn)
	{
		CharacterSheet clone = new CharacterSheet(ownerIn);
		clone.clone(this);
		return clone;
	}
	
	/** Repopulates the values of this sheet with those of the given sheet, without modifying the owner */
	public void clone(CharacterSheet sheet)
	{
		clear(false);
		speciesID = sheet.speciesID;
		templateIDs.addAll(sheet.templateIDs);
		customTypes.addAll(sheet.customTypes);
		sheet.customAbilities.abilities().forEach(inst -> customAbilities.add(inst));
		buildSheet();
		markDirty();
	}
	
	public boolean hasOwner() { return owner.isPresent(); }
	
	public Optional<LivingEntity> getOwner() { return owner; }
	
	public CharacterSheet setOwner(@Nullable LivingEntity entity)
	{
		owner = entity == null ? Optional.empty() : Optional.of(entity);
		return this;
	}
	
	public NbtCompound writeSheetToNbt(NbtCompound compound, WrapperLookup registryOps)
	{
		compound.putString("Home", home.getValue().toString());
		
		if(speciesID != null)
			compound.putString("Species", speciesID.toString());
		
		if(!templateIDs.isEmpty())
		{
			NbtList list = new NbtList();
			templateIDs.forEach(template -> list.add(NbtString.of(template.toString())));
			compound.put("Templates", list);
		}
		
		if(!customTypes.isEmpty())
			compound.put("CustomTypes", customTypes.writeToNbt(registryOps));
		
		if(!customAbilities.isEmpty())
			compound.put("CustomAbilities", customAbilities.writeToNbt(registryOps));
		
		return compound;
	}
	
	public void readSheetFromNbt(NbtCompound compound)
	{
		clear(false);
		home = RegistryKey.of(RegistryKeys.WORLD, new Identifier(compound.getString("Home")));
		
		if(compound.contains("Species", NbtElement.STRING_TYPE))
			speciesID = new Identifier(compound.getString("Species"));
		
		if(compound.contains("Templates", NbtElement.LIST_TYPE))
		{
			NbtList list = compound.getList("Templates", NbtElement.STRING_TYPE);
			for(int i=0; i<list.size(); i++)
			{
				Identifier name = new Identifier(list.getString(i));
				if(!hasTemplate(name))
					templateIDs.add(name);
			}
		}
		
		if(compound.contains("CustomTypes", NbtElement.LIST_TYPE))
			customTypes = TypeSet.readFromNbt(compound.getList("CustomTypes", NbtElement.STRING_TYPE));
		
		if(compound.contains("CustomAbilities", NbtElement.LIST_TYPE))
			customAbilities = AbilitySet.readFromNbt(compound.getList("CustomAbilities", NbtElement.COMPOUND_TYPE));
		
		buildSheet();
	}
	
	public void clear(boolean rebuild)
	{
		home = DEFAULT_HOME;
		speciesID = DEFAULT_SPECIES;
		templateIDs.clear();
		customTypes.clear();
		customAbilities.clear();
		
		if(rebuild)
			buildSheet();
		
		markDirty();
	}
	
	public void setHomeDimension(RegistryKey<World> world)
	{
		if(world == home)
			return;
		
		home = world;
		markDirty();
	}
	
	public RegistryKey<World> homeDimension()
	{
		return (!hasASpecies() || !getSpecies().get().hasConfiguredHome()) ? home : getSpecies().get().homeDimension();
	}
	
	public boolean hasASpecies() { return getSpecies().isPresent(); }
	
	public void setSpecies(@Nullable Identifier registryNameIn)
	{
		if(registryNameIn == speciesID)
			return;
		
		speciesID = registryNameIn == null ? null : registryNameIn;
		buildSheet();
		markDirty();
	}
	
	public Optional<Species> getSpecies() { return VTSpeciesRegistry.get(speciesID); }
	
	public boolean isSpecies(Identifier registryName) { return speciesID == registryName; }
	
	public boolean hasTemplate(@NotNull Template templateIn) { return hasTemplate(templateIn.registryName()); }
	
	public boolean hasTemplate(@NotNull Identifier registryName) { return templateIDs.contains(registryName); }
	
	public void addTemplate(@NotNull Identifier registryName)
	{
		if(hasTemplate(registryName))
			return;
		
		templateIDs.add(registryName);
		VTTemplateRegistry.instance().get(registryName).ifPresent(tem -> tem.applyTypeOperations(types));
		buildAbilities();
		buildActions();
		markDirty();
	}
	
	public void removeTemplate(@NotNull Identifier registryName)
	{
		if(!hasTemplate(registryName))
			return;
		
		templateIDs.remove(registryName);
		buildSheet();
		markDirty();
	}
	
	public void setCustomTypes(TypeSet types)
	{
		customTypes = types;
		buildSheet();
		markDirty();
	}
	
	public List<Template> getAppliedTemplates()
	{
		List<Template> templates = Lists.newArrayList();
		for(Identifier id : templateIDs)
			VTTemplateRegistry.instance().get(id).ifPresent(tem -> templates.add(tem));
		return templates;
	}
	
	public int power()
	{
		int power = 0;
		
		if(hasASpecies())
			power += getSpecies().get().power();
		
		if(!templateIDs.isEmpty())
			for(Template tem : getAppliedTemplates())
				power += tem.power();
		
		return power;
	}
	
	public TypeSet types() { return types.copy(); }
	
	public AbilitySet abilities() { return abilities; }
	
	public ActionHandler actions() { return actions.copy(); }
	
	/** Constructs the types and abilities from scratch */
	public final void buildSheet()
	{
		// Types are calculated first, before abilities, for efficiency-sake
		buildTypes();
		buildAbilities();
		
		if(actions.isDirty())
			buildActions();
	}
	
	/** Constructs the types based on species, custom types, and applied templates */
	public final void buildTypes()
	{
		// Source initial types from species, if any
		TypeSet types = !hasASpecies() ? new TypeSet(VTTypes.HUMAN.get()) : getSpecies().get().types().copy();
		
		// Apply all custom types
		// XXX How should custom types be applied? Replace, merge, overrule all, etc.?
		if(!customTypes.isEmpty())
			types = customTypes.copy();
		
		// Apply templates to types
		for(Template template : getAppliedTemplates())
			template.applyTypeOperations(types);
		
		ServerBus.GET_TYPES_EVENT.invoker().affectTypes(getOwner(), home, types);
		this.types = types.copy();
		this.actions.markDirty();
	}
	
	/** Constructs available abilities based on types, species, applied templates, and custom abilities */
	public final void buildAbilities()
	{
		AbilitySet abilities = !hasASpecies() ? new AbilitySet() : getSpecies().get().abilities().copy();
		
		// Add abilities from types
		types.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		// Apply templates to abilities
		for(Template template : getAppliedTemplates())
			template.applyAbilityOperations(abilities);
		
		// Add all custom abilities
		if(!customAbilities.isEmpty())
			customAbilities.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		// Rebuild actions, including what fluids are breathable
		this.abilities = abilities;
		this.actions.markDirty();
		
		this.onAbilitiesRebuilt();
	}
	
	protected void onAbilitiesRebuilt() { }
	
	/** Constructs enabled actions based on types and abilities, incl. determining what fluids are breathable */
	public void buildActions()
	{
		actions.clear();
		types.contents().forEach(type -> type.actions().stack(actions, types));
		
		for(Ability ability : new Ability[] {VTAbilities.BREATHE_FLUID.get(), VTAbilities.SUFFOCATE_FLUID.get()})
			this.abilities.getAbilitiesOfType(ability.registryName()).forEach(inst -> ((AbilityBreathing)ability).applyToActions(actions, inst));
		
		ServerBus.AFTER_REBUILD_ACTIONS_EVENT.invoker().affectActions(actions, abilities, getOwner());
	}
	
	public boolean hasAction(Action action) { return actions.can(action); }
	
	public boolean isAbleToBreathe(Fluid fluid, boolean hasWaterBreathing) { return hasWaterBreathing || actions.canBreathe(fluid); }
	
	public boolean addCustomAbility(Ability ability)
	{
		return addCustomAbility(ability, Consumers.nop());
	}
	
	public boolean addCustomAbility(Ability ability, Consumer<NbtCompound> dataModifier)
	{
		if(customAbilities.add(ability.instance(AbilitySource.CUSTOM, dataModifier)))
		{
			markDirty();
			return true;
		}
		return false;
	}
	
	public void markDirty()
	{
		if(hasOwner())
			VariousTypes.setSheet(getOwner().get(), this);
	}
}