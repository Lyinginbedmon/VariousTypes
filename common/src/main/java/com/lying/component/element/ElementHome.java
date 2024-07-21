package com.lying.component.element;

import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

public class ElementHome implements ISheetElement<RegistryKey<World>>
{
	private RegistryKey<World> dimension = World.OVERWORLD;
	
	public SheetElement<?> registry() { return VTSheetElements.HOME_DIM; }
	
	public RegistryKey<World> value() { return dimension; }
	
	public void set(RegistryKey<World> worldIn) { dimension = worldIn; }
	
	public void rebuild(CharacterSheet sheet)
	{
		set(World.OVERWORLD);
		
		sheet.modules().forEach(module -> module.affect(this));
	}
}
