package com.lying.component.element;

import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.init.VTTypes;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;
import com.lying.utility.ServerEvents.SheetEvents;

public class ElementTypeSet extends TypeSet implements ISheetElement<TypeSet>
{
	public SheetElement<?> registry() { return VTSheetElements.TYPES; }
	
	public TypeSet value() { return this; }
	
	public void rebuild(CharacterSheet sheet)
	{
		clear();
		
		sheet.modules().forEach(module -> module.affect(this));
		
		if(ofTier(Tier.SUPERTYPE).isEmpty())
			add(VTTypes.HUMAN.get());
		
		SheetEvents.GET_TYPES_EVENT.invoker().affectTypes(sheet.getOwner(), sheet.element(VTSheetElements.HOME_DIM), this);
	}
}
