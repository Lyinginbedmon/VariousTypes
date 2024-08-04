package com.lying.ability;

import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;

public class AbilityRemappablePassive extends Ability
{
	public AbilityRemappablePassive(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Identifier mapName(AbilityInstance instance)
	{
		if(instance.memory().contains("MapName", NbtElement.STRING_TYPE))
			return new Identifier(instance.memory().getString("MapName"));
		return super.registryName();
	}
}
