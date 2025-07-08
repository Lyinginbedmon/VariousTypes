package com.lying.client.screen;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.ability.ActivatedAbility;
import com.lying.component.element.Cooldown;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.mixin.IDrawContextInvoker;
import com.lying.network.SetFavouriteAbilityPacket;
import com.lying.reference.Reference;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class FavouriteAbilityButton extends ButtonWidget
{
	private static final Identifier TEXTURE = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/favourite_ability.png");
	private static final Identifier TEXTURE_HOVERED = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/favourite_ability_hovered.png");
	
	private final int index;
	private Optional<AbilityInstance> contents = Optional.empty();
	
	public FavouriteAbilityButton(int x, int y, int index, PressAction onPress)
	{
		this(x, y, 30, index, onPress);
	}
	
	public FavouriteAbilityButton(int x, int y, int size, int index, PressAction onPress)
	{
		super(x, y, size, size, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
		this.index = index;
		this.active = false;
	}
	
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		return this.visible && mouseX >= (double)this.getX() && mouseY >= (double)this.getY() && mouseX < (double)(this.getX() + this.width) && mouseY < (double)(this.getY() + this.height);
	}
	
	public void clear()
	{
		contents = Optional.empty();
		setTooltip(null);
		SetFavouriteAbilityPacket.send(index, null);
		this.active = false;
	}
	
	public void set(AbilityInstance inst, DynamicRegistryManager manager)
	{
		contents = Optional.of(inst);
		setTooltip(Tooltip.of(Text.empty()
				.append(inst.displayName(manager).copy().formatted(Formatting.BOLD)).append("\n").append(
				Text.translatable("gui.vartypes.clear_favourite").formatted(Formatting.GRAY, Formatting.ITALIC))));
		this.active = contents.isPresent();
	}
	
	public void setAbility(AbilityInstance inst, DynamicRegistryManager manager)
	{
		set(inst, manager);
		SetFavouriteAbilityPacket.send(index, inst.mapName());
	}
	
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta)
	{
		ElementActionables element = VariousTypes.getSheet(AbilityMenu.mc.player).get().<ElementActionables>element(VTSheetElements.ACTIONABLES);
		float v = contents.isPresent() && !element.coolingDown() ? 1F : 0.4F;
		((IDrawContextInvoker)context).drawTexRGBA(isHovered() && this.active ? TEXTURE_HOVERED : TEXTURE, getX(), getRight(), getY(), getBottom(), 0, 0F, 1F, 0F, 1F, v, v, v, 1F);
		contents.ifPresent(inst -> 
		{
			int iconHeight = getHeight() - 4;
			
			Cooldown cool = element.getCooldown(inst.mapName());
			if(cool == null)
			{
				// No cooldown in effect, just render if we can use it right now
				float f = ((ActivatedAbility)inst.ability()).canTrigger(AbilityMenu.mc.player, inst) ? 1F : 0.3F;
				((IDrawContextInvoker)context).drawTexRGBA(inst.ability().iconTexture(), getX() + 2, getRight() - 2, getBottom() - iconHeight, getBottom() - 2, 0, 0F, 1F, 0F, 1F, f, f, f, 1F);
				return;
			}
			else if(cool.isIndefinite())
			{
				// Indefinite cooldown in effect, render transparent icon
				((IDrawContextInvoker)context).drawTexRGBA(inst.ability().iconTexture(), getX() + 2, getRight() - 2, getBottom() - iconHeight, getBottom() - 2, 0, 0F, 1F, 0F, 1F, 0.3F, 0.3F, 0.3F, 0.5F);
				return;
			}
			
			// Temporary cooldown in effect, render partial icon
			float volume = cool.progress(AbilityMenu.mc.player.getWorld().getTime());
			float u1 = 0F;
			int renderedHeight = (int)(iconHeight * volume);
			if(volume < 1F)
			{
				volume -= volume%0.1F;
				renderedHeight = (int)(iconHeight * volume);
				volume = (float)renderedHeight / (float)iconHeight;
				
				u1 = 1F - volume;
			}
			
			((IDrawContextInvoker)context).drawTexRGBA(inst.ability().iconTexture(), getX() + 2, getRight() - 2, getBottom() - renderedHeight, getBottom() - 2, 0, 0F, 1F, u1, 1F, volume, volume, volume, 1F);
		});
	}
}