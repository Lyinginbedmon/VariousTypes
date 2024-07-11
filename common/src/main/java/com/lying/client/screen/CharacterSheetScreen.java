package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.joml.Vector2i;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.component.CharacterSheet;
import com.lying.screen.CharacterSheetScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.ActionHandler;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;
import com.lying.utility.VTUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CharacterSheetScreen extends HandledScreen<CharacterSheetScreenHandler>
{
	protected static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	
	private PlayerEntity player;
	private final DynamicRegistryManager manager;
	
	private ButtonWidget typeButton, speciesButton, templatesButton;
	private ButtonWidget[] abilityButtons = new ButtonWidget[5];
	
	private final CharacterSheet sheet;
	private final int power;
	private final Optional<OwnerStats> ownerStats;
	private final Optional<Species> species;
	private final List<Template> templates;
	private final TypeSet types;
	private final ActionHandler actions;
	private final List<AbilityInstance> abilities;
	private int abilityPage = 0;
	
	public CharacterSheetScreen(CharacterSheetScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
		manager = player.getRegistryManager();
		sheet = VariousTypes.getSheet(player).get();
		ownerStats = sheet.hasOwner() ? Optional.of(new OwnerStats(sheet.getOwner().get())) : Optional.empty();
		power = sheet.power();
		species = sheet.getSpecies();
		templates = sheet.getAppliedTemplates();
		types = sheet.types().copy();
		actions = sheet.actions().copy();
		abilities = sheet.abilities().allNonHidden();
		if(abilities.size() > 1)
			Collections.sort(abilities, AbilityInstance.sortFunc(manager));
	}
	
	public void init()
	{
		super.init();
		
		int midX = width / 2;
		int midY = height / 2;
		int spacing = 100;
		
		int leftX = midX - spacing;
		addDrawable(typeButton = ButtonWidget.builder(Text.empty(), (button) -> {}).dimensions(leftX - 45 - 22, midY - 90, 45, 45).build());
		addDrawable(speciesButton = ButtonWidget.builder(Text.empty(), (button) -> {}).dimensions(leftX - 90, midY - 90 + 50, 90, 30).build());
		addDrawable(templatesButton = ButtonWidget.builder(Text.empty(), (button) -> {}).dimensions(leftX - 90, midY - 90 + 85, 90, 30).build());
		
		for(int i=0; i<abilityButtons.length; i++)
			addDrawable(abilityButtons[i] = ButtonWidget.builder(Text.empty(), (button) -> {}).dimensions(midX + spacing, midY - 70 + i * 35, 90, 30).build());
		
		Vector2i offset = new Vector2i(0, 95);
		for(double deg : new Double[] {-30D, -12.5D, 12.5D, 30D})
			addActionButton(offset, deg, midX, midY);
	}
	
	private void addActionButton(Vector2i offset, double degrees, int midX, int midY)
	{
		Vector2i vec = VTUtils.rotateDegrees2D(offset, degrees);
		addDrawable(ButtonWidget.builder(Text.empty(), (button) -> {}).dimensions(midX + vec.x - 10, midY + vec.y - 10, 20, 20).build());
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		updateButtons();
		super.render(context, mouseX, mouseY, delta);
	}
	
	private void updateButtons()
	{
		// Types
		typeButton.setMessage(types.ofTier(Tier.SUPERTYPE).stream().findFirst().get().displayName(manager));
		typeButton.setTooltip(Tooltip.of(types.display(manager)));
		
		// Species
		speciesButton.active = species.isPresent();
		if(species.isPresent())
			speciesButton.setMessage(species.get().displayName());
		else
			speciesButton.setMessage(Text.empty());
		
		// Templates
		templatesButton.active = templatesButton.visible = !templates.isEmpty();
		switch(templates.size())
		{
			case 0:
				templatesButton.setMessage(Text.empty());
				break;
			case 1:
				templatesButton.setMessage(templates.get(0).displayName());
				break;
			default:
				templatesButton.setMessage(translate("gui", "templates", templates.size()));
				break;
		}
		
		// Abilities
		for(int i=0; i<abilityButtons.length; i++)
		{
			ButtonWidget button = abilityButtons[i];
			
			int index = i + (abilityPage * abilityButtons.length);
			button.active = index < abilities.size();
			if(index >= abilities.size())
				button.setMessage(Text.empty());
			else
				button.setMessage(abilities.get(index).displayName(manager));
		}
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
//		renderBackgroundTexture(context, sheet.types().ofTier(Tier.SUPERTYPE).stream().findFirst().get().sheetBackground(), 0, 0, 0, 0, width, height);
//		renderVignette(context);
		applyBlur(delta);
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		renderOwner(context, mouseX, mouseY);
	}
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		int midX = backgroundWidth / 2;
		int midY = backgroundHeight / 2;
		Text ownerName = sheet.getOwner().get().getDisplayName();
		context.drawCenteredTextWithShadow(textRenderer, ownerName, midX, midY - 100, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal(String.valueOf(power)), midX, midY + 90, 0xFFFFFF);
		
		ownerStats.ifPresent(stats -> stats.render(context, midX, midY));
	}
	
	public void renderVignette(DrawContext context)
	{
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
		float intensity = 1F;
		context.setShaderColor(intensity, intensity, intensity, 1F);
		context.drawTexture(VIGNETTE_TEXTURE, 0, 0, -90, 0.0f, 0.0f, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
		context.drawTexture(VIGNETTE_TEXTURE, 0, 0, -90, 0.0f, 0.0f, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1F, 1F, 1F, 1F);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
	}
	
	private void renderOwner(DrawContext context, int mouseX, int mouseY)
	{
		// TODO Exchange for animated biped model using same player skin
		if(!sheet.hasOwner())
			return;
		
		int renderX = width / 2;
		int renderY = height / 2;
		
		int width = 200;
		int height = 200;
		int size = 80;
		InventoryScreen.drawEntity(context, renderX - (width / 2), renderY - (height / 2), renderX + (width / 2), renderY + (height / 2), size, 0.0625f, mouseX, mouseY, sheet.getOwner().get());
	}
	
//	private void renderActions(DrawContext context)
//	{
//		user.sendMessage(Text.literal("Actions:"));
//		Action.actions().forEach(action -> 
//		{
//			if(sheet.actions().can(action.get()))
//				user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.can_action", action.get().translate())));
//			else
//				user.sendMessage(Text.literal(" * ").append(Text.translatable("action.vartypes.cannot_action", action.get().translate())));
//		});
//		if(sheet.actions().can(Action.BREATHE.get()))
//		{
//			user.sendMessage(Text.literal("Breathable fluids:"));
//			sheet.actions().canBreatheIn().forEach(fluid -> user.sendMessage(Text.literal(" ~").append(Text.literal(fluid.id().getPath()))));
//		}
//	}
	
//	private void renderAbilities(DrawContext context)
//	{
//		if(!sheet.abilities().isEmpty() && sheet.abilities().abilities().stream().anyMatch(inst -> !inst.ability().isHidden(inst)))
//		{
//			user.sendMessage(Text.literal("Abilities:"));
//			sheet.abilities().abilities().forEach(inst ->
//			{
//				if(!inst.ability().isHidden(inst))
//					user.sendMessage(Text.literal(" * ").append(inst.displayName(player.getRegistryManager())));
//			});
//		}
//	}
	
	/** Stores cached statistics about the character sheet's owner */
	private class OwnerStats
	{
		private final Map<Stat, Integer> stats = new HashMap<>();
		
		public OwnerStats(LivingEntity owner) { this(owner.getMaxHealth(), owner.getArmor(), owner.getMovementSpeed()); }
		
		public OwnerStats(float health, float armor, float moveSpeed)
		{
			stats.put(Stat.HEALTH, (int)health);
			stats.put(Stat.ARMOUR, (int)armor);
			stats.put(Stat.SPEED, (int)(moveSpeed * 100));	// Movement speed is amplified to be meaningfully readable as an integer
		}
		
		// HAS - Health, Armour, Speed
		// TODO Display current attack damage as well?
		public void render(DrawContext context, int midX, int midY)
		{
			for(Entry<Stat, Integer> entry : stats.entrySet())
				drawStatEntry(context, entry.getKey(), entry.getValue(), midX + 90, midY - 60);
		}
		
		private void drawStatEntry(DrawContext context, Stat stat, int value, int x, int y)
		{
			Text text = Text.of(String.valueOf(value));
			context.drawText(textRenderer, text, x - textRenderer.getWidth(text), y + stat.index * 10, 0xFFFFFF, false);
			context.drawText(textRenderer, stat.name().substring(0, 1).toUpperCase(), x + 1, y + stat.index * 10, 0xFFFFFF, false);
		}
		
		private enum Stat
		{
			HEALTH(0),
			ARMOUR(1),
			SPEED(2);
			
			private final int index;
			
			private Stat(int renderIndex)
			{
				index = renderIndex;
			}
			
		}
	}
}
