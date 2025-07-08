package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.function.Function;

import com.lying.VariousTypes;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSoundEvents;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public abstract class ActivatedAbility extends Ability
{
	/** Handler object controlling the playing of sounds when this ability is activated */
	protected ActivationSoundSettings soundSettings = new ActivationSoundSettings(i -> VTSoundEvents.ABILITY_ACTIVATED.get());
	
	public ActivatedAbility(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public AbilityType type() { return AbilityType.ACTIVATED; }
	
	public boolean canTrigger(LivingEntity owner, AbilityInstance instance) { return true; }
	
	public final boolean trigger(LivingEntity owner, AbilityInstance instance)
	{
		boolean isServerSide = !owner.getWorld().isClient();
		if(!instance.isReadOnly() && canTrigger(owner, instance))
		{
			if(isServerSide)
			{
				activate(owner, instance);
				announceActivation(owner, instance);
			}
			
			putAbilityOnCooldown(owner, instance.mapName());
			return true;
		}
		else if(owner.getType() == EntityType.PLAYER && isServerSide)
			((PlayerEntity)owner).sendMessage(translate("gui", "activated_ability.failed", instance.displayName(owner.getRegistryManager())), true);
		return false;
	}
	
	/** Called server-side immediately after an ability is activated, usually to notify the player via SFX */
	protected void announceActivation(LivingEntity owner, AbilityInstance instance)
	{
		if(owner.getType() != EntityType.PLAYER || owner.getWorld().isClient())
			return;
		
		ServerPlayerEntity player = (ServerPlayerEntity)owner;
		player.sendMessage(translate("gui", "activated_ability.success", instance.displayName(owner.getRegistryManager())), true);
		soundSettings.playSound(player, instance);
	}
	
	/** Called server side when a player activates an ability.<br>Not responsible for cooldowns. */
	protected abstract void activate(LivingEntity owner, AbilityInstance instance);
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND; }
	
	/** Puts the ability on cooldown, if it isn't already */
	public static void putAbilityOnCooldown(LivingEntity owner, Identifier mapName)
	{
		VariousTypes.getSheet(owner).ifPresent(sheet -> 
		{
			ElementActionables actionables = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			AbilityInstance inst = actionables.get(mapName);
			if(inst.cooldown() > 0 && actionables.getCooldown(mapName) == null)
			{
				actionables.putOnCooldown(mapName, owner.getEntityWorld().getTime(), inst.cooldown());
				sheet.markDirty();
			}
		});
	}
	
	protected record ActivationSoundSettings(Function<AbilityInstance,SoundEvent> sound, Function<Random,Float> pitch, float volume, boolean playInWorld)
	{
		/** No sound */
		public ActivationSoundSettings()
		{
			this(i -> null, 1F, false);
		}
		
		/** Default pitch variation */
		public ActivationSoundSettings(Function<AbilityInstance,SoundEvent> sound, float volume, boolean playInWorld)
		{
			this(sound, r -> 0.5F + r.nextFloat() * 0.5F, volume, playInWorld);
		}
		
		/** Default user-only sound volume and pitch variation */
		public ActivationSoundSettings(Function<AbilityInstance,SoundEvent> sound)
		{
			this(sound, r -> 0.5F + r.nextFloat() * 0.5F, 0.3F, false);
		}
		
		public void playSound(ServerPlayerEntity owner, AbilityInstance instance)
		{
			SoundEvent sound = sound().apply(instance);
			if(sound == null || owner.getWorld().isClient())
				return;
			
			float pitch = pitch().apply(owner.getRandom());
			if(playInWorld)
				VTUtils.playSound(owner, sound, SoundCategory.PLAYERS, pitch, volume);
			else
				VTUtils.playSoundFor(owner, sound, SoundCategory.MASTER, pitch, volume);
		}
	}
}