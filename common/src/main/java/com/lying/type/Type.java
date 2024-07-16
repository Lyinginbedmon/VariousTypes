package com.lying.type;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.utility.VTUtils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A hard-coded set of properties shared by all creatures of the same type.<br>
 * This serves as the constant on which later variation is built.
 */
public class Type
{
	public static final int DEFAULT_COLOR = 0x236EF5;
	
	protected final Identifier registryName;
	protected final Tier tier;
	protected final int colour;
	protected final ActionHandler actions;
	protected final AbilitySet abilities;
	protected final Predicate<Type> compatibilityCheck;
	
	protected Type(Identifier nameIn, AbilitySet abilitiesIn, ActionHandler actionsIn, Predicate<Type> compIn)
	{
		this(nameIn, Tier.SUBTYPE, DEFAULT_COLOR, abilitiesIn, actionsIn, compIn);
	}
	
	protected Type(Identifier nameIn, Tier tierIn, int colourIn, AbilitySet abilitiesIn, ActionHandler actionsIn, Predicate<Type> compIn)
	{
		registryName = nameIn;
		tier = tierIn;
		colour = colourIn;
		actions = actionsIn.copy();
		abilities = abilitiesIn.copy();
		compatibilityCheck = compIn;
	}
	
	/** Returns a comparator for sorting types alphabetically by their display name */
	public static Comparator<Type> sortFunc(DynamicRegistryManager manager)
	{
		return (a, b) -> 
			a.tier() == b.tier() ? 
				VTUtils.stringComparator(a.displayName(manager).getString(), b.displayName(manager).getString()) : 
				(int)Math.signum(a.tier().ordinal() - b.tier().ordinal());
	}
	
	public final Identifier registryName() { return registryName; }
	
	/** The name that is checked in most cases of checking for specific types */
	public Identifier listID() { return registryName(); }
	
	public final Tier tier() { return this.tier; }
	
	public final int color() { return this.colour; }
	
	public Text displayName(DynamicRegistryManager manager) { return Text.translatable("type."+registryName.getNamespace()+"."+registryName.getPath()); }
	
	/**
	 * Returns a collection ability instances compiled both from this type itself and from the action handler.
	 */
	public final Collection<AbilityInstance> abilities()
	{
		AbilitySet fullMap = abilities.copy();
		this.actions.abilities().forEach(ability -> fullMap.add(ability));
		return fullMap.abilities();
	}
	
	@Nullable
	public final ActionHandler actions() { return actions.copy(); }
	
	public boolean compatibleWith(Type other) { return compatibilityCheck.apply(other); }
	
	public final NbtCompound writeToNbt(NbtCompound data, WrapperLookup manager)
	{
		data.putString("Type", registryName.toString());
		write(data, manager);
		return data;
	}
	
	protected void write(NbtCompound data, WrapperLookup manager) { }
	
	public void read(NbtCompound data) { }
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager)
	{
		return new JsonPrimitive(registryName().toString());
	}
	
	@Nullable
	public static Type readFromJson(JsonElement entry)
	{
		// Only Dummy types are stored as JsonObjects, due to their memory requirements
		if(entry.isJsonObject())
			return DummyType.fromJson(entry.getAsJsonObject());
		else if(entry.isJsonPrimitive())
			return VTTypes.get(new Identifier(entry.getAsString()));
		return null;
	}
	
	public static enum Tier
	{
		SUPERTYPE,
		SUBTYPE;
	}
	
	public static class Builder
	{
		protected final Identifier name;
		protected final Tier tier;
		protected int colour = DEFAULT_COLOR;
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
		
		public Builder display(int colorIn)
		{
			colour = colorIn;
			return this;
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
			switch(tier)
			{
				case SUBTYPE:
					return new Type(name, abilities, actions, compCheck);
				default:
				case SUPERTYPE:
					return new Type(name, tier, colour, abilities, actions, compCheck);
			}
		}
	}
}