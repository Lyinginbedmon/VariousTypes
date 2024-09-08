package com.lying.ability;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.init.VTSheetElements;

import dev.architectury.event.EventResult;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class AbilityFly extends Ability
{
	public static final double DEFAULT_SPEED = 1D;
	public static final float DEFAULT_EXHAUSTION = 0.15F;
	
	public AbilityFly(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.register((living, ticking) -> 
		{
			if(living.getType() != EntityType.PLAYER)
				return EventResult.pass();
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return EventResult.pass();
			
			PlayerEntity player = (PlayerEntity)living;
			if(player.getHungerManager().getFoodLevel() <= 3)
				return EventResult.pass();
			
			if(ticking && player.isFallFlying() && (!player.getWorld().isClient() || player.isMainPlayer()))
			{
				double input = Math.max(0, player.forwardSpeed);
				if(input > 0)
				{
					AbilityInstance flight = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
					double multiplier = flight.memory().contains("Speed", NbtElement.DOUBLE_TYPE) ? flight.memory().getDouble("Speed") : DEFAULT_SPEED;
					
					Vec3d direction = player.getRotationVector();
					double forward = input * 0.3F * player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * multiplier;
					player.addVelocity(direction.multiply(forward));
				}
			}
			
			return EventResult.interruptTrue();
		});
		
		PlayerEvents.PLAYER_FLIGHT_INPUT_EVENT.register((player, forward, strafe, jump, sneak) -> 
		{
			if(player.getHungerManager().getFoodLevel() <= 3 || !player.isFallFlying())
				return;
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return;
			
			AbilityInstance flight = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
			float exhaustion = flight.memory().contains("Food", NbtElement.FLOAT_TYPE) ? flight.memory().getFloat("Food") : DEFAULT_EXHAUSTION;
			
			double input = Math.max(0, forward);
			if(input > 0)
				player.addExhaustion((float)Math.abs(input) * exhaustion);
		});
	}
}
