package com.lying.ability;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.Ability.AbilityType;
import com.lying.init.VTAbilities;
import com.lying.utility.LoreDisplay;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** A unique instance of a given ability */
public class AbilityInstance
{
	public static final Codec<AbilityInstance> CODEC_VITALS = RecordCodecBuilder.create(instance -> instance.group(
			Ability.CODEC.fieldOf("Ability").forGetter(AbilityInstance::ability),
			NbtCompound.CODEC.optionalFieldOf("Memory").forGetter(AbilityInstance::memoryMaybe))
				.apply(instance, (a,b) -> 
				{
					AbilityInstance inst = a.instance();
					b.ifPresent(mem -> inst.setMemory(mem));
					return inst;
				}));
	public static final Codec<AbilityInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ability.CODEC.fieldOf("Ability").forGetter(AbilityInstance::ability),
			AbilitySource.CODEC.fieldOf("Source").forGetter(AbilityInstance::source),
			Codec.BOOL.optionalFieldOf("ReadOnly").forGetter(AbilityInstance::lockedMaybe),
			LoreDisplay.CODEC.optionalFieldOf("Display").forGetter(AbilityInstance::display),
			NbtCompound.CODEC.optionalFieldOf("Memory").forGetter(AbilityInstance::memoryMaybe))
				.apply(instance, AbilityInstance::new));
	private final Ability ability;
	private final AbilitySource source;
	private boolean locked = false;
	private Optional<LoreDisplay> display = Optional.empty();
	private NbtCompound memory;
	
	public AbilityInstance(Ability abilityIn, AbilitySource sourceIn)
	{
		ability = abilityIn;
		source = sourceIn;
		memory = ability.initialiseNBT(new NbtCompound());
	}
	
	public AbilityInstance(Ability abilityIn, AbilitySource sourceIn, Consumer<NbtCompound> dataModifier)
	{
		this(abilityIn, sourceIn);
		dataModifier.accept(memory);
	}
	
	protected AbilityInstance(Ability ability, AbilitySource source, Optional<Boolean> isLocked, Optional<LoreDisplay> display, Optional<NbtCompound> memory)
	{
		this(ability,source);
		if(isLocked.isPresent() && isLocked.get())
			lock();
		display.ifPresent(dis -> setDisplay(dis));
		memory.ifPresent(mem -> setMemory(mem));
	}
	
	/** Returns a comparator for sorting abilities alphabetically by their display name */
	public static Comparator<AbilityInstance> sortFunc()
	{
		return (a, b) -> {
			int comparison;
			return (comparison = AbilityType.compare(a.ability().type(), b.ability().type())) == 0 ? VTUtils.stringComparator(a.displayName().getString(), b.displayName().getString()) : comparison;
			};
	}
	
	/** The variable map name for this specific ability instance */
	public Identifier mapName() { return ability.mapName(this); }
	
	public AbilitySource source() { return source; }
	
	public void setDisplay(LoreDisplay displayIn) { display = Optional.of(displayIn); }
	
	public Optional<LoreDisplay> display() { return display; }
	
	public void setDisplayName(Text component)
	{
		display = Optional.of(new LoreDisplay(component, display.isPresent() ? display.get().description() : Optional.empty()));
	}
	
	public Text displayName()
	{
		if(display.isPresent())
			return display.get().title();
		return ability.displayName(this);
	}
	
	public Optional<Text> description()
	{
		if(display.isPresent() && display.get().description().isPresent())
			return display.get().description();
		return ability.description(this);
	}
	
	public NbtElement writeToNbt(NbtCompound compound)
	{
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager, boolean vitalOnly)
	{
		if(vitalOnly && memoryMaybe().isEmpty())
			return new JsonPrimitive(ability.registryName().toString());
		
		RegistryOps<JsonElement> registryOps = manager.getOps(JsonOps.INSTANCE);
		Codec<AbilityInstance> codec = vitalOnly ? CODEC_VITALS : CODEC;
		return codec.encodeStart(registryOps, this).getOrThrow();
	}
	
	@Nullable
	public static AbilityInstance readFromNbt(NbtCompound data)
	{
		return CODEC.parse(NbtOps.INSTANCE, data).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	public static AbilityInstance readFromJson(JsonElement element, AbilitySource forceSource)
	{
		if(element.isJsonPrimitive())
		{
			Ability ability = VTAbilities.get(new Identifier(element.getAsString()));
			AbilitySource source = forceSource == null ? AbilitySource.MISC : forceSource;
			return ability.instance(source);
		}
		else if(element.isJsonObject())
		{
			AbilityInstance inst = CODEC.parse(JsonOps.INSTANCE, element.getAsJsonObject()).getOrThrow();
			if(forceSource != null && forceSource != inst.source())
			{
				AbilityInstance inst2 = inst.ability().instance(forceSource);
				inst.memoryMaybe().ifPresent(mem -> inst2.setMemory(mem));
				inst.lockedMaybe().ifPresent(lock -> inst2.lock());
				inst.display().ifPresent(dis -> inst2.setDisplay(dis));
				return inst2;
			}
			else
				return inst;
		}
		else
			return VTAbilities.DUMMY.get().instance();
	}
	
	/**
	 * Returns true if this ability instance is immutable.<br>
	 * This usually indicates that it is a default model not to be directly utilised.
	 */
	public boolean isReadOnly() { return locked; }
	
	protected Optional<Boolean> lockedMaybe() { return locked ? Optional.of(locked) : Optional.empty(); }
	
	public AbilityInstance lock()
	{
		locked = true;
		return this;
	}
	
	public Ability ability() { return ability; }
	
	public NbtCompound memory() { return memory; }
	
	protected Optional<NbtCompound> memoryMaybe()
	{
		return memory.isEmpty() ? Optional.empty() : Optional.of(memory);
	}
	
	public void setMemory(NbtCompound dataIn)
	{
		if(isReadOnly()) return;
		memory = dataIn;
	}
	
	/** Returns a modifiable duplicate of this instance */
	public final AbilityInstance copy()
	{
		AbilityInstance clone = new AbilityInstance(ability, source);
		clone.setMemory(memory());
		return clone;
	}
}