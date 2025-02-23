package com.lying.utility;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;
import com.lying.init.VTCosmetics;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.Identifier;

public class Cosmetic
{
	protected static final Codec<Cosmetic> REG_CODEC = Identifier.CODEC.comapFlatMap(id -> 
			{
				Optional<Cosmetic> cosmetic = VTCosmetics.get(id);
				if(cosmetic.isPresent())
					return DataResult.success(cosmetic.get());
				else
					return DataResult.error(() -> "Not a registered cosmetic: '"+String.valueOf(id) + "'");
			}, Cosmetic::registryName).stable();
	
	protected static final Codec<Cosmetic> FULL_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			REG_CODEC.fieldOf("ID").forGetter(cos -> VTCosmetics.get(cos.registryName()).get()),
			Codec.INT.optionalFieldOf("Color").forGetter(Cosmetic::color))
				.apply(instance, (reg, col) -> col.isPresent() ? reg.tint(col.get()) : reg));
	
	public static final PrimitiveCodec<Cosmetic> CODEC = new PrimitiveCodec<Cosmetic>() 
	{
		public <T> DataResult<Cosmetic> read(final DynamicOps<T> ops, final T input)
		{
			// First try to rebuild assuming a standard cosmetic ID
			try
			{
				DataResult<Cosmetic> std = REG_CODEC.parse(ops, input);
				if(std.isSuccess())
					return std;
				else
					throw new Exception();
			}
			catch(Exception e) { }
			
			// If that fails, attempt to build a tinted cosmetic
			try
			{
				DataResult<Cosmetic> dmy = FULL_CODEC.parse(ops, input);
				if(dmy.isSuccess())
					return DataResult.success(dmy.getOrThrow());
				else
					throw new Exception();
			}
			catch(Exception e) { }
			
			return DataResult.error(() -> "Unrecognised cosmetic");
		}
		
		public <T> T write(DynamicOps<T> ops, Cosmetic value)
		{
			if(value.tinted())
				return FULL_CODEC.encodeStart(ops, value).getOrThrow();
			else
				return REG_CODEC.encodeStart(ops, value).getOrThrow();
		}
	};
	
	private final Identifier registryName;
	private final Type type;
	private int colour = -1;
	
	protected Cosmetic(Identifier regName, Type typeIn, Optional<Integer> colourIn)
	{
		registryName = regName;
		type = typeIn;
		colourIn.ifPresent(c -> colour = c);
	}
	
	public Cosmetic(Identifier regName, Type typeIn)
	{
		this(regName, typeIn, Optional.empty());
	}
	
	public Cosmetic(Identifier regName, Type typeIn, int colourIn)
	{
		this(regName, typeIn);
		colour = colourIn;
	}
	
	public JsonElement toJson()
	{
		if(!tinted())
			return REG_CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();
		else
			return CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow();
	}
	
	@Nullable
	public static Cosmetic fromJson(JsonElement json)
	{
		if(json.isJsonPrimitive())
		{
			Identifier id = new Identifier(json.getAsString());
			Optional<Cosmetic> cosmetic = VTCosmetics.get(id);
			return cosmetic.isPresent() ? cosmetic.get() : null;
		}
		else
			return CODEC.parse(JsonOps.INSTANCE, json.getAsJsonObject()).getOrThrow();
	}
	
	public Identifier registryName() { return registryName; }
	
	public Type type() { return type; }
	
	public Cosmetic tint(int colour)
	{
		this.colour = colour;
		return this;
	}
	
	public boolean tinted() { return colour >= 0; }
	
	public Optional<Integer> color() { return tinted() ? Optional.of(colour) : Optional.empty(); }
	
	public static enum Type
	{
		EARS(1),
		TAIL(1),
		WINGS(1),
		HORNS(3),
		NOSE(1),
		MISC(-1);
		
		private final int capacity;
		
		private Type(int limitIn)
		{
			capacity = limitIn;
		}
		
		public int capacity() { return capacity; }
		
		public boolean uncapped() { return capacity < 1; }
	}
}