package com.lying.component.element;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.ability.Ability;
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
	
	public static AbilitySet getActivated(CharacterSheet sheet) { return sheet.elementValue(VTSheetElements.ACTIONABLES); }
	
	public boolean add(AbilityInstance ability)
	{
		return Ability.isActivatedAbility(ability.ability()) ? super.add(ability) : false;
	}
	
	public boolean isAvailable(Identifier mapName)
	{
		return !coolingDown() && !cooldowns.containsKey(mapName);
	}
	
	public boolean coolingDown() { return globalCooldown < GLOBAL_COOL_LENGTH; }
	
	/** Puts the ability on cooldown, overriding any existing cooldown */
	public void putOnCooldown(Identifier mapName, long startFrom, int duration)
	{
		putOnCooldown(mapName, startFrom, duration, true);
	}
	
	/** Puts the ability on cooldown, overriding any existing cooldown */
	public void putOnCooldown(Identifier mapName, long startFrom, int duration, boolean fireGlobal)
	{
		startCooldown(mapName, new Cooldown(startFrom, duration), fireGlobal);
	}
	
	/** Puts the ability on indefinite cooldown, without triggering global cooldown, usually for ticking abilities */
	public void putOnIndefiniteCooldown(Identifier mapName, boolean fireGlobal)
	{
		startCooldown(mapName, new Cooldown(), fireGlobal);
	}
	
	private void startCooldown(Identifier mapName, Cooldown cool, boolean fireGlobal)
	{
		cooldowns.put(mapName, cool);
		if(fireGlobal)
			globalCooldown = 0;
	}
	
	public void clearCooldown(Identifier mapName)
	{
		cooldowns.remove(mapName);
	}
	
	@Nullable
	public Cooldown getCooldown(Identifier mapName)
	{
		return cooldowns.getOrDefault(mapName, null);
	}
	
	/* Returns the progression of the given ability's cooldown, ranging from 0F (just started) to 1F (complete) */
	public float getCooldownProgress(Identifier mapName, long time)
	{
		if(!cooldowns.containsKey(mapName))
			return 1F;
		
		Cooldown cool = cooldowns.get(mapName);
		return cool.isIndefinite() ? 0F : cool.progress(time);
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
			Cooldown cool = entry.getValue();
			if(!cool.isIndefinite() && cool.hasElapsed(owner.getWorld().getTime()))
			{
				cooledOff.add(entry.getKey());
				return true;
			}
			return false;
		});
		
		// XXX Notify player of ability(s) coming off cooldown?
		if(!owner.getWorld().isClient() && !cooledOff.isEmpty())
			SyncActionablesPacket.send((ServerPlayerEntity)owner, this);
	}
	
	public NbtCompound storeNbt()
	{
		return this.writeToNbt(new NbtCompound());
	}
	
	public NbtCompound writeToNbt(NbtCompound nbt)
	{
		if(coolingDown())
			nbt.putInt("Global", globalCooldown);
		
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
		
		if(nbt.contains("Global", NbtElement.INT_TYPE))
			globalCooldown = nbt.getInt("Global");
		
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
	
	public static ElementActionables buildFromNbt(NbtCompound nbt)
	{
		ElementActionables actionables = new ElementActionables();
		actionables.readFromNbt(nbt);
		return actionables;
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
		ElementAbilitySet abilities = sheet.element(VTSheetElements.ABILITIES);
		abilities.mergeActivated(this);
		
		// Add all sub-abilities from activated abilities and re-merge
		abilities().forEach(inst -> inst.ability().getSubAbilities(inst).forEach(sub -> abilities.add(sub.copy())));
		abilities.mergeActivated(this);
		
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
}
