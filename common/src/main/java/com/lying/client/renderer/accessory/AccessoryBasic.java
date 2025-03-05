package com.lying.client.renderer.accessory;

import java.util.function.Function;

import com.lying.client.model.AnimatedBipedEntityModel;
import com.lying.client.model.IBipedLikeModel;
import com.lying.client.model.IModelWithRoot;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class AccessoryBasic<E extends LivingEntity, T extends EntityModel<E>> implements IAccessoryRenderer<E, T>
{
	protected final Function<Identifier, RenderLayer> layerProvider;
	protected final T model;
	protected final float alpha;
	protected Function<Boolean, Identifier> textureProvider;
	protected boolean tintable = true;
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E,T> create(T modelIn, Identifier colourTex)
	{
		return create(modelIn, colourTex, colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E,T> create(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		return create(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E,T> create(T modelIn, Identifier colourTex, Identifier tintedTex, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn)
	{
		return new AccessoryBasic<E,T>(modelIn, colourTex, tintedTex, layerProviderIn, alphaIn);
	}
	
	protected AccessoryBasic(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		this(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F);
	}
	
	protected AccessoryBasic(T modelIn, Identifier colourTex, Identifier tintedTex, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn)
	{
		layerProvider = layerProviderIn;
		textureProvider = bool -> bool ? tintedTex : colourTex;
		model = modelIn;
		alpha = alphaIn;
	}
	
	/** Sets this renderer to never be tinted */
	public AccessoryBasic<E,T> untinted()
	{
		tintable = false;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
	{
		model.animateModel(entity, limbAngle, limbDistance, tickDelta);
		model.setAngles(entity, limbAngle, limbDistance, (float)entity.age + tickDelta, headYaw, headPitch);
		
		if(model instanceof IBipedLikeModel)
		{
			IBipedLikeModel<E> bipedLike = (IBipedLikeModel<E>)model;
			if(contextModel instanceof BipedEntityModel)
				bipedLike.copyTransforms((BipedEntityModel<E>)contextModel);
			else if(contextModel instanceof AnimatedBipedEntityModel)
				bipedLike.copyTransforms((AnimatedBipedEntityModel<E>)contextModel);
		}
		else if(model instanceof IModelWithRoot)
		{
			IModelWithRoot rootModel = (IModelWithRoot)model;
			if(contextModel instanceof IModelWithRoot)
				rootModel.getRoot().copyTransform(((IModelWithRoot)contextModel).getRoot());
			else if(contextModel instanceof SinglePartEntityModel)
				rootModel.getRoot().copyTransform(((SinglePartEntityModel<E>)contextModel).getPart());
		}
	}
	
	public final void renderFor(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float partialTicks, boolean tinted, float r, float g, float b)
	{
		tinted = tinted & tintable;
		if(!tintable)
		{
			r = 1F;
			g = 1F;
			b = 1F;
		}
		doRender(matrixStack, vertexConsumerProvider, light, entity, partialTicks, tinted, r, g, b);
	}
	
	protected void doRender(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, E entity, float partialTicks, boolean tinted, float r, float g, float b)
	{
		Identifier texture = textureProvider.apply(tinted);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(layerProvider.apply(texture));
		model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
	}
}