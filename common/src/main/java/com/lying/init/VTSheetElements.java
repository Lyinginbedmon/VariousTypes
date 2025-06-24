package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.component.element.ElementAbilitySet;
import com.lying.component.element.ElementActionHandler;
import com.lying.component.element.ElementActionables;
import com.lying.component.element.ElementCosmetics;
import com.lying.component.element.ElementHome;
import com.lying.component.element.ElementNonLethal;
import com.lying.component.element.ElementSpecialPose;
import com.lying.component.element.ElementTypeSet;
import com.lying.component.element.ISheetElement;

import net.minecraft.util.Identifier;

/** Defines discrete modifiable values within a character sheet */
public class VTSheetElements
{
	private static final Map<Identifier, SheetElement<?>> ELEMENTS = new HashMap<>();
	
	/** Creature home dimension, which affects their types */
	public static final SheetElement<ElementHome> HOME_DIM	= register("home", List::of, ElementHome::new);
	/** Creature types */
	public static final SheetElement<ElementTypeSet> TYPES	= register("types", listOf(VTSheetElements.HOME_DIM), ElementTypeSet::new);
	/** All abilities */
	public static final SheetElement<ElementAbilitySet> ABILITIES	= register("abilities", listOf(VTSheetElements.TYPES), ElementAbilitySet::new);
	/** Activated abilities, a subset of abilities */
	public static final SheetElement<ElementActionables> ACTIONABLES	= register("actionables", listOf(VTSheetElements.ABILITIES), ElementActionables::new);
	/** Common physiological capacities */
	public static final SheetElement<ElementActionHandler> ACTIONS	= register("actions", listOf(VTSheetElements.ABILITIES, VTSheetElements.TYPES), ElementActionHandler::new);
	/** Total nonlethal damage */
	public static final SheetElement<ElementNonLethal> NONLETHAL	= register("nonlethal", List::of, ElementNonLethal::new);
	public static final SheetElement<ElementSpecialPose> SPECIAL_POSE	= register("special_pose", List::of, ElementSpecialPose::new);
	public static final SheetElement<ElementCosmetics> COSMETICS	= register("cosmetics", listOf(VTSheetElements.ABILITIES), ElementCosmetics::new);
	
	private static final List<SheetElement<?>> SORTED_LIST = Lists.newArrayList();
	
	/** Returns a list of all registered sheet elements in the order in which they must be calculated during building */
	public static List<SheetElement<?>> buildOrder()
	{
		List<SheetElement<?>> order = Lists.newArrayList();
		order.addAll(SORTED_LIST);
		return order;
	}
	
	public static <T extends ISheetElement<C>, C extends Object> SheetElement<T> register(String name, Supplier<List<SheetElement<?>>> order, Supplier<T> supplier)
	{
		Identifier registryName = prefix(name);
		SheetElement<T> element = SheetElement.create(registryName, order, supplier);
		ELEMENTS.put(registryName, element);
		return element;
	}
	
	public static void init()
	{
		VariousTypes.LOGGER.info(" # Initialised "+ELEMENTS.size()+" sheet elements");
		
		List<SheetElement<?>> elements = Lists.newArrayList();
		elements.addAll(ELEMENTS.values());
		
		while(!elements.isEmpty())
		{
			List<SheetElement<?>> elementsB = Lists.newArrayList();
			for(SheetElement<?> element : elements)
				if(element.canBuild(SORTED_LIST))
					elementsB.add(element);
			
			if(elementsB.isEmpty())
			{
				if(!elements.isEmpty())
					VariousTypes.LOGGER.warn("One of more sheet elements were not placed in the build order, check their registration!");
				break;
			}
			
			SORTED_LIST.addAll(elementsB);
			elements.removeAll(elementsB);
		}
		
		VariousTypes.LOGGER.info(" # Build order:");
		SORTED_LIST.forEach(el -> VariousTypes.LOGGER.info(" # - "+el.registryName.toString()));
	}
	
	@Nullable
	public static SheetElement<?> get(Identifier registryName) { return ELEMENTS.getOrDefault(registryName, null); }
	
	public static Collection<SheetElement<?>> getAll() { return ELEMENTS.values(); }
	
	private static Supplier<List<SheetElement<?>>> listOf(SheetElement<?>... entries)
	{
		return () -> 
		{
			List<SheetElement<?>> list = Lists.newArrayList();
			for(SheetElement<?> entry : entries)
				list.add(entry);
			return list;
		};
	}
	
	// XXX Maybe tidy this up to be more like AbstractSheetModule?
	public static class SheetElement<T extends ISheetElement<?>>
	{
		private final Identifier registryName;
		private final Supplier<List<SheetElement<?>>> calcFirst;
		private final Supplier<T> supplier;
		
		public SheetElement(Identifier regIn, Supplier<List<SheetElement<?>>> orderIn, Supplier<T> supplierIn)
		{
			registryName = regIn;
			calcFirst = orderIn;
			supplier = supplierIn;
		}
		
		public static <T extends ISheetElement<C>, C extends Object> SheetElement<T> create(Identifier regIn, Supplier<List<SheetElement<?>>> orderIn, Supplier<T> supplierIn)
		{
			return new SheetElement<T>(regIn, orderIn, supplierIn);
		}
		
		public T make() { return supplier.get(); }
		
		public Identifier registryName() { return registryName; }
		
		/** Determines the point in the build process that this element is recalculated */
		public boolean canBuild(List<SheetElement<?>> built)
		{
			return calcFirst.get().isEmpty() || built.containsAll(calcFirst.get());
		}
		
		public boolean needsBuilt(SheetElement<?> element)
		{
			return calcFirst.get().stream().anyMatch(el -> el.registryName().equals(element.registryName()));
		}
	}
}
