package com.lying.client.renderer.accessory;

import java.util.function.Function;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lying.client.renderer.IconSpriteManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class OverheadIconRenderer<E extends LivingEntity, T extends EntityModel<E>> implements IAccessoryRenderer<E, T>
{
	private static final float ICON_SIZE = 0.3F;
	private final Function<Boolean, Identifier> spriteId;
	
	public OverheadIconRenderer(Identifier icon)
	{
		spriteId = b -> icon;
	}
	
	public OverheadIconRenderer(Identifier icon, Identifier tinted)
	{
		spriteId = b -> b ? tinted : icon;
	}
	
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch) { }
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float tickDelta, boolean tinted, float r, float g, float b)
	{
		/**
		 * Goals:
		 * 	Render animated icon above the player's head
		 * 		Ideally lock icon position to above the head, regardless of model posing
		 *  Ensure icon always directly faces the camera
		 *  Ensure icon is occluded by geometry between it and camera as normal
		 */
		Identifier id = spriteId.apply(tinted);
		Sprite sprite = IconSpriteManager.INSTANCE.getIcon(id);
		matrixStack.push();
			RenderSystem.enableDepthTest();
			RenderSystem.enableCull();
			matrixStack.translate(0, -0.8D, 0);
			matrixStack.scale(ICON_SIZE, ICON_SIZE, ICON_SIZE);
			drawSprite(matrixStack, sprite, 0, 0, 0, 16, 16);
			RenderSystem.disableCull();
			RenderSystem.disableDepthTest();
		matrixStack.pop();
	}
	
	private static void drawSprite(MatrixStack matrixStack, Sprite sprite, int x, int y, int z, int width, int height)
	{
		if(width == 0 || height == 0) return;
		drawTexturedQuad(matrixStack, sprite.getAtlasId(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
	}
	
	private static void drawTexturedQuad(MatrixStack matrixStack, Identifier texture, float u1, float u2, float v1, float v2)
	{
		Vector3f[] vertices = new Vector3f[]{
				new Vector3f(1F, 1F, 0F), 
				new Vector3f(1F, -1F, 0F), 
				new Vector3f(-1F, -1F, 0F), 
				new Vector3f(-1F, 1F, 0F)};
		for(int i=0; i<4; ++i)
		{
			Vector3f vec = vertices[i];
			vec.mul(0.5F);
		}
		
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
		Matrix4f model = matrixStack.peek().getPositionMatrix();
		BufferBuilder vertexConsumer = Tessellator.getInstance().getBuffer();
		vertexConsumer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			vertexConsumer.vertex(model, vertices[0].x(), vertices[0].y(), vertices[0].z()).color(1F, 1F, 1F, 1F).texture(u2, v2).light(15).next();
			vertexConsumer.vertex(model, vertices[1].x(), vertices[1].y(), vertices[1].z()).color(1F, 1F, 1F, 1F).texture(u2, v1).light(15).next();
			vertexConsumer.vertex(model, vertices[2].x(), vertices[2].y(), vertices[2].z()).color(1F, 1F, 1F, 1F).texture(u1, v1).light(15).next();
			vertexConsumer.vertex(model, vertices[3].x(), vertices[3].y(), vertices[3].z()).color(1F, 1F, 1F, 1F).texture(u1, v2).light(15).next();
		BufferRenderer.drawWithGlobalProgram(vertexConsumer.end());
	}
}
