package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;

import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModuleCustomAbilities extends AbstractSheetModule
{
	public static final SimpleCommandExceptionType NO_VALUE = new SimpleCommandExceptionType(translate("command", "failed_no_custom_abilities"));
	private final AbilitySet customAbilities = new AbilitySet();
	
	public ModuleCustomAbilities() { super(Reference.ModInfo.prefix("custom_abilities"), 900); }
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context -> {
			if(customAbilities.isEmpty())
				throw NO_VALUE.create();
			
			source.sendFeedback(() -> translate("command","get.custom_abilities.success", owner.getDisplayName(), customAbilities.size()), true);
			customAbilities.abilities().forEach(inst -> source.sendFeedback(() -> Text.literal(" * ").append(VTUtils.describeAbility(inst)), false));
			return 15;
		};
	}
	
	public int power() { return 0; }
	
	public void clear()
	{
		customAbilities.clear();
		updateSheet();
	}
	
	public int size() { return customAbilities.size(); }
	
	public AbilityInstance get(Identifier mapName)
	{
		return customAbilities.get(mapName);
	}
	
	public void add(AbilityInstance instance)
	{
		customAbilities.set(instance);
		updateSheet();
	}
	
	public void remove(Identifier mapName)
	{
		customAbilities.remove(mapName);
		updateSheet();
	}
	
	public void affect(ISheetElement<?> element)
	{
		if(element.registry() == VTSheetElements.ABILITIES && !customAbilities.isEmpty())
			customAbilities.abilities().forEach(inst -> ((AbilitySet)element).add(inst.copy()));
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.put("Abilities", customAbilities.writeToNbt());
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		customAbilities.clear();
		AbilitySet.readFromNbt(nbt.getList("Abilities", NbtElement.COMPOUND_TYPE)).abilities().forEach(inst -> customAbilities.add(inst));
	}
}
