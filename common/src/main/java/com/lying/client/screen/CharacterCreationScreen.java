package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.client.utility.VTUtilsClient;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleTemplates;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.network.VTPacketHandler;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.utility.VTUtils;

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
	
	private Optional<DetailObject> detailObject = Optional.empty();
	private int scrollAmount = 0;
	
	private ButtonWidget confirmButton;
	
	private ButtonWidget speciesButton, templatesButton;
	private SpeciesListWidget speciesList;
	private TemplateListWidget templateList;
	
	public CharacterCreationScreen(CharacterCreationScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		testSheet = handler.testSheet();
	}
	
	@SuppressWarnings("removal")
	public void init()
	{
		super.init();
		
		addDrawableChild(confirmButton = ButtonWidget.builder(translate("gui", "creator_confirm"), (button) -> 
		{
			RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager());
			CharacterCreationScreenHandler handler = getScreenHandler();
			buffer.writeIdentifier(handler.species() == null ? new Identifier("debug:no_species") : handler.species());
			
			buffer.writeInt(handler.templates().size());
			handler.templates().forEach(tem -> buffer.writeIdentifier(tem));
			
			NetworkManager.sendToServer(VTPacketHandler.FINISH_CHARACTER_ID, buffer);
			close();
		}).dimensions(0, 0, 40, 20).build());
		
		addDrawableChild(speciesButton = ButtonWidget.builder(translate("gui", "creator_species"), (button) -> 
		{
			speciesList.visible = speciesList.active = true;
			templateList.visible = templateList.active = false;
		}).dimensions(0, 0, 60, 20).build());
		addDrawableChild(templatesButton = ButtonWidget.builder(translate("gui", "creator_template"), (button) -> 
		{
			templateList.visible = templateList.active = true;
			speciesList.visible = speciesList.active = false;
		}).dimensions(0, 0, 60, 20).build());
		
		addDrawableChild(speciesList = new SpeciesListWidget(client, 300, 210, 0));
		VTSpeciesRegistry.instance().getAll().forEach(spec -> 
		{
			if(spec.power() <= VariousTypes.POWER)
				speciesList.addEntry(spec);
		});
		
		addDrawableChild(templateList = new TemplateListWidget(client, 300, 210, 0));
		updateTemplateList();
		templateList.visible = templateList.active = false;
	}
	
	public void setSpecies(Species species)
	{
		getScreenHandler().setSpecies(species);
		updateTemplateList();
	}
	
	public void addTemplate(Template template)
	{
		getScreenHandler().addTemplate(template);
		updateTemplateList();
	}
	
	public void removeTemplate(Template template)
	{
		getScreenHandler().removeTemplate(template);
		updateTemplateList();
	}
	
	protected void updateTemplateList()
	{
		templateList.clear();
		CharacterSheet sheet = getScreenHandler().testSheet();
		List<Template> templates = Lists.newArrayList();
		VTTemplateRegistry.instance().getAll().forEach(tem -> 
		{
			if(ModuleTemplates.hasTemplate(sheet, tem.registryName()) || (tem.power() <= (VariousTypes.POWER - sheet.power()) && tem.validFor(sheet, mc.player)))
				templates.add(tem);
		});
		templates.sort((a,b) -> 
		{
			if(ModuleTemplates.hasTemplate(sheet, a.registryName()) != ModuleTemplates.hasTemplate(sheet, b.registryName()))
				return ModuleTemplates.hasTemplate(sheet, a.registryName()) ? -1 : 1;
			else
				return VTUtils.stringComparator(a.displayName().getString(), b.displayName().getString());
		});
		templates.forEach(tem -> templateList.addEntry(tem));
	}
	
	public void setDetail(@Nullable DetailObject obj)
	{
		detailObject = obj == null ? Optional.empty() : Optional.of(obj);
		scrollAmount = 0;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if(detailObject.isPresent())
		{
			scrollAmount += verticalAmount * textRenderer.fontHeight;
			return true;
		}
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		applyBlur(delta);
		
		Optional<Species> species = VTSpeciesRegistry.instance().get(getScreenHandler().species());
		if(species.isPresent())
			context.drawTexture(species.get().creatorBackground(), (width - 450) / 2, (height - 250) / 2, 0, 0, 450, 250, 512, 512);
		
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
		
		speciesList.setPosition((width + 450) / 2 - speciesList.getWidth(), (height + 250) / 2 - speciesList.getHeight() - 2);
		templateList.setPosition(speciesList.getX(), speciesList.getY());
		speciesButton.setPosition(speciesList.getX(), height / 2 - 110);
		templatesButton.setPosition(speciesButton.getX() + speciesButton.getWidth() + 5, speciesButton.getY());
		
		speciesButton.active = !VTSpeciesRegistry.instance().getAllIDs().isEmpty();
		templatesButton.active = !VTTemplateRegistry.instance().getAllIDs().isEmpty();
		
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
		int midX = backgroundWidth / 2;
		int midY = backgroundHeight / 2;
		
		Text ownerName = mc.player.getDisplayName();
		context.drawCenteredTextWithShadow(textRenderer, ownerName, (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 100, 0xFFFFFF);
		
		detailObject.ifPresent(detail -> 
		{
			int maxWidth = Math.min(400, context.getScaledWindowWidth() / 2);
			int detailHeight = detail.totalHeight(maxWidth);
			int detailY = midY;
			if(detailHeight > context.getScaledWindowHeight())
			{
				scrollAmount = (int)Math.clamp(scrollAmount, -detailHeight + (int)(backgroundHeight * 0.75), 0);
				detailY = scrollAmount;
			}
			else
				detailY = midY - detailHeight / 2;
			
			detail.render(context, midX, detailY, maxWidth);
		});
	}
}
