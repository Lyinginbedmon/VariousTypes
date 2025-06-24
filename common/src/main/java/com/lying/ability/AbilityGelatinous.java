package com.lying.ability;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.data.VTTags;
import com.lying.event.PlayerEvents;
import com.lying.init.VTCosmetics;
import com.lying.init.VTSheetElements;
import com.lying.utility.Cosmetic;

import net.minecraft.util.Identifier;

public class AbilityGelatinous extends Ability implements ICosmeticSupplier
{
	public AbilityGelatinous(Identifier regName, Category catIn) {
		super(regName, catIn);
	}
	
	public void registerEventHandlers()
	{
		PlayerEvents.MODIFY_DAMAGE_TAKEN_EVENT.register((player, damage, amount) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isPresent() && sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				if(damage.isIn(VTTags.PHYSICAL))
					return 0.85F;
			
			return 1F;
		});
	}
	
	public List<Cosmetic> getCosmetics(AbilityInstance inst) { return List.of(VTCosmetics.MISC_GELATINOUS.get()); }
}