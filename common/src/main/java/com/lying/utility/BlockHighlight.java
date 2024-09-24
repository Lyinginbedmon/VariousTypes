package com.lying.utility;

import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public record BlockHighlight(BlockPos pos, long start, int duration, int color)
{
	public static final Codec<BlockHighlight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			BlockPos.CODEC.fieldOf("Pos").forGetter(v -> v.pos), 
			Codec.LONG.fieldOf("Start").forGetter(v -> v.start),
			Codec.INT.fieldOf("Duration").forGetter(v -> v.duration),
			Codec.INT.fieldOf("Color").forGetter(v -> v.color))
				.apply(instance, BlockHighlight::new));
	
	public boolean hasExpired(long currentTime) { return currentTime > expiry(); }
	
	public float alpha(long currentTime)
	{
		float duration = MathHelper.clamp((float)(currentTime - start) / (float)this.duration, 0F, 1F);
		return duration < 0.1F ? (duration / 0.1F) : 1F - (float)Math.pow(duration, 7);
	}
	
	public long expiry() { return start + duration; }
	
	public NbtCompound toNbt(NbtCompound nbt)
	{
		return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static BlockHighlight fromNbt(NbtCompound nbt)
	{
		return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
}
