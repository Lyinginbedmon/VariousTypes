package com.lying.utility;

import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.lying.VariousTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class Highlight
{
	protected static final String TYPE = "Type";
	protected final Type type;
	protected final long start;
	protected final int duration;
	
	protected Highlight(Type typeIn, long startIn, int durationIn)
	{
		type = typeIn;
		start = startIn;
		duration = durationIn;
	}
	
	public Type type() { return type; }
	
	public long start() { return start; }
	
	public int duration() { return duration; }
	
	public long expiry() { return start + duration; }
	
	public boolean hasExpired(long currentTime) { return currentTime > expiry(); }
	
	public abstract NbtElement toNbt();
	
	@Nullable
	public static Highlight readFromNbt(NbtCompound nbt)
	{
		switch(Type.fromString(nbt.getString(TYPE)))
		{
			case BLOCK:
				return Block.fromNbt(nbt);
			case ENTITY:
				return Entity.fromNbt(nbt);
			default:
				return null;
		}
	}
	
	public static enum Type implements StringIdentifiable
	{
		BLOCK,
		ENTITY;
		
		@SuppressWarnings("deprecation")
		public static final StringIdentifiable.EnumCodec<Type> CODEC = StringIdentifiable.createCodec(Type::values);
		
		public String asString() { return name().toLowerCase(); }
		
		@Nullable
		public static Type fromString(String nameIn)
		{
			for(Type type : values())
				if(type.asString().equalsIgnoreCase(nameIn))
					return type;
			return null;
		}
	}
	
	public static class Block extends Highlight
	{
		public static final Codec<Block> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Type.CODEC.fieldOf(TYPE).forGetter(v -> v.type),
				Codec.LONG.fieldOf("Start").forGetter(v -> v.start),
				Codec.INT.fieldOf("Duration").forGetter(v -> v.duration),
				BlockPos.CODEC.fieldOf("Pos").forGetter(v -> v.pos), 
				Codec.INT.fieldOf("Color").forGetter(v -> v.color))
					.apply(instance, Block::new));
		
		protected final BlockPos pos;
		protected final int color;
		
		public Block(Type typeIn, long startIn, int durationIn, BlockPos posIn, int colorIn)
		{
			this(posIn, startIn, durationIn, colorIn);
		}
		
		public Block(BlockPos posIn, long startIn, int durationIn, int colorIn)
		{
			super(Type.BLOCK, startIn, durationIn);
			pos = posIn;
			color = colorIn;
		}
		
		public BlockPos pos() { return pos; }
		
		public int color() { return color; }
		
		public float alpha(long currentTime)
		{
			float duration = MathHelper.clamp((float)(currentTime - start) / (float)this.duration, 0F, 1F);
			return duration < 0.1F ? (duration / 0.1F) : 1F - (float)Math.pow(duration, 7);
		}
		
		public NbtElement toNbt()
		{
			return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static Block fromNbt(NbtElement nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
	
	public static class Entity extends Highlight
	{
		public static final Codec<Entity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Type.CODEC.fieldOf(TYPE).forGetter(v -> v.type), 
				Codec.LONG.fieldOf("Start").forGetter(v -> v.start),
				Codec.INT.fieldOf("Duration").forGetter(v -> v.duration),
				Uuids.INT_STREAM_CODEC.fieldOf("UUID").forGetter(v -> v.uuid))
					.apply(instance, Entity::new));
		
		protected final UUID uuid;
		
		public Entity(Type typeIn, long startIn, int durationIn, UUID uuidIn)
		{
			this(uuidIn, startIn, durationIn);
		}
		
		public Entity(UUID uuidIn, long startIn, int durationIn)
		{
			super(Type.ENTITY, startIn, durationIn);
			uuid = uuidIn;
		}
		
		public UUID uuid() { return uuid; }
		
		public NbtElement toNbt()
		{
			return CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
		}
		
		public static Entity fromNbt(NbtElement nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
