package com.lying.client.renderer.accessory;

import java.util.function.Function;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.lying.client.renderer.IconSpriteManager;
import com.lying.client.utility.ModelTransformHelper;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

public class OverheadIconRenderer<E extends LivingEntity, T extends EntityModel<E>> implements IAccessoryRenderer<E, T>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private static final float ICON_SIZE = 0.3F;
	private final Function<Boolean, Identifier> spriteId;
	
	private static final float baseOffset = 8 + (2F/3F);
	private Vector3f poseOffset = new Vector3f(0F, 0F, 0F);
	private float scale = 1F;
	private float size = 16F * scale;
	
	public OverheadIconRenderer(Identifier icon)
	{
		spriteId = b -> icon;
	}
	
	public OverheadIconRenderer(Identifier icon, Identifier tinted)
	{
		spriteId = b -> b ? tinted : icon;
	}
	
	public OverheadIconRenderer<E,T> withSize(int scaleIn)
	{
		size = (float)scaleIn;
		scale = size / 16F;
		return this;
	}
	
	public OverheadIconRenderer<E,T> withScale(float scaleIn)
	{
		scale = scaleIn;
		size = 16F * scale;
		return this;
	}
	
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch) 
	{
		// Calculate the icon's hover position based on the current posing of the context model
		poseOffset.zero();
		
		ModelPart head = ModelTransformHelper.getHead(contextModel);
		if(head == null)
			return;
		
		ModelPart root = ModelTransformHelper.getRoot(contextModel);
		if(root != null)
		{
			// Move from head to root at the player's feet
			Vector3f headToRoot = ModelTransformHelper.relativeTo(head, root);
			// Adjust position by however much the root has moved
			Vector3f rootOffset = ModelTransformHelper.getTranslation(root);
			// Go to head's current position based on root's rotation
			Vector3f rootToHead = ModelTransformHelper.getCurrentPivotRelativeto(head, root);
			
			poseOffset.add(headToRoot).add(rootOffset).add(rootToHead);
		}
		
		// Adjust position by however much the head has moved
		Vector3f headOffset = ModelTransformHelper.getTranslation(head);
		// Calculate relative up by rotating by both the root and head rotations
		Vector3f upVec = ModelTransformHelper.rotateBy(new Vector3f(0, -1, 0), head.getTransform());
		if(root != null)
			upVec = ModelTransformHelper.rotateBy(upVec, root.getTransform());
		
		poseOffset.add(headOffset).add(upVec.normalize().mul(baseOffset + (3F * scale)));
		
		// Divide by 16 to convert from model coordinates to global
		poseOffset.mul(1/16F);
	}
	
	public void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float tickDelta, boolean tinted, float r, float g, float b)
	{
		/**
		 * Goals:
		 * 	[X]	Render animated icon above the player's head
		 * 	[X]	Ideally lock icon position to above the head, regardless of model posing
		 *  []	Ensure icon always directly faces the camera
		 *  [X]	Ensure icon is occluded by geometry between it and camera as normal
		 */
		
		Identifier id = spriteId.apply(tinted);
		Sprite sprite = IconSpriteManager.INSTANCE.getIcon(id);
		matrixStack.push();
			RenderSystem.enableDepthTest();
			RenderSystem.enableCull();
			matrixStack.translate(poseOffset.x(), poseOffset.y(), poseOffset.z());
			matrixStack.push();
				// FIXME Reliably face the camera & remain visible in character sheet UI
				Camera camera = mc.gameRenderer.getCamera();
				
				Vec3d camPos = camera.getPos();
				Vec3d eyePos = entity.getEyePos().add(0D, 0.5D, 0D);
				Vec3d dir = camPos.subtract(eyePos).normalize();
				float yaw = (float)Math.atan2(dir.x, dir.z);
				float pitch = (float)Math.asin(-dir.y);
				
				float entityYaw = (float)Math.toRadians(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()));
				matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-yaw - entityYaw));
				matrixStack.multiply(RotationAxis.POSITIVE_X.rotation(pitch));
				
				matrixStack.scale(ICON_SIZE, ICON_SIZE, ICON_SIZE);
				drawSprite(matrixStack, sprite, 0, 0, 0, 16, 16, size, r, g, b);
			matrixStack.pop();
			RenderSystem.disableCull();
			RenderSystem.disableDepthTest();
		matrixStack.pop();
	}
	
	public static void drawSprite(MatrixStack matrixStack, Sprite sprite, int x, int y, int z, int width, int height, float scale, float r, float g, float b)
	{
		if(width == 0 || height == 0) return;
		drawTexturedQuad(matrixStack, sprite.getAtlasId(), sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV(), scale, r, g, b);
	}
	
	public static void drawTexturedQuad(MatrixStack matrixStack, Identifier texture, float u1, float u2, float v1, float v2, float scale, float r, float g, float b)
	{
		Vector3f[] vertices = new Vector3f[]{
				new Vector3f(1F, 1F, 0F), 
				new Vector3f(1F, -1F, 0F), 
				new Vector3f(-1F, -1F, 0F), 
				new Vector3f(-1F, 1F, 0F)};
		for(int i=0; i<4; ++i)
		{
			Vector3f vec = vertices[i];
			vec.mul(0.5F).mul(scale / 16F);
		}
		
		RenderSystem.setShaderTexture(0, texture);
		RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapProgram);
		Matrix4f model = matrixStack.peek().getPositionMatrix();
		BufferBuilder vertexConsumer = Tessellator.getInstance().getBuffer();
		vertexConsumer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
			vertexConsumer.vertex(model, vertices[0].x(), vertices[0].y(), vertices[0].z()).color(r, g, b, 1F).texture(u2, v2).light(15).next();
			vertexConsumer.vertex(model, vertices[1].x(), vertices[1].y(), vertices[1].z()).color(r, g, b, 1F).texture(u2, v1).light(15).next();
			vertexConsumer.vertex(model, vertices[2].x(), vertices[2].y(), vertices[2].z()).color(r, g, b, 1F).texture(u1, v1).light(15).next();
			vertexConsumer.vertex(model, vertices[3].x(), vertices[3].y(), vertices[3].z()).color(r, g, b, 1F).texture(u1, v2).light(15).next();
		BufferRenderer.drawWithGlobalProgram(vertexConsumer.end());
	}
}
