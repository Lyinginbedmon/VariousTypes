package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;

import com.lying.VariousTypes;
import com.lying.client.utility.CosmeticSet;
import com.lying.component.element.ElementCosmetics;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;
import com.mojang.brigadier.Command;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModuleCustomCosmetics extends AbstractSheetModule
{
	private final CosmeticSet cosmetics = CosmeticSet.of(List.of());
	
	public ModuleCustomCosmetics()
	{
		super(Reference.ModInfo.prefix("custom_cosmetics"), 900);
	}
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context -> 
		{
			source.sendFeedback(() -> translate("command", "get.custom_cosmetics.success", owner.getName(), cosmetics.size()), true);
			cosmetics.values().forEach(cos -> source.sendFeedback(() -> Text.literal(" * ").append(cos.describe()), false));
			return 15;
		};
	}
	
	public int power() { return 0; }
	
	public CosmeticSet get() { return cosmetics; }
	
	public void add(Cosmetic cosmetic)
	{
		cosmetics.add(cosmetic);
		updateSheet();
	}
	
	public boolean remove(Identifier registryId)
	{
		if(cosmetics.has(registryId))
		{
			cosmetics.remove(registryId);
			updateSheet();
			return true;
		}
		return false;
	}
	
	public boolean remove(Identifier registryId, int colour)
	{
		if(cosmetics.has(registryId, colour))
		{
			cosmetics.remove(registryId, colour);
			updateSheet();
			return true;
		}
		return false;
	}
	
	public boolean removeAll(CosmeticType category)
	{
		if(cosmetics.removeAll(category))
		{
			updateSheet();
			return true;
		}
		return false;
	}
	
	public void clear()
	{
		boolean shouldUpdate = !cosmetics.isEmpty();
		cosmetics.clear();
		if(shouldUpdate)
			updateSheet();
	}
	
	public void affect(ISheetElement<?> element)
	{
		if(!cosmetics.isEmpty() && element.registry() == VTSheetElements.COSMETICS)
			((ElementCosmetics)element).value().addAll(cosmetics);
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.put("Values", CosmeticSet.CODEC.encodeStart(NbtOps.INSTANCE, cosmetics).getOrThrow());
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		cosmetics.clear();
		if(!nbt.contains("Values"))
			return;
		
		CosmeticSet data = CosmeticSet.CODEC.parse(NbtOps.INSTANCE, nbt.get("Values")).resultOrPartial(VariousTypes.LOGGER::error).orElse(CosmeticSet.of(List.of()));
		cosmetics.addAll(data);
	}
}
