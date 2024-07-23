package com.lying.client.screen;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.client.screen.TemplateListWidget.TemplateEntry;
import com.lying.client.utility.VTUtilsClient;
import com.lying.reference.Reference;
import com.lying.template.Template;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class TemplateListWidget extends GuiAbstractList<TemplateEntry>
{
	public TemplateListWidget(MinecraftClient client, int width, int height, int y)
	{
		super(client, width, height, y, 25);
	}
	
	public int getRowWidth() { return this.width - 20; }
	
	public int getScrollbarX() { return getX() + 3; }
	
	public void addEntry(Template spec, CharacterCreationEditScreen parent)
	{
		addEntry(new TemplateEntry(spec, parent, this.width));
	}
	
	public static class TemplateEntry extends GuiAbstractList.ListEntry<TemplateEntry>
	{
		private static final Identifier TEXTURE_ADD = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/add.png");
		private static final Identifier TEXTURE_REM = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/remove.png");
		private static final Identifier TEXTURE_ADD_HOV = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/add_hovered.png");
		private static final Identifier TEXTURE_REM_HOV = new Identifier(Reference.ModInfo.MOD_ID, "textures/gui/remove_hovered.png");
		
		private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		private final Template template;
		private final CharacterCreationEditScreen parent;
		
		private final ButtonWidget infoButton, addButton, removeButton;
		private List<ButtonWidget> children = Lists.newArrayList();
		
		public TemplateEntry(Template templateIn, CharacterCreationEditScreen parentIn, int widthIn)
		{
			template = templateIn;
			parent = parentIn;
			children.add(infoButton = new InspectButton(0, 0, 24, 24, button -> { parent.setDetail(new DetailObject(VTUtilsClient.templateToDetail(template))); button.setFocused(false); }));
			children.add(addButton = new IconButton(0, 0, 24, 24, button -> { parent.addTemplate(template); button.setFocused(false); }, TEXTURE_ADD, TEXTURE_ADD_HOV));
			children.add(removeButton = new IconButton(0, 0, 24, 24, button -> { parent.removeTemplate(template); button.setFocused(false); }, TEXTURE_REM, TEXTURE_REM_HOV));
		}
		
		public void updatePosition(int x, int y)
		{
			infoButton.setPosition(x + 1, y);
			addButton.setPosition(x + 150, y);
			removeButton.setPosition(x + 150 + 1 + addButton.getWidth(), y);
		}
		
		public List<? extends Element> children() { return children; }
		
		public List<? extends Selectable> selectableChildren() { return children; }
		
		public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta)
		{
			addButton.active = !parent.hasTemplate(template);
			removeButton.active = parent.hasTemplate(template);
			children.forEach(child -> child.render(context, mouseX, mouseY, tickDelta));
			context.drawText(textRenderer, template.displayName(), x + 25 + (125 - textRenderer.getWidth(template.displayName())) / 2, y + (25 - textRenderer.fontHeight) / 2, 0xFFFFFF, false);
		}
	}
}
