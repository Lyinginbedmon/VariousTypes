package com.lying.client.init;

import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilitySet;
import com.lying.ability.ICosmeticSupplier;
import com.lying.client.event.RenderEvents;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;

import net.minecraft.util.Identifier;

public class VTAbilitiesClient
{
	public static void init()
	{
		VTAbilities.abilities().forEach(entry -> 
		{
			Ability ab = entry.get();
			if(ab instanceof ICosmeticSupplier)
			{
				Identifier registry = ab.registryName();
				ICosmeticSupplier supplier = (ICosmeticSupplier)ab;
				RenderEvents.GET_LIVING_COSMETICS_EVENT.register((living, type, set) -> 
				{
					VariousTypes.getSheet(living).ifPresent(sheet -> 
					{
						AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
						if(abilities.hasAbility(registry))
							abilities.getAbilitiesOfType(registry).forEach(inst -> supplier.getCosmetics(inst).forEach(cos -> set.add(cos)));
					});
				});
			}
		});
	}
}
