package com.lying.utility;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.LivingEntity;
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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

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
	
	private Text description;
	
	protected LootBag()
	{
		this(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
	}
	
	protected LootBag(Optional<List<RegistryEntry<Item>>> itemsIn, Optional<List<ItemStack>> stacksIn, Optional<RegistryKey<LootTable>> sysTableIn, Optional<LootTable> tableIn)
	{
		items = itemsIn.isPresent() && !itemsIn.get().isEmpty() ? itemsIn : Optional.empty();
		itemStacks = stacksIn.isPresent() && !stacksIn.get().isEmpty() ? stacksIn : Optional.empty();
		systemTable = sysTableIn;
		customTable = tableIn;
		
		generateDescription();
	}
	
	public static LootBag ofItems(Item... itemsIn)
	{
		return new LootBag().withItems(itemsIn);
	}
	
	public static LootBag ofStacks(ItemStack... stacksIn)
	{
		return new LootBag().withStacks(stacksIn);
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
		List<RegistryEntry<Item>> set = items.orElse(Lists.newArrayList());
		for(Item item : itemsIn)
			if(item != null)
				set.add(item.getRegistryEntry());
		items = set.isEmpty() ? Optional.empty() : Optional.of(set);
		generateDescription();
		return this;
	}
	
	public LootBag withStacks(ItemStack... stacksIn)
	{
		List<ItemStack> set = itemStacks.orElse(Lists.newArrayList());
		for(ItemStack stack : stacksIn)
			if(stack != null && !stack.isEmpty())
				set.add(stack.copy());
		itemStacks = set.isEmpty() ? Optional.empty() : Optional.of(set);
		generateDescription();
		return this;
	}
	
	public LootBag withSystemTable(@Nullable RegistryKey<LootTable> tableIn)
	{
		systemTable = tableIn == null ? Optional.empty() : Optional.of(tableIn);
		generateDescription();
		return this;
	}
	
	public LootBag withCustomTable(@Nullable LootTable tableIn)
	{
		customTable = tableIn == null ? Optional.empty() : Optional.of(tableIn);
		generateDescription();
		return this;
	}
	
	/** Returns true if there are no contents in any portion of this bag */
	public boolean isEmpty()
	{
		return
				systemTable.isEmpty() &&
				customTable.isEmpty() &&
				(items.isEmpty() || items.get().isEmpty()) &&
				(itemStacks.isEmpty() || itemStacks.get().isEmpty());
	}
	
	private void generateDescription()
	{
		if(isEmpty())
		{
			description = Text.literal("Nothing");
			return;
		}
		
		List<Text> entries = Lists.newArrayList();		
		if(items.isPresent() || itemStacks.isPresent())
		{
			// Create map of each unique (ie. stackable) itemstack and how many of it are provided
			Map<ItemStack, Integer> tallies = new HashMap<>();
			if(items.isPresent() && !items.get().isEmpty())
				collateStacksIntoMap(items.get().stream().map(item -> new ItemStack(item)).toList(), tallies);
			if(itemStacks.isPresent())
				collateStacksIntoMap(itemStacks.get(), tallies);
			
			// Convert tallied entries into description entries with quantities
			tallies.entrySet().forEach(entry -> 
			{
				int count = entry.getValue();
				ItemStack stack = entry.getKey();
				if(count > 1)
					entries.add(Text.literal(String.valueOf(count)).append("x ").append(stack.getName()));
				else
					entries.add(stack.getName());
			});
			
			// Sort entries
			if(entries.size() > 1)
				Collections.sort(entries, VTUtils.TEXT_ALPHABETICAL);
		}
		
		if(systemTable.isPresent() || customTable.isPresent())
			entries.add(Text.literal("Random Loot"));
		
		description = VTUtils.listToString(entries, v -> v, ", ");
	}
	
	/** Creates an entry for each unique item stack with the total count of it */
	private static Map<ItemStack, Integer> collateStacksIntoMap(List<ItemStack> itemStacks, Map<ItemStack, Integer> tallies)
	{
		for(ItemStack stack : itemStacks)
		{
			boolean shouldLog = true;
			for(Entry<ItemStack, Integer> entry : tallies.entrySet())
				if(ItemStack.areItemsAndComponentsEqual(stack, entry.getKey()))
				{
					entry.setValue(entry.getValue() + stack.getCount());
					shouldLog = false;
					break;
				}
			
			if(shouldLog)
				tallies.put(stack.copy(), stack.getCount());
		}
		return tallies;
	}
	
	public Text description() { return description; }
	
	public void giveTo(ServerPlayerEntity player)
	{
		if(!isEmpty())
			getTotalLoot(player, player.getWorld().getRandom().nextLong()).forEach(stack -> player.giveItemStack(stack.copy()));
	}
	
	public void dropFrom(LivingEntity living)
	{
		if(!isEmpty())
			getTotalLoot(living, living.getWorld().getRandom().nextLong()).forEach(stack -> living.dropStack(stack.copy()));
	}
	
	private List<ItemStack> getTotalLoot(LivingEntity spawner, long seed)
	{
		List<ItemStack> loot = Lists.newArrayList();
		items.ifPresent(set -> set.forEach(item -> loot.add(new ItemStack(item))));
		itemStacks.ifPresent(set -> set.forEach(stack -> loot.add(stack.copy())));
		systemTable.ifPresent(id -> 
		{
			LootTable table = spawner.getWorld().getServer().getReloadableRegistries().getLootTable(id);
			if(table != null)
				getGeneratedLoot(table, spawner, spawner.getWorld().getRandom().nextLong());
		});
		customTable.ifPresent(table -> getGeneratedLoot(table, spawner, spawner.getWorld().getRandom().nextLong()));
		return loot;
	}
	
	private static List<ItemStack> getGeneratedLoot(LootTable table, LivingEntity recipient, long seed)
	{
		if(recipient.getWorld().isClient())
			return Lists.newArrayList();
		LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)recipient.getWorld())
				.add(LootContextParameters.THIS_ENTITY, recipient)
				.add(LootContextParameters.ORIGIN, recipient.getPos());
		List<ItemStack> loot = Lists.newArrayList();
		table.generateLoot(builder.build(LootContextTypes.GIFT), seed, s -> loot.add(s.copy()));
		return loot;
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
