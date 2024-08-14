package com.lying.component.element;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;

public class Cooldown
{
	protected static final Codec<Cooldown> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.LONG.optionalFieldOf("Start").forGetter(Cooldown::startMaybe), 
			Codec.INT.optionalFieldOf("Duration").forGetter(Cooldown::durationMaybe),
			Codec.BOOL.optionalFieldOf("Indefinite").forGetter(Cooldown::indefiniteMaybe))
				.apply(instance, (a,b,c) -> new Cooldown((Optional<Long>)a, (Optional<Integer>)b, (Optional<Boolean>)c)));
	
	private final long start, end;
	private final int duration;
	private final boolean indefinite;
	
	private Cooldown(Optional<Long> startIn, Optional<Integer> lengthIn, Optional<Boolean> indefIn)
	{
		indefinite = indefIn.isPresent() && indefIn.get();
		if(indefinite)
		{
			start = 0;
			duration = 0;
		}
		else
		{
			start = startIn.orElse(0L);
			duration = lengthIn.orElse(1);
		}
		end = start + duration;
	}
	
	/** Returns an indefinite cooldown */
	public Cooldown()
	{
		this(Optional.empty(), Optional.empty(), Optional.of(true));
	}
	
	/** Returns a limited-duration cooldown */
	public Cooldown(long startTime, int length)
	{
		this(Optional.of(startTime), Optional.of(length), Optional.of(false));
	}
	
	private Optional<Long> startMaybe(){ return Optional.of(start); }
	private Optional<Integer> durationMaybe(){ return Optional.of(duration); }
	private Optional<Boolean> indefiniteMaybe(){ return Optional.of(indefinite); }
	
	public boolean hasElapsed(long time) { return !indefinite && time > end; }
	
	public float progress(long time)
	{
		return Math.clamp((time - start) / (float)duration, 0F, 1F);
	}
	
	public boolean isIndefinite() { return indefinite; }
	
	public NbtCompound toNbt()
	{
		return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	@Nullable
	public static Cooldown fromNbt(NbtCompound nbt)
	{
		return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
}