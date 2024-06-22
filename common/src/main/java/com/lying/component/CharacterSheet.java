package com.lying.component;

import java.util.List;
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
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.species.SpeciesRegistry;
import com.lying.template.Template;
import com.lying.template.TemplateRegistry;
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
	private Species species = SpeciesRegistry.get(Reference.ModInfo.prefix("human"));
	private List<Template> templates = Lists.newArrayList();
	
	private TypeSet types = new TypeSet();
	private AbilitySet abilities;
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
		
		if(species != null)
			compound.putString("Species", species.registryName().toString());
		
		if(!templates.isEmpty())
		{
			NbtList list = new NbtList();
			templates.forEach(template -> list.add(NbtString.of(template.registryName().toString())));
			compound.put("Templates", list);
		}
		
		if(!customTypes.isEmpty())
			compound.put("CustomTypes", customTypes.writeToNbt(owner.getWorld().getRegistryManager()));
		
		if(!customAbilities.isEmpty())
			compound.put("CustomAbilities", customAbilities.writeToNbt());
		
		return compound;
	}
	
	public void readFromNbt(NbtCompound compound)
	{
		home = RegistryKey.of(RegistryKeys.WORLD, new Identifier(compound.getString("Home")));
		
		if(compound.contains("Species", NbtElement.STRING_TYPE))
			species = SpeciesRegistry.get(new Identifier(compound.getString("Species")));
		
		if(compound.contains("Templates", NbtElement.LIST_TYPE))
		{
			NbtList list = compound.getList("Templates", NbtElement.STRING_TYPE);
			for(int i=0; i<list.size(); i++)
			{
				Template template = TemplateRegistry.get(new Identifier(list.getString(i)));
				if(template != null)
					templates.add(template);
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
		return species == null ? home : species.homeDimension();
	}
	
	public void setSpecies(@Nullable Species speciesIn)
	{
		species = speciesIn;
		buildSheet();
	}
	
	public boolean hasTemplate(@NotNull Template templateIn) { return templates.stream().anyMatch(temp -> temp.registryName().equals(templateIn.registryName())); }
	
	public void addTemplate(@NotNull Template templateIn)
	{
		if(hasTemplate(templateIn))
			return;
		
		templates.add(templateIn);
		templateIn.applyTypeOperations(types);
		buildAbilities();
	}
	
	public void removeTemplate(@NotNull Template templateIn)
	{
		if(!hasTemplate(templateIn))
			return;
		
		templates.remove(templateIn);
		buildSheet();
	}
	
	public void setCustomTypes(TypeSet types)
	{
		customTypes = types;
		buildSheet();
	}
	
	public TypeSet getTypes() { return types.copy(); }
	
	public AbilitySet getAbilities() { return abilities; }
	
	public List<Template> getAppliedTemplates() { return templates; }
	
	/** Constructs the types and abilities from scratch */
	public void buildSheet()
	{
		// Types are calculated first for efficiency-sake
		buildTypes();
		buildAbilities();
		buildActions();
	}
	
	public void buildTypes()
	{
		TypeSet types = species == null ? new TypeSet() : species.types().copy();
		
		// Apply all custom types
		// XXX How should custom types be applied? Replace, merge, overrule all, etc.?
		if(!customTypes.isEmpty())
			types = customTypes.copy();
		
		// Apply templates to types
		for(Template template : templates)
			template.applyTypeOperations(types);
		
		ServerBus.GET_TYPES_EVENT.invoker().affectTypes(owner, home, types);
		this.types = types;
		buildActions();
	}
	
	public void buildAbilities()
	{
		AbilitySet abilities = species == null ? new AbilitySet() : species.abilities().copy();
		
		// Add abilities from types
		types.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		// Apply templates to abilities
		for(Template template : templates)
			template.applyAbilityOperations(abilities);
		
		// Add all custom abilities
		if(!customAbilities.isEmpty())
			customAbilities.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		// Rebuild actions, including what fluids are breathable
		this.abilities = abilities;
		buildActions();
		for(Ability ability : new Ability[] {VTAbilities.BREATHE_FLUID.get(), VTAbilities.SUFFOCATE_FLUID.get()})
			this.abilities.getAbilitiesOfType(ability.registryName()).forEach(inst -> ((AbilityBreathing)ability).applyToActions(actions, inst));
	}
	
	public void buildActions()
	{
		actions.clear();
		types.contents().forEach(type -> type.actions().stack(actions, this.types));
		ServerBus.GET_ACTIONS_EVENT.invoker().affectActions(actions, types);
	}
	
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