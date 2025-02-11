package com.lying.client.model;

import java.util.HashMap;
import java.util.Map;

import com.lying.client.init.VTAnimations;
import com.lying.entity.AnimatedPlayerEntity;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class AnimatedPlayerEntityModel<E extends AnimatedPlayerEntity> extends AnimatedBipedEntityModel<E>
{
	private static final Map<Integer, Animation> ANIMS = new HashMap<>();
	
	static
	{
			ANIMS.put(AnimatedPlayerEntity.ANIM_IDLE, VTAnimations.PlayerAnimations.PLAYER_IDLE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_TPOSE, VTAnimations.PlayerAnimations.PLAYER_TPOSE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_LOOK_AROUND, VTAnimations.PlayerAnimations.PLAYER_LOOK_AROUND);
			ANIMS.put(AnimatedPlayerEntity.ANIM_FGAME_START, VTAnimations.PlayerAnimations.PLAYER_FGAME_START);
			ANIMS.put(AnimatedPlayerEntity.ANIM_FGAME_MAIN, VTAnimations.PlayerAnimations.PLAYER_FGAME_MAIN);
			ANIMS.put(AnimatedPlayerEntity.ANIM_FGAME_END, VTAnimations.PlayerAnimations.PLAYER_FGAME_END);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SIT_START, VTAnimations.PlayerAnimations.PLAYER_SIT_START);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SIT_MAIN, VTAnimations.PlayerAnimations.PLAYER_SIT_MAIN);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SIT_END, VTAnimations.PlayerAnimations.PLAYER_SIT_END);
			ANIMS.put(AnimatedPlayerEntity.ANIM_WOLOLO, VTAnimations.PlayerAnimations.PLAYER_WOLOLO);
			ANIMS.put(AnimatedPlayerEntity.ANIM_PDANCE, VTAnimations.PlayerAnimations.PLAYER_PDANCE);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SNEAK, VTAnimations.PlayerAnimations.PLAYER_SNEAK);
			ANIMS.put(AnimatedPlayerEntity.ANIM_SWAY, VTAnimations.PlayerAnimations.PLAYER_SWAY);
			ANIMS.put(AnimatedPlayerEntity.ANIM_WAVE, VTAnimations.PlayerAnimations.PLAYER_WAVE);
	}
	
	public final ModelPart jacket;
	public final ModelPart rightSleeve, leftSleeve;
	public final ModelPart rightPants, leftPants;
	
	public AnimatedPlayerEntityModel(ModelPart part)
	{
		super(part);
		jacket = body.getChild(EntityModelPartNames.JACKET);
		rightSleeve = rightArm.getChild("right_sleeve");
		leftSleeve = leftArm.getChild("left_sleeve");
		rightPants = rightLeg.getChild("right_pants");
		leftPants = leftLeg.getChild("left_pants");
	}
	
	public static TexturedModelData createBodyLayer(Dilation dilation, boolean slimArms)
	{
		ModelData modelData = getModelData(dilation, slimArms);
		ModelPartData root = modelData.getRoot();
		
		root.getChild(EntityModelPartNames.BODY).addChild(EntityModelPartNames.JACKET, ModelPartBuilder.create().uv(16, 32).cuboid(-4.0F, -24.0F, -2.0F, 8.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		
		if(slimArms)
		{
			root.getChild(EntityModelPartNames.RIGHT_ARM).addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-7.0F, -24.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 22.0F, 0.0F));
			root.getChild(EntityModelPartNames.LEFT_ARM).addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(4.0F, -24.0F, -2.0F, 3.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));
		}
		else
		{
			root.getChild(EntityModelPartNames.RIGHT_ARM).addChild("right_sleeve", ModelPartBuilder.create().uv(40, 32).cuboid(-8.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(5.0F, 22.0F, 0.0F));
			root.getChild(EntityModelPartNames.LEFT_ARM).addChild("left_sleeve", ModelPartBuilder.create().uv(48, 48).cuboid(4.0F, -24.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-5.0F, 22.0F, 0.0F));
		}
		
		root.getChild(EntityModelPartNames.RIGHT_LEG).addChild("right_pants", ModelPartBuilder.create().uv(0, 32).cuboid(-3.9F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
		root.getChild(EntityModelPartNames.LEFT_LEG).addChild("left_pants", ModelPartBuilder.create().uv(0, 48).cuboid(-0.1F, -12.0F, -2.0F, 4.0F, 12.0F, 4.0F, dilation.add(0.25F)), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	public void setAngles(E livingEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch)
	{
		super.setAngles(livingEntity, limbSwing, limbSwingAmount, headYaw, ageInTicks, headPitch);
		getPart().traverse().forEach(ModelPart::resetTransform);
		ANIMS.entrySet().forEach(entry -> updateAnimation(livingEntity.animations.get(entry.getKey()), entry.getValue(), ageInTicks));
		hat.copyTransform(head);
	}
}
