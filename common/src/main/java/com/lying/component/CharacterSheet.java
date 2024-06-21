package com.lying.component;

import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilitySet;
import com.lying.species.Species;
import com.lying.species.SpeciesRegistry;
import com.lying.template.Template;
import com.lying.template.TemplateRegistry;
import com.lying.type.TypeSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

public class CharacterSheet
{
	private final LivingEntity owner;
	
	private TypeSet customTypes = new TypeSet();
	private AbilitySet customAbilities = new AbilitySet();
	
	private TypeSet types = new TypeSet();
	private Species species = null;
	private List<Template> templates = Lists.newArrayList();
	private AbilitySet abilities;
	
	public CharacterSheet(LivingEntity ownerIn)
	{
		owner = ownerIn;
	}
	
	public CharacterSheet copy(LivingEntity ownerIn)
	{
		CharacterSheet clone = new CharacterSheet(ownerIn);
		clone.customTypes = customTypes.copy();
		clone.customAbilities = customAbilities;
		clone.types = types.copy();
		clone.templates = templates;
		clone.abilities = abilities;
		return clone;
	}
	
	public NbtCompound writeToNbt(NbtCompound compound)
	{
		if(species != null)
			compound.putString("Species", species.registryName().toString());
		
		if(!templates.isEmpty())
		{
			NbtList list = new NbtList();
			templates.forEach(template -> list.add(NbtString.of(template.registryName().toString())));
			compound.put("Templates", list);
		}
		
		if(!customTypes.isEmpty())
			compound.put("CustomTypes", customTypes.writeToNbt());
		
		if(!customAbilities.isEmpty())
			compound.put("CustomAbilities", customAbilities.writeToNbt());
		
		return compound;
	}
	
	public void readFromNbt(NbtCompound compound)
	{
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
	
	public TypeSet getTypes() { return types; }
	
	public AbilitySet getAbilities() { return abilities; }
	
	public List<Template> getAppliedTemplates() { return templates; }
	
	/** Constructs the types and abilities from scratch */
	public void buildSheet()
	{
		// Types are calculated first for efficiency-sake
		buildTypes();
		buildAbilities();
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
		
		AbilitySet abilities = species.abilities().copy();
		
		// Add abilities from types
		types.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		// Apply templates to abilities
		for(Template template : templates)
			template.applyAbilityOperations(abilities);
		
		// Add all custom abilities
		if(!customAbilities.isEmpty())
			customAbilities.abilities().forEach(inst -> abilities.add(inst.copy()));
		
		this.types = types;
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
		
		this.abilities = abilities;
	}
	
	public boolean addCustomAbility(Ability ability)
	{
		return addCustomAbility(ability, Consumers.nop());
	}
	
	public boolean addCustomAbility(Ability ability, Consumer<NbtCompound> dataModifier)
	{
		return customAbilities.add(ability.instance(AbilitySource.CUSTOM, dataModifier));
	}
}