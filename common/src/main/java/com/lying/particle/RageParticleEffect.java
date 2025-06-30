package com.lying.particle;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.lying.init.VTParticleTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Uuids;

// FIXME Ensure particle is generated properly on CLIENT
public class RageParticleEffect implements ParticleEffect
{
	public static final MapCodec<RageParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Uuids.CODEC.optionalFieldOf("parent").forGetter(RageParticleEffect::getParentUuid)
			).apply(instance, RageParticleEffect::new));
	public static final PacketCodec<RegistryByteBuf, RageParticleEffect> PACKET_CODEC = PacketCodec.tuple(
			Uuids.PACKET_CODEC.collect(PacketCodecs::optional), RageParticleEffect::getParentUuid, RageParticleEffect::new);
	
	public Optional<UUID> playerID;
	
	public RageParticleEffect(@Nullable UUID parentId)
	{
		this(parentId == null ? Optional.empty() : Optional.of(parentId));
	}
	
	public RageParticleEffect(Optional<UUID> parentId)
	{
		playerID = parentId;
	}
	
	public ParticleType<?> getType() { return VTParticleTypes.RAGE.get(); }
	
	public Optional<UUID> getParentUuid() { return playerID; }
}
