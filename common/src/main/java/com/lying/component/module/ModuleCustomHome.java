package com.lying.component.module;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.component.element.ElementHome;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModuleCustomHome extends AbstractSheetModule
{
	public static final SimpleCommandExceptionType NO_VALUE = new SimpleCommandExceptionType(translate("command", "failed_no_dimension_set"));
	private Optional<RegistryKey<World>> dimension = Optional.empty();
	
	public ModuleCustomHome()
	{
		super(Reference.ModInfo.prefix("home"), 1000);
	}
	
	public Command<ServerCommandSource> describeTo(ServerCommandSource source, LivingEntity owner)
	{
		return context ->
		{
			if(dimension.isEmpty())
				throw NO_VALUE.create();
			
			source.sendFeedback(() -> translate("command", "get.custom_home.success", dimension.get().getValue().toString()), true);
			return 15;
		};
	}
	
	public int power() { return 0; }
	
	public boolean isPresent() { return dimension.isPresent(); }
	
	public RegistryKey<World> get() { return dimension.get(); }
	
	public void set(@Nullable RegistryKey<World> worldIn)
	{
		if(worldIn == null && dimension.isEmpty() || !dimension.isEmpty() && dimension.get() == worldIn)
			return;
		
		dimension = worldIn == null ? Optional.empty() : Optional.of(worldIn);
		updateSheet();
	}
	
	public void clear() { set(null); }
	
	public void affect(ISheetElement<?> element)
	{
		if(!dimension.isPresent())
			return;
		else if(element.registry() == VTSheetElements.HOME_DIM)
			((ElementHome)element).set(dimension.get());
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		dimension.ifPresent(id -> nbt.putString("ID", id.getValue().toString()));
		return nbt;
	}
	
	protected void readFromNbt(NbtCompound nbt)
	{
		if(nbt.contains("ID", NbtElement.STRING_TYPE))
			dimension = Optional.of(RegistryKey.of(RegistryKeys.WORLD, new Identifier(nbt.getString("ID"))));
	}
}
