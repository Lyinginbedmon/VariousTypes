package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.data.VTTags;
import com.lying.utility.ServerEvents;
import com.lying.utility.VTUtils;

import dev.architectury.event.EventResult;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilityStatusTagImmune extends Ability
{
	public AbilityStatusTagImmune(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability",registryName().getPath()+".desc", VTUtils.tagListToString(getTags(instance.memory()), ", ")));
	}
	
	public void registerEventHandlers()
	{
		ServerEvents.LivingEvents.CAN_HAVE_STATUS_EFFECT_EVENT.register((effect,abilities) -> 
		{
			List<AbilityInstance> set = abilities.getAbilitiesOfType(registryName());
			return set.stream().anyMatch(inst -> getTags(inst.memory()).stream().anyMatch(tag -> effect.getEffectType().isIn(tag))) ? EventResult.interruptFalse() : EventResult.pass();
		});
	}
	
	public static List<TagKey<StatusEffect>> getTags(NbtCompound memory)
	{
		return AbilityHelper.getTags(memory, "Effects", RegistryKeys.STATUS_EFFECT, () -> List.of(VTTags.POISONS));
	}
}