package com.lying.client.renderer;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class VTAnimations
{
	public static final Animation PLAYER_IDLE = Animation.Builder.create(2.0F).looping()
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	
	public static final Animation PLAYER_TPOSE = Animation.Builder.create(3.0F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 47.5F, 90.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(0.0F, 47.5F, 90.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(-2.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.375F, AnimationHelper.createTranslationalVector(-2.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, -47.5F, -90.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.2917F, AnimationHelper.createRotationalVector(0.0F, -47.5F, -90.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createTranslationalVector(2.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.2917F, AnimationHelper.createTranslationalVector(2.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 2.16F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 2.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, -2.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.25F, AnimationHelper.createRotationalVector(5.0F, 0.0F, -2.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
	
	public static final Animation PLAYER_WALK = Animation.Builder.create(3.0F)
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.625F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-5.0F, 10.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(-1.7324F, -11.5933F, 0.4491F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_LOOK_AROUND = Animation.Builder.create(2.0F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 31.67F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-5.0F, 37.66F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, -32.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(0.0F, -32.5F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(7.5F, 10.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(7.5F, 10.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-10.0F, -7.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-10.0F, -7.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(27.4071F, 2.1329F, 6.7386F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(27.4071F, 2.1329F, 6.7386F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-27.4071F, -2.1329F, 6.7386F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-27.4071F, -2.1329F, 6.7386F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(-0.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-12.6044F, 7.3212F, -1.6322F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-12.6044F, 7.3212F, -1.6322F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(5.6352F, -8.4257F, -2.1159F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(5.6352F, -8.4257F, -2.1159F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, -2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(-0.25F, 0.0F, -2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(-0.14F, 0.0F, 2.4F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(-0.14F, 0.0F, 2.4F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.25F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
	
	public static final Animation PLAYER_FGAME = Animation.Builder.create(3.0F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-64.9472F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-64.9472F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-103.39F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-103.39F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.3333F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.375F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
	
	public static final Animation PLAYER_SIT = Animation.Builder.create(5.0F)
		.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.8333F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-12.3159F, -2.1539F, -9.7676F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9167F, AnimationHelper.createRotationalVector(-12.3654F, 1.5951F, 7.2404F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.625F, AnimationHelper.createRotationalVector(-12.3159F, -2.1539F, -9.7676F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(-12.3654F, 1.5951F, 7.2404F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createRotationalVector(56.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createTranslationalVector(0.0F, -2.0F, -7.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(11.32F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createRotationalVector(37.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.0F, 1.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createRotationalVector(-12.3964F, 1.6189F, 15.4011F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createTranslationalVector(0.0F, -0.54F, -6.38F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, -15.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createRotationalVector(17.5F, 0.0F, -8.0769F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.75F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.25F, AnimationHelper.createTranslationalVector(0.0F, -2.54F, -6.38F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.625F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0417F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4583F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.75F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.3333F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-65.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.625F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.0417F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4583F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.9167F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.4583F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.8333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(4.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_WOLOLO = Animation.Builder.create(2.9167F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createRotationalVector(-7.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, -17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.75F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.375F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(-195.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createRotationalVector(-165.0F, 0.0F, 17.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.4167F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.75F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_PDANCE = Animation.Builder.create(3.5417F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(-1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4167F, AnimationHelper.createTranslationalVector(-1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9167F, AnimationHelper.createTranslationalVector(1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4167F, AnimationHelper.createTranslationalVector(-1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(1.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2083F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.6667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.1667F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(-1.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.2083F, AnimationHelper.createTranslationalVector(-1.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -52.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.7083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.9583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -72.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -57.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(1.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.2083F, AnimationHelper.createTranslationalVector(1.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_SNEAK = Animation.Builder.create(2.0833F)
		.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0F, AnimationHelper.createRotationalVector(-22.7642F, -20.9043F, 8.5154F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-22.7642F, -20.9043F, 8.5154F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.5417F, AnimationHelper.createRotationalVector(-4.6216F, 16.2825F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.75F, AnimationHelper.createRotationalVector(-4.6216F, 16.2825F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -4.0F, -9.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -4.0F, -9.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -3.0F, -7.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -3.0F, -7.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2083F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createRotationalVector(45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -3.0F, -7.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -3.0F, -7.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_SWAY = Animation.Builder.create(3.25F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, -30.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.9583F, AnimationHelper.createRotationalVector(0.0F, -30.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.125F, AnimationHelper.createRotationalVector(0.0F, 32.77F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.625F, AnimationHelper.createRotationalVector(0.0F, 32.77F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.875F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(-15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.875F, AnimationHelper.createRotationalVector(15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(-15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createRotationalVector(15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.875F, AnimationHelper.createRotationalVector(-15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createRotationalVector(15.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createRotationalVector(-15.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.375F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(2.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 1.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(3.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();

	public static final Animation PLAYER_WAVE = Animation.Builder.create(1.5F)
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -17.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -17.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-3.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -12.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -12.5F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 12.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 12.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-2.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-2.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, -90.0F, -167.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.5417F, AnimationHelper.createRotationalVector(0.0F, -90.0F, -140.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, -90.0F, -167.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, -90.0F, -107.5F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-2.25F, 1.25F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-2.25F, 1.25F, -1.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
			new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -25.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.0417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -25.0F), Transformation.Interpolations.CUBIC),
			new Keyframe(1.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
			new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR),
			new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
		))
		.build();
}
