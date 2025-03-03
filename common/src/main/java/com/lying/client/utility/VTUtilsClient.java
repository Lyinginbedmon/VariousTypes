package com.lying.client.utility;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.google.common.collect.Lists;
import com.lying.ability.AbilityInstance;
import com.lying.client.screen.DetailObject;
import com.lying.client.screen.DetailObject.Batch;
import com.lying.reference.Reference;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.utility.VTUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class VTUtilsClient
{
	public static final MinecraftClient client = MinecraftClient.getInstance();
	public static final PlayerEntity player = client.player;
	
	public static void renderDisplayEntity(@Nullable LivingEntity entity, DrawContext context, int renderX, int renderY, float pitch, float yaw)
	{
		if(entity == null)
			return;
		
		int size = 80;
		float p = entity.getScale();
		Quaternionf quaternionf = new Quaternionf().rotateZ((float)Math.PI);
		Quaternionf quaternionf2 = new Quaternionf().rotateX((float)Math.toRadians(pitch));
		quaternionf.mul(quaternionf2);
		entity.bodyYaw = 180F + yaw * 20F;
		entity.setYaw(180F + yaw * 40F);
		entity.setPitch(pitch);
		entity.headYaw = entity.getYaw();
		entity.prevHeadYaw = entity.getYaw();
		float q = size / p;
		InventoryScreen.drawEntity(context, renderX, renderY, q, new Vector3f(0.0f, entity.getHeight() / 2.0f + 0.0625f * p, 0.0f), quaternionf, quaternionf2, entity);
	}
	
	public static void renderDisplayEntity(@Nullable LivingEntity entity, DrawContext context, int renderX, int renderY, float pitch)
	{
		if(entity == null)
			return;
		
		float yaw;
		switch(client.options.getMainArm().getValue())
		{
			case LEFT:
				yaw = (float)Math.toRadians(75D);
				break;
			case RIGHT:
			default:
				yaw = (float)Math.toRadians(-75D);
				break;
		}
		renderDisplayEntity(entity, context, renderX, renderY, pitch, yaw);
	}
	
	public static MutableText[] speciesToDetail(Species species)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(species.displayName().copy().formatted(Formatting.BOLD));
		entries.add(species.types().display().copy());
		species.display().description().ifPresent(desc -> entries.add(desc.copy().formatted(Formatting.ITALIC, Formatting.GRAY)));
		if(species.power() != 0)
			entries.add(translate("gui", "power_value", species.power()).copy());
		species.abilities().allNonHidden().forEach(inst -> entries.add(Text.literal(" * ").append(inst.displayName())));
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
			template.preconditions().forEach(pc -> entries.add(Text.literal(" - ").append(pc.describe().copy().formatted(Formatting.GRAY))));
		}
		if(!template.operations().isEmpty())
		{
			entries.add(translate("gui", "operations").copy());
			template.operations().forEach(op -> entries.add(Text.literal(" * ").append(op.describe().copy().formatted(Formatting.GRAY))));
		}
		if(client.options.advancedItemTooltips || player.isCreative())
			entries.add(Text.literal(template.registryName().toString()).copy().formatted(Formatting.DARK_GRAY));
		return entries.toArray(new MutableText[0]);
	}
	
	public static  MutableText[] abilityToDetail(AbilityInstance instance)
	{
		List<MutableText> entries = Lists.newArrayList();
		entries.add(instance.displayName().copy().formatted(Formatting.BOLD));
		entries.add(instance.ability().type().translate().copy());
		if(instance.cooldown() > 0)
			entries.add(Reference.ModInfo.translate("gui","ability_cooldown", VTUtils.ticksToTime(instance.cooldown())).copy());
		instance.tooltip().forEach(line -> entries.add(line.copy().formatted(Formatting.GRAY)));
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
	
	/** Multiply colour components together as percentiles and return equivalent integer form */
	public static int mixColors(int colA, int colB)
	{
		float a = (float)colA / 255F;
		float b = (float)colB / 255F;
		return (int)(255 * (a * b));
	}
	
	/** Returns a float vector containing the RGB colours of the given decimal, ranged 0 to 1 */
	public static Vector3f decimalToVector(int color)
	{
		int r = (color & 0xFF0000) >> 16;
		int g = (color & 0xFF00) >> 8;
		int b = (color & 0XFF) >> 0;
		return new Vector3f((float)r / 255F, (float)g / 255F, (float)b / 255F);
	}
	
	public static int vectorToDecimal(Vector3f color)
	{
		int r = (int)(255 * color.x());
		int g = (int)(255 * color.y());
		int b = (int)(255 * color.z());
		return (r << 16) + (g << 8) + (b << 0);
	}
}
