package com.lying.network;

import java.util.UUID;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class ParentedParticlePacket
{
	private static final Identifier PACKET_ID = VTPacketHandler.PARENTED_PARTICLE_ID;
	public static final CustomPayload.Id<Payload> PACKET_TYPE = new CustomPayload.Id<>(PACKET_ID);
	public static final PacketCodec<RegistryByteBuf, Payload> PACKET_CODEC = CustomPayload.codecOf(Payload::write, Payload::new);
	
	public static <T extends ParticleEffect> void send(
			ServerPlayerEntity player, 
			UUID parentID, 
			T params, 
			double x, double y, double z, 
			double velX, double velY, double velZ)
	{
		NetworkManager.sendToPlayer(player, new Payload(parentID, x, y, z, velX, velY, velZ, params));
	}
	
	public static <T extends ParticleEffect> void send(
			ServerPlayerEntity player, 
			UUID parentID, 
			T params, 
			Vec3d pos, 
			Vec3d vel)
	{
		send(player, parentID, params, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
	}
	
	public static record Payload(
			UUID uuid, 
			double x, double y, double z, 
			double velX, double velY, double velZ, 
			ParticleEffect params) implements CustomPayload
	{
		public Payload(RegistryByteBuf buffer)
		{
			this(
					buffer.readUuid(),
					buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
					buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
					ParticleTypes.PACKET_CODEC.decode(buffer));
		}
		
		public void write(RegistryByteBuf buffer)
		{
			buffer.writeUuid(uuid);
			buffer.writeDouble(x);
			buffer.writeDouble(y);
			buffer.writeDouble(z);
			buffer.writeDouble(velX);
			buffer.writeDouble(velY);
			buffer.writeDouble(velZ);
			ParticleTypes.PACKET_CODEC.encode(buffer, params);
		}
		
		public Id<? extends CustomPayload> getId() { return PACKET_TYPE; }
	}
}
