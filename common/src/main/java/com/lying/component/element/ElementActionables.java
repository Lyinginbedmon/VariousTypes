package com.lying.component.element;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.ability.ActivatedAbility;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.network.SyncActionablesPacket;
import com.lying.reference.Reference;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ElementActionables extends AbilitySet implements ISheetElement<AbilitySet>
{
	public static final int GLOBAL_COOL_LENGTH = Reference.Values.TICKS_PER_SECOND / 2;
	private final Map<Identifier, Cooldown> cooldowns = new HashMap<>();
	private int globalCooldown = GLOBAL_COOL_LENGTH;
	
	protected final DefaultedList<Optional<Identifier>> favourites = DefaultedList.ofSize(4, Optional.empty());
	
	public SheetElement<?> registry(){ return VTSheetElements.ACTIONABLES; }
	
	public static AbilitySet getActivated(CharacterSheet sheet) { return ((ElementActionables)sheet.element(VTSheetElements.ACTIONABLES)); }
	
	public boolean isAvailable(Identifier mapName)
	{
		return !coolingDown() && !cooldowns.containsKey(mapName);
	}
	
	public boolean coolingDown() { return globalCooldown < GLOBAL_COOL_LENGTH; }
	
	public void putOnCooldown(Identifier mapName, long startFrom, int duration)
	{
		cooldowns.put(mapName, new Cooldown(startFrom, duration));
		globalCooldown = 0;
	}
	
	public void clearCooldown(Identifier mapName)
	{
		cooldowns.remove(mapName);
	}
	
	/* Returns the progression of the given ability's cooldown, ranging from 0F (just started) to 1F (complete) */
	public float getCooldown(Identifier mapName, long time)
	{
		return cooldowns.containsKey(mapName) ? cooldowns.get(mapName).progress(time) : 1F;
	}
	
	public void tick(LivingEntity owner)
	{
		if(!coolingDown() && cooldowns.isEmpty())
			return;
		
		if(coolingDown())
			++globalCooldown;
		
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
		nbt.put("Abilities", writeToNbt());
		
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
		clear();
		readFromNbt(nbt.getList("Abilities", NbtElement.COMPOUND_TYPE)).abilities().forEach(inst -> add(inst));
		
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
	
	public Optional<Identifier> getFirstActivated()
	{
		return Optional.of(abilities().stream().findFirst().get().mapName());
	}
	
	public Optional<Identifier> getFavourite(int index) { return favourites.get(index); }
	
	public DefaultedList<Optional<Identifier>> getFavourites() { return favourites; }
	
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
		sheet.<ElementAbilitySet>element(VTSheetElements.ABILITES).mergeActivated(this);
		
		for(int i=0; i<favourites.size(); i++)
		{
			final int index = i;
			Optional<Identifier> fave = favourites.get(index);
			fave.ifPresent(id -> 
			{
				if(!hasAbilityInstance(id))
					favourites.set(index, Optional.empty());
			});
		}
	}
	
	/** Called client and server-side to interact with activated abilities */
	public void enactActionable(PlayerEntity owner, Identifier mapName)
	{
		if(!hasAbilityInstance(mapName))
			return;
		else if(!isAvailable(mapName))
		{
			owner.sendMessage(translate("gui", "activated_ability.failed", get(mapName).displayName()), true);
			return;
		}
		
		AbilityInstance inst = get(mapName);
		if(((ActivatedAbility)inst.ability()).trigger(owner, inst) && !owner.getWorld().isClient())
			SyncActionablesPacket.send((ServerPlayerEntity)owner, this);
		
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
