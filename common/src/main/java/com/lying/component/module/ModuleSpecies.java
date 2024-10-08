package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.describeSpecies;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.ability.AbilitySet;
import com.lying.component.element.ElementHome;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSpeciesRegistry;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.type.TypeSet;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class ModuleSpecies extends AbstractSheetModule
{
	public static final SimpleCommandExceptionType NO_VALUE = new SimpleCommandExceptionType(translate("command", "failed_no_species_selected"));
	private static final Identifier DEFAULT_SPECIES = Reference.ModInfo.prefix("human");
	private Optional<Identifier> speciesId = Optional.of(DEFAULT_SPECIES);
	
	public ModuleSpecies() { super(Reference.ModInfo.prefix("species"), 0); }
	
	public int power()
	{
		Optional<Species> spec = getMaybe();
		return spec.isPresent() ? spec.get().power() : 0;
	}
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context -> 
		{
			if(speciesId.isEmpty() || getMaybe().isEmpty())
				throw NO_VALUE.create();
			
			source.sendFeedback(() -> translate("command","get.species.success", owner.getDisplayName(), describeSpecies(getMaybe().get())), true);
			return 15;
		};
	}
	
	public Optional<Species> getMaybe() { return speciesId.isPresent() ? VTSpeciesRegistry.instance().get(speciesId.get()) : Optional.empty(); }
	
	public boolean is(Identifier registryName) { return speciesId.isPresent() && speciesId.get().equals(registryName); }
	
	public void clear() { speciesId = Optional.of(DEFAULT_SPECIES); }
	
	public void set(@Nullable Identifier speciesIdIn)
	{
		if(speciesId.isEmpty() && speciesIdIn == null || !speciesId.isEmpty() && speciesId.get() == speciesIdIn)
			return;
		
		speciesId = speciesIdIn == null ? Optional.empty() : Optional.of(speciesIdIn);
		updateSheet();
	}
	
	public void affect(ISheetElement<?> element)
	{
		Optional<Species> spec = getMaybe();
		
		if(!spec.isPresent())
			return;
		else if(element.registry() == VTSheetElements.HOME_DIM)
			spec.ifPresent(species -> 
			{
				if(species.hasConfiguredHome())
					((ElementHome)element).set(species.homeDimension());
			});
		else if(element.registry() == VTSheetElements.TYPES)
			spec.ifPresent(species -> 
			{
				TypeSet types = (TypeSet)element;
				types.clear();			
				types.addAll(species.types());
			});
		else if(element.registry() == VTSheetElements.ABILITIES)
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
