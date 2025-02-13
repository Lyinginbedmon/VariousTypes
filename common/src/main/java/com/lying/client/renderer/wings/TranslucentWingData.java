package com.lying.client.renderer.wings;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

/** Renders a semi-transparent wing model, usually for diaphanous insect wings */
public class TranslucentWingData<E extends LivingEntity, T extends EntityModel<E>> extends WingData<E, T>
{
	protected TranslucentWingData(T modelIn, Identifier colourTex)
	{
		this(modelIn, colourTex, colourTex);
	}
	
	protected TranslucentWingData(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		super(modelIn, colourTex, tintedTex, tex -> RenderLayer.getEntityTranslucent(tex), 0.6F);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> TranslucentWingData<E,T> create(T modelIn, Identifier colourTex)
	{
		return new TranslucentWingData<E,T>(modelIn, colourTex);
	}
	
	public static <E extends LivingEntity, T extends EntityModel<E>> TranslucentWingData<E,T> create(T modelIn, Identifier colourTex, Identifier tintedTex)
	{
		return new TranslucentWingData<E,T>(modelIn, colourTex, tintedTex);
	}
}