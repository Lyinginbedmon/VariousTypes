package com.lying.client.screen;

import static com.lying.utility.VTUtils.describeSpecies;
import static com.lying.utility.VTUtils.describeType;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.screen.CharacterSheetScreenHandler;
import com.lying.type.Type.Tier;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CharacterSheetScreen extends HandledScreen<CharacterSheetScreenHandler>
{
	protected static final Identifier VIGNETTE_TEXTURE = new Identifier("textures/misc/vignette.png");
	
	private CharacterSheet sheet;
	private PlayerEntity player;
	
	private ButtonWidget speciesButton, templatesButton;
	
	public CharacterSheetScreen(CharacterSheetScreenHandler handler, PlayerInventory inventory, Text title)
	{
		super(handler, inventory, title);
		player = inventory.player;
		sheet = VariousTypes.getSheet(player).get();
	}
	
	public void init()
	{
		super.init();
		
		int midX = width / 2;
		int midY = height / 2;
		int spacing = 120;
		addDrawable(ButtonWidget.builder(Text.literal("Type"), (button) -> {}).dimensions(midX - spacing - 22, midY - 90, 45, 45).tooltip(Tooltip.of(describeType(sheet.types().ofTier(Tier.SUPERTYPE).stream().findFirst().get(), player.getRegistryManager()))).build());
		addDrawable(speciesButton = ButtonWidget.builder(Text.literal("Species"), (button) -> {}).dimensions(midX - spacing - 20, midY - 90 + 50, 40, 40).tooltip(Tooltip.of(describeSpecies(sheet.getSpecies().get()))).build());
		addDrawable(templatesButton = ButtonWidget.builder(Text.literal("Templates"), (button) -> {}).dimensions(midX - spacing - 20, midY - 90 + 100, 40, 40).tooltip(Tooltip.of(describeSpecies(sheet.getSpecies().get()))).build());
		
		for(int i=0; i<4; i++)
			addDrawable(ButtonWidget.builder(Text.literal("Ability "+i), (button) -> {}).dimensions(midX + spacing - 30, midY - 80 + i * 50, 60, 30).build());
		
		speciesButton.active = sheet.hasASpecies();
		templatesButton.active = templatesButton.visible = !sheet.getAppliedTemplates().isEmpty();
	}
	
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta)
	{
		renderBackgroundTexture(context, sheet.types().ofTier(Tier.SUPERTYPE).stream().findFirst().get().sheetBackground(), 0, 0, 0, 0, width, height);
		renderVignette(context);
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
		context.drawCenteredTextWithShadow(textRenderer, ownerName, midX, midY - 90, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal(String.valueOf(sheet.power())), midX, midY + 90, 0xFFFFFF);
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
	
//	private void renderMainStats(DrawContext context)
//	{
//		int x = backgroundWidth / 2 - 100;
//		int y = 0;
//		if(sheet.hasASpecies())
//		{
//			context.drawText(textRenderer, describeSpecies(sheet.getSpecies().get()), x, y, 0xFFFFFF, true);
//			y += 10;
//		}
//		
//		if(!sheet.getAppliedTemplates().isEmpty())
//			for(Template tem : sheet.getAppliedTemplates())
//			{
//				context.drawText(textRenderer, describeTemplate(tem), x, y, 0xFFFFFF, true);
//				y += 10;
//			}
//		
//		if(!sheet.types().isEmpty())
//			for(Type type : sheet.types().contents())
//			{
//				context.drawText(textRenderer, describeType(type, client.player.getRegistryManager()), x, y, 0xFFFFFF, true);
//				y += 10;
//			}
//	}
	
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
}
