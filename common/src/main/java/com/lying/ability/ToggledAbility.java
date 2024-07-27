package com.lying.ability;

import com.lying.VariousTypes;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class ToggledAbility extends ActivatedAbility
{
	public ToggledAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public AbilityType type() { return AbilityType.TOGGLED; }
	
	/** Returns true if the given ability set has an active ToggledAbility of the given registry name */
	public static boolean hasActive(AbilitySet set, Identifier registryName)
	{
		return set.getAbilitiesOfType(registryName).stream().anyMatch(inst -> ((ToggledAbility)inst.ability()).isActive(inst));
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		NbtCompound mem = instance.memory();
		mem.putBoolean("IsActive", !isActive(instance));
		instance.setMemory(mem);
		VariousTypes.getSheet(owner).ifPresent(sheet -> sheet.markDirty());
		
		if(isActive(instance))
			onActivation(owner, instance);
		else
			onDeactivation(owner, instance);
	}
	
	public boolean isActive(AbilityInstance instance)
	{
		return instance.memory().getBoolean("IsActive");
	}
	
	protected void onActivation(LivingEntity owner, AbilityInstance instance) { }
	
	protected void onDeactivation(LivingEntity owner, AbilityInstance instance) { }
}