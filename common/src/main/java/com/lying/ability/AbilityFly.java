package com.lying.ability;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.init.VTSheetElements;
import com.lying.utility.ServerEvents;

import dev.architectury.event.EventResult;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class AbilityFly extends Ability
{
	public AbilityFly(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.register((living, ticking) -> 
		{
			if(living.getType() != EntityType.PLAYER)
				return EventResult.pass();
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITES).hasAbility(registryName()))
				return EventResult.pass();
			
			PlayerEntity player = (PlayerEntity)living;
			if(player.getHungerManager().getFoodLevel() <= 3)
				return EventResult.pass();
			
			if(ticking && player.isFallFlying() && player.isMainPlayer())
			{
				double input = Math.max(0, player.forwardSpeed);
				if(input > 0)
				{
					Vec3d direction = player.getRotationVector();
					double forward = input * 0.3F * player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
					player.addVelocity(direction.multiply(forward));
					
					// TODO Fix using hunger for propulsion
//						float exhaustion = (float)Math.abs(input);
//						player.addExhaustion(exhaustion);
				}
			}
			
			return EventResult.interruptTrue();
		});
		
		ServerEvents.LivingEvents.PLAYER_INPUT_EVENT.register((player, forward, strafe, jump, sneak) -> 
		{
			System.out.println("Firing input event for "+player.getDisplayName().getString());
			if(player.getHungerManager().getFoodLevel() <= 3 || !player.isFallFlying())
				return;
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITES).hasAbility(registryName()))
				return;
			
			double input = Math.max(0, forward);
			System.out.println("Server motion: "+input);
			if(input > 0)
			{
				Vec3d direction = player.getRotationVector();
				double thrust = input * 0.3F * player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED);
				player.addVelocity(direction.multiply(thrust));
				
				// TODO Fix using hunger for propulsion
				float exhaustion = (float)Math.abs(input);
				player.addExhaustion(exhaustion);
			}
		});
	}
}
