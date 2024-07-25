package com.lying.component.element;

import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

public class ElementAbilitySet extends AbilitySet implements ISheetElement<AbilitySet>
{
	private AbilitySet activated = new AbilitySet();
	
	public SheetElement<?> registry(){ return VTSheetElements.ABILITES; }
	
	public static AbilitySet getActivated(CharacterSheet sheet) { return ((ElementAbilitySet)sheet.element(VTSheetElements.ABILITES)).activated(); }
	
	public NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.put("Activated", activated.writeToNbt());
		return nbt;
	}
	
	public void readFromNbt(NbtCompound nbt)
	{
		activated = AbilitySet.readFromNbt(nbt.getList("Activated", NbtElement.COMPOUND_TYPE));
	}
	
	public AbilitySet value() { return this; }
	
	public AbilitySet activated() { return activated; }
	
	public void rebuild(CharacterSheet sheet)
	{
		clear();
		
		(sheet.<TypeSet>element(VTSheetElements.TYPES)).abilities().forEach(inst -> add(inst.copy()));
		
		sheet.modules().forEach(module -> module.affect(this));
		
		mergeActivated(activated);
	}
}
