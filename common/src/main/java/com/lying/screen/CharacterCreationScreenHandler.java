package com.lying.screen;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.component.CharacterSheet;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.species.Species;
import com.lying.template.Template;

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
	
	/** Sets the current selected species, then validates. */
	public void setSpecies(Species species)
	{
		speciesId = species.registryName();
		validateTemplates();
	}
	
	/** Adds the given template to the list of applied templates. */
	public void addTemplate(Template template)
	{
		if(!templateIds.contains(template.registryName()))
			templateIds.add(template.registryName());
	}
	
	/**
	 * Tests each applied template to ensure it is still valid.<br>
	 * Valid templates are retained, all others discarded.
	 */
	private void validateTemplates()
	{
		if(templateIds.isEmpty())
			return;
		
		CharacterSheet sheet = new CharacterSheet(thePlayer);
		if(speciesId != null)
			sheet.setSpecies(speciesId);
		
		for(int i=0; i<templateIds.size(); i++)
		{
			Identifier name = templateIds.get(i);
			VTTemplateRegistry.instance().get(name).ifPresent(tem -> 
			{
				if(tem.validFor(sheet, thePlayer))
					sheet.addTemplate(name);
			});
		}
		
		templateIds.clear();
		sheet.getTemplates().forEach(tem -> templateIds.add(tem.registryName()));
	}
	
	/**
	 * Removes the given template from the list of applied templates, then validates.
	 */
	public void removeTemplate(Template template)
	{
		Identifier regName = template.registryName();
		if(templateIds.contains(regName))
		{
			int index = templateIds.indexOf(regName);
			/*
			 * If a template is removed from earlier in the sequence,
			 * ensure that all subsequent templates are still valid.
			 */
			if(index == templateIds.size() - 1)
			{
				templateIds.remove(index);
				return;
			}
			
			templateIds.remove(index);
			validateTemplates();
			return;
		}
	}
}
