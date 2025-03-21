package com.lying.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class GelatinousBipedEntityModel<T extends LivingEntity> extends BipedEntityModel<T> implements IBipedLikeModel<T>
{
	public GelatinousBipedEntityModel(ModelPart root)
	{
		super(root);
	}
	
	public ModelPart getModelHead() { return head; }
	
	public static TexturedModelData getTexturedModelData()
	{
		Dilation dilation = Dilation.NONE;
		ModelData modelData = BipedEntityModel.getModelData(dilation, 0F);
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, dilation.add(-1F)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create(), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 16).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, dilation.add(-1F)), ModelTransform.pivot(0.0f, 0.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create(), ModelTransform.pivot(-5.0f, 2.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create(), ModelTransform.pivot(5.0f, 2.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(-1.9f, 12.0f, 0.0f));
		modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create(), ModelTransform.pivot(1.9f, 12.0f, 0.0f));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	public void copyTransforms(BipedEntityModel<T> parent)
	{
		parent.copyStateTo(this);
        leftArmPose = parent.leftArmPose;
        rightArmPose = parent.rightArmPose;
        sneaking = parent.sneaking;
        head.copyTransform(parent.head);
        hat.copyTransform(parent.hat);
        body.copyTransform(parent.body);
        rightArm.copyTransform(parent.rightArm);
        leftArm.copyTransform(parent.leftArm);
        rightLeg.copyTransform(parent.rightLeg);
        leftLeg.copyTransform(parent.leftLeg);
	}
	
	public void copyTransforms(AnimatedBipedEntityModel<T> parent)
	{
		parent.copyStateTo(this);
        head.copyTransform(parent.head);
        hat.copyTransform(parent.hat);
        body.copyTransform(parent.body);
        rightArm.copyTransform(parent.rightArm);
        leftArm.copyTransform(parent.leftArm);
        rightLeg.copyTransform(parent.rightLeg);
        leftLeg.copyTransform(parent.leftLeg);
	}
}
