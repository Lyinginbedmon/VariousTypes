package com.lying.type;

import java.util.Collection;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A hard-coded set of properties shared by all creatures of the same type.<br>
 * This serves as the constant on which later variation is built.
 */
public class Type
{
	protected final Identifier registryName;
	protected final Tier tier;
	protected final ActionHandler actions;
	protected final AbilitySet abilities;
	protected final Predicate<Type> compatibilityCheck;
	
	protected Type(Identifier nameIn, Tier tierIn, AbilitySet abilitiesIn, ActionHandler actionsIn, Predicate<Type> compIn)
	{
		registryName = nameIn;
		tier = tierIn;
		actions = actionsIn.copy();
		abilities = abilitiesIn.copy();
		compatibilityCheck = compIn;
	}
	
	public final Identifier registryName() { return registryName; }
	
	/** The name that is checked in most cases of checking for specific types */
	public Identifier listID() { return registryName(); }
	
	public final Tier tier() { return this.tier; }
	
	public Text displayName(DynamicRegistryManager manager) { return Text.translatable("type."+registryName.getNamespace()+"."+registryName.getPath()); }
	
	public final Collection<AbilityInstance> abilities() { return abilities.abilities(); }
	
	@Nullable
	public final ActionHandler actions() { return actions.copy(); }
	
	public boolean compatibleWith(Type other) { return compatibilityCheck.apply(other); }
	
	public final NbtCompound writeToNbt(NbtCompound data, DynamicRegistryManager manager)
	{
		data.putString("Type", registryName.toString());
		write(data, manager);
		return data;
	}
	
	protected void write(NbtCompound data, DynamicRegistryManager manager) { }
	
	public void read(NbtCompound data) { }
	
	public static enum Tier
	{
		SUPERTYPE,
		SUBTYPE;
	}
	
	public static class Builder
	{
		protected final Identifier name;
		protected final Tier tier;
		protected final AbilitySet abilities = new AbilitySet();
		protected ActionHandler actions = ActionHandler.STANDARD_SET.copy();
		protected Predicate<Type> compCheck = Predicates.alwaysTrue();
		
		protected Builder(Identifier nameIn, Tier tierIn)
		{
			name = nameIn;
			tier = tierIn;
			switch(tier)
			{
				case SUBTYPE:
					actions = ActionHandler.of();
					break;
				case SUPERTYPE:
				default:
					actions = ActionHandler.STANDARD_SET.copy();
					break;
			}
		}
		
		public static Builder of(Identifier nameIn, Tier tierIn)
		{
			return new Builder(nameIn, tierIn);
		}
		
		public Builder setActions(ActionHandler handler)
		{
			actions = handler.copy();
			return this;
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
			return new Type(name, tier, abilities, actions, compCheck);
		}
	}
}