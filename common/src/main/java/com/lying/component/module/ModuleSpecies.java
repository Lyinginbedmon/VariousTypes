package com.lying.component.module;

import java.util.Optional;

import com.lying.ability.AbilitySet;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSpeciesRegistry;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class ModuleSpecies extends AbstractSheetModule
{
	private static final Identifier DEFAULT_SPECIES = Reference.ModInfo.prefix("human");
	private Optional<Identifier> speciesId = Optional.of(DEFAULT_SPECIES);
	
	public ModuleSpecies() { super(Reference.ModInfo.prefix("species"), 0); }
	
	public int power()
	{
		Optional<Species> spec = get();
		return spec.isPresent() ? spec.get().power() : 0;
	}
	
	public Optional<Species> get() { return speciesId.isPresent() ? VTSpeciesRegistry.instance().get(speciesId.get()) : Optional.empty(); }
	
	public boolean is(Identifier registryName) { return speciesId.isPresent() && speciesId.get().equals(registryName); }
	
	public void clear() { speciesId = Optional.of(DEFAULT_SPECIES); }
	
	public void set(Identifier speciesIdIn) { speciesId = Optional.of(speciesIdIn); }
	
	public void affect(ISheetElement<?> element)
	{
		Optional<Species> spec = get();
		
		if(!spec.isPresent())
			return;
		else if(element.registry() == VTSheetElements.TYPES)
			spec.ifPresent(species -> 
			{
				TypeSet types = (TypeSet)element;
				types.clear();			
				types.addAll(species.types());
			});
		else if(element.registry() == VTSheetElements.ABILITES)
			spec.ifPresent(species -> 
			{
				AbilitySet abilities = (AbilitySet)element;
				species.abilities().abilities().forEach(inst -> abilities.add(inst.copy()));
			});
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		speciesId.ifPresent(id -> nbt.putString("ID", id.toString()));
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("ID", NbtElement.STRING_TYPE))
			speciesId = Optional.of(new Identifier(nbt.getString("ID")));
	}

}
