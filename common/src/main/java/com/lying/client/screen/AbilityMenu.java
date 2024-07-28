package com.lying.client.screen;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.client.KeybindHandling;
import com.lying.client.network.ActivateAbilityPacket;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.screen.AbilityMenuHandler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AbilityMenu extends HandledScreen<AbilityMenuHandler>
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	private Optional<AbilityInstance> abilityUnderMouse = Optional.empty();
	
	private ButtonWidget[] abilityButtons = new ButtonWidget[5];
	private FavouriteAbilityButton[] favouriteButtons = new FavouriteAbilityButton[4];
	
	private static ElementActionables element;
	private List<AbilityInstance> abilities = Lists.newArrayList();
	private int abilityPages = 0;
	private int abilityPage = 0;
	
	public AbilityMenu(AbilityMenuHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			element = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			abilities = element.allNonHidden();
			abilityPages = Math.ceilDiv(abilities.size(), abilityButtons.length);
			if(abilities.size() > 1)
				Collections.sort(abilities, AbilityInstance.SORT_FUNC);
		});
	}
	
	public boolean shouldPause() { return false; }
	
	public void init()
	{
		super.init();
		int midX = width / 2;
		int midY = height / 2;
		int spacing = 100;
		
		for(int i=0; i<abilityButtons.length; i++)
			addDrawableChild(abilityButtons[i] = makeAbilityButton(i, midX + spacing, midY - 70 + i * 35));
		
		for(int i=0; i<favouriteButtons.length; i++)
			addDrawableChild(favouriteButtons[i] = makeFavouriteButton(i, midX - spacing, midY - 55 + i * 35));
		
		if(abilityPages > 1)
		{
			addDrawableChild(ButtonWidget.builder(Text.literal("<"), (button) -> {
				if(abilityPage == 0) return;
				abilityPage -= Math.signum(abilityPage);
				updateButtons();
				setFocused(null);
				}).dimensions(midX + spacing, midY - 90, 15, 15).build());
			
			addDrawableChild(ButtonWidget.builder(Text.literal(">"), (button) -> {
				if(abilityPage >= abilityPages - 1) return;
				abilityPage = Math.min(abilityPage + 1, abilityPages - 1);
				updateButtons();
				setFocused(null);
				}).dimensions(midX + spacing + 90 - 15, midY - 90, 15, 15).build());
		}
		
		VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			ElementActionables element = sheet.element(VTSheetElements.ACTIONABLES);
			for(int i=0; i<favouriteButtons.length; i++)
			{
				final int slot = i;
				element.getFavourite(slot).ifPresent(id -> favouriteButtons[slot].setAbility(element.get(id)));
			}
		});
		
		updateButtons();
	}
	
	private ButtonWidget makeAbilityButton(int index, int x, int y)
	{
		return ButtonWidget.builder(Text.empty(), (button) -> 
		{
			ActivateAbilityPacket.send(abilities.get(index + (abilityPage * abilityButtons.length)).mapName());
			close();
		}).dimensions(x, y, 90, 30).build();
	}
	
	private FavouriteAbilityButton makeFavouriteButton(int index, int x, int y)
	{
		return new FavouriteAbilityButton(x - 30, y, index, (button) -> 
		{
			VariousTypes.getSheet(mc.player).ifPresent(sheet -> KeybindHandling.sendActivationPacket(sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).getFavourite(index)));
			close();
		});
	}
	
	private void updateButtons()
	{
		for(int i=0; i<abilityButtons.length; i++)
		{
			ButtonWidget button = abilityButtons[i];
			int index = i + (abilityPage * abilityButtons.length);
			button.active = index < abilities.size();
			if(index >= abilities.size())
			{
				button.setMessage(Text.empty());
				button.setTooltip(null);
			}
			else
			{
				AbilityInstance inst = abilities.get(index);
				button.setMessage(inst.displayName());
				button.setTooltip(Tooltip.of(inst.description().get()));
			}
		}
	}
	
	public void render(DrawContext context, int mouseX, int mouseY, float delta)
	{
		super.render(context, mouseX, mouseY, delta);
		abilityUnderMouse.ifPresent(inst -> 
		{
			List<OrderedText> lines = Lists.newArrayList();
			lines.add(inst.displayName().copy().formatted(Formatting.BOLD).asOrderedText());
			
			boolean setFave = false;
			for(ButtonWidget favourite : favouriteButtons)
				if(favourite.isMouseOver(mouseX, mouseY))
				{
					setFave = true;
					break;
				}
			if(setFave)
				lines.add(Text.translatable("gui.vartypes.set_favourite").formatted(Formatting.GRAY, Formatting.ITALIC).asOrderedText());
			else
				lines.add(Text.translatable("gui.vartypes.clear_held_ability").formatted(Formatting.GRAY, Formatting.ITALIC).asOrderedText());
			
			context.drawTooltip(this.textRenderer, lines, HoveredTooltipPositioner.INSTANCE, mouseX, mouseY);
		});
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		applyBlur(delta);
		renderDarkening(context);
		drawBackground(context, delta, mouseX, mouseY);
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button)
	{
		if(button == 0)
		{
			if(abilityUnderMouse.isPresent())
			{
				// If we're hovering over a favourite button, set it to the held ability
				for(FavouriteAbilityButton favourite : favouriteButtons)
					if(favourite.isMouseOver(mouseX, mouseY))
					{
						favourite.setAbility(abilityUnderMouse.get());
						abilityUnderMouse = Optional.empty();
						return true;
					}
				
				// Otherwise, clear the held ability
				abilityUnderMouse = Optional.empty();
				return true;
			}
		}
		else if(button == 1)
		{
			// If we are hovering over an active ability button, place its ability under the mouse
			for(int i=0; i<abilityButtons.length; i++)
				if(abilityButtons[i].isMouseOver(mouseX, mouseY) && abilityButtons[i].active)
				{
					abilityUnderMouse = Optional.of(abilities.get(i + (abilityPage * abilityButtons.length)));
					return true;
				}
			abilityUnderMouse = Optional.empty();
			
			// If we are hovering over a favourite button, clear its setting
			for(FavouriteAbilityButton favourite : favouriteButtons)
				if(favourite.isMouseOver(mouseX, mouseY))
				{
					favourite.clear();
					return true;
				}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) { }
	
	protected void drawForeground(DrawContext context, int mouseX, int mouseY) { }
}
