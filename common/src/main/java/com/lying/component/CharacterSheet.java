package com.lying.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.component.element.ISheetElement;
import com.lying.component.module.AbstractSheetModule;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.init.VTSheetModules;
import com.lying.network.SyncActionablesPacket;
import com.lying.network.SyncFatiguePacket;
import com.lying.network.SyncPosePacket;
import com.lying.utility.ServerEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * General data management class for handling species & templates of a specific entity
 * @author Lying
 */
public class CharacterSheet
{
	protected Optional<LivingEntity> owner = Optional.empty();
	
	protected int timesCreated = 0;
	
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
			module.setParent(this);
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
	
	/** Returns the number of times this sheet has been altered in the character creation screen */
	public int timesCreated() { return this.timesCreated; }
	
	public void incEdits() { this.timesCreated++; }
	
	public NbtCompound writeSheetToNbt(NbtCompound compound)
	{
		compound.putInt("Edits", timesCreated());
		
		NbtList listM = new NbtList();
		modules.values().forEach(module -> listM.add(module.write(new NbtCompound())));
		compound.put("Modules", listM);
		
		NbtList listE = new NbtList();
		elements.values().forEach(element -> 
		{
			NbtCompound data = element.writeToNbt(new NbtCompound());
			if(!data.isEmpty())
			{
				data.putString("RegistryName", element.registry().registryName().toString());
				listE.add(data);
			}
		});
		if(!listE.isEmpty())
			compound.put("Elements", listE);
		
		return compound;
	}
	
	public void readSheetFromNbt(NbtCompound compound)
	{
		this.timesCreated = compound.getInt("Edits");
		
		clear();
		NbtList list = compound.getList("Modules", NbtElement.COMPOUND_TYPE);
		list.forEach(element -> 
		{
			NbtCompound data = (NbtCompound)element;
			Identifier id = new Identifier(data.getString("ID"));
			modules.get(id).read(data.contains("Data", NbtElement.COMPOUND_TYPE) ? data.getCompound("Data") : new NbtCompound());
		});
		
		if(compound.contains("Elements", NbtElement.LIST_TYPE))
			for(NbtElement el : compound.getList("Elements", NbtElement.COMPOUND_TYPE))
			{
				NbtCompound nbt = (NbtCompound)el;
				Identifier regName = new Identifier(nbt.getString("RegistryName"));
				nbt.remove("RegistryName");
				SheetElement<?> element = VTSheetElements.get(regName);
				if(element == null || nbt.isEmpty())
					continue;
				
				elements.get(element).readFromNbt(nbt);
			}
		
		buildSheet();
	}
	
	/** Resets all module values, note that these changes are not applied to elements until {@link buildSheet} is next called. */
	public void clear()
	{
		modules.values().forEach(module -> module.clear());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> T elementValue(SheetElement<?> element) { return (T)elements.get(element).value(); }
	
	@SuppressWarnings("unchecked")
	public <T extends ISheetElement<?>> T element(SheetElement<?> element) { return (T)elements.get(element); }
	
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
	
	/** Reconstructs all sheet elements in build order */
	public final void buildSheet()
	{
		owner.ifPresent(living -> ServerEvents.SheetEvents.BEFORE_REBUILD_EVENT.invoker().process(living, elementValue(VTSheetElements.ABILITES)));
		VTSheetElements.buildOrder().forEach(element -> this.elements.get(element).rebuild(this));
		owner.ifPresent(living -> ServerEvents.SheetEvents.AFTER_REBUILD_EVENT.invoker().process(living, elementValue(VTSheetElements.ABILITES)));
	}
	
	public final void buildAndSync()
	{
		buildSheet();
		markDirty();
		owner.ifPresent(owner -> 
		{
			if(owner.getType() != EntityType.PLAYER || owner.getWorld().isClient())
				return;
			
			ServerPlayerEntity player = (ServerPlayerEntity)owner;
			SyncActionablesPacket.send(player, element(VTSheetElements.ACTIONABLES));
			SyncFatiguePacket.send(player, elementValue(VTSheetElements.NONLETHAL));
			SyncPosePacket.sendSync(player, elementValue(VTSheetElements.SPECIAL_POSE));
		});
	}
	
	public void markDirty()
	{
		if(hasOwner())
			VariousTypes.setSheet(getOwner().get(), this);
	}
}