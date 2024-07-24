package com.lying.client.model;

import java.util.List;

import com.lying.client.entity.AnimatedPlayerEntity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class AnimatedPlayerEntityModel<E extends AnimatedPlayerEntity> extends SinglePartEntityModel<E>
{
	final ModelPart root;
	final ModelPart head;
	final ModelPart body;
	final ModelPart rightArm, leftArm;
	final ModelPart rightLeg, leftLeg;
	
	public AnimatedPlayerEntityModel(ModelPart part)
	{
		root = part.getChild(EntityModelPartNames.ROOT);
		
		head = root.getChild(EntityModelPartNames.HEAD);
		body = root.getChild(EntityModelPartNames.BODY);
		rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
		leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
		rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
		leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
	}
	
	public static TexturedModelData createBodyLayer(Dilation dilation, boolean slimArms)
	{
		ModelData modelData = new ModelData();
		ModelPartData ModelPartData = modelData.getRoot();
		
		ModelPartData root = ModelPartData.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		ModelPartData head = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F))
		.uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));
		
		ModelPartData body = root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.0F))
		.uv(16, 32).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		
		ModelPartData right_arm = root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F))
		.uv(39, 32).cuboid(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-4.0F, -21.0F, 0.0F));
		
		ModelPartData left_arm = root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F))
		.uv(48, 48).cuboid(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(4.0F, -21.0F, 0.0F));
		
		ModelPartData right_leg = root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F))
		.uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));
		
		ModelPartData left_leg = root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F))
		.uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(2.0F, -12.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	public ModelPart getPart() { return root; }
	
	public void setAngles(E playerEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch)
	{
		getPart().traverse().forEach(ModelPart::resetTransform);
		List<Animation> animations = playerEntity.animations.animations();
		for(int i=0; i<animations.size(); i++)
			updateAnimation(playerEntity.animations.get(i), animations.get(i), ageInTicks);
	}
	
	public void setVisible(boolean visible)
	{
		getPart().traverse().forEach(part -> part.visible = visible);
	}
}
