package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

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
import com.mojang.datafixers.util.Pair;

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

public class CharacterCreationEditScreen extends HandledScreen<CharacterCreationScreenHandler>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final Vector2i backingSize = new Vector2i(450, 250);
	
	private final PlayerInventory inventory;
	
	private Optional<DetailObject> detailObject = Optional.empty();
	private int scrollAmount = 0;
	
	private ButtonWidget confirmButton, previewButton;
	
	private final Map<ActiveElement, Pair<ButtonWidget, GuiAbstractList<?>>> tabs = new HashMap<>();
	private ActiveElement currentTab = ActiveElement.SPECIES;
	
	private ButtonWidget speciesButton, templatesButton;
	private SpeciesListWidget speciesList;
	private TemplateListWidget templateList;
	
	public CharacterCreationEditScreen(CharacterCreationScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.inventory = inventory;
	}
	
	public boolean shouldCloseOnEsc() { return false; }
	
	public void init()
	{
		super.init();
		
		addDrawableChild(confirmButton = ButtonWidget.builder(translate("gui", "creator_confirm"), (button) -> 
		{
			confirmCharacterCreation(getScreenHandler());
			close();
		}).dimensions(0, 0, 50, 20).build());
		
		addDrawableChild(previewButton = ButtonWidget.builder(Text.literal("Preview"), (button) -> 
		{
			mc.setScreen(new CharacterCreationPreviewScreen(getScreenHandler(), inventory, this.title));
		}).dimensions(0, 0, 50, 20).build());
		
		addDrawableChild(speciesButton = ButtonWidget.builder(translate("gui", "creator_species"), (button) -> setTab(ActiveElement.SPECIES)).dimensions(0, 0, 60, 20).build());
		addDrawableChild(templatesButton = ButtonWidget.builder(translate("gui", "creator_template"), (button) -> setTab(ActiveElement.TEMPLATES)).dimensions(0, 0, 60, 20).build());
		
		addSelectableChild(speciesList = new SpeciesListWidget(client, (backingSize.x / 3) * 2, 210, 0));
		VTSpeciesRegistry.instance().getAll().forEach(spec -> 
		{
			if(spec.power() <= VariousTypes.POWER)
				speciesList.addEntry(spec, this);
		});
		
		addSelectableChild(templateList = new TemplateListWidget(client, speciesList.getWidth(), speciesList.getHeight(), 0));
		updateTemplateList();
		
		tabs.put(ActiveElement.SPECIES, Pair.of(speciesButton, speciesList));
		tabs.put(ActiveElement.TEMPLATES, Pair.of(templatesButton, templateList));
		setTab(ActiveElement.SPECIES);
	}
	
	private void setTab(ActiveElement tab)
	{
		currentTab = tab;
		tabs.entrySet().forEach(entry -> 
		{
			ActiveElement key = entry.getKey();
			Pair<ButtonWidget, GuiAbstractList<?>> values = entry.getValue();
			values.getFirst().active = key != tab;
			GuiAbstractList<?> list = values.getSecond();
			list.active = list.visible = key == tab && list.entryCount() > 0;
			list.setScrollAmount(0D);
		});
	}
	
	public boolean isSpecies(Species species) { return getScreenHandler().species().equals(species.registryName()); }
	
	public void setSpecies(Species species)
	{
		getScreenHandler().setSpecies(species);
		updateTemplateList();
	}
	
	public boolean hasTemplate(Template template) { return getScreenHandler().templates().contains(template.registryName()); }
	
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
		templates.forEach(tem -> templateList.addEntry(tem, this));
	}
	
	public void setDetail(@Nullable DetailObject obj)
	{
		detailObject = obj == null ? Optional.empty() : Optional.of(obj);
		scrollAmount = 0;
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		setDetail(null);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount)
	{
		if(detailObject.isPresent())
		{
			scrollAmount += verticalAmount * textRenderer.fontHeight;
			return true;
		}
		
		GuiAbstractList<?> currentList = tabs.get(currentTab).getSecond();
		if(currentList.isMouseOver(mouseX, mouseY))
			currentList.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		applyBlur(delta);
		
		Optional<Species> species = VTSpeciesRegistry.instance().get(getScreenHandler().species());
		if(species.isPresent())
			context.drawTexture(species.get().creatorBackground(), (width - 450) / 2, (height - 250) / 2, 0, 0, backingSize.x, backingSize.y, 512, 512);
		
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
		
		speciesList.setPosition((width + backingSize.x) / 2 - speciesList.getWidth(), (height + backingSize.y) / 2 - speciesList.getHeight() - 2);
		templateList.setPosition(speciesList.getX(), speciesList.getY());
		speciesButton.setPosition(speciesList.getX() + 4, height / 2 - 110);
		templatesButton.setPosition(speciesButton.getX() + speciesButton.getWidth() + 5, speciesButton.getY());
		
		confirmButton.setPosition(width / 2 - 150 + 1, height / 2 + 100);
		previewButton.setPosition(width / 2 - 150 - 1 - previewButton.getWidth(), confirmButton.getY());
		
		tabs.get(currentTab).getSecond().render(context, mouseX, mouseY, delta);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		getScreenHandler().testSheet().getOwner().ifPresent(owner -> 
		{
			if(owner.getType() == EntityType.PLAYER)
				VTUtilsClient.renderDemoEntity((PlayerEntity)owner, context, mouseX, mouseY, (width / 2) - 150, (height / 2));
		});
	}
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		int midX = backgroundWidth / 2;
		int midY = backgroundHeight / 2;
		
		context.drawCenteredTextWithShadow(textRenderer, this.title, (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 100, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal(String.valueOf(getScreenHandler().testSheet().power())), (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 90, 0xFFFFFF);
		
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
	
	@SuppressWarnings("removal")
	public static void confirmCharacterCreation(CharacterCreationScreenHandler handler)
	{
		RegistryByteBuf buffer = new RegistryByteBuf(Unpooled.buffer(), mc.player.getRegistryManager());
		buffer.writeIdentifier(handler.species() == null ? new Identifier("debug:no_species") : handler.species());
		
		buffer.writeInt(handler.templates().size());
		handler.templates().forEach(tem -> buffer.writeIdentifier(tem));
		
		NetworkManager.sendToServer(VTPacketHandler.FINISH_CHARACTER_ID, buffer);
	}
	
	private static enum ActiveElement
	{
		SPECIES,
		TEMPLATES;
	}
}
