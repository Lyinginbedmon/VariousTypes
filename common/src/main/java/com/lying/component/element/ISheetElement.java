package com.lying.component.element;

import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements.SheetElement;

/** A rebuildable holder object that a character sheet is currently referencing. Not stored in memory. */
public interface ISheetElement<T>
{
	public SheetElement<?> registry();
	
	public T value();
	
	/** Rebuilds this element within the given character sheet */
	public default void rebuild(CharacterSheet sheetIn) { }
}
