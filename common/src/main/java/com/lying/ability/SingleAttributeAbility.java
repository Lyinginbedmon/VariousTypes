package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.lying.init.VTAbilities;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public abstract class SingleAttributeAbility extends Ability
{
	public static final String AMOUNT = "Amount";
	protected final UUID modifierID;
	protected final String modifierName;
	
	protected final Supplier<RegistryEntry<EntityAttribute>> attribute;
	protected final EntityAttributeModifier.Operation operation;
	
	protected SingleAttributeAbility(Identifier regName, Category catIn, Supplier<RegistryEntry<EntityAttribute>> attributeIn, UUID uuidIn, String nameIn)
	{
		this(regName, catIn, attributeIn, EntityAttributeModifier.Operation.ADD_VALUE, uuidIn, nameIn);
	}
	
	protected SingleAttributeAbility(Identifier regName, Category catIn, Supplier<RegistryEntry<EntityAttribute>> attributeIn, EntityAttributeModifier.Operation opIn, UUID uuidIn, String nameIn)
	{
		super(regName, catIn);
		this.attribute = attributeIn;
		this.operation = opIn;
		this.modifierID = uuidIn;
		this.modifierName = nameIn;
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		return Optional.of(translate("ability", registryName().getPath()+".desc", getAmount(instance.memory())));
	}
	
	protected void generateAttributeModifiers()
	{
		addAttributeModifier(attribute.get(), inst -> 
			new EntityAttributeModifier(modifierID, modifierName, getAmount(inst.memory()), operation));
	}
	
	protected static int getAmount(NbtCompound memory)
	{
		return memory.contains(AMOUNT) ? memory.getInt(AMOUNT) : 3;
	}
	
	public static class Damage extends SingleAttributeAbility
	{
		public Damage(Identifier regName, Category catIn)
		{
			super(regName, catIn, () -> EntityAttributes.GENERIC_ATTACK_DAMAGE, UUID.fromString("051f48b9-4c6f-4b68-9a38-d488b1b5cc01"), "natural_weapon");
		}
		
		public static AbilityInstance of(int amount) { return of(amount, AbilitySource.MISC); }
		
		public static AbilityInstance of(int amount, AbilitySource source)
		{
			return VTAbilities.BONUS_DMG.get().instance(source, nbt -> nbt.putInt(AMOUNT, amount));
		}
		
		public Text displayName(AbilityInstance instance) { return translate("ability", registryName().getPath()+(getAmount(instance.memory()) >= 0 ? ".bonus" : ".penalty")); }
		
		public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
		{
			int amount = getAmount(instance.memory());
			return Optional.of(translate("ability", registryName().getPath()+(amount >= 0 ? ".bonus" : ".penalty")+".desc", amount));
		}
	}
	
	public static class Armour extends SingleAttributeAbility
	{
		public Armour(Identifier regName, Category catIn)
		{
			super(regName, catIn, () -> EntityAttributes.GENERIC_ARMOR, UUID.fromString("04ccadcc-2c51-48ce-89fd-4204e746f2bb"), "natural_armour_bonus");
		}
		
		public static AbilityInstance of(int amount) { return of(amount, AbilitySource.MISC); }
		
		public static AbilityInstance of(int amount, AbilitySource source)
		{
			return VTAbilities.NAT_ARMOUR.get().instance(source, nbt -> nbt.putInt(AMOUNT, amount));
		}
	}
	
	public static class Health extends SingleAttributeAbility
	{
		public Health(Identifier regName, Category catIn)
		{
			super(regName, catIn, () -> EntityAttributes.GENERIC_MAX_HEALTH, UUID.fromString("b6315362-9843-4881-a5d9-f186ab38baf6"), "bonus_health");
		}
		
		public static AbilityInstance of(int amount) { return of(amount, AbilitySource.MISC); }
		
		public static AbilityInstance of(int amount, AbilitySource source)
		{
			return VTAbilities.BONUS_HEALTH.get().instance(source, nbt -> nbt.putInt(AMOUNT, amount));
		}
		
		public Text displayName(AbilityInstance instance) { return translate("ability", registryName().getPath()+(getAmount(instance.memory()) >= 0 ? ".bonus" : ".penalty")); }
		
		public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
		{
			int amount = getAmount(instance.memory());
			return Optional.of(translate("ability", registryName().getPath()+(amount >= 0 ? ".bonus" : ".penalty")+".desc", (int)MathHelper.abs(amount)));
		}
	}
	
	public static class Scale extends SingleAttributeAbility
	{
		public Scale(Identifier regName, Category catIn)
		{
			super(regName, catIn, () -> EntityAttributes.GENERIC_SCALE, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL, UUID.fromString("ebcaed8f-0be9-4ab5-b430-0140b9a3f5a2"), "size");
		}
		
		public static AbilityInstance of(float amount) { return of(amount, AbilitySource.MISC); }
		
		public static AbilityInstance of(float amount, AbilitySource source)
		{
			return VTAbilities.SCALE.get().instance(source, nbt -> nbt.putFloat(AMOUNT, amount));
		}
		
		protected void generateAttributeModifiers()
		{
			addAttributeModifier(attribute.get(), inst -> 
				new EntityAttributeModifier(modifierID, modifierName, getScale(inst.memory()), operation));
		}
		
		protected static float getScale(NbtCompound memory)
		{
			return memory.contains(AMOUNT) ? MathHelper.clamp(memory.getFloat(AMOUNT), -0.95F, 1500F) : 0F;
		}
		
		public Text displayName(AbilityInstance instance) { return translate("ability", registryName().getPath()+(getScale(instance.memory()) >= 0 ? ".increase" : ".decrease")); }
		
		public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
		{
			float amount = getScale(instance.memory());
			return Optional.of(translate("ability", registryName().getPath()+(amount >= 0 ? ".increase" : ".decrease")+".desc", (int)(Math.abs(amount * 100))));
		}
	}
}
