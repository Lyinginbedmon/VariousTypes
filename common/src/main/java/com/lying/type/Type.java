package com.lying.type;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;
import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.gson.JsonElement;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;
import com.lying.utility.LoreDisplay;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A hard-coded set of properties shared by all creatures of the same type.<br>
 * This serves as the constant on which later variation is built.
 */
public class Type
{
	// Main codec used by standard types, which only need to store their registry name
	protected static final Codec<Type> CODEC_ID = Identifier.CODEC.comapFlatMap(id -> 
			{
				Type type = VTTypes.get(id);
				if(type != null)
					return DataResult.success(type);
				else
					return DataResult.error(() -> "Not a recognised type: '"+String.valueOf(id) + "'");
			}, Type::registryName);
	
	// Bulkier codec used by dummy types, which need to store custom data
	protected static final Codec<DummyType> CODEC_OBJ = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("ID").forGetter(DummyType::listID), 
			LoreDisplay.CODEC.optionalFieldOf("Display").forGetter(DummyType::display))
				.apply(instance, DummyType::create));
	
	public static final PrimitiveCodec<Type> CODEC = new PrimitiveCodec<Type>() 
	{
		public <T> DataResult<Type> read(final DynamicOps<T> ops, final T input)
		{
			// First try to rebuild assuming a standard type ID
			try
			{
				DataResult<Type> std = CODEC_ID.parse(ops, input);
				if(std.isSuccess() && !std.getOrThrow().registryName().equals(VTTypes.DUMMY_ID))
					return std;
				else
					throw new Exception();
			}
			catch(Exception e) { }
			
			// If that fails, attempt to build a dummy type
			try
			{
				DataResult<DummyType> dmy = CODEC_OBJ.parse(ops, input);
				if(dmy.isSuccess())
					return DataResult.success((Type)dmy.getOrThrow());
				else
					throw new Exception();
			}
			catch(Exception e) { }
			
			return DataResult.error(() -> "Unrecognised type");
		}
		
		public <T> T write(DynamicOps<T> ops, Type value)
		{
			if(value instanceof DummyType)
				return CODEC_OBJ.encodeStart(ops, (DummyType)value).getOrThrow();
			else
				return CODEC_ID.encodeStart(ops, value).getOrThrow();
		}
	};
	
	/** Comparator for sorting types alphabetically by their display name */
	public static final Comparator<Type> SORT_FUNC = (a, b) -> 
		a.tier() == b.tier() ? 
			VTUtils.stringComparator(a.displayName().getString(), b.displayName().getString()) : 
			(int)Math.signum(a.tier().ordinal() - b.tier().ordinal());
	public static final int DEFAULT_COLOR = 0x236EF5;
	
	protected final Identifier registryName;
	protected final Tier tier;
	protected final int colour;
	protected final LoreDisplay display;
	
	protected final ActionHandler actions;
	protected final AbilitySet abilities;
	protected final Predicate<Type> compatibilityCheck;
	
	protected Type(Identifier nameIn, AbilitySet abilitiesIn, ActionHandler actionsIn, Predicate<Type> compIn, LoreDisplay displayIn)
	{
		this(nameIn, Tier.SUBTYPE, DEFAULT_COLOR, abilitiesIn, actionsIn, compIn, displayIn);
	}
	
	protected Type(Identifier nameIn, Tier tierIn, int colourIn, AbilitySet abilitiesIn, ActionHandler actionsIn, Predicate<Type> compIn, LoreDisplay displayIn)
	{
		registryName = nameIn;
		tier = tierIn;
		colour = colourIn;
		display = displayIn;
		actions = actionsIn.copy();
		abilities = abilitiesIn.copy();
		compatibilityCheck = compIn;
	}
	
	public final Identifier registryName() { return registryName; }
	
	/** The name that is checked in most cases of checking for specific types */
	public Identifier listID() { return registryName(); }
	
	public final Tier tier() { return this.tier; }
	
	public final int color() { return this.colour; }
	
	public Text displayName() { return display.title(); }
	
	public Optional<Text> description() { return display.description(); }
	
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
	
	public final NbtCompound writeToNbt(NbtCompound data)
	{
		data.putString("Type", registryName.toString());
		write(data);
		return data;
	}
	
	protected void write(NbtCompound data) { }
	
	public void read(NbtCompound data) { }
	
	public JsonElement writeToJson()
	{
		return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();
	}
	
	@Nullable
	public static Type readFromJson(JsonElement entry)
	{
		return CODEC.parse(JsonOps.INSTANCE, entry).getOrThrow();
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
		
		protected Text title;
		protected Optional<Text> description = Optional.empty();
		
		protected Builder(Identifier nameIn, Tier tierIn)
		{
			name = nameIn;
			tier = tierIn;
			title = Text.translatable("type."+nameIn.getNamespace()+"."+nameIn.getPath());
			switch(tier)
			{
				case SUBTYPE:
					actions = ActionHandler.of();
					break;
				case SUPERTYPE:
				default:
					actions = ActionHandler.STANDARD_SET.copy();
					description = Optional.of(Text.translatable("type."+nameIn.getNamespace()+"."+nameIn.getPath()+".desc"));
					break;
			}
		}
		
		public static Builder of(Identifier nameIn, Tier tierIn)
		{
			return new Builder(nameIn, tierIn);
		}
		
		public Builder colour(int colorIn)
		{
			colour = colorIn;
			return this;
		}
		
		public Builder description(Text desc)
		{
			description = desc == null ? Optional.empty() : Optional.of(desc);
			return this;
		}
		
		public Builder setActions(ActionHandler handler)
		{
			actions = handler.copy();
			return this;
		}
		
		public Builder addAbility(Ability... ability)
		{
			for(Ability inst : ability)
				addAbility(inst, Consumers.nop());
			return this;
		}
		
		public Builder addAbility(Ability ability, Consumer<NbtCompound> modifier)
		{
			abilities.add(ability.instance(AbilitySource.TYPE, modifier).lock());
			return this;
		}
		
		public Builder addAbility(AbilityInstance inst)
		{
			AbilityInstance conformed = inst.ability().instance(AbilitySource.TYPE);
			conformed.copyDetails(inst);
			abilities.add(conformed.lock());
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
					return new Type(name, abilities, actions, compCheck, new LoreDisplay(title, description));
				default:
				case SUPERTYPE:
					return new Type(name, tier, colour, abilities, actions, compCheck, new LoreDisplay(title, description));
			}
		}
	}
}