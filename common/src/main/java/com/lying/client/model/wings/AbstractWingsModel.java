package com.lying.client.model.wings;

import com.lying.client.model.AbstractAccessoryModel;
import com.lying.entity.AccessoryAnimationInterface;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public abstract class AbstractWingsModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public static final String LEFT_WING = EntityModelPartNames.LEFT_WING;
	public static final String RIGHT_WING = EntityModelPartNames.RIGHT_WING;
	
	public static final String LEFT_HUMERUS = "left_humerus";
	public static final String RIGHT_HUMERUS = "right_humerus";
	
	public static final String LEFT_RADIUS = "left_radius";
	public static final String RIGHT_RADIUS = "right_radius";
	
	public static final String LEFT_PHALANGES = "left_phalanges";
	public static final String RIGHT_PHALANGES = "right_phalanges";
	
	public static final String LEFT_METATARSAL = "left_metatarsal";	
	public static final String RIGHT_METATARSAL = "right_metatarsal";
	
	protected final ModelPart wingLeft;
	protected final ModelPart wingRight;
	
	protected AbstractWingsModel(ModelPart root)
	{
		super(root);
		this.wingLeft = body.getChild(LEFT_WING);
		this.wingRight = body.getChild(RIGHT_WING);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		wingRight.traverse().forEach(part -> part.resetTransform());
		wingLeft.traverse().forEach(part -> part.resetTransform());
		
		if(livingEntity instanceof AccessoryAnimationInterface)
			animateWings((AccessoryAnimationInterface)livingEntity, age);
	}
	
	protected void animateWings(AccessoryAnimationInterface anims, float age) { }
}
