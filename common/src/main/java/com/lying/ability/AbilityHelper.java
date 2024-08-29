package com.lying.ability;

import java.util.List;
import java.util.function.Supplier;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class AbilityHelper
{
	public static <T> List<TagKey<T>> getTags(NbtCompound memory, String entry, RegistryKey<? extends Registry<T>> key, Supplier<List<TagKey<T>>> defaultTags)
	{
		if(memory.contains(entry, NbtElement.LIST_TYPE))
		{
			List<TagKey<T>> tags = Lists.newArrayList();
			for(NbtElement element : memory.getList(entry, NbtElement.STRING_TYPE))
				tags.add(TagKey.of(key, new Identifier(element.asString())));
			return tags;
		}
		
		return defaultTags.get();
	}
}
