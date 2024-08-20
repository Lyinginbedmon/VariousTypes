package com.lying.component.element;

import com.lying.ability.Ability;
import com.lying.ability.AbilityBreathing;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;
import com.lying.type.ActionHandler;
import com.lying.type.TypeSet;
import com.lying.utility.ServerEvents.SheetEvents;

import net.minecraft.fluid.Fluid;

public class ElementActionHandler extends ActionHandler implements ISheetElement<ActionHandler>
{
	public ElementActionHandler()
	{
		super();
		clone(STANDARD_SET);
	}
	
	public SheetElement<?> registry() { return VTSheetElements.ACTIONS; }
	
	public static boolean canBreathe(CharacterSheet sheet, Fluid fluid, boolean hasWaterBreathing) { return hasWaterBreathing || sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).canBreathe(fluid); }
	
	public ActionHandler value() { return this; }
	
	public void rebuild(CharacterSheet sheet)
	{
		clear();
		
		TypeSet typeSet = sheet.elementValue(VTSheetElements.TYPES);
		typeSet.forEach(type -> type.actions().stack(this, typeSet));
		
		AbilitySet abilitySet = sheet.elementValue(VTSheetElements.ABILITIES);
		for(Ability ability : new Ability[] {VTAbilities.BREATHE_FLUID.get(), VTAbilities.SUFFOCATE_FLUID.get()})
			abilitySet.getAbilitiesOfType(ability.registryName()).forEach(inst -> ((AbilityBreathing)ability).applyToActions(this, inst));
		
		SheetEvents.AFTER_REBUILD_ACTIONS_EVENT.invoker().affectActions(this, abilitySet, sheet.getOwner());
	}
}
