package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.init.VTSheetElements;
import com.lying.reference.Reference;
import com.lying.utility.VTUtils;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbilityOnMeleeHit extends Ability
{
	protected AbilityOnMeleeHit(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public void registerEventHandlers()
	{
		EntityEvent.LIVING_HURT.register((entity, source, amount) -> 
		{
			Entity sourceEntity = source.getSource();
			if(sourceEntity != null && sourceEntity instanceof LivingEntity)
			{
				LivingEntity attacker = (LivingEntity)sourceEntity;
				VariousTypes.getSheet(attacker).ifPresent(sheet -> 
					sheet.<AbilitySet>elementValue(VTSheetElements.ABILITIES).getAbilitiesOfClass(AbilityOnMeleeHit.class).forEach(inst -> 
						((AbilityOnMeleeHit)inst.ability()).onMeleeHit(entity, attacker, inst)));
			}
			
			return EventResult.pass();
		});
	}
	
	public abstract void onMeleeHit(LivingEntity victim, LivingEntity attacker, AbilityInstance inst);
	
	public static class SetFire extends AbilityOnMeleeHit
	{
		public SetFire(Identifier regName, Category catIn)
		{
			super(regName, catIn);
		}
		
		public Optional<Text> description(AbilityInstance instance)
		{
			return Optional.of(translate("ability", registryName().getPath()+".desc", VTUtils.ticksToTime(getFireTicks(instance))));
		}
		
		public void onMeleeHit(LivingEntity victim, LivingEntity attacker, AbilityInstance inst)
		{
			if(victim.isOnFire() || victim.isFireImmune())
				return;
			victim.setFireTicks(getFireTicks(inst));
		}
		
		private static int getFireTicks(AbilityInstance inst) { return inst.memory().contains("Ticks", NbtElement.INT_TYPE) ? inst.memory().getInt("Ticks") : 4 * Reference.Values.TICKS_PER_SECOND; }
	}
}
