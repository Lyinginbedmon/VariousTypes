package com.lying.client.renderer;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.lying.client.VariousTypesClient;
import com.lying.client.particle.ParentedParticles.ParticleSet;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class ParentedParticlesFeatureRenderer<T extends Entity, M extends EntityModel<T>> extends FeatureRenderer<T, M>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final List<ParticleTextureSheet> PARTICLE_TEXTURE_SHEETS = ImmutableList.of(
			ParticleTextureSheet.TERRAIN_SHEET, 
			ParticleTextureSheet.PARTICLE_SHEET_OPAQUE, 
			ParticleTextureSheet.PARTICLE_SHEET_LIT, 
			ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT, 
			ParticleTextureSheet.CUSTOM);
	
	public ParentedParticlesFeatureRenderer(FeatureRendererContext<T, M> context)
	{
		super(context);
	}
	
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		// Don't render parented particles on client when in first person, to avoid view occlusion
		if(entity.equals(mc.player) && mc.options.getPerspective().isFirstPerson())
			return;
		
		double prevX = entity.prevX;
		double prevY = entity.prevY;
		double prevZ = entity.prevZ;
		
		double posX = entity.getX();
		double posY = entity.getY();
		double posZ = entity.getZ();
		
		ParticleSet set = VariousTypesClient.PARENTED_PARTICLES.getParentedParticles(entity.getUuid());
		set.getAll().forEach(particle -> 
		{
        	// Offset particle position by parent's position
        	particle.prevPosX += prevX;
        	particle.x += posX;
        	
        	particle.prevPosY += prevY;
        	particle.y += posY;
        	
        	particle.prevPosZ += prevZ;
        	particle.z += posZ;
		});
		
		LightmapTextureManager lightmap = mc.gameRenderer.getLightmapTextureManager();
		Camera camera = mc.gameRenderer.getCamera();
		lightmap.enable();
        RenderSystem.enableDepthTest();
        for (ParticleTextureSheet particleTextureSheet : PARTICLE_TEXTURE_SHEETS)
        {
            List<Particle> list = set.get(particleTextureSheet);
            if(list == null || list.isEmpty())
            	continue;
            
            RenderSystem.setShader(GameRenderer::getParticleProgram);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            particleTextureSheet.begin(bufferBuilder, mc.getTextureManager());
            for(Particle particle : list)
                try
                {
                    particle.buildGeometry(bufferBuilder, camera, tickDelta);
                }
                catch (Throwable throwable) { }
            particleTextureSheet.draw(tessellator);
        }
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightmap.disable();
        
		set.getAll().forEach(particle -> 
		{
            // Revert offset to retain association to parent
        	particle.prevPosX -= prevX;
        	particle.x -= posX;
        	
        	particle.prevPosY -= prevY;
        	particle.y -= posY;
        	
        	particle.prevPosZ -= prevZ;
        	particle.z -= posZ;
        	
        	particle.tick();
		});
	}
}
