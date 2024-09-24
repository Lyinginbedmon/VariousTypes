package com.lying.utility;

import java.util.UUID;

import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Uuids;

public record EntityHighlight(UUID uuid, long start, int duration)
{
	public static final Codec<EntityHighlight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Uuids.INT_STREAM_CODEC.fieldOf("Pos").forGetter(v -> v.uuid), 
			Codec.LONG.fieldOf("Start").forGetter(v -> v.start),
			Codec.INT.fieldOf("Duration").forGetter(v -> v.duration))
				.apply(instance, EntityHighlight::new));
	
	public boolean hasExpired(long currentTime) { return currentTime > expiry(); }
	
	public long expiry() { return start + duration; }
	
	public NbtCompound toNbt(NbtCompound nbt)
	{
		return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static EntityHighlight fromNbt(NbtCompound nbt)
	{
		return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
}
