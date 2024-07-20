package com.lying.client.screen;

import java.util.Optional;

import com.lying.client.utility.VTUtilsClient;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSpeciesRegistry;
import com.lying.network.VTPacketHandler;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.species.Species;

import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CharacterCreationScreen extends HandledScreen<CharacterCreationScreenHandler>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	private CharacterSheet testSheet;
	
	private ButtonWidget confirmButton;
	
	public CharacterCreationScreen(CharacterCreationScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		testSheet = handler.testSheet();
	}
	
	@SuppressWarnings("removal")
	public void init()
	{
		super.init();
		
		addDrawableChild(confirmButton = ButtonWidget.builder(Text.literal("Confirm"), (button) -> 
		{
			RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager());
			CharacterCreationScreenHandler handler = getScreenHandler();
			buffer.writeIdentifier(handler.species() == null ? new Identifier("debug:no_species") : handler.species());
			
			buffer.writeInt(handler.templates().size());
			handler.templates().forEach(tem -> buffer.writeIdentifier(tem));
			
			NetworkManager.sendToServer(VTPacketHandler.FINISH_CHARACTER_ID, buffer);
			close();
		}).dimensions(0, 0, 40, 20).build());
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		applyBlur(delta);
		
		Optional<Species> species = VTSpeciesRegistry.instance().get(getScreenHandler().species());
		if(species.isPresent())
			context.drawTexture(species.get().creatorBackground(), (width - 450) / 2, (height - 250) / 2, 0, 0, 450, 250, 512, 512);
		
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
		
		confirmButton.setPosition(width / 2 - 150 - (confirmButton.getWidth() / 2), height / 2 + 100);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		testSheet.getOwner().ifPresent(owner -> 
		{
			if(owner.getType() == EntityType.PLAYER)
				VTUtilsClient.renderDemoEntity((PlayerEntity)owner, context, mouseX, mouseY, (width / 2) - 150, (height / 2));
		});
	}
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		Text ownerName = mc.player.getDisplayName();
		context.drawCenteredTextWithShadow(textRenderer, ownerName, (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 100, 0xFFFFFF);
	}
}
