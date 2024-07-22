package com.lying.component.element;

import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.type.TypeSet;

public class ElementAbilitySet extends AbilitySet implements ISheetElement<AbilitySet>
{
	private AbilitySet activated = new AbilitySet();
	
	public SheetElement<?> registry(){ return VTSheetElements.ABILITES; }
	
	public static AbilitySet getActivated(CharacterSheet sheet) { return ((ElementAbilitySet)sheet.element(VTSheetElements.ABILITES)).activated(); }
	
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
