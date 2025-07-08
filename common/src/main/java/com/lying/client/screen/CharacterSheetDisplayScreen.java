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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability.Category;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.client.VariousTypesClient;
import com.lying.client.utility.VTUtilsClient;
import com.lying.component.CharacterSheet;
import com.lying.entity.AnimatedPlayerEntity;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTTypes;
import com.lying.mixin.IDrawContextInvoker;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.lying.type.Type;
import com.lying.type.Type.Tier;
import com.lying.type.TypeSet;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public abstract class CharacterSheetDisplayScreen<T extends ScreenHandler> extends HandledScreen<T>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	public static final PlayerEntity PLAYER = mc.player;
	public final DynamicRegistryManager registryManager;
	
	private Optional<DetailObject> detailObject = Optional.empty();
	private int scrollAmount = 0;
	
	private TypeButtonWidget typeButton;
	private ButtonWidget speciesButton, templatesButton;
	private ButtonWidget[] abilityButtons = new ButtonWidget[5];
	private ButtonWidget[] categoryButtons = new ButtonWidget[Category.values().length];
	private ButtonWidget[] pageButtons = new ButtonWidget[2];
	
	private CharacterSheet sheet;
	protected Optional<AnimatedPlayerEntity> animatedPlayer = Optional.empty();
	private int power;
	private Optional<OwnerStats> ownerStats;
	private Optional<Species> species;
	private List<Template> templates;
	private TypeSet types;
	private ActionHandler actions;
	
	private Map<Category, List<AbilityInstance>> abilities = new HashMap<>();
	private Category currentCategory = Category.OFFENSE;
	private int abilityPages;
	private int abilityPage = 0;
	
	protected CharacterSheetDisplayScreen(CharacterSheet sheetIn, T handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		registryManager = PLAYER.getRegistryManager();
		setCharacterSheet(sheetIn);
	}
	
	public void setCharacterSheet(CharacterSheet sheetIn)
	{
		sheet = sheetIn;
		sheet.getOwner().ifPresentOrElse(owner -> 
		{
			ownerStats = Optional.of(new OwnerStats(owner));
			if(owner.getType() == EntityType.PLAYER)
				animatedPlayer = Optional.of(AnimatedPlayerEntity.of(((PlayerEntity)owner).getGameProfile(), mc.world));
		}, () -> 
		{
			ownerStats = Optional.of(new OwnerStats(sheet.getOwner().get()));
			animatedPlayer = Optional.empty();
		});
		animatedPlayer.ifPresent(player -> VariousTypes.setSheet(player, sheet));
		
		power = sheet.power();
		species = sheet.module(VTSheetModules.SPECIES).getMaybe();
		templates = sheet.module(VTSheetModules.TEMPLATES).get();
		
		types = sheet.<TypeSet>elementValue(VTSheetElements.TYPES).copy();
		actions = sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).copy();
		sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).allNonHidden().forEach(inst -> 
		{
			Category cat = inst.ability().category();
			List<AbilityInstance> set = abilities(cat);
			set.add(inst);
			if(set.size() > 1)
				Collections.sort(set, AbilityInstance.sortFunc(registryManager));
			abilities.put(cat, set);
		});
		
		// Set current category to first non-empty category
		if(!abilities.isEmpty())
			for(Category cat : Category.values())
				if(!abilities.getOrDefault(cat, Lists.newArrayList()).isEmpty())
				{
					currentCategory = cat;
					break;
				}
	}
	
	public void setCharacter(AnimatedPlayerEntity character)
	{
		animatedPlayer = Optional.of(character);
	}
	
	public boolean shouldPause() { return true; }
	
	public void init()
	{
		super.init();
		
		int midX = width / 2;
		int midY = height / 2;
		int spacing = 100;
		
		// Information buttons
		int leftX = midX - spacing;
		addDrawableChild(typeButton = new TypeButtonWidget(leftX - 45 - 22, midY - 90, types.ofTier(Tier.SUPERTYPE).stream().findFirst().get().displayName(), (button) -> 
		{
			List<Type> typeList = types.contents();
			typeList.sort(Type.SORT_FUNC);
			setDetail(VTUtilsClient.listToDetail(typeList, this::typeToDetail));
			setFocused(null);
		}));
		addDrawableChild(speciesButton = ButtonWidget.builder(Text.empty(), (button) -> 
		{
			Species spec = species.get();
			setDetail(new DetailObject(VTUtilsClient.speciesToDetail(spec)));
			setFocused(null);
		}).dimensions(leftX - 90, midY - 90 + 50, 90, 30).build());
		addDrawableChild(templatesButton = ButtonWidget.builder(Text.empty(), (button) -> 
		{
			setDetail(VTUtilsClient.listToDetail(templates, VTUtilsClient::templateToDetail));
			setFocused(null);
		}).dimensions(leftX - 90, midY - 90 + 85, 90, 30).build());
		
		// Ability buttons
		for(int i=0; i<abilityButtons.length; i++)
			addDrawableChild(abilityButtons[i] = makeAbilityButton(i, midX + spacing, midY - 70 + i * 35));
		
		// Ability category buttons
		int catY = 0;
		int pageY = 0;
		switch(VariousTypesClient.config.buttonLayout())
		{
			case CATS_TOP:
				catY = midY - 93;
				pageY = midY + 105;
				break;
			case CATS_BOT:
				catY = midY + 105;
				pageY = midY - 90;
				break;
		}
		for(int i=0; i<Category.values().length; i++)
		{
			Category cat = Category.values()[i];
			addDrawableChild(categoryButtons[i] = new CategoryButton(midX + spacing + i * 36, catY, cat, (button) -> {
				setCategory(cat);
				setFocused(null);
				}));
		}
		
		// Ability page navigation buttons
		addDrawableChild(pageButtons[0] = ButtonWidget.builder(Text.literal("<"), (button) -> {
			if(abilityPage == 0) return;
			abilityPage -= Math.signum(abilityPage);
			updateButtons();
			setFocused(null);
			}).dimensions(midX + spacing, pageY, 15, 15).build());
		
		addDrawableChild(pageButtons[1] = ButtonWidget.builder(Text.literal(">"), (button) -> {
			if(abilityPage >= abilityPages - 1) return;
			abilityPage = Math.min(abilityPage + 1, abilityPages - 1);
			updateButtons();
			setFocused(null);
			}).dimensions(midX + spacing + 90 - 15, pageY, 15, 15).build());
		
		// Action buttons
		List<Action> actions = Lists.newArrayList();
		Action.actions().forEach(action -> actions.add(action.get()));
		Collections.sort(actions, Action.SORT);
		Vector2i offset = new Vector2i(0, 95);
		Double[] deg = new Double[] {-30D, -12.5D, 12.5D, 30D};
		for(int i=0; i<deg.length; i++)
			addActionButton(actions.get(i), offset, deg[i], midX, midY);
		
		initializeButtons();
	}
	
	protected void handledScreenTick()
	{
		animatedPlayer.ifPresent(player -> 
		{
			++player.age;
			player.tick();
		});
	}
	
	private ButtonWidget makeAbilityButton(int index, int x, int y)
	{
		return ButtonWidget.builder(Text.empty(), (button) -> 
		{
			int i = index + (abilityPage * abilityButtons.length);
			if(i < abilities(currentCategory).size())
				setDetail(new DetailObject(VTUtilsClient.abilityToDetail(abilities(currentCategory).get(i))));
			setFocused(null);
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
				setDetail(new DetailObject(entries.toArray(new MutableText[0])));
				setFocused(null);
			};
		else
			consumer = (button) -> 
			{
				List<MutableText> entries = Lists.newArrayList();
				entries.add(Text.translatable("action.vartypes.cannot_action", action.translated()));
				action.description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
				setDetail(new DetailObject(entries.toArray(new MutableText[0])));
				setFocused(null);
			};
		
		addDrawableChild(new ActionButtonWidget(action, actions.can(action), midX + vec.x - 10, midY + vec.y - 10, consumer));
	}
	
	private void initializeButtons()
	{
		Type supertype = types.ofTier(Tier.SUPERTYPE).stream().findFirst().get();
		typeButton.updateSupertype(supertype);
		typeButton.setTooltip(Tooltip.of(types.display()));
		
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
	
	public List<AbilityInstance> abilities(Category cat) { return abilities.getOrDefault(cat, Lists.newArrayList()); }
	
	private void updateButtons()
	{
		for(int i=0; i<abilityButtons.length; i++)
		{
			ButtonWidget button = abilityButtons[i];
			int index = i + (abilityPage * abilityButtons.length);
			button.active = index < abilities(currentCategory).size();
			if(index >= abilities(currentCategory).size())
				button.setMessage(Text.empty());
			else
				button.setMessage(abilities(currentCategory).get(index).displayName(this.registryManager));
		}
		
		int catY = 0;
		int catOffset = 0;
		switch(VariousTypesClient.config.buttonLayout())
		{
			case CATS_TOP:
				catY = (height / 2) - 93;
				catOffset = -3;
				break;
			case CATS_BOT:
				catY = (height / 2) + 105;
				catOffset = 3;
				break;
		}
		for(int i=0; i<categoryButtons.length; i++)
		{
			Category cat = Category.values()[i];
			categoryButtons[i].active = currentCategory != cat && !abilities(cat).isEmpty();
			categoryButtons[i].setY(catY + (cat == currentCategory ? catOffset : 0));
		}
		
		for(int i=0; i<pageButtons.length; i++)
			if(abilityPages == 0)
				pageButtons[i].active = pageButtons[i].visible = false;
			else
			{
				pageButtons[i].visible = true;
				pageButtons[i].active = (i == 0 && abilityPage > 0) || (i == 1 && abilityPage < abilityPages - 1);
			}
	}
	
	public void setCategory(Category cat)
	{
		if(abilities(cat).isEmpty())
			return;
		
		currentCategory = cat;
		abilityPages = Math.ceilDiv(abilities(cat).size(), abilityButtons.length);
		abilityPage = 0;
		
		updateButtons();
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
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		applyBlur(delta);
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY)
	{
		animatedPlayer.ifPresent(player -> 
		{
			VTUtilsClient.renderDisplayEntity(player, context, width / 2, height / 2, -7.5F);
		});
	}
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY)
	{
		int midX = backgroundWidth / 2;
		int midY = backgroundHeight / 2;
		Text ownerName = sheet.getOwner().get().getDisplayName();
		context.drawCenteredTextWithShadow(textRenderer, ownerName, midX, midY - 100, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal(String.valueOf(power)), midX, midY + 90, 0xFFFFFF);
		if(abilityPages > 1)
			switch(VariousTypesClient.config.buttonLayout())
			{
				case CATS_TOP:
					context.drawCenteredTextWithShadow(textRenderer, Text.literal((1 + abilityPage) + " / " + abilityPages), midX + 145, midY + 109, 0xFFFFFF);
					break;
				case CATS_BOT:
					context.drawCenteredTextWithShadow(textRenderer, Text.literal((1 + abilityPage) + " / " + abilityPages), midX + 145, midY - 85, 0xFFFFFF);
					break;
			}
		ownerStats.ifPresent(stats -> stats.render(context, midX + 90, midY - 60, midX + 90, midY - 60));
		
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
	
	public void setDetail(@Nullable DetailObject obj)
	{
		detailObject = obj == null ? Optional.empty() : Optional.of(obj);
		scrollAmount = 0;
	}
	
	private MutableText[] typeToDetail(Type type)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(type.displayName().copy().formatted(Formatting.BOLD));
		type.description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
		type.abilities().forEach(inst -> 
		{
			if(!inst.ability().isHidden(inst))
				entries.add(Text.literal(" * ").append(inst.displayName(this.registryManager)));
		});
		if(client.options.advancedItemTooltips || PLAYER.isCreative())
			entries.add(Text.literal(type.listID().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
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
		// XXX Display current attack damage as well?
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
			
			((IDrawContextInvoker)context).drawTexRGBA(isHovered() ? TEXTURE_HOVERED : TEXTURE, this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, r, g, b, 1F);
			context.drawCenteredTextWithShadow(textRenderer, getMessage(), getX() + getWidth() / 2, getY() + getHeight() / 2 - textRenderer.fontHeight / 2, 0xFFFFFF);
		}
	}
	
	private static class ActionButtonWidget extends IconButtonWidget
	{
		private static final Identifier TEXTURE = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/sheet/action_outline.png");
		private static final Identifier TEXTURE_ALT = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/sheet/action_outline_alt.png");
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
			((IDrawContextInvoker)context).drawTexRGBA(action.texture(), this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
			((IDrawContextInvoker)context).drawTexRGBA(can ? TEXTURE : TEXTURE_ALT, this.getX(), this.getRight(), this.getY(), this.getBottom(), 0, 0, 1F, 0F, 1, brightness, brightness, brightness, 1F);
		}
	}
}
