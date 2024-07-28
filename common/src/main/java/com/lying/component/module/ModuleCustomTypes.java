package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.type.Type;
import com.lying.type.TypeSet;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;

public class ModuleCustomTypes extends AbstractSheetModule
{
	public static final SimpleCommandExceptionType NO_VALUE = new SimpleCommandExceptionType(translate("command", "failed_no_custom_types_applied"));
	private final TypeSet customTypes = new TypeSet();
	
	public ModuleCustomTypes() { super(Reference.ModInfo.prefix("custom_types"), 900); }
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context -> {
			if(customTypes.isEmpty())
				throw NO_VALUE.create();
			
			source.sendFeedback(() -> translate("command","get.custom_types.success", owner.getDisplayName(), customTypes.display()), true);
			return 15;
		};
	}
	
	public int power() { return 0; }
	
	public boolean has(Type typeIn)
	{
		return customTypes.contains(typeIn);
	}
	
	public Optional<Type> get(Identifier id)
	{
		return customTypes.get(id);
	}
	
	public boolean add(Type typeIn)
	{
		if(has(typeIn))
			return false;
		
		customTypes.add(typeIn);
		updateSheet();
		return true;
	}
	
	public boolean remove(Type typeIn)
	{
		if(!has(typeIn))
			return false;
		
		customTypes.remove(typeIn);
		updateSheet();
		return true;
	}
	
	public void clear()
	{
		customTypes.clear();
		updateSheet();
	}
	
	public void affect(ISheetElement<?> element)
	{
		if(element.registry() == VTSheetElements.TYPES && !customTypes.isEmpty())
		{
			TypeSet types = (TypeSet)element;
			types.clear();
			types.addAll(customTypes);
		}
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		nbt.put("Types", customTypes.writeToNbt());
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		customTypes.clear();
		customTypes.addAll(TypeSet.readFromNbt(nbt.getList("Types", NbtElement.COMPOUND_TYPE)));
	}
}
