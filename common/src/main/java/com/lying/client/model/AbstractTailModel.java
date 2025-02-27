package com.lying.client.model;

import com.lying.entity.AccessoryAnimationInterface;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public abstract class AbstractTailModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public static final String TAIL = EntityModelPartNames.TAIL;
	public static final String SEG1 = "segment_1";
	public static final String SEG2 = "segment_2";
	public static final String SEG3 = "segment_3";
	
	protected final ModelPart tail;
	
	public AbstractTailModel(ModelPart part)
	{
		super(part);
		tail = this.body.getChild(TAIL);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		tail.traverse().forEach(part -> part.resetTransform());
		boolean isMoving = limbDistance > 1.0E-5f;
		if(livingEntity instanceof AccessoryAnimationInterface)
			animateTail((AccessoryAnimationInterface)livingEntity, isMoving, limbAngle, limbDistance, age);
	}
	
	protected abstract void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age);
}
