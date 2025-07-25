package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import com.google.common.collect.Lists;
import com.lying.client.utility.VTUtilsClient;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleTemplates;
import com.lying.entity.AnimatedPlayerEntity;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSoundEvents;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.network.FinishCharacterCreationPacket;
import com.lying.reference.Reference;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.utility.VTUtils;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class CharacterCreationEditScreen extends HandledScreen<CharacterCreationScreenHandler>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final Vector2i backingSize = new Vector2i(450, 250);
	
	private final PlayerInventory inventory;
	private Optional<AnimatedPlayerEntity> animatedPlayer = Optional.empty();
	
	private Optional<DetailObject> detailObject = Optional.empty();
	private int scrollAmount = 0;
	
	private ButtonWidget confirmButton, previewButton;
	
	private final Map<ActiveElement, Pair<ButtonWidget, GuiAbstractList<?>>> tabs = new HashMap<>();
	private ActiveElement currentTab = ActiveElement.SPECIES;
	
	private ButtonWidget speciesButton, templatesButton, randomButton;
	private SpeciesListWidget speciesList;
	private TemplateListWidget templateList;
	
	public CharacterCreationEditScreen(CharacterCreationScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		this.inventory = inventory;
		animatedPlayer = Optional.of(AnimatedPlayerEntity.of(mc.player.getGameProfile(), mc.world));
		animatedPlayer.get().setCustomName(mc.player.getDisplayName());
		handler.setTestEntity(animatedPlayer.get());
	}
	
	public boolean shouldCloseOnEsc() { return false; }
	
	public boolean shouldPause() { return true; }
	
	public void init()
	{
		super.init();
		
		addDrawableChild(confirmButton = ButtonWidget.builder(translate("gui", "creator_confirm"), (button) -> 
		{
			confirmCharacterCreation(getScreenHandler());
			close();
		}).dimensions(0, 0, 50, 20).build());
		
		addDrawableChild(previewButton = ButtonWidget.builder(translate("gui", "creator_preview"), (button) -> 
		{
			CharacterCreationPreviewScreen preview = new CharacterCreationPreviewScreen(getScreenHandler(), inventory, this.title);
			animatedPlayer.ifPresent(character -> 
			{
				getScreenHandler().setTestEntity(character);
				preview.setCharacter(character);
			});
			mc.setScreen(preview);
		}).dimensions(0, 0, 50, 20).build());
		
		addDrawableChild(randomButton = new IconButton(0, 0, 20, 20, button -> 
		{
			mc.getSoundManager().play(PositionedSoundInstance.master(VTSoundEvents.SHEET_RANDOMISE.get(), 1F));
			getScreenHandler().copySheet(VTUtils.makeRandomSheet(mc.player, getScreenHandler().powerLimit));
			updateTemplateList();
		}, Reference.ModInfo.prefix("textures/gui/randomise.png"), Reference.ModInfo.prefix("textures/gui/randomise_hovered.png")));
		
		addDrawableChild(speciesButton = ButtonWidget.builder(translate("gui", "creator_species"), (button) -> setTab(ActiveElement.SPECIES)).dimensions(0, 0, 60, 20).build());
		addDrawableChild(templatesButton = ButtonWidget.builder(translate("gui", "creator_template"), (button) -> setTab(ActiveElement.TEMPLATES)).dimensions(0, 0, 60, 20).build());
		
		addSelectableChild(speciesList = new SpeciesListWidget(client, (backingSize.x / 3) * 2, 210, 0));
		VTSpeciesRegistry.instance().getAll().forEach(spec -> 
		{
			if(getScreenHandler().powerLimit < 0 || spec.power() <= getScreenHandler().powerLimit)
				speciesList.addEntry(spec, this);
		});
		
		addSelectableChild(templateList = new TemplateListWidget(client, speciesList.getWidth(), speciesList.getHeight(), 0));
		updateTemplateList();
		
		tabs.put(ActiveElement.SPECIES, Pair.of(speciesButton, speciesList));
		tabs.put(ActiveElement.TEMPLATES, Pair.of(templatesButton, templateList));
		setTab(ActiveElement.SPECIES);
	}
	
	public void setCharacter(AnimatedPlayerEntity character)
	{
		animatedPlayer = Optional.of(character);
	}
	
	protected void handledScreenTick()
	{
		animatedPlayer.ifPresent(player -> 
		{
			++player.age;
			player.tick();
		});
	}
	
	private void setTab(ActiveElement tab)
	{
		currentTab = tab;
		tabs.entrySet().forEach(entry -> 
		{
			ActiveElement key = entry.getKey();
			Pair<ButtonWidget, GuiAbstractList<?>> values = entry.getValue();
			GuiAbstractList<?> list = values.getSecond();
			values.getFirst().active = key != tab && !list.isEmpty();
			list.active = list.visible = key == tab && !list.isEmpty();
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
		int sheetPower = sheet.power();
		int powerLimit = getScreenHandler().powerLimit;
		VTTemplateRegistry.instance().getAll().forEach(tem -> 
		{
			/*
			 * Only display the template if 
			 * 	a. the sheet already has it
			 * 	b. the player is in Creative mode, and therefore not subject to power limits or preconditions
			 * 	c. the template's power is within budget and it's valid for the sheet in its present condition
			 */
			boolean valid = false;
			if(ModuleTemplates.hasTemplate(sheet, tem.registryName()))
				valid = true;
			else if(mc.player.isCreative())
				valid = true;
			else if(tem.validFor(sheet, mc.player) && (powerLimit < 0 || (sheetPower + tem.power()) <= powerLimit))
				valid = true;
			
			if(valid)
				templates.add(tem);
		});
		ModuleTemplates templateMod = sheet.module(VTSheetModules.TEMPLATES);
		templates.sort((a,b) -> 
		{
			// Sort applied templates to the top of the list, in order of application, to make removal easier
			boolean hasA = templateMod.contains(a.registryName());
			boolean hasB = templateMod.contains(b.registryName());
			if(hasA != hasB)
				return hasA ? -1 : 1;
			else if(hasA && hasB)
				return (int)Math.signum(templateMod.index(a.registryName()) - templateMod.index(b.registryName()));
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
		randomButton.setPosition((width + backingSize.x) / 2 - randomButton.getWidth() - 2, speciesButton.getY());
		
		confirmButton.setPosition(width / 2 - 150 + 1, height / 2 + 100);
		previewButton.setPosition(width / 2 - 150 - 1 - previewButton.getWidth(), confirmButton.getY());
		
		tabs.get(currentTab).getSecond().render(context, mouseX, mouseY, delta);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		animatedPlayer.ifPresent(owner -> VTUtilsClient.renderDisplayEntity(owner, context, (width / 2) - 150, (height / 2), -10F));
	}
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		int midX = backgroundWidth / 2;
		int midY = backgroundHeight / 2;
		
		context.drawCenteredTextWithShadow(textRenderer, this.title, (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 100, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Reference.ModInfo.translate("gui", "power_value", getScreenHandler().testSheet().power( ) + " / " + getScreenHandler().powerLimit), (backgroundWidth / 2) - 150, (backgroundHeight / 2) - 90, 0xFFFFFF);
		
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
	
	public static void confirmCharacterCreation(CharacterCreationScreenHandler handler)
	{
		FinishCharacterCreationPacket.send(handler.species(), handler.templates());
	}
	
	private static enum ActiveElement
	{
		SPECIES,
		TEMPLATES;
	}
}
