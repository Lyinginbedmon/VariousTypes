package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import com.lying.VariousTypes;
import com.lying.init.VTSoundEvents;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class ToggledAbility extends ActivatedAbility
{
	public ToggledAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
		this.soundSettings = new ActivationSoundSettings(i -> isActive(i) ? VTSoundEvents.ABILITY_TOGGLE_ON.get() : VTSoundEvents.ABILITY_TOGGLE_OFF.get());
	}
	
	public AbilityType type() { return AbilityType.TOGGLED; }
	
	/** Returns true if the given ability set has an active ToggledAbility of the given registry name */
	public static boolean hasActive(AbilitySet set, Identifier registryName)
	{
		return set.getAbilitiesOfType(registryName).stream().anyMatch(inst -> ((ToggledAbility)inst.ability()).isActive(inst));
	}
	
	protected void announceActivation(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER || owner.getWorld().isClient())
			return;
		
		ServerPlayerEntity player = (ServerPlayerEntity)owner;
		player.sendMessage(translate("gui", "toggled_ability."+(isActive(instance) ? "activated" : "deactivated"), instance.displayName(owner.getRegistryManager())), true);
		soundSettings.playSound(player, instance);
	}
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		NbtCompound mem = instance.memory();
		mem.putBoolean("IsActive", !isActive(instance));
		instance.setMemory(mem);
		
		if(isActive(instance))
			onToggledOn(owner, instance);
		else
			onToggledOff(owner, instance);
		
		VariousTypes.getSheet(owner).ifPresent(sheet -> sheet.buildAndSync());
	}
	
	public boolean isActive(AbilityInstance instance)
	{
		return instance.memory().getBoolean("IsActive");
	}
	
	protected void onToggledOn(LivingEntity owner, AbilityInstance instance) { }
	
	protected void onToggledOff(LivingEntity owner, AbilityInstance instance) { }
}