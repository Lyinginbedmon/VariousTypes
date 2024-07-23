package com.lying.client.utility;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.lying.ability.AbilityInstance;
import com.lying.client.screen.DetailObject;
import com.lying.client.screen.DetailObject.Batch;
import com.lying.species.Species;
import com.lying.template.Template;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class VTUtilsClient
{
	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final PlayerEntity player = client.player;
	public static final DynamicRegistryManager manager = player.getRegistryManager();
	
	public static void renderDemoEntity(@Nullable PlayerEntity owner, DrawContext context, int mouseX, int mouseY, int renderX, int renderY)
	{
		// TODO Exchange for animated biped model using same player skin
		if(owner == null)
			return;
		
		int displayWidth = 200;
		int displayHeight = 200;
		int size = 80;
		InventoryScreen.drawEntity(context, renderX - (displayWidth / 2), renderY - (displayHeight / 2), renderX + (displayWidth / 2), renderY + (displayHeight / 2), size, 0.0625f, mouseX, mouseY, owner);
	}
	
	public static MutableText[] speciesToDetail(Species species)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(species.displayName().copy().formatted(Formatting.BOLD));
		entries.add(species.types().display(manager).copy());
		species.display().description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
		if(species.power() != 0)
			entries.add(translate("gui", "power_value", species.power()).copy());
		species.abilities().allNonHidden().forEach(inst -> entries.add(Text.literal(" * ").append(inst.displayName(manager))));
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(species.registryName().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
	}
	
	public static  MutableText[] templateToDetail(Template template)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(template.displayName().copy().formatted(Formatting.BOLD));
		template.display().description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
		if(template.power() != 0)
			entries.add(translate("gui", "power_value", Text.literal(String.valueOf(template.power())).copy().formatted(Formatting.GRAY)).copy());
		if(player.isCreative() && !template.preconditions().isEmpty())
		{
			entries.add(translate("gui", "preconditions").copy());
			template.preconditions().forEach(pc -> entries.add(Text.literal(" - ").append(pc.describe(manager).copy().formatted(Formatting.GRAY))));
		}
		if(!template.operations().isEmpty())
		{
			entries.add(translate("gui", "operations").copy());
			template.operations().forEach(op -> entries.add(Text.literal(" * ").append(op.describe(manager).copy().formatted(Formatting.GRAY))));
		}
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(template.registryName().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
	}
	
	public static  MutableText[] abilityToDetail(AbilityInstance instance)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(instance.displayName(manager).copy().formatted(Formatting.BOLD));
		entries.add(instance.ability().type().translate().copy());
		instance.description(manager).ifPresent(text -> entries.add(text.copy().formatted(Formatting.GRAY)));
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(instance.mapName().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
	}
	
	public static <T extends Object> DetailObject listToDetail(List<T> set, Function<T,MutableText[]> provider)
	{
		List<Batch> entries = Lists.newArrayList();
		set.forEach(entry -> entries.add(new Batch(provider.apply(entry))));
		return new DetailObject(entries.toArray(new Batch[0]));
	}
}
