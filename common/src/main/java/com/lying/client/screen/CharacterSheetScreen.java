package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.rotateDegrees2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;

import org.joml.Vector2i;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.component.CharacterSheet;
import com.lying.reference.Reference;
import com.lying.screen.CharacterSheetScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class CharacterSheetScreen extends HandledScreen<CharacterSheetScreenHandler>
{
	protected static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	
	private PlayerEntity player;
	private final DynamicRegistryManager manager;
	
	private DetailObject detailObject = null;
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
		addDrawableChild(typeButton = ButtonWidget.builder(Text.empty(), (button) -> 
		{
			List<Type> typeList = types.contents();
			typeList.sort(Type.sortFunc(player.getRegistryManager()));
			detailObject = listToDetail(typeList, type -> type.displayName(player.getRegistryManager()), Type::registryName, type -> Optional.empty());
			setFocused(null);
		}).dimensions(leftX - 45 - 22, midY - 90, 45, 45).build());
		addDrawableChild(speciesButton = ButtonWidget.builder(Text.empty(), (button) -> 
		{
			Species spec = species.get();
			detailObject = new DetailObject(objToDetail(spec, Species::displayName, Species::registryName, obj -> obj.display().description()));
			setFocused(null);
		}).dimensions(leftX - 90, midY - 90 + 50, 90, 30).build());
		addDrawableChild(templatesButton = ButtonWidget.builder(Text.empty(), (button) -> 
		{
			detailObject = listToDetail(templates, Template::displayName, Template::registryName, tem -> tem.display().description());
			setFocused(null);
		}).dimensions(leftX - 90, midY - 90 + 85, 90, 30).build());
		
		for(int i=0; i<abilityButtons.length; i++)
			addDrawableChild(abilityButtons[i] = ButtonWidget.builder(Text.empty(), (button) -> { detailObject = null; }).dimensions(midX + spacing, midY - 70 + i * 35, 90, 30).build());
		
		List<Action> actions = Lists.newArrayList();
		Action.actions().forEach(action -> actions.add(action.get()));
		Collections.sort(actions, Action.SORT);
		Vector2i offset = new Vector2i(0, 95);
		Double[] deg = new Double[] {-30D, -12.5D, 12.5D, 30D};
		for(int i=0; i<deg.length; i++)
			addActionButton(actions.get(i), offset, deg[i], midX, midY);
	}
	
	private void addActionButton(Action action, Vector2i offset, double degrees, int midX, int midY)
	{
		Vector2i vec = rotateDegrees2D(offset, degrees);
		addDrawableChild(ButtonWidget.builder(Text.empty(), (button) -> 
		{
			if(actions.can(action))
			{
				List<MutableText> entries = Lists.newArrayList();
				entries.add(Text.translatable("action.vartypes.can_action", action.translate()));
				if(action.equals(Action.BREATHE.get()))
					actions.canBreatheIn().forEach(fluid -> entries.add(Text.literal(" * ").append(fluid.id().getPath())));
				detailObject = new DetailObject(entries.toArray(new MutableText[0]));
			}
			else
				detailObject = new DetailObject(Text.translatable("action.vartypes.cannot_action", action.translate()));
		}).dimensions(midX + vec.x - 10, midY + vec.y - 10, 20, 20).build());
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
	
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		this.detailObject = null;
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
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
		
		ownerStats.ifPresent(stats -> stats.render(context, midX + 90, midY - 60, midX + 90, midY - 60));
		
		if(detailObject != null)
			detailObject.render(context, midX, midY);
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
	
	private <T extends Object> MutableText[] objToDetail(T obj, Function<T,Text> nameGetter, Function<T,Identifier> regGetter, Function<T,Optional<Text>> descGetter)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(nameGetter.apply(obj).copy().formatted(Formatting.BOLD));
		descGetter.apply(obj).ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(regGetter.apply(obj).toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
	}
	
	private <T extends Object> DetailObject listToDetail(List<T> set, Function<T,Text> nameGetter, Function<T,Identifier> regGetter, Function<T,Optional<Text>> descGetter)
	{
		List<MutableText> entries = Lists.newArrayList();
		for(int i=0; i<set.size(); i++)
		{
			T type = set.get(i);
			for(MutableText entry : objToDetail(type, nameGetter, regGetter, descGetter))
				entries.add(entry);
			
			if(i < set.size() - 1)
				entries.add(Text.empty());
		}
		return new DetailObject(entries.toArray(new MutableText[0]));
	}
	
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
		public void render(DrawContext context, int textX, int textY, int iconX, int iconY)
		{
			for(Entry<Stat, Integer> entry : stats.entrySet())
				drawStatEntry(context, entry.getKey(), entry.getValue(), textX, textY, iconX, iconY);
		}
		
		private void drawStatEntry(DrawContext context, Stat stat, int value, int textX, int textY, int iconX, int iconY)
		{
			Text text = Text.of(String.valueOf(value));
			context.drawText(textRenderer, text, textX - textRenderer.getWidth(text), textY + stat.index * 10, 0xFFFFFF, false);
			context.drawTexture(stat.iconTex, iconX, iconY - 1 + stat.index * 10, 0, 0, 9, 9, 9, 9);
		}
		
		private enum Stat
		{
			HEALTH(0),
			ARMOUR(1),
			SPEED(2);
			
			private final int index;
			private final Identifier iconTex;
			
			private Stat(int renderIndex)
			{
				index = renderIndex;
				iconTex = Reference.ModInfo.prefix("textures/gui/sheet/stat_"+name().toLowerCase()+".png");
			}
		}
	}
	
	private class DetailObject
	{
		private final List<Text> entries = Lists.newArrayList();
		
		public DetailObject(MutableText... entriesIn)
		{
			for(MutableText text : entriesIn)
				entries.add(text);
		}
		
		public void render(DrawContext context, int x, int y)
		{
			context.drawTooltip(textRenderer, entries, Optional.empty(), x, y);
		}
	}
}
