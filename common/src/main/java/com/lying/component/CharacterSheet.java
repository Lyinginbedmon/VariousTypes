package com.lying.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilitySet;
import com.lying.component.element.ElementAbilitySet;
import com.lying.component.element.ISheetElement;
import com.lying.component.module.AbstractSheetModule;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.init.VTSheetModules;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/**
 * General data management class for handling species & templates of a specific entity
 * @author Lying
 */
public class CharacterSheet
{
	protected Optional<LivingEntity> owner = Optional.empty();
	
	// These values represent the information the character is currently working on
	private Map<SheetElement<?>, ISheetElement<?>> elements = new HashMap<>();
	// These values alter the contents of elements during sheet building
	private Map<Identifier, AbstractSheetModule> modules = new HashMap<>();
	
	public CharacterSheet(@Nullable LivingEntity ownerIn)
	{
		VTSheetElements.getAll().forEach(sup -> 
		{
			ISheetElement<?> element = sup.make();
			elements.put(sup, element);
		});
		
		VTSheetModules.getAll().forEach(sup -> 
		{
			AbstractSheetModule module = sup.get();
			modules.put(module.registryName(), module);
		});
		
		if(ownerIn != null)
			owner = Optional.of(ownerIn);
	}
	
	/** Creates a duplicate of this character sheet for the given entity */
	public CharacterSheet copy(@Nullable LivingEntity ownerIn)
	{
		CharacterSheet clone = new CharacterSheet(ownerIn);
		clone.clone(this, false);
		return clone;
	}
	
	/** Repopulates the values of this sheet with those of the given sheet, without modifying the owner */
	public void clone(CharacterSheet sheet, boolean rebuild)
	{
		readSheetFromNbt(sheet.writeSheetToNbt(new NbtCompound()));
		
		if(rebuild)
		{
			buildSheet();
			markDirty();
		}
	}
	
	public boolean hasOwner() { return owner.isPresent(); }
	
	public Optional<LivingEntity> getOwner() { return owner; }
	
	public CharacterSheet setOwner(@Nullable LivingEntity entity)
	{
		owner = entity == null ? Optional.empty() : Optional.of(entity);
		return this;
	}
	
	public NbtCompound writeSheetToNbt(NbtCompound compound)
	{
		NbtList list = new NbtList();
		modules.values().forEach(module -> list.add(module.write(new NbtCompound())));
		compound.put("Modules", list);
		return compound;
	}
	
	public void readSheetFromNbt(NbtCompound compound)
	{
		clear(false);
		NbtList list = compound.getList("Modules", NbtElement.COMPOUND_TYPE);
		list.forEach(element -> 
		{
			NbtCompound data = (NbtCompound)element;
			Identifier id = new Identifier(data.getString("ID"));
			modules.get(id).read(data.contains("Data", NbtElement.COMPOUND_TYPE) ? data.getCompound("Data") : new NbtCompound());
		});
		
		buildSheet();
	}
	
	public void clear(boolean rebuild)
	{
		modules.values().forEach(module -> module.clear());
		if(rebuild)
		{
			buildSheet();
			markDirty();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T element(SheetElement<?> element) { return (T)elements.get(element).value(); }
	
	public void setHomeDimension(RegistryKey<World> world) { module(VTSheetModules.HOME).set(world); }
	
	public void clearHomeDimension()
	{
		module(VTSheetModules.HOME).clear();
		buildSheet();
		markDirty();
	}
	
	public RegistryKey<World> homeDimension() { return element(VTSheetElements.HOME_DIM); }
	
	public TypeSet types() { return element(VTSheetElements.TYPES); }
	
	public AbilitySet abilities() { return element(VTSheetElements.ABILITES); }
	
	public Optional<Species> getSpecies() { return module(VTSheetModules.SPECIES).get(); }
	
	public List<Template> getTemplates() { return module(VTSheetModules.TEMPLATES).get(); }
	
	public AbilitySet activatedAbilities() { return ((ElementAbilitySet)element(VTSheetElements.ABILITES)).activated(); }
	
	public ActionHandler actions() { return element(VTSheetElements.ACTIONS); }
	
	/** Returns true if this sheet is using an identifiable species (ie. a registry name that exists in the active datapack) */
	public boolean hasASpecies() { return getSpecies().isPresent(); }
	
	public void setSpecies(@Nullable Identifier registryNameIn)
	{
		module(VTSheetModules.SPECIES).set(registryNameIn);
		buildSheet();
		markDirty();
	}
	
	public boolean isSpecies(Identifier registryName){ return module(VTSheetModules.SPECIES).is(registryName); }
	
	public boolean hasTemplate(@NotNull Template templateIn) { return hasTemplate(templateIn.registryName()); }
	
	public boolean hasTemplate(@NotNull Identifier registryName) { return module(VTSheetModules.TEMPLATES).contains(registryName); }
	
	public void addTemplate(@NotNull Identifier registryName)
	{
		module(VTSheetModules.TEMPLATES).add(registryName);
		buildSheet();
		markDirty();
	}
	
	public void removeTemplate(@NotNull Identifier registryName)
	{
		module(VTSheetModules.TEMPLATES).remove(registryName);
		buildSheet();
		markDirty();
	}
	
	public void clearTemplates()
	{
		module(VTSheetModules.TEMPLATES).clear();
		buildSheet();
		markDirty();
	}
	
	public int power()
	{
		int power = 0;
		
		for(AbstractSheetModule module : modules())
			power += module.power();
		
		return power;
	}
	
	public final List<AbstractSheetModule> modules()
	{
		List<AbstractSheetModule> modules = Lists.newArrayList();
		modules.addAll(this.modules.values());
		modules.sort((a,b) -> (int)Math.signum(a.buildOrder() - b.buildOrder()));
		return modules;
	}
	
	@SuppressWarnings("unchecked")
	@Nullable
	public final <T extends AbstractSheetModule> T module(Supplier<T> moduleIn)
	{
		return (T)modules.getOrDefault(moduleIn.get().registryName(), null);
	}
	
	/** Constructs the types and abilities from scratch */
	public final void buildSheet()
	{
		List<SheetElement<?>> elements = Lists.newArrayList();
		elements.addAll(this.elements.keySet());
		elements.sort((a,b) -> (int)Math.signum(a.buildOrder() - b.buildOrder()));
		
		elements.forEach(element -> this.elements.get(element).rebuild(this));
	}
	
	public boolean hasAction(Action action) { return actions().can(action); }
	
	public boolean isAbleToBreathe(Fluid fluid, boolean hasWaterBreathing) { return hasWaterBreathing || actions().canBreathe(fluid); }
	
	public void markDirty()
	{
		if(hasOwner())
			VariousTypes.setSheet(getOwner().get(), this);
	}
}