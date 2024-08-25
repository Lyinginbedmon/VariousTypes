package com.lying.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

public abstract class AnimatedBipedEntityModel<E extends LivingEntity> extends SinglePartEntityModel<E>
{
	final ModelPart root;
	public final ModelPart head, hat;
	public final ModelPart body;
	public final ModelPart rightArm, leftArm;
	public final ModelPart rightLeg, leftLeg;
	
	public AnimatedBipedEntityModel(ModelPart part)
	{
		root = part.getChild(EntityModelPartNames.ROOT);
		
		head = root.getChild(EntityModelPartNames.HEAD);
		hat = head.getChild(EntityModelPartNames.HAT);
		body = root.getChild(EntityModelPartNames.BODY);
		rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
		leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
		rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
		leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
	}
	
	public ModelPart getPart() { return root; }
	
	public void setVisible(boolean visible)
	{
		getPart().traverse().forEach(part -> part.visible = visible);
	}
	
	public void copyTransformsTo(BipedEntityModel<E> child)
	{
		copyStateTo(child);
		child.head.copyTransform(head);
		child.hat.copyTransform(hat);
		child.body.copyTransform(body);
		child.leftArm.copyTransform(leftArm);
		child.rightArm.copyTransform(rightArm);
		child.leftLeg.copyTransform(leftLeg);
		child.rightLeg.copyTransform(rightLeg);
	}
}
