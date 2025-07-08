package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.utility.VTUtils;

import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityPariah extends Ability
{
	public AbilityPariah(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		NbtList list = instance.memory().contains("Entities", NbtElement.LIST_TYPE) ? instance.memory().getList("Entities", NbtElement.STRING_TYPE) : new NbtList();
		MutableText names = VTUtils.listToString(listToTypes(list), EntityType::getName, ", ");
		return Optional.of(translate("ability",registryName().getPath()+".desc", names));
	}
	
	public static boolean includes(AbilityInstance inst, EntityType<?> type)
	{
		NbtList list = inst.memory().contains("Entities", NbtElement.LIST_TYPE) ? inst.memory().getList("Entities", NbtElement.STRING_TYPE) : new NbtList();
		return listToTypes(list).contains(type);
	}
	
	public static List<EntityType<?>> listToTypes(NbtList listIn)
	{
		List<EntityType<?>> listOut = Lists.newArrayList();
		for(NbtElement element : listIn)
		{
			EntityType<?> type = Registries.ENTITY_TYPE.get(new Identifier(element.asString()));
			if(type != null)
				listOut.add(type);
		}
		return listOut;
	}
}
