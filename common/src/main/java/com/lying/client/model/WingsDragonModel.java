package com.lying.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class WingsDragonModel<E extends LivingEntity> extends WingsSkeletonModel<E>
{
	public static final String RIGHT_TERTIAL = "right_tertial";
	public static final String RIGHT_DIGIT_1_SMALL = "right_digit_1_small";
	public static final String RIGHT_DIGIT_1_LARGE = "right_digit_1_large";
	public static final String RIGHT_DIGIT_2_SMALL = "right_digit_2_small";
	public static final String RIGHT_DIGIT_2_LARGE = "right_digit_2_large";
	public static final String RIGHT_DIGIT_3_SMALL = "right_digit_3_small";
	public static final String RIGHT_DIGIT_3_LARGE = "right_digit_3_large";
	public static final String RIGHT_DIGIT_4_SMALL = "right_digit_4_small";
	public static final String RIGHT_DIGIT_4_LARGE = "right_digit_4_large";
	
	public static final String LEFT_TERTIAL = "left_tertial";
	public static final String LEFT_DIGIT_1_SMALL = "left_digit_1_small";
	public static final String LEFT_DIGIT_1_LARGE = "left_digit_1_large";
	public static final String LEFT_DIGIT_2_SMALL = "left_digit_2_small";
	public static final String LEFT_DIGIT_2_LARGE = "left_digit_2_large";
	public static final String LEFT_DIGIT_3_SMALL = "left_digit_3_small";
	public static final String LEFT_DIGIT_3_LARGE = "left_digit_3_large";
	public static final String LEFT_DIGIT_4_SMALL = "left_digit_4_small";
	public static final String LEFT_DIGIT_4_LARGE = "left_digit_4_large";
	
	public WingsDragonModel(ModelPart root)
	{
		super(root);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = createBones();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData rightWing = body.getChild(RIGHT_WING);
		ModelPartData rightHumerus = rightWing.getChild(RIGHT_HUMERUS);
		
		ModelPartData rightTertial = rightHumerus.addChild(RIGHT_TERTIAL, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
			rightTertial.addChild("cube_r1", ModelPartBuilder.create().uv(34, 8).cuboid(0.0F, -2.0F, -1.0F, 0.0F, 5.0F, 10.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 2.0F, 1.0F, 0.0F, 0.0F, -0.0873F));
		
		ModelPartData rightRadius = rightHumerus.getChild(RIGHT_RADIUS);
		
		ModelPartData rightDigit1 = rightRadius.getChild(RIGHT_DIGIT_1);
			rightDigit1.addChild(RIGHT_DIGIT_1_SMALL, ModelPartBuilder.create().uv(0, -4).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			rightDigit1.addChild(RIGHT_DIGIT_1_LARGE, ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData rightHand = rightRadius.getChild(RIGHT_HAND);

		ModelPartData rightDigit2 = rightHand.getChild(RIGHT_DIGIT_2);
			rightDigit2.addChild(RIGHT_DIGIT_2_SMALL, ModelPartBuilder.create().uv(0, -4).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			rightDigit2.addChild(RIGHT_DIGIT_2_LARGE, ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rightDigit3 = rightHand.getChild(RIGHT_DIGIT_3);
			rightDigit3.addChild(RIGHT_DIGIT_3_SMALL, ModelPartBuilder.create().uv(0, -4).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			rightDigit3.addChild(RIGHT_DIGIT_3_LARGE, ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData rightDigit4 = rightHand.getChild(RIGHT_DIGIT_4);
			rightDigit4.addChild(RIGHT_DIGIT_4_SMALL, ModelPartBuilder.create().uv(0, -4).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			rightDigit4.addChild(RIGHT_DIGIT_4_LARGE, ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData leftWing = body.getChild(LEFT_WING);
		ModelPartData leftHumerus = leftWing.getChild(LEFT_HUMERUS);
		
		ModelPartData leftTertial = leftHumerus.addChild(LEFT_TERTIAL, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.5F, 0.0F));
			leftTertial.addChild("cube_r10", ModelPartBuilder.create().uv(34, 8).mirrored().cuboid(0.0F, -2.0F, -1.0F, 0.0F, 5.0F, 10.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, 2.0F, 1.0F, 0.0F, 0.0F, 0.0873F));
		
		ModelPartData leftRadius = leftHumerus.getChild(LEFT_RADIUS);
		
		ModelPartData leftDigit1 = leftRadius.getChild(LEFT_DIGIT_1);
			leftDigit1.addChild(LEFT_DIGIT_1_SMALL, ModelPartBuilder.create().uv(0, -4).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			leftDigit1.addChild(LEFT_DIGIT_1_LARGE, ModelPartBuilder.create().uv(0, 1).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData leftHand = leftRadius.getChild(LEFT_HAND);
		
		ModelPartData leftDigit2 = leftHand.getChild(LEFT_DIGIT_2);
			leftDigit2.addChild(LEFT_DIGIT_2_SMALL, ModelPartBuilder.create().uv(0, -4).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			leftDigit2.addChild(LEFT_DIGIT_2_LARGE, ModelPartBuilder.create().uv(0, 1).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData leftDigit3 = leftHand.getChild(LEFT_DIGIT_3);
			leftDigit3.addChild(LEFT_DIGIT_3_SMALL, ModelPartBuilder.create().uv(0, -4).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			leftDigit3.addChild(LEFT_DIGIT_3_LARGE, ModelPartBuilder.create().uv(0, 1).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData leftDigit4 = leftHand.getChild(LEFT_DIGIT_4);
			leftDigit4.addChild(LEFT_DIGIT_4_SMALL, ModelPartBuilder.create().uv(0, -4).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 5.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			leftDigit4.addChild(LEFT_DIGIT_4_LARGE, ModelPartBuilder.create().uv(0, 1).mirrored().cuboid(0.0F, 0.75F, 0.0F, 0.0F, 10.0F, 17.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
}
