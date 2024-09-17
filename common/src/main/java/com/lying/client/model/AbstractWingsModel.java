package com.lying.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public abstract class AbstractWingsModel<E extends LivingEntity> extends AnimatedBipedEntityModel<E> implements IModelWithRoot, IBipedLikeModel<E>
{
	protected static final String LEFT_WING = EntityModelPartNames.LEFT_WING;
	protected static final String RIGHT_WING = EntityModelPartNames.RIGHT_WING;
	
	protected final ModelPart wingLeft;
	protected final ModelPart wingRight;
	
	public AbstractWingsModel(ModelPart root)
	{
		super(root);
		this.wingLeft = body.getChild(LEFT_WING);
		this.wingRight = body.getChild(RIGHT_WING);
	}
	
	protected static void copyRotation(ModelPart from, ModelPart to)
	{
		to.pitch = from.pitch;
		to.yaw = from.yaw;
		to.roll = from.roll;
	}
}
