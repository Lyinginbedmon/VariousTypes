package com.lying.client.renderer.accessory;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.lying.client.model.AnimatedBipedEntityModel;
import com.lying.client.model.IBipedLikeModel;
import com.lying.client.model.IModelWithRoot;
import com.lying.reference.Reference;

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
	private final Function<Identifier, RenderLayer> layerProvider;
	private final Function<E, EntityModel<E>> modelProvider;
	private BiFunction<E, Boolean, Identifier> textureProvider = AccessoryBasic::noTexture;
	protected final float alpha;
	protected boolean tintable = true;
	
	protected EntityModel<E> model = null;
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E, T> create(Function<E, EntityModel<E>> modelSupplierIn, Identifier colourTex)
	{
		return new AccessoryBasic<E, T>(modelSupplierIn, (e,b) -> colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E, T> create(Function<E, EntityModel<E>> modelSupplierIn, Identifier colourTex, Identifier tintedTex)
	{
		return new AccessoryBasic<E, T>(modelSupplierIn, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F).texture((e,b) -> b ? tintedTex : colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryBasic<E, T> create(Function<E, EntityModel<E>> modelSupplierIn, BiFunction<E, Boolean, Identifier> texSupplierIn)
	{
		return new AccessoryBasic<E, T>(modelSupplierIn, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F).texture(texSupplierIn);
	}
	
	protected AccessoryBasic(Function<E, EntityModel<E>> modelIn, BiFunction<E, Boolean, Identifier> textureProviderIn)
	{
		this(modelIn);
		texture(textureProviderIn);
	}
	
	protected AccessoryBasic(Function<E, EntityModel<E>> modelIn)
	{
		this(modelIn, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F);
	}
	
	protected AccessoryBasic(Function<E, EntityModel<E>> modelIn, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn)
	{
		layerProvider = layerProviderIn;
		modelProvider = modelIn;
		alpha = alphaIn;
	}
	
	public static <E extends LivingEntity> Identifier noTexture(E entity, boolean tinted) { return Reference.ModInfo.prefix("no_texture.png"); }
	
	/** Sets this renderer to never be tinted */
	public AccessoryBasic<E,T> untinted()
	{
		tintable = false;
		return this;
	}
	
	public AccessoryBasic<E, T> texture(BiFunction<E, Boolean, Identifier> textureProviderIn)
	{
		textureProvider = textureProviderIn;
		return this;
	}
	
	public final Identifier texture(E entity, boolean tinted) { return textureProvider.apply(entity, tinted); }
	
	@SuppressWarnings("unchecked")
	public void prepareModel(E entity, T contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
	{
		model = modelProvider.apply(entity);
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
		Identifier texture = texture(entity, tinted);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(layerProvider.apply(texture));
		model.render(matrixStack, vertexConsumer, light, LivingEntityRenderer.getOverlay(entity, 0f), r, g, b, alpha);
	}
}