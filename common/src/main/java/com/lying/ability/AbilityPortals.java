package com.lying.ability;

import java.util.Optional;

import org.apache.commons.lang3.function.Consumers;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementHome;
import com.lying.entity.PortalEntity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public abstract class AbilityPortals extends ActivatedAbility
{
	protected AbilityPortals(Identifier regName)
	{
		super(regName, Category.UTILITY);
	}

	public static class ToHomeDim extends AbilityPortals
	{
		public ToHomeDim(Identifier regName)
		{
			super(regName);
		}
		
		public boolean canTrigger(LivingEntity owner, AbilityInstance instance)
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(owner);
			return sheetOpt.isPresent() && ElementHome.get(sheetOpt.get()) != owner.getWorld().getRegistryKey();
		}
		
		protected void activate(LivingEntity owner, AbilityInstance instance)
		{
			ServerWorld currentWorld = (ServerWorld)owner.getWorld();
			
			BlockPos currentPos = owner.getBlockPos();
			double range = 8F;
			HitResult trace = owner.raycast(range, 1F, true);
			if(trace.getType() != HitResult.Type.MISS)
				currentPos = BlockPos.ofFloored(trace.getPos().getX(), trace.getPos().getY(), trace.getPos().getZ());
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(owner);
			if(sheetOpt.isEmpty())
				return;
			
			ServerWorld homeWorld = currentWorld.getServer().getWorld(ElementHome.get(sheetOpt.get())); 
			PortalEntity.createLinked(currentWorld, homeWorld, currentPos, Optional.empty(), Consumers.nop());
		}
	}
}
