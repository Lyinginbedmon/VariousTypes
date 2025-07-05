package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.lying.VariousTypes;
import com.lying.utility.CosmeticType;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class VTCosmeticTypes
{
	private static final Map<Identifier, Supplier<CosmeticType>> TYPE_REGISTRY = new HashMap<>();
	
	private static Predicate<LivingEntity> WEARING_SKULL = e -> e.getEquippedStack(EquipmentSlot.HEAD).isIn(ItemTags.SKULLS);
	
	public static final Supplier<CosmeticType> WINGS	= register(prefix("wings"), 1, e -> e.getEquippedStack(EquipmentSlot.BODY).isOf(Items.ELYTRA));
	public static final Supplier<CosmeticType> EARS		= register(prefix("ears"), 1, WEARING_SKULL);
	public static final Supplier<CosmeticType> TAIL		= register(prefix("tail"), 1);
	public static final Supplier<CosmeticType> HORNS	= register(prefix("horns"), 3, WEARING_SKULL);
	public static final Supplier<CosmeticType> NOSE		= register(prefix("nose"), 1, WEARING_SKULL);
	public static final Supplier<CosmeticType> ICON		= register(prefix("icon"), 1);
	public static final Supplier<CosmeticType> MISC		= register(prefix("misc"), -1);
	
	public static Supplier<CosmeticType> register(Identifier regName, int slotLimit)
	{
		return register(regName, () -> new CosmeticType(regName, slotLimit, Predicates.alwaysFalse()));
	}
	
	public static Supplier<CosmeticType> register(Identifier regName, int slotLimit, Predicate<LivingEntity> hideCondition)
	{
		return register(regName, () -> new CosmeticType(regName, slotLimit, hideCondition));
	}
	
	public static Supplier<CosmeticType> register(Identifier regName, Supplier<CosmeticType> supplierIn)
	{
		TYPE_REGISTRY.put(regName, supplierIn);
		return supplierIn;
	}
	
	public static Optional<CosmeticType> get(Identifier registryName)
	{
		CosmeticType cos = TYPE_REGISTRY.getOrDefault(registryName, () -> null).get();
		return cos == null ? Optional.empty() : Optional.of(cos);
	}
	
	public static void init()
	{
		VariousTypes.LOGGER.info(" # Initialised "+TYPE_REGISTRY.size()+" cosmetic types");
	}
	
	public static Collection<Identifier> typeIds() { return TYPE_REGISTRY.keySet(); }
	
	public static Collection<CosmeticType> types() { return TYPE_REGISTRY.values().stream().map(Supplier::get).toList(); }
}
