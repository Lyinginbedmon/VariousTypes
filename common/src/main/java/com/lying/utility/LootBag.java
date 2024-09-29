package com.lying.utility;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;

/** Generic handler for loot, both hard-defined and randomly generated */
public class LootBag
{
	public static final Codec<LootBag> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ItemStack.ITEM_CODEC.listOf().optionalFieldOf("Items").forGetter(v -> v.items),
			ItemStack.CODEC.listOf().optionalFieldOf("Stacks").forGetter(v -> v.itemStacks),
			RegistryKey.createCodec(RegistryKeys.LOOT_TABLE).optionalFieldOf("LootTableName").forGetter(v -> v.systemTable),
			LootTable.CODEC.optionalFieldOf("LootTable").forGetter(v -> v.customTable))
				.apply(instance, LootBag::new));
	
	private Optional<List<RegistryEntry<Item>>> items = Optional.empty();
	private Optional<List<ItemStack>> itemStacks = Optional.empty();
	private Optional<RegistryKey<LootTable>> systemTable = Optional.empty();
	private Optional<LootTable> customTable = Optional.empty();
	
	protected LootBag(Optional<List<RegistryEntry<Item>>> itemsIn, Optional<List<ItemStack>> stacksIn, Optional<RegistryKey<LootTable>> sysTableIn, Optional<LootTable> tableIn)
	{
		items = itemsIn.isPresent() && !itemsIn.get().isEmpty() ? itemsIn : Optional.empty();
		itemStacks = stacksIn.isPresent() && !stacksIn.get().isEmpty() ? stacksIn : Optional.empty();
		systemTable = sysTableIn;
		customTable = tableIn;
	}
	
	public static LootBag ofItems(Item... itemsIn)
	{
		LootBag bag = new LootBag(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
		bag.withItems(itemsIn);
		return bag;
	}
	
	public static LootBag ofStacks(ItemStack... stacksIn)
	{
		LootBag bag = new LootBag(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
		bag.withStacks(stacksIn);
		return bag;
	}
	
	public static LootBag ofTable(@NotNull RegistryKey<LootTable> tableIn)
	{
		return new LootBag(Optional.empty(), Optional.empty(), Optional.of(tableIn), Optional.empty());
	}
	
	public static LootBag ofCustom(@NotNull LootTable tableIn)
	{
		return new LootBag(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(tableIn));
	}
	
	@SuppressWarnings("deprecation")
	public LootBag withItems(Item... itemsIn)
	{
		for(Item item : itemsIn)
			if(item != null)
			{
				List<RegistryEntry<Item>> set = items.orElse(Lists.newArrayList());
				set.add(item.getRegistryEntry());
				items = Optional.of(set);
			}
		return this;
	}
	
	public LootBag withStacks(ItemStack... stacksIn)
	{
		for(ItemStack stack : stacksIn)
			if(stack != null && !stack.isEmpty())
			{
				List<ItemStack> set = itemStacks.orElse(Lists.newArrayList());
				set.add(stack.copy());
				itemStacks = Optional.of(set);
			}
		return this;
	}
	
	public LootBag withSystemTable(@Nullable RegistryKey<LootTable> tableIn)
	{
		systemTable = tableIn == null ? Optional.empty() : Optional.of(tableIn);
		return this;
	}
	
	public LootBag withCustomTable(@Nullable LootTable tableIn)
	{
		customTable = tableIn == null ? Optional.empty() : Optional.of(tableIn);
		return this;
	}
	
	public void giveTo(ServerPlayerEntity player)
	{
		items.ifPresent(set -> set.forEach(item -> player.giveItemStack(new ItemStack(item))));
		itemStacks.ifPresent(set -> set.forEach(stack -> player.giveItemStack(stack.copy())));
		systemTable.ifPresent(id -> 
		{
			LootTable table = player.getWorld().getServer().getReloadableRegistries().getLootTable(id);
			if(table != null)
				giveGeneratedLoot(table, player, player.getWorld().getRandom().nextLong());
		});
		customTable.ifPresent(loot -> giveGeneratedLoot(loot, player, player.getWorld().getRandom().nextLong()));
	}
	
	private static void giveGeneratedLoot(LootTable table, ServerPlayerEntity recipient, long seed)
	{
		LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(recipient.getServerWorld())
				.add(LootContextParameters.THIS_ENTITY, recipient)
				.add(LootContextParameters.ORIGIN, recipient.getPos());
		table.generateLoot(builder.build(LootContextTypes.GIFT), seed, s -> recipient.giveItemStack(s));
	}
	
	public static LootBag fromJson(JsonElement obj)
	{
		return CODEC.parse(JsonOps.INSTANCE, obj.getAsJsonObject()).getOrThrow();
	}
	
	public JsonElement toJson()
	{
		return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();
	}
}
