package com.lying.ability;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
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
	/** Used for storing the absolute bare minimum operational information only */
	public static final Codec<AbilityInstance> CODEC_VITALS = RecordCodecBuilder.create(instance -> instance.group(
			Ability.CODEC.fieldOf("Ability").forGetter(AbilityInstance::ability),
			Codec.INT.optionalFieldOf("Cooldown").forGetter(AbilityInstance::cooldownMaybe),
			NbtCompound.CODEC.optionalFieldOf("Memory").forGetter(AbilityInstance::memoryMaybe))
				.apply(instance, (a,b,c) -> 
				{
					AbilityInstance inst = a.instance();
					b.ifPresent(val -> inst.cooldown = Optional.of(Math.abs(val)));
					c.ifPresent(mem -> inst.memory = mem);
					return inst;
				}));
	/** Used for storing absolutely everything about an AbilityInstance */
	public static final Codec<AbilityInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ability.CODEC.fieldOf("Ability").forGetter(AbilityInstance::ability),
			AbilitySource.CODEC.fieldOf("Source").forGetter(AbilityInstance::source),
			Codec.INT.optionalFieldOf("Cooldown").forGetter(AbilityInstance::cooldownMaybe),
			Codec.BOOL.optionalFieldOf("ReadOnly").forGetter(AbilityInstance::lockedMaybe),
			LoreDisplay.CODEC.optionalFieldOf("Display").forGetter(AbilityInstance::display),
			NbtCompound.CODEC.optionalFieldOf("Memory").forGetter(AbilityInstance::memoryMaybe))
				.apply(instance, AbilityInstance::new));
	
	/** Comparator for sorting abilities alphabetically by their display name */
	public static final Comparator<AbilityInstance> SORT_FUNC = (a,b) -> {
		int comparison;
		return (comparison = AbilityType.compare(a.ability().type(), b.ability().type())) == 0 ? VTUtils.stringComparator(a.displayName().getString(), b.displayName().getString()) : comparison;
		};
	
	private final Ability ability;
	private final AbilitySource source;
	private boolean locked = false;
	private Optional<Integer> cooldown = Optional.empty();
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
	
	protected AbilityInstance(Ability ability, AbilitySource source, Optional<Integer> cool, Optional<Boolean> isLocked, Optional<LoreDisplay> display, Optional<NbtCompound> memory)
	{
		this(ability,source);
		cool.ifPresent(val -> cooldown = Optional.of(Math.abs(val)));
		if(isLocked.isPresent() && isLocked.get())
			lock();
		display.ifPresent(dis -> setDisplay(dis));
		memory.ifPresent(mem -> setMemory(mem));
	}
	
	/** The variable map name for this specific ability instance */
	public Identifier mapName() { return ability.mapName(this); }
	
	public AbilitySource source() { return source; }
	
	public void setDisplay(LoreDisplay displayIn) { display = Optional.of(displayIn); }
	
	public Optional<LoreDisplay> display() { return display; }
	
	public void setCooldown(int cooldown) { this.cooldown = Optional.of(cooldown); }
	
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
	
	protected Optional<Integer> cooldownMaybe() { return cooldown; }
	
	public int cooldown()
	{
		if(ability.type() != AbilityType.PASSIVE)
			return cooldown.isPresent() ? cooldown.get() : ((ActivatedAbility)ability).cooldownDefault();
		return 0;
	}
	
	public NbtElement writeToNbt(NbtCompound compound)
	{
		return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	@Nullable
	public static AbilityInstance readFromNbt(NbtCompound data)
	{
		return CODEC.parse(NbtOps.INSTANCE, data).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	public JsonElement writeToJson(RegistryWrapper.WrapperLookup manager, boolean vitalOnly)
	{
		if(vitalOnly && memoryMaybe().isEmpty() && cooldownMaybe().isEmpty())
			return Ability.CODEC.encodeStart(JsonOps.INSTANCE, ability).getOrThrow();
		
		RegistryOps<JsonElement> registryOps = manager.getOps(JsonOps.INSTANCE);
		Codec<AbilityInstance> codec = vitalOnly ? CODEC_VITALS : CODEC;
		return codec.encodeStart(registryOps, this).getOrThrow();
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
		AbilityNbt.fromInstance(this).applyTo(clone);
		return clone;
	}
	
	/** Helper class used by commands to permit changing ability instance customisable values in the same NBT compound */
	public static class AbilityNbt
	{
		public static final Codec<AbilityNbt> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				LoreDisplay.CODEC.optionalFieldOf("Display").forGetter(AbilityNbt::display),
				Codec.INT.optionalFieldOf("Cooldown").forGetter(AbilityNbt::cooldownMaybe),
				NbtCompound.CODEC.optionalFieldOf("Memory").forGetter(AbilityNbt::memoryMaybe))
					.apply(instance, AbilityNbt::new));
		
		private final Optional<LoreDisplay> display;
		private final Optional<Integer> cooldown;
		private final Optional<NbtCompound> memory;
		
		public AbilityNbt(Optional<LoreDisplay> displayIn, Optional<Integer> coolIn, Optional<NbtCompound> memoryIn)
		{
			display = displayIn;
			cooldown = coolIn;
			memory = memoryIn;
		}
		
		public Optional<LoreDisplay> display() { return display; }
		public Optional<Integer> cooldownMaybe() { return cooldown; }
		public Optional<NbtCompound> memoryMaybe() { return memory; }
		
		public void applyTo(AbilityInstance inst)
		{
			display.ifPresent(dis -> inst.setDisplay(dis));
			cooldown.ifPresent(cool -> inst.setCooldown(cool));
			memory.ifPresent(mem -> inst.setMemory(inst.memory.copyFrom(mem)));
		}
		
		@Nullable
		public static AbilityNbt readFromNbt(NbtCompound data)
		{
			return CODEC.parse(NbtOps.INSTANCE, data).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
		
		public static AbilityNbt fromInstance(AbilityInstance inst)
		{
			return new AbilityNbt(inst.display(), inst.cooldownMaybe(), inst.memoryMaybe());
		}
	}
}