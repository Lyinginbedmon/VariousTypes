package com.lying.ability;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lying.VariousTypes;
import com.lying.ability.AbilityCosmetics.ConfigCosmetics;
import com.lying.init.VTAbilities;
import com.lying.utility.Cosmetic;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;

public class AbilityCosmetics extends Ability implements IComplexAbility<ConfigCosmetics>, ICosmeticSupplier
{
	public AbilityCosmetics(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public static AbilityInstance of(Identifier mapName, Cosmetic... values)
	{
		ConfigCosmetics config = new ConfigCosmetics(List.of());
		for(Cosmetic cos : values)
			config.add(cos);
		
		NbtCompound nbt = (NbtCompound)ConfigCosmetics.CODEC.encodeStart(NbtOps.INSTANCE, config).getOrThrow();
		nbt.putString("MapName", mapName.toString());
		
		AbilityInstance inst = VTAbilities.COSMETICS.get().instance();
		inst.setMemory(nbt);
		return inst;
	}
	
	public static AbilityInstance of(Cosmetic... values)
	{
		ConfigCosmetics config = new ConfigCosmetics(List.of());
		for(Cosmetic cos : values)
			config.add(cos);
		
		AbilityInstance inst = VTAbilities.COSMETICS.get().instance();
		inst.setMemory((NbtCompound)ConfigCosmetics.CODEC.encodeStart(NbtOps.INSTANCE, config).getOrThrow());
		return inst;
	}
	
	protected boolean remappable() { return true; }
	
	public boolean isHidden(AbilityInstance instance) { return true; }
	
	public List<Cosmetic> getCosmetics(AbilityInstance inst)
	{
		return instanceToValues(inst).values();
	}
	
	public ConfigCosmetics memoryToValues(NbtCompound nbt) { return ConfigCosmetics.fromNbt(nbt); }
	
	public static class ConfigCosmetics
	{
		protected static final Codec<ConfigCosmetics> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Cosmetic.CODEC.listOf().fieldOf("Values").forGetter(ConfigCosmetics::values))
					.apply(instance, ConfigCosmetics::new));
		
		private Map<Identifier, Cosmetic> values = Maps.newHashMap();
		
		public ConfigCosmetics(List<Cosmetic> valuesIn)
		{
			valuesIn.forEach(cos -> add(cos));
		}
		
		protected void add(Cosmetic cos) { values.put(cos.registryName(), cos); }
		
		public List<Cosmetic> values()
		{
			List<Cosmetic> list = Lists.newArrayList();
			list.addAll(values.values());
			return list;
		}
		
		public static ConfigCosmetics fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
