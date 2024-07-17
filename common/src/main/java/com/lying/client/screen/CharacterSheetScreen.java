package com.lying.client.screen;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.rotateDegrees2D;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.ActivatedAbility;
import com.lying.client.VariousTypesClient;
import com.lying.component.CharacterSheet;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.screen.CharacterSheetScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class CharacterSheetScreen extends HandledScreen<CharacterSheetScreenHandler>
{
	private PlayerEntity player;
	private final DynamicRegistryManager manager;
	
	private DetailObject detailObject = null;
	private TypeButtonWidget typeButton;
	private ButtonWidget speciesButton, templatesButton;
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
		addDrawableChild(typeButton = new TypeButtonWidget(leftX - 45 - 22, midY - 90, types.ofTier(Tier.SUPERTYPE).stream().findFirst().get().displayName(manager), (button) -> 
		{
			List<Type> typeList = types.contents();
			typeList.sort(Type.sortFunc(player.getRegistryManager()));
			
			Batch[] batches = new Batch[typeList.size()];
			for(int i=0; i<typeList.size(); i++)
				batches[i] = new Batch(objToDetail(typeList.get(i), type -> type.displayName(player.getRegistryManager()), Type::registryName, Type::description));
			detailObject = new DetailObject(batches);
			setFocused(null);
		}));
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
			addDrawableChild(abilityButtons[i] = makeAbilityButton(i, midX + spacing, midY - 70 + i * 35));
		
		List<Action> actions = Lists.newArrayList();
		Action.actions().forEach(action -> actions.add(action.get()));
		Collections.sort(actions, Action.SORT);
		Vector2i offset = new Vector2i(0, 95);
		Double[] deg = new Double[] {-30D, -12.5D, 12.5D, 30D};
		for(int i=0; i<deg.length; i++)
			addActionButton(actions.get(i), offset, deg[i], midX, midY);
		
		Type supertype = types.ofTier(Tier.SUPERTYPE).stream().findFirst().get();
		typeButton.updateSupertype(supertype);
		typeButton.setTooltip(Tooltip.of(types.display(manager)));
		
		speciesButton.active = species.isPresent();
		if(species.isPresent())
			speciesButton.setMessage(species.get().displayName());
		else
			speciesButton.setMessage(Text.empty());
		
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
		updateButtons();
	}
	
	private ButtonWidget makeAbilityButton(int index, int x, int y)
	{
		return ButtonWidget.builder(Text.empty(), (button) -> 
		{
			int i = index + (abilityPage * abilityButtons.length);
			if(i < abilities.size())
				detailObject = new DetailObject(abilityToDetail(abilities.get(i)));
		}).dimensions(x, y, 90, 30).build();
	}
	
	private void addActionButton(Action action, Vector2i offset, double degrees, int midX, int midY)
	{
		Vector2i vec = rotateDegrees2D(offset, degrees);
		Consumer<PressableWidget> consumer;
		if(actions.can(action))
			consumer = (button) -> 
			{
				List<MutableText> entries = Lists.newArrayList();
				entries.add(Text.translatable("action.vartypes.can_action", action.translated()));
				if(action.equals(Action.BREATHE.get()))
					actions.canBreatheIn().forEach(fluid -> entries.add(Text.literal(" * ").append(fluid.id().getPath())));
				action.description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
				detailObject = new DetailObject(entries.toArray(new MutableText[0]));
			};
		else
			consumer = (button) -> 
			{
				List<MutableText> entries = Lists.newArrayList();
				entries.add(Text.translatable("action.vartypes.cannot_action", action.translated()));
				action.description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
				detailObject = new DetailObject(entries.toArray(new MutableText[0]));
			};
		
		addDrawableChild(new ActionButtonWidget(action, actions.can(action), midX + vec.x - 10, midY + vec.y - 10, consumer));
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		updateButtons();
		super.render(context, mouseX, mouseY, delta);
	}
	
	private void updateButtons()
	{
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
			detailObject.render(context, midX, midY, Math.min(400, context.getScaledWindowWidth() / 2));
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
	
	private MutableText[] abilityToDetail(AbilityInstance instance)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(instance.displayName(manager).copy().formatted(Formatting.BOLD));
		if(instance.ability() instanceof ActivatedAbility)
			entries.add(translate("gui", "activated_ability").copy());
		instance.description(manager).ifPresent(text -> entries.add(text.copy().formatted(Formatting.GRAY)));
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(instance.mapName().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
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
	
	/**
	 * An individual rendered tooltip as part of a DetailObject
	 */
	private class Batch
	{
		private static int PADDING = 5;
		private final List<Text> entries = Lists.newArrayList();
		
		public Batch(MutableText... entriesIn)
		{
			for(MutableText text : entriesIn)
				entries.add(text);
		}
		
		public int backgroundHeight(int maxEntryWidth) { return getConstrainedEntries(maxEntryWidth).size() * textRenderer.fontHeight + PADDING; }
		
		public static int backgroundWidth(List<TooltipComponent> entries, int maxEntryWidth, TextRenderer textRenderer)
		{
			int maxWidth = 0;
			for(TooltipComponent text : entries)
				if(text.getWidth(textRenderer) > maxWidth)
					maxWidth = text.getWidth(textRenderer);
			return maxWidth + PADDING;
		}
		
		private List<TooltipComponent> getConstrainedEntries(int maxEntryWidth)
		{
			List<OrderedText> wrapped = Lists.newArrayList();
			for(Text text : entries)
				wrapped.addAll(textRenderer.wrapLines(text, maxEntryWidth));
			return wrapped.stream().map(TooltipComponent::of).collect(Util.toArrayList());
		}
		
		@SuppressWarnings("deprecation")
		public void render(DrawContext context, int x, int y, int maxEntryWidth)
		{
			List<TooltipComponent> entries = getConstrainedEntries(maxEntryWidth);
			int backgroundWidth = backgroundWidth(entries, maxEntryWidth, textRenderer);
			int backgroundHeight = backgroundHeight(maxEntryWidth);
			TooltipComponent comp;
			MatrixStack matrices = context.getMatrices();
			matrices.push();
				context.draw(() -> TooltipBackgroundRenderer.render(context, x - backgroundWidth / 2, y - (backgroundHeight / 2), backgroundWidth, backgroundHeight, 400));
				matrices.translate(0, 0, 400);
				int textY = y - (backgroundHeight / 2) + (PADDING / 2);
				int textWidth = (backgroundWidth - PADDING);
				for(TooltipComponent text : entries)
				{
					comp = text;
					int textX = x;
					switch(VariousTypesClient.ALIGN_TEXT)
					{
						case CENTRE:
							textX = x - text.getWidth(textRenderer) / 2;
							break;
						case RIGHT:
							textX = x + (textWidth / 2) - text.getWidth(textRenderer);
							break;
						default:
						case LEFT:
							textX = x - (textWidth / 2);
							break;
					}
					
					comp.drawText(textRenderer, textX, textY, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers());
					textY += textRenderer.fontHeight;
				}
			matrices.pop();
		}
	}
	
	private class DetailObject
	{
		private final int SPACING = 10;
		private final List<Batch> entries = Lists.newArrayList();
		
		public DetailObject(MutableText... entriesIn)
		{
			entries.add(new Batch(entriesIn));
		}
		
		public DetailObject(Batch... batches)
		{
			for(Batch batch : batches)
				entries.add(batch);
		}
		
		public void render(DrawContext context, int x, int y, int maxEntryWidth)
		{
			int totalHeight = (entries.size() - 1) * SPACING;
			for(Batch batch : entries)
				totalHeight += batch.backgroundHeight(maxEntryWidth);
			
			y -= totalHeight / 2;
			for(Batch batch : entries)
			{
				int height = batch.backgroundHeight(maxEntryWidth);
				y += height / 2;
				batch.render(context, x, y, maxEntryWidth);
				y += (height / 2) + SPACING;
			}
		}
	}
	
	private static abstract class IconButtonWidget extends PressableWidget
	{
		private final Consumer<PressableWidget> action;
		
		protected IconButtonWidget(int x, int y, int width, int height, Consumer<PressableWidget> actionIn)
		{
			super(x, y, width, height, Text.empty());
			this.action = actionIn;
		}
		
		protected void appendClickableNarrations(NarrationMessageBuilder var1)
		{
			this.appendDefaultNarrations(var1);
		}
		
		public void onPress()
		{
			this.action.accept(this);
		}
	}
	
	private class TypeButtonWidget extends IconButtonWidget
	{
		private static final Identifier TEXTURE = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/sheet/types.png");
		private static final Identifier TEXTURE_HOVERED = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/sheet/types_hovered.png");
		private Type supertype = VTTypes.HUMAN.get();
		
		protected TypeButtonWidget(int x, int y, Text message, Consumer<PressableWidget> actionIn)
		{
			super(x, y, 45, 45, actionIn);
			setMessage(message);
		}
		
		public void updateSupertype(@NotNull Type typeIn) { this.supertype = typeIn; }
		
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
		{
			int colour = this.active ? supertype.color() : 0;
			float r = (float)((colour >> 16) & 0xFF) / 255F;
			float g = (float)((colour >> 8) & 0xFF) / 255F;
			float b = (float)(colour & 0xFF) / 255F;
			
			context.drawTexturedQuad(isHovered() ? TEXTURE_HOVERED : TEXTURE, this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, r, g, b, 1F);
			
			context.drawCenteredTextWithShadow(textRenderer, getMessage(), getX() + getWidth() / 2, getY() + getHeight() / 2 - textRenderer.fontHeight / 2, 0xFFFFFF);
		}
	}
	
	private static class ActionButtonWidget extends IconButtonWidget
	{
		private static final Identifier TEXTURE = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/sheet/action_outline.png");
		private final Action action;
		private final boolean can;
		
		protected ActionButtonWidget(Action actionIn, boolean canIn, int x, int y, Consumer<PressableWidget> consumerIn)
		{
			super(x, y, 20, 20, consumerIn);
			action = actionIn;
			can = canIn;
		}
		
		public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
		{
			float brightness = isHovered() ? 1F : can ? 0.75F : 0.45F;
			context.drawTexturedQuad(TEXTURE, this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
			context.drawTexturedQuad(action.texture(), this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
		}
	}
}
