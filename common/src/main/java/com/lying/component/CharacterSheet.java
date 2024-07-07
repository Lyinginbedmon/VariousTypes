package com.lying.component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilitySet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
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
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CharacterSheet
{
	private final LivingEntity owner;
	
	private TypeSet customTypes = new TypeSet();
	private AbilitySet customAbilities = new AbilitySet();
	
	private RegistryKey<World> home = World.OVERWORLD;
	
	// Species and templates are stored as IDs so we can retain them even if the datapack changes
	private Identifier speciesID = Reference.ModInfo.prefix("human");
	private List<Identifier> templateIDs = Lists.newArrayList();
	
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	private ActionHandler actions = ActionHandler.STANDARD_SET.copy();
	
	public CharacterSheet(LivingEntity ownerIn)
	{
		owner = ownerIn;
	}
	
	public CharacterSheet copy(LivingEntity ownerIn)
	{
		CharacterSheet clone = new CharacterSheet(ownerIn);
		clone.readFromNbt(this.writeToNbt(new NbtCompound()));
		return clone;
	}
	
	public NbtCompound writeToNbt(NbtCompound compound)
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
			compound.put("CustomTypes", customTypes.writeToNbt(owner.getWorld().getRegistryManager()));
		
		if(!customAbilities.isEmpty())
			compound.put("CustomAbilities", customAbilities.writeToNbt(owner.getRegistryManager()));
		
		return compound;
	}
	
	public void readFromNbt(NbtCompound compound)
	{
		home = RegistryKey.of(RegistryKeys.WORLD, new Identifier(compound.getString("Home")));
		
		if(compound.contains("Species", NbtElement.STRING_TYPE))
			speciesID = new Identifier(compound.getString("Species"));
		
		templateIDs.clear();
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
	
	public void setHomeDimension(RegistryKey<World> world) { home = world; }
	
	public RegistryKey<World> getHome()
	{
		return !hasSpecies() || !getSpecies().get().hasConfiguredHome() ? home : getSpecies().get().homeDimension();
	}
	
	public void setSpecies(@Nullable Identifier registryNameIn)
	{
		speciesID = registryNameIn == null ? null : registryNameIn;
		buildSheet();
	}
	
	public boolean hasSpecies() { return getSpecies().isPresent(); }
	
	public Optional<Species> getSpecies() { return VTSpeciesRegistry.get(speciesID); }
	
	public boolean hasTemplate(@NotNull Template templateIn) { return hasTemplate(templateIn.registryName()); }
	
	public boolean hasTemplate(@NotNull Identifier registryName) { return templateIDs.contains(registryName); }
	
	public void addTemplate(@NotNull Identifier registryName)
	{
		if(hasTemplate(registryName))
			return;
		
		templateIDs.add(registryName);
		// templateIn.applyTypeOperations(types);
		buildAbilities();
		buildActions();
	}
	
	public void removeTemplate(@NotNull Identifier registryName)
	{
		if(!hasTemplate(registryName))
			return;
		
		templateIDs.remove(registryName);
		buildSheet();
	}
	
	public void setCustomTypes(TypeSet types)
	{
		customTypes = types;
		buildSheet();
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
		
		if(hasSpecies())
			power += getSpecies().get().power();
		
		if(!templateIDs.isEmpty())
			for(Template tem : getAppliedTemplates())
				power += tem.power();
		
		return power;
	}
	
	public TypeSet getTypes() { return types.copy(); }
	
	public AbilitySet getAbilities() { return abilities; }
	
	/** Constructs the types and abilities from scratch */
	public void buildSheet()
	{
		// Types are calculated first, before abilities, for efficiency-sake
		buildTypes();
		buildAbilities();
		
		if(actions.isDirty())
			buildActions();
	}
	
	/** Constructs the types based on species, custom types, and applied templates */
	public void buildTypes()
	{
		// Source initial types from species, if any
		TypeSet types = !hasSpecies() ? new TypeSet() : getSpecies().get().types().copy();
		
		// Apply all custom types
		// XXX How should custom types be applied? Replace, merge, overrule all, etc.?
		if(!customTypes.isEmpty())
			types = customTypes.copy();
		
		// Apply templates to types
		for(Template template : getAppliedTemplates())
			template.applyTypeOperations(types);
		
		ServerBus.GET_TYPES_EVENT.invoker().affectTypes(owner, home, types);
		this.types = types;
		this.actions.markDirty();
	}
	
	
	/** Constructs available abilities based on types, species, applied templates, and custom abilities */
	public void buildAbilities()
	{
		AbilitySet abilities = !hasSpecies() ? new AbilitySet() : getSpecies().get().abilities().copy();
		
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
	}
	
	/** Constructs enabled actions based on types and abilities, incl. determining what fluids are breathable */
	public void buildActions()
	{
		actions.clear();
		types.contents().forEach(type -> type.actions().stack(actions, this.types));
		
		for(Ability ability : new Ability[] {VTAbilities.BREATHE_FLUID.get(), VTAbilities.SUFFOCATE_FLUID.get()})
			this.abilities.getAbilitiesOfType(ability.registryName()).forEach(inst -> ((AbilityBreathing)ability).applyToActions(actions, inst));
		
		ServerBus.AFTER_REBUILD_ACTIONS_EVENT.invoker().affectActions(actions, abilities, owner);
	}
	
	public ActionHandler getActions() { return actions; }
	
	public boolean hasAction(Action action) { return actions.can(action); }
	
	public boolean isAbleToBreathe(Fluid fluid, boolean hasWaterBreathing) { return hasWaterBreathing || actions.canBreathe(fluid); }
	
	public boolean addCustomAbility(Ability ability)
	{
		return addCustomAbility(ability, Consumers.nop());
	}
	
	public boolean addCustomAbility(Ability ability, Consumer<NbtCompound> dataModifier)
	{
		return customAbilities.add(ability.instance(AbilitySource.CUSTOM, dataModifier));
	}
}