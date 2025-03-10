package com.lying.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;

public class AbstractAccessoryModel<E extends LivingEntity> extends AnimatedBipedEntityModel<E> implements IBipedLikeModel<E>
{
	protected AbstractAccessoryModel(ModelPart part)
	{
		super(part);
	}
	
	public ModelPart getModelHead() { return head; }
	
	protected static void copyRotation(ModelPart from, ModelPart to)
	{
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
