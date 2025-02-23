package com.lying.component.element;

import com.lying.client.utility.CosmeticSet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;

public class ElementCosmetics implements ISheetElement<CosmeticSet>
{
	private CosmeticSet set = new CosmeticSet();
	private boolean cached = false;
	
	public SheetElement<?> registry() { return VTSheetElements.COSMETICS; }
	
	public CosmeticSet value() { return set; }
	
	public boolean hasBeenCached() { return cached; }
	
	public void rebuild(CharacterSheet sheet)
	{
		set.clear();
		cached = false;
	}
	
	public void set(CosmeticSet cosmeticsIn)
	{
		set.clone(cosmeticsIn);
		cached = true;
	}
}
