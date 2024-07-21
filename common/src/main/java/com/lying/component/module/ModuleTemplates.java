package com.lying.component.module;

import java.util.List;

import com.google.common.collect.Lists;
import com.lying.ability.AbilitySet;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.init.VTTemplateRegistry;
import com.lying.reference.Reference;
import com.lying.template.Template;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;

public class ModuleTemplates extends AbstractSheetModule
{
	private List<Identifier> templateIds = Lists.newArrayList();
	
	public ModuleTemplates() { super(Reference.ModInfo.prefix("templates"), 10); }
	
	public int power()
	{
		int tally = 0;
		for(Template tem : get())
			tally += tem.power();
		return tally;
	}
	
	public void clear() { templateIds.clear(); }
	
	public boolean contains(Identifier registryName) { return templateIds.contains(registryName); }
	
	public void add(Identifier idIn)
	{
		if(!templateIds.contains(idIn))
			templateIds.add(idIn);
	}
	
	public void remove(Identifier idIn)
	{
		if(templateIds.contains(idIn))
			templateIds.remove(idIn);
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
		else if(element.registry() == VTSheetElements.ABILITES)
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
