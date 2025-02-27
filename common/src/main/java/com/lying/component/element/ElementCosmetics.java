package com.lying.component.element;

import java.util.List;

import com.lying.client.utility.CosmeticSet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;

public class ElementCosmetics implements ISheetElement<CosmeticSet>
{
	private CosmeticSet set = CosmeticSet.of(List.of());
	private boolean cached = false;
	
	public SheetElement<?> registry() { return VTSheetElements.COSMETICS; }
	
	public CosmeticSet value() { return set; }
	
	public boolean hasBeenCached() { return cached; }
	
	public void rebuild(CharacterSheet sheet)
	{
		set.clear();
		sheet.modules().forEach(module -> module.affect(this));
		cached = false;
	}
	
	public void set(CosmeticSet cosmeticsIn)
	{
		set.addAll(cosmeticsIn);
		cached = true;
	}
}
