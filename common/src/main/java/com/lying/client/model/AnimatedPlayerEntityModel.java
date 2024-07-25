package com.lying.client.model;

import java.util.HashMap;
import java.util.Map;

import com.lying.client.entity.AnimatedPlayerEntity;
import com.lying.client.renderer.VTAnimations;

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
	private static final Map<Integer, Animation> ANIMS = new HashMap<>();
	
	static
	{
			ANIMS.put(AnimatedPlayerEntity.ANIM_IDLE, VTAnimations.PLAYER_IDLE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_TPOSE, VTAnimations.PLAYER_TPOSE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_WALK, VTAnimations.PLAYER_WALK);
			ANIMS.put(AnimatedPlayerEntity.ANIM_LOOK_AROUND, VTAnimations.PLAYER_LOOK_AROUND);
			ANIMS.put(AnimatedPlayerEntity.ANIM_FGAME, VTAnimations.PLAYER_FGAME);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SIT, VTAnimations.PLAYER_SIT);
			ANIMS.put(AnimatedPlayerEntity.ANIM_WOLOLO, VTAnimations.PLAYER_WOLOLO);
			ANIMS.put(AnimatedPlayerEntity.ANIM_PDANCE, VTAnimations.PLAYER_PDANCE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SNEAK, VTAnimations.PLAYER_SNEAK);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SWAY, VTAnimations.PLAYER_SWAY);
			ANIMS.put(AnimatedPlayerEntity.ANIM_WAVE, VTAnimations.PLAYER_WAVE);
	}
	
	final ModelPart root;
	public final ModelPart head, hat;
	public final ModelPart body, jacket;
	public final ModelPart rightArm, rightSleeve, leftArm, leftSleeve;
	public final ModelPart rightLeg, rightPants, leftLeg, leftPants;
	
	public AnimatedPlayerEntityModel(ModelPart part)
	{
		root = part.getChild(EntityModelPartNames.ROOT);
		
		head = root.getChild(EntityModelPartNames.HEAD);
		hat = head.getChild(EntityModelPartNames.HAT);
		body = root.getChild(EntityModelPartNames.BODY);
		jacket = body.getChild(EntityModelPartNames.JACKET);
		rightArm = root.getChild(EntityModelPartNames.RIGHT_ARM);
		rightSleeve = rightArm.getChild("right_sleeve");
		leftArm = root.getChild(EntityModelPartNames.LEFT_ARM);
		leftSleeve = leftArm.getChild("left_sleeve");
		rightLeg = root.getChild(EntityModelPartNames.RIGHT_LEG);
		rightPants = rightLeg.getChild("right_pants");
		leftLeg = root.getChild(EntityModelPartNames.LEFT_LEG);
		leftPants = leftLeg.getChild("left_pants");
	}
	
	public static TexturedModelData createBodyLayer(Dilation dilation, boolean slimArms)
	{
		ModelData modelData = new ModelData();
		ModelPartData ModelPartData = modelData.getRoot();
		
		ModelPartData root = ModelPartData.addChild(EntityModelPartNames.ROOT, ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		ModelPartData head = root.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.0F)), ModelTransform.pivot(0.0F, -24.0F, 0.0F));
		head.addChild(EntityModelPartNames.HAT, ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, dilation.add(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData body = root.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, -12.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(0.0F, -12.0F, 0.0F));
		body.addChild("jacket", ModelPartBuilder.create().uv(16, 32).cuboid(-7.0F, -12.0F, -1.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(3.0F, 0.0F, -1.0F));
		
		if(slimArms)
		{
			ModelPartData right_arm = root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(-4.0F, -22.0F, 0.0F));
			right_arm.addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-2.0F, -12.0F, -1.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-1.0F, 10.0F, -1.0F));
			
			ModelPartData left_arm = root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(4.0F, -22.0F, 0.0F));
			left_arm.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(0.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		}
		else
		{
			ModelPartData right_arm = root.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(-4.0F, -21.0F, 0.0F));
			right_arm.addChild("right_sleeve", ModelPartBuilder.create().uv(39, 32).cuboid(-4.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
			
			ModelPartData left_arm = root.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create().uv(32, 48).cuboid(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(4.0F, -21.0F, 0.0F));
			left_arm.addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(0.0F, -3.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		}
		
		ModelPartData right_leg = root.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(-2.0F, -12.0F, 0.0F));
		right_leg.addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		ModelPartData left_leg = root.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.0F)), ModelTransform.pivot(2.0F, -12.0F, 0.0F));
		left_leg.addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	public ModelPart getPart() { return root; }
	
	public void setAngles(E playerEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch)
	{
		getPart().traverse().forEach(ModelPart::resetTransform);
		ANIMS.entrySet().forEach(entry -> updateAnimation(playerEntity.animations.get(entry.getKey()), entry.getValue(), ageInTicks));
	}
	
	public void setVisible(boolean visible)
	{
		getPart().traverse().forEach(part -> part.visible = visible);
	}
}
