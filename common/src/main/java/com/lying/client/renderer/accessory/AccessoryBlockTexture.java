package com.lying.client.renderer.accessory;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.lying.client.model.AnimatedBipedEntityModel;
import com.lying.client.model.IBipedLikeModel;
import com.lying.client.model.IModelWithRoot;
import com.lying.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AccessoryBlockTexture<E extends LivingEntity, M extends EntityModel<E>> implements IAccessoryRenderer<E, M>
{
	private final Function<Identifier, RenderLayer> layerProvider;
	private final Function<E, EntityModel<E>> modelProvider;
	private BiFunction<E, Boolean, Supplier<Identifier>> textureProvider = AccessoryBlockTexture::noTexture;
	protected final float alpha;
	
	protected boolean tintable = true;
	protected BlockColorProvider tintProvider = null;
	
	protected EntityModel<E> model = null;
	
	public static <E extends LivingEntity, M extends EntityModel<E>> AccessoryBlockTexture<E, M> create(Function<E, EntityModel<E>> modelSupplierIn, Supplier<Block> blockIn)
	{
		Supplier<Identifier> blockTex = () -> 
		{
			Identifier tex = Registries.BLOCK.getId(blockIn.get());
			return Identifier.of(tex.getNamespace(), "textures/block/"+tex.getPath()+".png");
		};
		return new AccessoryBlockTexture<E, M>(modelSupplierIn, (e,b) -> blockTex);
	}
	
	protected AccessoryBlockTexture(Function<E, EntityModel<E>> modelIn, BiFunction<E, Boolean, Supplier<Identifier>> textureProviderIn)
	{
		this(modelIn);
		texture(textureProviderIn);
	}
	
	protected AccessoryBlockTexture(Function<E, EntityModel<E>> modelIn)
	{
		this(modelIn, tex -> RenderLayer.getEntityCutoutNoCull(tex), 1F);
	}
	
	protected AccessoryBlockTexture(Function<E, EntityModel<E>> modelIn, Function<Identifier, RenderLayer> layerProviderIn, float alphaIn)
	{
		layerProvider = layerProviderIn;
		modelProvider = modelIn;
		alpha = alphaIn;
	}
	
	public static <E extends LivingEntity> Supplier<Identifier> noTexture(E entity, boolean tinted) { return () -> Reference.ModInfo.prefix("no_texture.png"); }
	
	/** Sets this renderer to never be tinted */
	public AccessoryBlockTexture<E,M> untinted()
	{
		tintable = false;
		return this;
	}
	
	public AccessoryBlockTexture<E,M> tintFunc(BlockColorProvider tintProviderIn)
	{
		tintProvider = tintProviderIn;
		return this;
	}
	
	public AccessoryBlockTexture<E, M> texture(BiFunction<E, Boolean, Supplier<Identifier>> textureProviderIn)
	{
		textureProvider = textureProviderIn;
		return this;
	}
	
	public final Identifier texture(E entity, boolean tinted) { return textureProvider.apply(entity, tinted).get(); }
	
	@SuppressWarnings("unchecked")
	public void prepareModel(E entity, M contextModel, float limbAngle, float limbDistance, float tickDelta, float headYaw, float headPitch)
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
		if(!tinted)
			if(tintProvider == null)
			{
				r = 1F;
				g = 1F;
				b = 1F;
			}
			else
			{
				BlockPos pos = entity.getBlockPos();
				World world = entity.getWorld();
				BlockState state = world.getBlockState(pos);
				int tint = tintProvider.getColor(state, world, pos, 0);
				r = (float)((tint >> 16) & 0xff) / 255F;
				g = (float)((tint >> 8) & 0xff) / 255F;
				b = (float)(tint & 0xff) / 255F;
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