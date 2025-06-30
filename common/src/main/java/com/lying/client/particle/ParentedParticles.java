package com.lying.client.particle;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lying.client.VariousTypesClient;
import com.lying.mixin.AccessorParticleManager;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.particle.ParticleEffect;

public class ParentedParticles
{
	protected final Map<UUID, ParticleSet> uuidToParticlesMap = new HashMap<>();
	private long lastTick = 0L;
	
	public ParentedParticles.ParticleSet getParentedParticles(UUID uuid)
	{
		return uuidToParticlesMap.computeIfAbsent(uuid, ParticleSet::new);
	}
	
	public void clear() { uuidToParticlesMap.clear(); }
	
	public void tick(long time)
	{
		if(time <= lastTick) return;
		List<ParticleSet> dirty = Lists.newArrayList();
		for(ParticleSet set : uuidToParticlesMap.values())
			if(set.tick().isDirty())
				dirty.add(set);
		
		dirty.forEach(set -> uuidToParticlesMap.put(set.parentID, set.clean()));
	}
	
	public void addParticle(UUID uuid, @Nullable Particle particle)
	{
		if(particle == null) return;
		ParticleSet set = getParentedParticles(uuid).add(particle);
		if(set.isDirty())
			uuidToParticlesMap.put(uuid, set.clean());
	}
	
	public void addParticle(UUID uuid, ParticleEffect params, double x, double y, double z, double velX, double velY, double velZ)
	{
		addParticle(uuid, ((AccessorParticleManager)VariousTypesClient.mc.particleManager).makeParticle(params, x, y, z, velX, velY, velZ));
	}
	
	public static class ParticleSet
	{
		private final UUID parentID;
		private final Map<ParticleTextureSheet, List<Particle>> particles = Maps.newIdentityHashMap();
		private boolean dirty = false;
		
		public ParticleSet(UUID parent)
		{
			parentID = parent;
		}
		
		public ParticleSet add(Particle particle)
		{
			particles.computeIfAbsent(particle.getType(), sheet -> Lists.newArrayList()).add(particle);
			markDirty();
			return this;
		}
		
		public ParticleSet tick()
		{
			particles.values().forEach(l -> l.forEach(Particle::tick));
			particles.forEach((sheet,list) -> list.removeIf(p -> !p.isAlive()));
			markDirty();
			return this;
		}
		
		public void markDirty()
		{
			dirty = true;
		}
		
		/** Marks this set as needing to be updated in the storage map */
		public boolean isDirty() { return dirty; }
		
		/** Resets the dirty flag */
		public ParticleSet clean()
		{
			dirty = false;
			return this;
		}
		
		public Collection<Particle> getAll()
		{
			List<Particle> p = Lists.newArrayList();
			particles.values().forEach(l -> p.addAll(l));
			return p;
		}
		
		public List<Particle> get(ParticleTextureSheet sheet)
		{
			return particles.getOrDefault(sheet, Lists.newArrayList());
		}
	}
}