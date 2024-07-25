package com.lying.component.element;

import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements.SheetElement;

import net.minecraft.nbt.NbtCompound;

/** A rebuildable holder object that a character sheet is currently referencing. Not typically stored in memory. */
public interface ISheetElement<T>
{
	public SheetElement<?> registry();
	
	public T value();
	
	/** Rebuilds this element within the given character sheet */
	public default void rebuild(CharacterSheet sheetIn) { }
	
	public default NbtCompound writeToNbt(NbtCompound nbt) { return nbt; }
	
	public default void readFromNbt(NbtCompound nbt) { }
}
