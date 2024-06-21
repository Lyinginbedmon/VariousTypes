package com.lying.type;

import java.util.Collection;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 * A hard-coded set of properties shared by all creatures of the same type.<br>
 * This serves as the constant on which later variation is built.
 */
public class Type
{
	private final Identifier registryName;
	private final Tier tier;
	private final AbilitySet defaultAbilities;
	private final Predicate<Type> compatibilityCheck;
	
	private Type(Identifier nameIn, Tier tierIn, AbilitySet abilitiesIn, Predicate<Type> compIn)
	{
		registryName = nameIn;
		tier = tierIn;
		defaultAbilities = abilitiesIn.copy();
		compatibilityCheck = compIn;
	}
	
	public final Identifier registryName() { return registryName; }
	
	public final Tier tier() { return this.tier; }
	
	public final Collection<AbilityInstance> abilities() { return this.defaultAbilities.abilities(); }
	
	public boolean compatibleWith(Type other) { return compatibilityCheck.apply(other); }
	
	public static enum Tier
	{
		SUPERTYPE,
		SUBTYPE;
	}
	
	public static class Builder
	{
		private Identifier name;
		private Tier tier;
		private final AbilitySet abilities = new AbilitySet();
		private Predicate<Type> compCheck = Predicates.alwaysTrue();
		
		public Builder(Identifier nameIn, Tier tierIn)
		{
			name = nameIn;
			tier = tierIn;
		}
		
		public Builder addAbility(Ability ability)
		{
			return addAbility(ability, Consumers.nop());
		}
		
		public Builder addAbility(Ability ability, Consumer<NbtCompound> modifier)
		{
			abilities.add(ability.instance(AbilitySource.TYPE, modifier).lock());
			return this;
		}
		
		public Builder compatibility(Predicate<Type> checkIn)
		{
			compCheck = checkIn;
			return this;
		}
		
		public Type build()
		{
			return new Type(name, tier, abilities, compCheck);
		}
	}
}