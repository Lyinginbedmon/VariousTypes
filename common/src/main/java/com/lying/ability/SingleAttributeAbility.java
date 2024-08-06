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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class SingleAttributeAbility extends Ability
{
	public static final String AMOUNT = "Amount";
	private final UUID modifierID;
	private final String modifierName;
	private final Supplier<RegistryEntry<EntityAttribute>> attribute;
	
	protected SingleAttributeAbility(Identifier regName, Category catIn, Supplier<RegistryEntry<EntityAttribute>> attributeIn, UUID uuidIn, String nameIn)
	{
		super(regName, catIn);
		this.attribute = attributeIn;
		this.modifierID = uuidIn;
		this.modifierName = nameIn;
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		return Optional.of(translate("ability", registryName().getPath()+".desc", getAmount(instance.memory())));
	}
	
	protected void generateAttributeModifiers()
	{
		addAttributeModifier(attribute.get(), inst -> 
			new EntityAttributeModifier(modifierID, modifierName, getAmount(inst.memory()), EntityAttributeModifier.Operation.ADD_VALUE));
	}
	
	protected static int getAmount(NbtCompound memory)
	{
		return memory.contains(AMOUNT) ? memory.getInt(AMOUNT) : 3;
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
		
		public Optional<Text> description(AbilityInstance instance)
		{
			int amount = getAmount(instance.memory());
			return Optional.of(translate("ability", registryName().getPath()+(amount >= 0 ? ".bonus" : ".penalty")+".desc", amount));
		}
	}
}
