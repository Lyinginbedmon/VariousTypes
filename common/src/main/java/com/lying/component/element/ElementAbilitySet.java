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
	
	public AbilitySet value() { return this; }
	
	public AbilitySet activated() { return activated; }
	
	public void rebuild(CharacterSheet sheet)
	{
		clear();
		
		(sheet.<TypeSet>element(VTSheetElements.TYPES)).abilities().forEach(inst -> add(inst.copy()));
		
		sheet.modules().forEach(module -> module.affect(this));
//		// Source initial types from species, if any
//		types = !sheet.hasASpecies() ? new TypeSet(VTTypes.HUMAN.get()) : sheet.getSpecies().get().types().copy();
//		
//		// Apply all custom types
//		// XXX How should custom types be applied? Replace, merge, overrule all, etc.?
//		if(!sheet.customTypes.isEmpty())
//			types = sheet.customTypes.copy();
//		
//		// Apply templates to types
//		for(Template template : sheet.getAppliedTemplates())
//			template.applyTypeOperations(types);
		
		mergeActivated(activated);
	}
}
