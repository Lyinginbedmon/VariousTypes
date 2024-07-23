package com.lying.client.screen;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.client.screen.SpeciesListWidget.SpeciesEntry;
import com.lying.client.utility.VTUtilsClient;
import com.lying.reference.Reference;
import com.lying.species.Species;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class SpeciesListWidget extends GuiAbstractList<SpeciesEntry>
{
	public SpeciesListWidget(MinecraftClient client, int width, int height, int y)
	{
		super(client, width, height, y, 25);
	}
	
	public int getRowWidth() { return this.width - 20; }
	
	public int getScrollbarX() { return this.getX() + 1; }
	
	public void addEntry(Species spec, CharacterCreationEditScreen parent)
	{
		addEntry(new SpeciesEntry(spec, parent, getRowWidth()));
	}
	
	public static class SpeciesEntry extends GuiAbstractList.ListEntry<SpeciesEntry>
	{
		private final Species species;
		private final CharacterCreationEditScreen parent;
		
		private final ButtonWidget selectButton;
		private List<ButtonWidget> children = Lists.newArrayList();
		
		public SpeciesEntry(Species speciesIn, CharacterCreationEditScreen parentIn, int width)
		{
			species = speciesIn;
			parent = parentIn;
			children.add(new InspectButton(0, 0, 24, 24, button -> { parent.setDetail(new DetailObject(VTUtilsClient.speciesToDetail(species))); button.setFocused(false); }));
			children.add(selectButton = ButtonWidget.builder(species.displayName(), button -> { parent.setSpecies(species); button.setFocused(false); }).dimensions(25, 0, 150, 24).build());
		}
		
		public void updatePosition(int x, int y)
		{
			for(int i=0; i<children.size(); i++)
				switch(i)
				{
					case 0:
						children.get(i).setPosition(x + 1, y);
						break;
					default:
						ButtonWidget previous = children.get(i - 1);
						children.get(i).setPosition(previous.getX() + previous.getWidth() + 1, y);
						break;
				}
		}
		
		public List<? extends Element> children() { return children; }
		
		public List<? extends Selectable> selectableChildren() { return children; }
		
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta)
		{
			selectButton.active = !parent.isSpecies(species);
			selectButton.setTooltip(selectButton.active ? Tooltip.of(Text.translatable("gui."+Reference.ModInfo.MOD_ID+".creator_set_species", species.displayName())) : null);
			children.forEach(child -> child.render(context, mouseX, mouseY, tickDelta));
		}
	}
}
