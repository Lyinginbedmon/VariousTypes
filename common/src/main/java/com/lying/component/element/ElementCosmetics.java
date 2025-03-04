package com.lying.component.element;

import java.util.List;

import com.lying.client.utility.CosmeticSet;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetElements.SheetElement;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.entity.LivingEntity;

public class ElementCosmetics implements ISheetElement<CosmeticSet>
{
	public static final Event<GetPlayerCosmeticsEvent> GET_LIVING_COSMETICS_EVENT = EventFactory.createLoop(GetPlayerCosmeticsEvent.class);
	
	@FunctionalInterface
	public static interface GetPlayerCosmeticsEvent
	{
		void getCosmeticsFor(LivingEntity player, final CosmeticSet set);
	}
	
	private CosmeticSet set = CosmeticSet.of(List.of());
	
	public SheetElement<?> registry() { return VTSheetElements.COSMETICS; }
	
	public CosmeticSet value() { return set; }
	
	public void rebuild(CharacterSheet sheet)
	{
		set.clear();
		sheet.getOwner().ifPresent(owner -> GET_LIVING_COSMETICS_EVENT.invoker().getCosmeticsFor(owner, set));
		sheet.modules().forEach(module -> module.affect(this));
	}
}
