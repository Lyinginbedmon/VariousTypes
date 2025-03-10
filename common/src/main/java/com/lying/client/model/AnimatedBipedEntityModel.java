package com.lying.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.LivingEntity;

public abstract class AnimatedBipedEntityModel<E extends LivingEntity> extends SinglePartEntityModel<E> implements IModelWithRoot, IBipedLikeModel<E>
{
	final ModelPart root;
	public final ModelPart head, hat;
	public final ModelPart body;
	public final ModelPart rightArm, leftArm;
	public final ModelPart rightLeg, leftLeg;
	public ArmPose rightArmPose = ArmPose.EMPTY, leftArmPose = ArmPose.EMPTY;
	public boolean sneaking;
	public float leaningPitch;
	
	public AnimatedBipedEntityModel(ModelPart part)
	{
		root = part;
		head = root.getChild(EntityModelPartNames.HEAD);
		hat = root.getChild(EntityModelPartNames.HAT);
		body = root.getChild(EntityModelPartNames.BODY);
		rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
		leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
		rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
		leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
	}
	
	public static ModelData getModelData(Dilation dilation, boolean slimArms)
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		if(slimArms)
		{
			modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
			modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		}
		else
		{
			modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
			modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		}
		modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		return modelData;
	}
	
	/* Returns ModelData representing an otherwise-blank biped structure in line with PlayerEntityModel */
	public static ModelData getRig()
	{
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create(), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create(), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		return modelData;
	}
	
	public ModelPart getPart() { return root; }
	
	public ModelPart getRoot() { return root; }
	
	public ModelPart getModelHead() { return head; }
	
	public void setVisible(boolean visible)
	{
		getPart().traverse().forEach(part -> part.visible = visible);
	}
	
	public void animateModel(E entity, float limbAngle, float limbDistance, float tickDelta)
	{
		this.leaningPitch = entity.getLeaningPitch(tickDelta);
		super.animateModel(entity, limbAngle, limbDistance, tickDelta);
	}
	
	public void setAngles(E livingEntity, float f, float g, float h, float i, float j) { }
	
	public void copyTransformsTo(BipedEntityModel<E> child)
	{
		child.rightArmPose = this.rightArmPose;
		child.leftArmPose = this.leftArmPose;
		child.sneaking = this.sneaking;
		child.head.copyTransform(head);
		child.hat.copyTransform(hat);
		child.body.copyTransform(body);
		child.rightArm.copyTransform(rightArm);
		child.leftArm.copyTransform(leftArm);
		child.rightLeg.copyTransform(rightLeg);
		child.leftLeg.copyTransform(leftLeg);
	}
	
	public void copyTransforms(BipedEntityModel<E> parent)
	{
		this.rightArmPose = parent.rightArmPose;
		this.leftArmPose = parent.leftArmPose;
		this.sneaking = parent.sneaking;
		root.resetTransform();;
		head.copyTransform(parent.head);
		hat.copyTransform(parent.hat);
		body.copyTransform(parent.body);
		rightArm.copyTransform(parent.rightArm);
		leftArm.copyTransform(parent.leftArm);
		rightLeg.copyTransform(parent.rightLeg);
		leftLeg.copyTransform(parent.leftLeg);
	}
	
	public void copyTransforms(AnimatedBipedEntityModel<E> parent)
	{
		this.rightArmPose = parent.rightArmPose;
		this.leftArmPose = parent.leftArmPose;
		this.sneaking = parent.sneaking;
		root.copyTransform(parent.root);
		head.copyTransform(parent.head);
		hat.copyTransform(parent.hat);
		body.copyTransform(parent.body);
		rightArm.copyTransform(parent.rightArm);
		leftArm.copyTransform(parent.leftArm);
		rightLeg.copyTransform(parent.rightLeg);
		leftLeg.copyTransform(parent.leftLeg);
	}
}
