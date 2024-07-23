package com.lying.init;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Supplier;
import com.lying.component.element.ElementAbilitySet;
import com.lying.component.element.ElementActionHandler;
import com.lying.component.element.ElementHome;
import com.lying.component.element.ElementTypeSet;
import com.lying.component.element.ISheetElement;

import net.minecraft.util.Identifier;

/** Defines discrete modifiable values within a character sheet */
public class VTSheetElements
{
	private static final Map<Identifier, SheetElement<?>> ELEMENTS = new HashMap<>();
	
	public static final SheetElement<ElementHome> HOME_DIM	= register("home", 0, ElementHome::new);
	public static final SheetElement<ElementTypeSet> TYPES	= register("types", 1, ElementTypeSet::new);
	public static final SheetElement<ElementAbilitySet> ABILITES	= register("abilities", 2, ElementAbilitySet::new);
	public static final SheetElement<ElementActionHandler> ACTIONS	= register("actions", 3, ElementActionHandler::new);
	
	public static <T extends ISheetElement<C>, C extends Object> SheetElement<T> register(String name, int order, Supplier<T> supplier)
	{
		Identifier registryName = prefix(name);
		SheetElement<T> element = SheetElement.create(registryName, order, supplier);
		ELEMENTS.put(registryName, element);
		return element;
	}
	
	public static void init() { }
	
	@Nullable
	public static SheetElement<?> get(Identifier registryName) { return ELEMENTS.getOrDefault(registryName, null); }
	
	public static Collection<SheetElement<?>> getAll() { return ELEMENTS.values(); }
	
	// XXX Maybe tidy this up to be more like AbstractSheetModule
	public static class SheetElement<T extends ISheetElement<?>>
	{
		private final Identifier registryName;
		private final int order;	// XXX Change to a predicate-based approach to eliminate mandatory ordering?
		private final Supplier<T> supplier;
		
		public SheetElement(Identifier regIn, int orderIn, Supplier<T> supplierIn)
		{
			registryName = regIn;
			order = orderIn;
			supplier = supplierIn;
		}
		
		public static <T extends ISheetElement<C>, C extends Object> SheetElement<T> create(Identifier regIn, int orderIn, Supplier<T> supplierIn)
		{
			return new SheetElement<T>(regIn, orderIn, supplierIn);
		}
		
		public T make() { return supplier.get(); }
		
		public Identifier registryName() { return registryName; }
		
		/** Determines the point in the build process that this element is recalculated */
		public int buildOrder() { return order; }
	}
}
