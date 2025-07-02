package com.lying.emission;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.entity.EmitterEntity;
import com.lying.init.VTEmissions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public abstract class Emission
{
	private static final Codec<Emission> ID_CODEC = Identifier.CODEC.comapFlatMap(id -> 
		{
			Emission emission = VTEmissions.get(id);
			if(emission != null)
				return DataResult.success(emission);
			else
				return DataResult.error(() -> "Not a registered emission: '"+String.valueOf(id) + "'");
		}, Emission::registryName).stable();
	
	public static final Codec<Emission> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ID_CODEC.fieldOf("type").forGetter(e -> e),
			NbtCompound.CODEC.optionalFieldOf("data").forGetter(Emission::write))
				.apply(instance, (e,n) -> e.readFromNbt(n.orElse(new NbtCompound()))));
	
	private final Identifier regName;
	
	protected Emission(Identifier regNameIn)
	{
		this.regName = regNameIn;
	}
	
	public Identifier registryName() { return this.regName; }
	
	public abstract boolean shouldTick(int age);
	
	/** Called server-side by the emitter entity */
	public abstract void tick(Vec3d pos, ServerWorld world, int age, EmitterEntity emitter);
	
	public final NbtCompound toNbt()
	{
		return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow();
	}
	
	public static Emission fromNbt(NbtCompound nbt)
	{
		return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
	}
	
	public final Optional<NbtCompound> write()
	{
		NbtCompound nbt = writeToNbt(new NbtCompound());
		return nbt.isEmpty() ? Optional.empty() : Optional.of(nbt);
	}
	
	protected Emission readFromNbt(NbtCompound nbt)
	{
		return this;
	}
	
	protected NbtCompound writeToNbt(NbtCompound nbt)
	{
		return nbt;
	}
}
