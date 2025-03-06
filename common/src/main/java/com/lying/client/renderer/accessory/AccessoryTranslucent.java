package com.lying.client.renderer.accessory;

import java.util.function.Function;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/** Renders a semi-transparent wing model, usually for diaphanous insect wings */
public class AccessoryTranslucent<E extends LivingEntity, T extends EntityModel<E>> extends AccessoryBasic<E, T>
{
	protected AccessoryTranslucent(Function<E, EntityModel<E>> modelIn, Identifier colourTex)
	{
		this(modelIn, colourTex, colourTex);
	}
	
	protected AccessoryTranslucent(Function<E, EntityModel<E>> modelIn, Identifier colourTex, Identifier tintedTex)
	{
		super(modelIn, tex -> RenderLayer.getEntityTranslucent(tex), 0.6F);
		texture((e,b) -> b ? tintedTex : colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryTranslucent<E,T> create(Function<E, EntityModel<E>> modelIn, Identifier colourTex)
	{
		return new AccessoryTranslucent<E,T>(modelIn, colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryTranslucent<E,T> create(Function<E, EntityModel<E>> modelIn, Identifier colourTex, Identifier tintedTex)
	{
		return new AccessoryTranslucent<E,T>(modelIn, colourTex, tintedTex);
	}
}