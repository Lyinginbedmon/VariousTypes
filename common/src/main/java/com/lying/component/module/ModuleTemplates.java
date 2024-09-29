package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.describeTemplate;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTTemplateRegistry;
import com.lying.reference.Reference;
import com.lying.template.Template;
import com.lying.type.TypeSet;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModuleTemplates extends AbstractSheetModule
{
	public static final SimpleCommandExceptionType NO_VALUE = new SimpleCommandExceptionType(translate("command", "failed_no_templates_applied"));
	private List<Identifier> templateIds = Lists.newArrayList();
	
	public ModuleTemplates() { super(Reference.ModInfo.prefix("templates"), 100); }
	
	public static boolean hasTemplate(CharacterSheet sheet, Identifier registryName) { return sheet.module(VTSheetModules.TEMPLATES).contains(registryName); }
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context -> 
		{
			List<Template> templates = get();
			if(templates.isEmpty())
				throw NO_VALUE.create();
			
			source.sendFeedback(() -> translate("command","get.templates.success", owner.getDisplayName(), templates.size()), true);
			templates.forEach(tem -> source.sendFeedback(() -> Text.literal(" * ").append(describeTemplate(tem)), false));
			return 15;
		};
	}
	
	public int power()
	{
		int tally = 0;
		for(Template tem : get())
			tally += tem.power();
		return tally;
	}
	
	public void clear()
	{
		templateIds.clear();
		updateSheet();
	}
	
	public boolean contains(Identifier registryName) { return templateIds.contains(registryName); }
	
	public int index(Identifier registryName)
	{
		return contains(registryName) ? templateIds.indexOf(registryName) : -1;
	}
	
	public void set(Identifier... idIn)
	{
		templateIds.clear();
		add(idIn);
		if(idIn.length == 0)
			updateSheet();
	}
	
	public boolean add(Identifier... idIn)
	{
		boolean shouldUpdate = false;
		for(Identifier id : idIn)
			if(!templateIds.contains(id))
			{
				templateIds.add(id);
				shouldUpdate = true;
			}
		if(shouldUpdate)
			updateSheet();
		return shouldUpdate;
	}
	
	/** Applies the given template, providing any loot if specified */
	public boolean addTemplate(Identifier idIn, ServerPlayerEntity player)
	{
		if(add(idIn))
		{
			VTTemplateRegistry.instance().get(idIn).ifPresent(template -> template.giveLootTo(player));
			return true;
		}
		return false;
	}
	
	public void remove(Identifier... idIn)
	{
		boolean shouldUpdate = false;
		for(Identifier id : idIn)
			if(templateIds.contains(id))
			{
				templateIds.remove(id);
				shouldUpdate = true;
			}
		if(shouldUpdate)
			updateSheet();
	}
	
	public List<Template> get()
	{
		List<Template> templates = Lists.newArrayList();
		templateIds.forEach(id -> VTTemplateRegistry.instance().get(id).ifPresent(tem -> templates.add(tem)));
		return templates;
	}
	
	public void affect(ISheetElement<?> element)
	{
		List<Template> templates = get();
		if(templates.isEmpty())
			return;
		else if(element.registry() == VTSheetElements.TYPES)
		{
			TypeSet types = (TypeSet)element;
			templates.forEach(tem -> tem.applyTypeOperations(types));
		}
		else if(element.registry() == VTSheetElements.ABILITIES)
		{
			AbilitySet abilities = (AbilitySet)element;
			templates.forEach(tem -> tem.applyAbilityOperations(abilities));
		}
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		if(templateIds.isEmpty()) return nbt;
		NbtList list = new NbtList();
		templateIds.forEach(id -> list.add(NbtString.of(id.toString())));
		nbt.put("IDs", list);
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("IDs", NbtElement.LIST_TYPE))
		{
			NbtList list = nbt.getList("IDs", NbtElement.STRING_TYPE);
			list.forEach(entry -> templateIds.add(new Identifier(entry.asString())));
		}
	}

}
