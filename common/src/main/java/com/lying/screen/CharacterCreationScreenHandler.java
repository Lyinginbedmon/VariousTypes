package com.lying.screen;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.component.CharacterSheet;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSpeciesRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class CharacterCreationScreenHandler extends ScreenHandler
{
	private final PlayerEntity thePlayer;
	
	private Identifier speciesId;
	private List<Identifier> templateIds = Lists.newArrayList();
	
	public CharacterCreationScreenHandler(int syncId, PlayerEntity player)
	{
		super(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), syncId);
		thePlayer = player;
		
		speciesId = VTSpeciesRegistry.instance().get(VTSpeciesRegistry.DEFAULT_SPECIES).isPresent() ? VTSpeciesRegistry.DEFAULT_SPECIES : VTSpeciesRegistry.instance().getAllIDs().stream().findFirst().get();
	}
	
	public ItemStack quickMove(PlayerEntity var1, int var2) { return ItemStack.EMPTY; }
	
	public boolean canUse(PlayerEntity var1) { return true; }
	
	public CharacterSheet testSheet()
	{
		CharacterSheet sheet = new CharacterSheet(thePlayer);
		if(speciesId != null)
			sheet.setSpecies(speciesId);
		if(!templateIds.isEmpty())
			templateIds.forEach(id -> sheet.addTemplate(id));
		return sheet;
	}
	
	@Nullable
	public Identifier species() { return speciesId; }
	
	public List<Identifier> templates() { return templateIds; }
}
