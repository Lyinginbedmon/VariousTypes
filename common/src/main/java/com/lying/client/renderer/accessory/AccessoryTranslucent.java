package com.lying.client.renderer.accessory;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/** Renders a semi-transparent wing model, usually for diaphanous insect wings */
public class AccessoryTranslucent<E extends LivingEntity, T extends EntityModel<E>> extends AccessoryBasic<E, T>
{
	protected AccessoryTranslucent(T modelIn, Identifier colourTex)
	{
		this(modelIn, colourTex, colourTex);
	}
	
	protected AccessoryTranslucent(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		super(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityTranslucent(tex), 0.6F);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryTranslucent<E,T> create(T modelIn, Identifier colourTex)
	{
		return new AccessoryTranslucent<E,T>(modelIn, colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> AccessoryTranslucent<E,T> create(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		return new AccessoryTranslucent<E,T>(modelIn, colourTex, tintedTex);
	}
}