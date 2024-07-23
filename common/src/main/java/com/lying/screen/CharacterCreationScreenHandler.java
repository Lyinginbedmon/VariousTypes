package com.lying.screen;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleTemplates;
import com.lying.init.VTScreenHandlerTypes;
import com.lying.init.VTSheetModules;
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
	
	private CharacterSheet testSheet;
	
	public CharacterCreationScreenHandler(int syncId, PlayerEntity player)
	{
		super(VTScreenHandlerTypes.CREATION_SCREEN_HANDLER.get(), syncId);
		thePlayer = player;
		
		Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
		sheetOpt.ifPresentOrElse(sheet -> 
		{
			sheet.module(VTSheetModules.SPECIES).getMaybe().ifPresent(spec -> speciesId = spec.registryName());
			sheet.module(VTSheetModules.TEMPLATES).get().forEach(tem -> templateIds.add(tem.registryName())); 
		}, () -> 
		{
			speciesId = VTSpeciesRegistry.instance().get(VTSpeciesRegistry.DEFAULT_SPECIES).isPresent() ? VTSpeciesRegistry.DEFAULT_SPECIES : VTSpeciesRegistry.instance().getAllIDs().stream().findFirst().get();
		});
		
		buildSheet();
	}
	
	public ItemStack quickMove(PlayerEntity var1, int var2) { return ItemStack.EMPTY; }
	
	public boolean canUse(PlayerEntity var1) { return true; }
	
	/** Returns the current calculated character sheet */
	public CharacterSheet testSheet() { return testSheet; }
	
	/** Constructs a character sheet from the current parameters */
	protected void buildSheet()
	{
		CharacterSheet sheet = new CharacterSheet(thePlayer);
		if(speciesId != null)
			sheet.module(VTSheetModules.SPECIES).set(speciesId);
		if(!templateIds.isEmpty())
		{
			ModuleTemplates templates = sheet.module(VTSheetModules.TEMPLATES);
			templateIds.forEach(id -> templates.add(id));
		}
		sheet.buildSheet();
		testSheet = sheet;
	}
	
	@Nullable
	public Identifier species() { return speciesId; }
	
	public List<Identifier> templates() { return templateIds; }
	
	/** Sets the current selected species, then validates. */
	public void setSpecies(Species species)
	{
		speciesId = species.registryName();
		validateTemplates();
		buildSheet();
	}
	
	/** Adds the given template to the list of applied templates. */
	public void addTemplate(Template template)
	{
		if(!templateIds.contains(template.registryName()))
		{
			templateIds.add(template.registryName());
			buildSheet();
		}
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
				templateIds.remove(index);
			else
			{
				templateIds.remove(index);
				validateTemplates();
			}
			
			buildSheet();
		}
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
			sheet.module(VTSheetModules.SPECIES).set(speciesId);
		
		ModuleTemplates templates = sheet.module(VTSheetModules.TEMPLATES);
		for(int i=0; i<templateIds.size(); i++)
		{
			Identifier name = templateIds.get(i);
			VTTemplateRegistry.instance().get(name).ifPresent(tem -> 
			{
				if(tem.validFor(sheet, thePlayer))
					templates.add(name);
			});
		}
		
		templateIds.clear();
		templates.get().forEach(tem -> templateIds.add(tem.registryName()));
	}
}
