package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AbilityFlexible extends ToggledAbility
{
	private static final UUID BONUS_UUID = UUID.fromString("9cb80bf6-6028-4c2e-904d-5075ba118572");
	private static final UUID PENALTY_UUID = UUID.fromString("383d08f0-7950-48d5-9176-5a1de4c2511f");
	
	public AbilityFlexible(Identifier regName, Category catIn) {
		super(regName, catIn);
	}
	
	public Identifier mapName(AbilityInstance inst)
	{
		Identifier regName = inst.ability().registryName();
		boolean isBonus = getScale(inst.memory()) >= 0;
		return new Identifier(regName.getNamespace(), regName.getPath()+"_"+(isBonus ? "bonus" : "penalty"));
	}
	
	protected void onToggledOn(LivingEntity owner, AbilityInstance instance)
	{
		float amount = getScale(instance.memory());
		EntityAttributeInstance attribute = owner.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
		if(attribute == null)
			return;
		
		EntityAttributeModifier modifier = new EntityAttributeModifier(amount < 0 ? PENALTY_UUID : BONUS_UUID, amount < 0 ? "flexible_penalty" : "flexible_bonus", amount, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		if(attribute.getModifier(modifier.uuid()) == null)
			attribute.addPersistentModifier(modifier);
	}
	
	protected void onToggledOff(LivingEntity owner, AbilityInstance instance)
	{
		float amount = getScale(instance.memory());
		EntityAttributeInstance attribute = owner.getAttributeInstance(EntityAttributes.GENERIC_SCALE);
		if(attribute == null)
			return;
		
		UUID modID = amount < 0 ? PENALTY_UUID : BONUS_UUID;
		if(attribute.getModifier(modID) != null)
			attribute.removeModifier(modID);
	}
	
	protected static float getScale(NbtCompound memory)
	{
		return memory.contains("Amount") ? MathHelper.clamp(memory.getFloat("Amount"), -0.95F, 1500F) : -0.5F;
	}
	
	public Text displayName(AbilityInstance instance) { return translate("ability", registryName().getPath()+(getScale(instance.memory()) >= 0 ? ".bonus" : ".penalty")); }
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		float amount = getScale(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+(amount >= 0 ? ".bonus" : ".penalty")+".desc", (int)(Math.abs(amount * 100))));
	}
}