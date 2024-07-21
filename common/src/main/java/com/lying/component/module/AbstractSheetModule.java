package com.lying.component.module;

import com.lying.component.element.ISheetElement;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/** A component that alters sheet elements in a character sheet, stored in NBT. */
public abstract class AbstractSheetModule
{
	private final Identifier regName;
	private final int buildOrder;
	
	protected AbstractSheetModule(Identifier nameIn, int order)
	{
		regName = nameIn;
		buildOrder = order;
	}
	
	public Identifier registryName() { return regName; }
	
	/** The order in which this module should be applied during rebuilding.<br>The higher the value, the later in the rebuild */
	public int buildOrder() { return buildOrder; }
	
	public abstract int power();
	
	public abstract void clear();
	
	/** Applies changes from this module to the given sheet element */
	public abstract void affect(ISheetElement<?> element);
	
	public final NbtCompound write(NbtCompound nbt)
	{
		nbt.putString("ID", registryName().toString());
		NbtCompound data = writeToNbt(new NbtCompound());
		if(!data.isEmpty())
			nbt.put("Data", data);
		return nbt;
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt) { return nbt; }
	
	public final void read(NbtCompound nbt)
	{
		clear();
		readFromNbt(nbt);
	}
	
	protected void readFromNbt(NbtCompound nbt) { }
}
