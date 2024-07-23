package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;

import com.lying.screen.CharacterCreationScreenHandler;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class CharacterCreationPreviewScreen extends CharacterSheetDisplayScreen<CharacterCreationScreenHandler>
{
	private final PlayerInventory inventory;
	
	public CharacterCreationPreviewScreen(CharacterCreationScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler.testSheet(), handler, inventory, title);
		this.inventory = inventory;
	}
	
	public boolean shouldCloseOnEsc() { return false; }
	
	public void init()
	{
		super.init();
		
		int midX = width / 2;
		int midY = height / 2;
		addDrawableChild(ButtonWidget.builder(translate("gui", "creator_back"), button -> mc.setScreen(new CharacterCreationEditScreen(getScreenHandler(), this.inventory, this.title))).dimensions(midX - 55, midY - 125, 50, 20).build());
		addDrawableChild(ButtonWidget.builder(translate("gui", "creator_confirm"), button -> 
		{
			CharacterCreationEditScreen.confirmCharacterCreation(getScreenHandler());
			close();
		}).dimensions(midX + 5, midY - 125, 50, 20).build());
	}
}
