package com.lying.component.module;

import java.util.Optional;

import com.lying.component.element.ElementHome;
import com.lying.component.element.ISheetElement;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ModuleHome extends AbstractSheetModule
{
	private Optional<RegistryKey<World>> dimension = Optional.empty();
	
	public ModuleHome()
	{
		super(Reference.ModInfo.prefix("home"), 1000);
	}
	
	public int power() { return 0; }
	
	public boolean isPresent() { return dimension.isPresent(); }
	
	public RegistryKey<World> get() { return dimension.get(); }
	
	public void set(RegistryKey<World> worldIn) { dimension = worldIn == null ? Optional.empty() : Optional.of(worldIn); }
	
	public void clear() { dimension = Optional.empty(); }
	
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
