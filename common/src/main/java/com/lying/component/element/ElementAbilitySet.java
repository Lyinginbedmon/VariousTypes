package com.lying.component.element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.reference.Reference;
import com.lying.type.TypeSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ElementAbilitySet extends AbilitySet implements ISheetElement<AbilitySet>
{
	public static final int GLOBAL_COOL_LENGTH = Reference.Values.TICKS_PER_SECOND / 2;
	
	private AbilitySet activated = new AbilitySet();
	private Map<Identifier, Cooldown> cooldowns = new HashMap<>();
	private int globalCooldown = 0;
	
	protected final DefaultedList<Optional<Identifier>> favourites = DefaultedList.ofSize(4, Optional.empty());
	
	public SheetElement<?> registry(){ return VTSheetElements.ABILITES; }
	
	public static AbilitySet getActivated(CharacterSheet sheet) { return ((ElementAbilitySet)sheet.element(VTSheetElements.ABILITES)).activated(); }
	
	public boolean isAvailable(Identifier mapName)
	{
		return globalCooldown == 0 && !cooldowns.containsKey(mapName);
	}
	
	public void putOnCooldown(Identifier mapName, long startFrom, int duration)
	{
		cooldowns.put(mapName, new Cooldown(startFrom, duration));
	}
	
	public void clearCooldown(Identifier mapName)
	{
		cooldowns.remove(mapName);
	}
	
	/* Returns the progression of the given ability's cooldown, ranging from 0F (just started) to 1F (complete) */
	public float getCooldown(Identifier mapName, long time)
	{
		float global = 1F - Math.clamp((float)globalCooldown / (float)GLOBAL_COOL_LENGTH, 0F, 1F);
		if(cooldowns.containsKey(mapName))
			return Math.min(global, cooldowns.get(mapName).progress(time));
		else
			return global;
	}
	
	public boolean hasAbility(Identifier registryName)
	{
		return super.hasAbility(registryName) || activated.hasAbility(registryName);
	}
	
	public boolean hasAbilityInstance(Identifier mapName)
	{
		return super.hasAbilityInstance(mapName) || activated.hasAbilityInstance(mapName);
	}
	
	public AbilityInstance get(Identifier mapName)
	{
		AbilityInstance act = activated.get(mapName);
		return act == null ? get(mapName) : act;
	}
	
	public List<AbilityInstance> getAbilitiesOfType(Identifier registryName)
	{
		Map<Identifier, AbilityInstance> abilities = new HashMap<>();
		super.getAbilitiesOfType(registryName).forEach(inst -> abilities.put(inst.mapName(), inst));
		activated.getAbilitiesOfType(registryName).forEach(inst -> abilities.put(inst.mapName(), inst));
		List<AbilityInstance> finalised = Lists.newArrayList();
		finalised.addAll(abilities.values());
		return finalised;
	}
	
	public void tick(LivingEntity owner)
	{
		if(globalCooldown == 0 && cooldowns.isEmpty())
			return;
		
		if(globalCooldown > 0)
			--globalCooldown;
		
		List<Identifier> cooledOff = Lists.newArrayList();
		cooldowns.entrySet().removeIf(entry -> 
		{
			if(entry.getValue().hasElapsed(owner.getWorld().getTime()))
			{
				cooledOff.add(entry.getKey());
				return true;
			}
			return false;
		});
		if(!owner.getWorld().isClient() && !cooledOff.isEmpty())
		{
			// XXX Notify player of ability(s) coming off cooldown?
			VariousTypes.getSheet(owner).get().markDirty();
		}
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.put("Activated", activated.writeToNbt());
		if(!cooldowns.isEmpty())
		{
			NbtList cools = new NbtList();
			cooldowns.entrySet().forEach(entry -> 
			{
				NbtCompound data = entry.getValue().toNbt();
				data.putString("Name", entry.getKey().toString());
				cools.add(data);
			});
			nbt.put("Cooldowns", cools);
		}
		NbtList favourited = new NbtList();
		for(int i=0; i<favourites.size(); i++)
		{
			Optional<Identifier> entry = favourites.get(i);
			if(entry.isEmpty()) continue;
			NbtCompound data = new NbtCompound();
			data.putInt("Slot", i);
			data.putString("Name", entry.get().toString());
			favourited.add(data);
		}
		if(!favourited.isEmpty())
			nbt.put("Favourites", favourited);
		return nbt;
	}
	
	public void readFromNbt(NbtCompound nbt)
	{
		activated = AbilitySet.readFromNbt(nbt.getList("Activated", NbtElement.COMPOUND_TYPE));
		
		cooldowns.clear();
		if(nbt.contains("Cooldowns", NbtElement.LIST_TYPE))
		{
			NbtList cools = nbt.getList("Cooldowns", NbtElement.COMPOUND_TYPE);
			cools.forEach(element -> 
			{
				NbtCompound data = (NbtCompound)element;
				cooldowns.put(new Identifier(data.getString("Name")), Cooldown.fromNbt(data));
			});
		}
		
		favourites.clear();
		if(nbt.contains("Favourites", NbtElement.LIST_TYPE))
		{
			NbtList favourited = nbt.getList("Favourites", NbtElement.COMPOUND_TYPE);
			favourited.forEach(element -> 
			{
				NbtCompound data = (NbtCompound)element;
				favourites.set(data.getInt("Slot"), Optional.of(new Identifier(data.getString("Name"))));
			});
		}
	}
	
	public AbilitySet value() { return this; }
	
	/** An AbilitySet containing exclusively non-passive abilities */
	public AbilitySet activated() { return activated; }
	
	public Optional<Identifier> getFirstActivated()
	{
		return Optional.of(activated.abilities().stream().findFirst().get().mapName());
	}
	
	public Optional<Identifier> getFavourite(int index) { return favourites.get(index); }
	
	public boolean setFavourite(int index, @Nullable Identifier mapName)
	{
		if(mapName == null && favourites.get(index).isEmpty())
			return false;
		else if(mapName != null && favourites.get(index).isPresent() && mapName.equals(favourites.get(index).get()))
			return false;
		
		favourites.set(index, mapName == null ? Optional.empty() : Optional.of(mapName));
		return true;
	}
	
	public void rebuild(CharacterSheet sheet)
	{
		clear();
		
		(sheet.<TypeSet>element(VTSheetElements.TYPES)).abilities().forEach(inst -> add(inst.copy()));
		
		sheet.modules().forEach(module -> module.affect(this));
		
		mergeActivated(activated);
	}
	
	private static class Cooldown
	{
		private final long start, end;
		private final int duration;
		
		public Cooldown(long startTime, int length)
		{
			start = startTime;
			duration = length;
			end = start + duration;
		}
		
		public boolean hasElapsed(long time) { return time > end; }
		
		public float progress(long time)
		{
			return Math.clamp((time - start) / (float)duration, 0F, 1F);
		}
		
		public NbtCompound toNbt()
		{
			NbtCompound nbt = new NbtCompound();
			nbt.putLong("Start", start);
			nbt.putInt("Duration", duration);
			return nbt;
		}
		
		public static Cooldown fromNbt(NbtCompound nbt)
		{
			return new Cooldown(nbt.getLong("Start"), nbt.getInt("Duration"));
		}
	}
}
