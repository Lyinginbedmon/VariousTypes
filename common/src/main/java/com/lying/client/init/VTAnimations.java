package com.lying.client.init;

import com.lying.client.model.ears.GillsAxolotlModel;
import com.lying.client.model.tail.AbstractTailModel;
import com.lying.client.model.wings.AbstractWingsModel;
import com.lying.client.model.wings.WingsAngelModel;
import com.lying.client.model.wings.WingsBirdModel;
import com.lying.client.model.wings.WingsDragonModel;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModelPartNames;

public class VTAnimations
{
	public static class PlayerAnimations
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
			.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3333F, AnimationHelper.createTranslationalVector(0.0F, 2.16F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5833F, AnimationHelper.createTranslationalVector(0.0F, 3.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
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
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.875F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6667F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
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
		
		public static final Animation PLAYER_FGAME_START = Animation.Builder.create(0.3333F)
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation PLAYER_FGAME_MAIN = Animation.Builder.create(2.25F).looping()
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(1.0084F, 0.044F, -2.4996F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(2.35F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(-64.9472F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.25F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(-64.9472F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-61.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(-54.789F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.25F, -0.75F, 6.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(-103.39F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.25F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(-103.39F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-100.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(-90.41F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(-1.25F, -1.0F, 1.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 4.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 1.5F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.CUBIC)
			))
			.build();
		
		public static final Animation PLAYER_FGAME_END = Animation.Builder.create(0.3333F)
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 35.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 2.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-56.874F, 10.5179F, 6.7929F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.25F, 0.0F, 5.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-95.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(-1.25F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation PLAYER_SIT_START = Animation.Builder.create(0.7917F)
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -3.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(11.32F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(25.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -2.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-65.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation PLAYER_SIT_MAIN = Animation.Builder.create(3.0833F).looping()
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-12.3159F, -2.1539F, -9.7676F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.1667F, AnimationHelper.createRotationalVector(-12.3654F, 1.5951F, 7.2404F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-12.3159F, -2.1539F, -9.7676F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.4167F, AnimationHelper.createRotationalVector(-12.3654F, 1.5951F, 7.2404F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0833F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9167F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2083F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.625F, AnimationHelper.createRotationalVector(-95.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0833F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.8333F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2083F, AnimationHelper.createRotationalVector(-95.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.625F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0833F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation PLAYER_SIT_END = Animation.Builder.create(1.0833F)
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(56.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.HEAD, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -2.0F, -7.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(37.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -1.73F, -6.38F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-12.3964F, 1.6189F, 15.4011F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -0.54F, -6.38F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(17.5F, 0.0F, -8.0769F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_ARM, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 3.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createTranslationalVector(0.0F, -2.54F, -6.38F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.RIGHT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-95.68F, -16.1F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-90.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(EntityModelPartNames.LEFT_LEG, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7083F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.0833F, AnimationHelper.createTranslationalVector(0.0F, -10.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation PLAYER_SNEAK = Animation.Builder.create(2.0833F)
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
			.addBoneAnimation(EntityModelPartNames.BODY, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -4.0F, -8.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -4.0F, -8.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
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
			.addBoneAnimation("root", new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.125F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.9583F, AnimationHelper.createTranslationalVector(0.0F, -1.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.0833F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
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
				new Keyframe(0.2917F, AnimationHelper.createTranslationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0417F, AnimationHelper.createTranslationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
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
	}
	
	protected static class WingAnimations
	{
		protected static final String RIGHT_WING = AbstractWingsModel.RIGHT_WING;
		protected static final String RIGHT_HUMERUS = AbstractWingsModel.RIGHT_HUMERUS;
		protected static final String RIGHT_RADIUS = AbstractWingsModel.RIGHT_RADIUS;
		protected static final String RIGHT_PHALANGES = AbstractWingsModel.RIGHT_PHALANGES;
		protected static final String RIGHT_METATARSAL = AbstractWingsModel.RIGHT_METATARSAL;
		
		protected static final String LEFT_WING = AbstractWingsModel.LEFT_WING;
		protected static final String LEFT_HUMERUS = AbstractWingsModel.LEFT_HUMERUS;
		protected static final String LEFT_RADIUS = AbstractWingsModel.LEFT_RADIUS;
		protected static final String LEFT_PHALANGES = AbstractWingsModel.LEFT_PHALANGES;
		protected static final String LEFT_METATARSAL = AbstractWingsModel.LEFT_METATARSAL;
	}
	
	public static class BirdWings extends WingAnimations
	{
		protected static final String RIGHT_SECONDARIES = WingsBirdModel.RIGHT_SECONDARIES;
		protected static final String RIGHT_TERTIALS = WingsBirdModel.RIGHT_TERTIALS;
		
		protected static final String LEFT_SECONDARIES = WingsBirdModel.LEFT_SECONDARIES;
		protected static final String LEFT_TERTIALS = WingsBirdModel.LEFT_TERTIALS;
		
		public static final Animation WINGS_BIRD_IDLE = Animation.Builder.create(5.0F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.4306F, AnimationHelper.createRotationalVector(125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.875F, AnimationHelper.createRotationalVector(-82.9F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.2639F, AnimationHelper.createRotationalVector(-90.51F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, -67.4279F, -47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.3194F, AnimationHelper.createRotationalVector(-25.221F, -67.4279F, -47.1695F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5417F, AnimationHelper.createRotationalVector(-30.5095F, -67.4279F, -47.1695F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-25.221F, -67.4279F, -47.1695F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, 67.4279F, 47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(-25.221F, 67.4279F, 47.1695F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.1667F, AnimationHelper.createRotationalVector(-30.5095F, 67.4279F, 47.1695F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-25.221F, 67.4279F, 47.1695F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.9861F, AnimationHelper.createRotationalVector(125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.2917F, AnimationHelper.createRotationalVector(-82.9F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.0278F, AnimationHelper.createRotationalVector(-90.51F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.4306F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.9444F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_SECONDARIES, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_SECONDARIES, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation WINGS_BIRD_FLYING_IDLE = Animation.Builder.create(13.0435F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.6159F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createRotationalVector(5.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -90.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createRotationalVector(2.5F, -87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(5.0F, -90.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 90.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createRotationalVector(2.5F, 87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(5.0F, 90.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.6159F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createRotationalVector(5.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.3478F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.6957F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.3478F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.9855F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.971F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.9855F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation WINGS_BIRD_FLYING_POWERED = Animation.Builder.create(0.625F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -22.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, -52.5F, -22.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(143.4893F, -77.5229F, -142.8322F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(42.7195F, -89.3988F, -46.4186F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(12.5F, -52.5F, -22.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, 52.5F, 22.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 100.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(42.7195F, 89.3988F, 46.4186F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(12.5F, 52.5F, 22.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(2.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 22.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5283F, -4.9571F, -0.6543F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.6144F, 9.9136F, 1.3184F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIALS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 0.9F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIALS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 0.9F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation WINGS_BIRD_CROUCH = Animation.Builder.create(10.0F)
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, -67.4279F, -47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-34.6829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5F, AnimationHelper.createRotationalVector(-37.1829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.7917F, AnimationHelper.createRotationalVector(-29.6829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.625F, AnimationHelper.createRotationalVector(-34.6829F, -50.153F, -10.5355F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(145.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.875F, AnimationHelper.createRotationalVector(147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.8333F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.3333F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.4583F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.2917F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.9167F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.25F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.7917F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.8333F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, 67.4279F, 47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-34.6829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.6667F, AnimationHelper.createRotationalVector(-37.1829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.5F, AnimationHelper.createRotationalVector(-29.6829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.625F, AnimationHelper.createRotationalVector(-34.6829F, 50.153F, 10.5355F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(145.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.375F, AnimationHelper.createRotationalVector(147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.75F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.7083F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0417F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.2083F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.4583F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.9583F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.0417F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.6667F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class AngelWings extends BirdWings
	{
		protected static final String HALO = WingsAngelModel.HALO;
		
		public static final Animation WINGS_ANGEL_HALO = Animation.Builder.create(25.0F).looping()
				.addBoneAnimation(HALO, new Transformation(Transformation.Targets.TRANSLATE, 
					new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
					new Keyframe(2.1F, AnimationHelper.createTranslationalVector(0.18F, -0.12F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(4.375F, AnimationHelper.createTranslationalVector(-0.25F, -0.25F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(7.7F, AnimationHelper.createTranslationalVector(-0.8F, 0.03F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(10.8333F, AnimationHelper.createTranslationalVector(-0.25F, 0.25F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(13.9F, AnimationHelper.createTranslationalVector(0.08F, 0.5F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(16.6667F, AnimationHelper.createTranslationalVector(0.25F, -0.25F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(19.2F, AnimationHelper.createTranslationalVector(0.5F, -0.31F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(21.875F, AnimationHelper.createTranslationalVector(-0.25F, -0.25F, 0.0F), Transformation.Interpolations.CUBIC),
					new Keyframe(25.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
				))
				.build();
		
		public static final Animation WINGS_ANGEL_FLYING_IDLE = Animation.Builder.create(13.0435F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.6159F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createRotationalVector(5.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -90.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.7101F, AnimationHelper.createRotationalVector(2.5F, -87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(5.0F, -90.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 90.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createRotationalVector(2.5F, 87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(5.0F, 90.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.6159F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createRotationalVector(5.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(5.2536F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.3478F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.6957F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.3478F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.9855F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.971F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(13.0435F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.9855F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(13.0435F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.5F, 1.5F, 1.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.5F, 1.5F, 1.5F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation WINGS_ANGEL_FLYING_POWERED = Animation.Builder.create(0.625F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, -22.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, -52.5F, -22.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(143.4893F, -77.5229F, -142.8322F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(42.7195F, -89.3988F, -46.4186F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(12.5F, -52.5F, -22.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(-2.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(12.5F, 52.5F, 22.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 100.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(42.7195F, 89.3988F, 46.4186F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(12.5F, 52.5F, 22.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(2.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(32.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 22.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5283F, -4.9571F, -0.6543F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.6144F, 9.9136F, 1.3184F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIALS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 0.9F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIALS, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.0F, 0.9F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.5F, 1.5F, 1.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.5F, 1.5F, 1.5F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation WINGS_ANGEL_CROUCH = Animation.Builder.create(10.0F)
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, -67.4279F, -47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-34.6829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5F, AnimationHelper.createRotationalVector(-37.1829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.7917F, AnimationHelper.createRotationalVector(-29.6829F, -50.153F, -10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.625F, AnimationHelper.createRotationalVector(-34.6829F, -50.153F, -10.5355F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(145.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.875F, AnimationHelper.createRotationalVector(147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.8333F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.3333F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.4583F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.2917F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.9167F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.25F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.7917F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.8333F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-25.221F, 67.4279F, 47.1695F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-34.6829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.6667F, AnimationHelper.createRotationalVector(-37.1829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.5F, AnimationHelper.createRotationalVector(-29.6829F, 50.153F, 10.5355F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.625F, AnimationHelper.createRotationalVector(-34.6829F, 50.153F, 10.5355F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(145.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.375F, AnimationHelper.createRotationalVector(147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.75F, AnimationHelper.createRotationalVector(150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.5F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-90.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.7083F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.0417F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.2083F, AnimationHelper.createRotationalVector(-47.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-52.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_METATARSAL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.4583F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.9583F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.0417F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.6667F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.2F, 1.2F, 1.2F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.2F, 1.2F, 1.2F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class BatWings extends WingAnimations
	{
		public static final Animation WINGS_BAT_IDLE = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, -52.5F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9167F, AnimationHelper.createRotationalVector(0.0F, -47.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.9583F, AnimationHelper.createRotationalVector(-5.0F, -52.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-5.0F, -52.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.75F, AnimationHelper.createRotationalVector(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-72.4843F, -2.3842F, -0.7522F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-62.4776F, -2.2174F, -1.1549F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-72.4843F, -2.3842F, -0.7522F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.25F, -0.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 52.5F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.625F, AnimationHelper.createRotationalVector(-5.0F, 52.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5833F, AnimationHelper.createRotationalVector(0.0F, 47.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-5.0F, 52.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-35.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5417F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-37.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-72.4843F, 2.3842F, 0.7522F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.5F, AnimationHelper.createRotationalVector(-64.9791F, 2.2656F, 1.0571F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-72.4843F, 2.3842F, 0.7522F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.25F, -0.25F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation WINGS_BAT_FLYING_IDLE = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -85.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.7083F, AnimationHelper.createRotationalVector(4.5131F, -77.5115F, 3.0156F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(5.0F, -85.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.3333F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 85.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9583F, AnimationHelper.createRotationalVector(4.5131F, 77.5115F, -3.0156F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(5.0F, 85.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-17.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4167F, AnimationHelper.createRotationalVector(-27.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-22.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.25F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation WINGS_BAT_FLYING_POWERED = Animation.Builder.create(0.2917F).looping()
			.addBoneAnimation(RIGHT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, -42.517F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(0.0F, -87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(5.0F, -42.517F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-14.7024F, 18.1581F, 1.171F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-8.6439F, -29.7174F, 4.3096F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-14.7024F, 18.1581F, 1.171F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-32.2904F, 21.3345F, -0.6535F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-11.8087F, -31.9472F, 6.3127F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-32.2904F, 21.3345F, -0.6535F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_WING, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 42.517F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(0.0F, 87.5F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(5.0F, 42.517F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-14.7024F, -18.1581F, -1.171F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-8.6439F, 29.7174F, -4.3096F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-14.7024F, -18.1581F, -1.171F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_PHALANGES, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-32.2904F, -21.3345F, 0.6535F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.1667F, AnimationHelper.createRotationalVector(-11.8087F, 31.9472F, -6.3127F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-32.2904F, -21.3345F, 0.6535F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class DragonWings extends WingAnimations
	{
		protected static final String RIGHT_CLAW = WingsDragonModel.RIGHT_CLAW;
		protected static final String RIGHT_TERTIAL = WingsDragonModel.RIGHT_TERTIAL;
		protected static final String RIGHT_HAND = WingsDragonModel.RIGHT_HAND;
		protected static final String RIGHT_DIGIT_1 = WingsDragonModel.RIGHT_DIGIT_1;
		protected static final String RIGHT_DIGIT_2 = WingsDragonModel.RIGHT_DIGIT_2;
		protected static final String RIGHT_DIGIT_3 = WingsDragonModel.RIGHT_DIGIT_3;
		protected static final String RIGHT_DIGIT_4 = WingsDragonModel.RIGHT_DIGIT_4;
		protected static final String RIGHT_DIGIT_1_SMALL = WingsDragonModel.RIGHT_DIGIT_1_SMALL;
		protected static final String RIGHT_DIGIT_1_LARGE = WingsDragonModel.RIGHT_DIGIT_1_LARGE;
		protected static final String RIGHT_DIGIT_2_SMALL = WingsDragonModel.RIGHT_DIGIT_2_SMALL;
		protected static final String RIGHT_DIGIT_2_LARGE = WingsDragonModel.RIGHT_DIGIT_2_LARGE;
		protected static final String RIGHT_DIGIT_3_SMALL = WingsDragonModel.RIGHT_DIGIT_3_SMALL;
		protected static final String RIGHT_DIGIT_3_LARGE = WingsDragonModel.RIGHT_DIGIT_3_LARGE;
		protected static final String RIGHT_DIGIT_4_SMALL = WingsDragonModel.RIGHT_DIGIT_4_SMALL;
		protected static final String RIGHT_DIGIT_4_LARGE = WingsDragonModel.RIGHT_DIGIT_4_LARGE;
		
		protected static final String LEFT_CLAW = WingsDragonModel.LEFT_CLAW;
		protected static final String LEFT_TERTIAL = WingsDragonModel.LEFT_TERTIAL;
		protected static final String LEFT_HAND = WingsDragonModel.LEFT_HAND;
		protected static final String LEFT_DIGIT_1 = WingsDragonModel.LEFT_DIGIT_1;
		protected static final String LEFT_DIGIT_2 = WingsDragonModel.LEFT_DIGIT_2;
		protected static final String LEFT_DIGIT_3 = WingsDragonModel.LEFT_DIGIT_3;
		protected static final String LEFT_DIGIT_4 = WingsDragonModel.LEFT_DIGIT_4;
		protected static final String LEFT_DIGIT_1_SMALL = WingsDragonModel.LEFT_DIGIT_1_SMALL;
		protected static final String LEFT_DIGIT_1_LARGE = WingsDragonModel.LEFT_DIGIT_1_LARGE;
		protected static final String LEFT_DIGIT_2_SMALL = WingsDragonModel.LEFT_DIGIT_2_SMALL;
		protected static final String LEFT_DIGIT_2_LARGE = WingsDragonModel.LEFT_DIGIT_2_LARGE;
		protected static final String LEFT_DIGIT_3_SMALL = WingsDragonModel.LEFT_DIGIT_3_SMALL;
		protected static final String LEFT_DIGIT_3_LARGE = WingsDragonModel.LEFT_DIGIT_3_LARGE;
		protected static final String LEFT_DIGIT_4_SMALL = WingsDragonModel.LEFT_DIGIT_4_SMALL;
		protected static final String LEFT_DIGIT_4_LARGE = WingsDragonModel.LEFT_DIGIT_4_LARGE;
		
		public static final Animation WINGS_DRAGON_IDLE = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0417F, AnimationHelper.createRotationalVector(135.1092F, -3.5333F, -3.54F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.875F, AnimationHelper.createRotationalVector(135.0273F, 1.7675F, 1.7683F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-73.1676F, -20.5581F, -1.1832F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.0833F, AnimationHelper.createRotationalVector(-69.5246F, -27.6116F, -3.9783F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(-71.8654F, -19.5715F, -0.966F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-73.1676F, -20.5581F, -1.1832F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.0F, AnimationHelper.createRotationalVector(-147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-107.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-97.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.8333F, AnimationHelper.createRotationalVector(-85.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-97.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2083F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(-115.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.125F, AnimationHelper.createRotationalVector(-105.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 2.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-73.1676F, 20.5581F, 1.1832F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(-69.5246F, 27.6116F, 3.9783F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-70.9566F, 18.197F, 0.3122F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-73.1676F, 20.5581F, 1.1832F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 2.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.9167F, AnimationHelper.createRotationalVector(135.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.9583F, AnimationHelper.createRotationalVector(-115.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(-115.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.5F, AnimationHelper.createRotationalVector(-147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.375F, AnimationHelper.createRotationalVector(0.0F, 5.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6667F, AnimationHelper.createRotationalVector(-125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2083F, AnimationHelper.createRotationalVector(-107.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-97.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.7083F, AnimationHelper.createRotationalVector(-85.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-97.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation WINGS_DRAGON_CROUCH = Animation.Builder.create(10.0F)
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(135.1092F, -3.5333F, -3.54F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.125F, AnimationHelper.createRotationalVector(127.6092F, -3.5333F, -3.54F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.6667F, AnimationHelper.createRotationalVector(135.1092F, -3.5333F, -3.54F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-73.1676F, -20.5581F, -1.1832F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-69.5246F, -27.6116F, -3.9783F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.4167F, AnimationHelper.createRotationalVector(-65.992F, -32.2804F, -6.0449F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.9583F, AnimationHelper.createRotationalVector(-70.3349F, -22.9148F, -2.0815F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.75F, AnimationHelper.createRotationalVector(-69.5246F, -27.6116F, -3.9783F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9167F, AnimationHelper.createRotationalVector(-146.6F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.9167F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.75F, AnimationHelper.createRotationalVector(-145.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_DIGIT_1_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4167F, AnimationHelper.createRotationalVector(-125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.4583F, AnimationHelper.createRotationalVector(-129.66F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.3333F, AnimationHelper.createRotationalVector(-127.61F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.7917F, AnimationHelper.createRotationalVector(-130.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_DIGIT_2_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-107.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.625F, AnimationHelper.createRotationalVector(-105.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.3333F, AnimationHelper.createRotationalVector(-102.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.2083F, AnimationHelper.createRotationalVector(-110.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_DIGIT_3_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-54.12F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.8333F, AnimationHelper.createRotationalVector(-54.04F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5833F, AnimationHelper.createRotationalVector(-45.75F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.125F, AnimationHelper.createRotationalVector(-57.84F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-59.12F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 10.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(2.5F, -5.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.7083F, AnimationHelper.createRotationalVector(5.3324F, -1.6034F, 5.0442F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.9583F, AnimationHelper.createRotationalVector(0.8637F, -4.9274F, 5.072F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.2917F, AnimationHelper.createRotationalVector(2.9944F, -5.1283F, 6.5168F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.1667F, AnimationHelper.createRotationalVector(0.6475F, -4.9604F, 7.5811F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-90.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-110.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.625F, AnimationHelper.createRotationalVector(-105.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.875F, AnimationHelper.createRotationalVector(-110.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 2.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-73.1676F, 20.5581F, 1.1832F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-69.5246F, 27.6116F, 3.9783F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5417F, AnimationHelper.createRotationalVector(-68.492F, 32.2804F, 6.0449F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.875F, AnimationHelper.createRotationalVector(-64.5246F, 27.6116F, 3.9783F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-69.0387F, 29.95F, 4.9873F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 2.25F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(135.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.1667F, AnimationHelper.createRotationalVector(127.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.5417F, AnimationHelper.createRotationalVector(132.03F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(124.53F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-102.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3333F, AnimationHelper.createRotationalVector(-95.47F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9583F, AnimationHelper.createRotationalVector(-107.97F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.1667F, AnimationHelper.createRotationalVector(-102.97F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.7083F, AnimationHelper.createRotationalVector(-97.97F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5417F, AnimationHelper.createRotationalVector(-147.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.4583F, AnimationHelper.createRotationalVector(-148.28F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.6667F, AnimationHelper.createRotationalVector(-146.67F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-150.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_DIGIT_1_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -10.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.375F, AnimationHelper.createRotationalVector(2.5F, 5.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0833F, AnimationHelper.createRotationalVector(5.0F, 5.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.7083F, AnimationHelper.createRotationalVector(-0.2187F, 4.9952F, -17.5095F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.5833F, AnimationHelper.createRotationalVector(0.6475F, 4.9604F, -7.5811F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-132.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-125.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.1667F, AnimationHelper.createRotationalVector(-129.22F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.5417F, AnimationHelper.createRotationalVector(-130.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_DIGIT_2_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.2917F, AnimationHelper.createRotationalVector(-107.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.7917F, AnimationHelper.createRotationalVector(-107.25F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.4583F, AnimationHelper.createRotationalVector(-103.59F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(9.2917F, AnimationHelper.createRotationalVector(-110.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(LEFT_DIGIT_3_LARGE, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.0833F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(5.875F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(8.875F, AnimationHelper.createRotationalVector(-50.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(RIGHT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation WINGS_DRAGON_FLYING_IDLE = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(35.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.0623F, -77.7286F, -22.1635F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(-2.9377F, -77.7286F, -22.1635F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(2.0623F, -77.7286F, -22.1635F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.125F, AnimationHelper.createRotationalVector(-62.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2917F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-8.67F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.1F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.1F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(2.0623F, 77.7286F, 22.1635F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(-2.9377F, 77.7286F, 22.1635F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(2.0623F, 77.7286F, 22.1635F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.1F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.1F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(35.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.6667F, AnimationHelper.createRotationalVector(-8.67F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4583F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.125F, AnimationHelper.createRotationalVector(-62.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-67.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2917F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.7917F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(27.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation WINGS_DRAGON_FLYING_POWERED = Animation.Builder.create(0.7083F).looping()
			.addBoneAnimation(RIGHT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(26.2886F, 31.0422F, -0.076F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(31.2266F, -20.6653F, -9.4352F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(26.2886F, 31.0422F, -0.076F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(19.7287F, -68.694F, -21.053F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-135.1092F, -82.9334F, 134.8908F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(19.7287F, -68.694F, -21.053F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-120.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-120.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_1_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-82.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-62.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-82.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_2_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_3_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-1.1499F, 19.9502F, -5.2745F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-3.1166F, -34.8025F, 8.8324F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-1.1499F, 19.9502F, -5.2745F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-1.1499F, 19.9502F, -5.2745F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_TERTIAL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.6F, 1.1F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7083F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(19.7287F, 68.694F, 21.053F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-135.1092F, 82.9334F, -134.8908F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(19.7287F, 68.694F, 21.053F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HUMERUS, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.5F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_TERTIAL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createScalingVector(1.0F, 1.6F, 1.1F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.7083F, AnimationHelper.createScalingVector(1.0F, 1.0F, 1.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_RADIUS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(26.2886F, -31.0422F, 0.076F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(31.2266F, 20.6653F, 9.4352F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(26.2886F, -31.0422F, 0.076F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_CLAW, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(7.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-57.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-120.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-115.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-120.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_1_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_HAND, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-1.1499F, -19.9502F, 5.2745F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-3.1166F, 34.8025F, -8.8324F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-1.1499F, -19.9502F, 5.2745F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7083F, AnimationHelper.createRotationalVector(-1.1499F, -19.9502F, 5.2745F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-82.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-62.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-82.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_2_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(-2.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_3_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4583F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_DIGIT_4_SMALL, new Transformation(Transformation.Targets.SCALE, 
				new Keyframe(0.0F, AnimationHelper.createScalingVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class AxolotlGills
	{
		protected static final String LEFT_GILLS = GillsAxolotlModel.LEFT_GILLS;
		protected static final String RIGHT_GILLS = GillsAxolotlModel.RIGHT_GILLS;
		protected static final String TOP_GILLS = GillsAxolotlModel.TOP_GILLS;
		
		public static final Animation GILLS_GROUND_STANDING = Animation.Builder.create(0.0F)
				.addBoneAnimation(RIGHT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 60.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -60.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(TOP_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(60.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation GILLS_GROUND_MOVING = Animation.Builder.create(0.0F)
			.addBoneAnimation(RIGHT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(TOP_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation GILLS_WATER_STANDING = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.3333F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.8333F, AnimationHelper.createRotationalVector(0.0F, 5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.1667F, AnimationHelper.createRotationalVector(0.0F, -5.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(TOP_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.4167F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation GILLS_WATER_MOVING = Animation.Builder.create(3.0F).looping()
			.addBoneAnimation(RIGHT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(0.0F, -25.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.625F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.3333F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, -20.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.8333F, AnimationHelper.createRotationalVector(0.0F, 25.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.1667F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(0.0F, 20.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(TOP_GILLS, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5833F, AnimationHelper.createRotationalVector(-25.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5833F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.4167F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0F, AnimationHelper.createRotationalVector(-20.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	protected static class TailAnimations
	{
		protected static final String TAIL = AbstractTailModel.TAIL;
		protected static final String SEG1 = AbstractTailModel.SEG1;
		protected static final String SEG2 = AbstractTailModel.SEG2;
		protected static final String SEG3 = AbstractTailModel.SEG3;
	}
	
	public static class DragonTail extends TailAnimations
	{
		public static final Animation TAIL_DRAGON_IDLE = Animation.Builder.create(6.122F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.9558F, AnimationHelper.createRotationalVector(-55.1026F, 2.8654F, -4.0992F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2517F, AnimationHelper.createRotationalVector(-55.231F, -4.2936F, 6.1552F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.1224F, AnimationHelper.createRotationalVector(-55.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6156F, AnimationHelper.createRotationalVector(11.4285F, 2.5531F, -11.4129F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.7619F, AnimationHelper.createRotationalVector(12.6626F, -1.0581F, 5.1118F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.1224F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.381F, AnimationHelper.createRotationalVector(15.9591F, 2.0731F, -7.2094F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.8265F, AnimationHelper.createRotationalVector(16.1459F, -3.0683F, 10.4971F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.1224F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
		
		public static final Animation TAIL_DRAGON_FLYING = Animation.Builder.create(3.0612F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.8503F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2109F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0612F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.8503F, AnimationHelper.createRotationalVector(5.6087F, 0.4899F, -4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2109F, AnimationHelper.createRotationalVector(5.6087F, -0.4899F, 4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0612F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.8503F, AnimationHelper.createRotationalVector(5.6087F, 0.4899F, -4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2109F, AnimationHelper.createRotationalVector(5.6087F, -0.4899F, 4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0612F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
		
		public static final Animation TAIL_DRAGON_SITTING = Animation.Builder.create(6.0374F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.2755F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9762F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.7619F, AnimationHelper.createRotationalVector(-12.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.0374F, AnimationHelper.createRotationalVector(-5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5306F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9762F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.5068F, AnimationHelper.createRotationalVector(10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.0374F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.9558F, AnimationHelper.createRotationalVector(9.9162F, 1.2988F, -7.3873F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.1667F, AnimationHelper.createRotationalVector(9.9162F, -1.2988F, 7.3873F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.0374F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
	}
	
	public static class KirinTail extends TailAnimations
	{
		public static final Animation TAIL_KIRIN_IDLE = Animation.Builder.create(4.3342F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-65.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3245F, AnimationHelper.createRotationalVector(-62.6026F, 2.8654F, -4.0992F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.0102F, AnimationHelper.createRotationalVector(-62.6026F, -2.8654F, 4.0992F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.3347F, AnimationHelper.createRotationalVector(-65.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, 0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.5051F, AnimationHelper.createRotationalVector(11.1561F, 2.9766F, -13.7632F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.1908F, AnimationHelper.createRotationalVector(11.1561F, -2.9766F, 13.7632F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.3347F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6857F, AnimationHelper.createRotationalVector(15.5685F, 4.1133F, -14.4375F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.3715F, AnimationHelper.createRotationalVector(15.5685F, -4.1133F, 14.4375F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.3347F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.8663F, AnimationHelper.createRotationalVector(15.3811F, 4.7805F, -16.8541F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5521F, AnimationHelper.createRotationalVector(15.3811F, -4.7805F, 16.8541F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.3347F, AnimationHelper.createRotationalVector(15.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();

		public static final Animation TAIL_KIRIN_FLYING = Animation.Builder.create(1.6316F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.3625F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.9968F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 0.0F, -0.5F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.4984F, AnimationHelper.createRotationalVector(5.6087F, 0.4899F, -4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.1328F, AnimationHelper.createRotationalVector(5.6087F, -0.4899F, 4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.589F, AnimationHelper.createRotationalVector(5.6087F, 0.4899F, -4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.2234F, AnimationHelper.createRotationalVector(5.6087F, -0.4899F, 4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.6797F, AnimationHelper.createRotationalVector(5.6087F, 0.4899F, -4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.314F, AnimationHelper.createRotationalVector(5.6087F, -0.4899F, 4.976F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(5.63F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
		
		public static final Animation TAIL_KIRIN_SITTING = Animation.Builder.create(4.2517F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3174F, AnimationHelper.createRotationalVector(-9.8901F, 12.4314F, 13.0713F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5749F, AnimationHelper.createRotationalVector(-9.8901F, -12.4314F, -13.0713F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(-10.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6168F, AnimationHelper.createRotationalVector(22.9264F, 14.6364F, 3.3191F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.8743F, AnimationHelper.createRotationalVector(22.9264F, -14.6364F, -3.3191F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(22.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.9162F, AnimationHelper.createRotationalVector(20.2936F, 12.1991F, 2.7471F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.1737F, AnimationHelper.createRotationalVector(20.2936F, -12.1991F, -2.7471F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(20.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.addBoneAnimation(SEG3, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.2156F, AnimationHelper.createRotationalVector(35.3813F, 8.6492F, 5.0384F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.4731F, AnimationHelper.createRotationalVector(35.3813F, -8.6492F, -5.0384F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(30.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC)
			))
			.build();
	}
	
	public static class FoxTail extends TailAnimations
	{
		public static final Animation TAIL_FOX_IDLE = Animation.Builder.create(4.997F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6857F, AnimationHelper.createRotationalVector(-15.1235F, -7.243F, 1.9516F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.7929F, AnimationHelper.createRotationalVector(-15.1235F, 7.243F, -1.9516F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.997F, AnimationHelper.createRotationalVector(-15.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation TAIL_FOX_FLYING = Animation.Builder.create(1.6316F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.4984F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.2234F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation TAIL_FOX_SITTING = Animation.Builder.create(4.2517F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6168F, AnimationHelper.createRotationalVector(5.019F, 4.9809F, 0.4369F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.2934F, AnimationHelper.createRotationalVector(5.019F, -4.9809F, -0.4369F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class AxolotlTail extends TailAnimations
	{
		public static final Animation TAIL_AXOLOTL_IDLE = Animation.Builder.create(4.997F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2041F, AnimationHelper.createRotationalVector(-45.1235F, -7.243F, 1.9516F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.3113F, AnimationHelper.createRotationalVector(-45.1235F, 7.243F, -1.9516F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.997F, AnimationHelper.createRotationalVector(-45.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4449F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 4.45F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.5521F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -4.45F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.997F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6857F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 4.45F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.7929F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -4.45F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.997F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation TAIL_AXOLOTL_FLYING = Animation.Builder.create(1.6316F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.3625F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.9968F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5437F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.2234F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.6797F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.3593F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.6312F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation TAIL_AXOLOTL_SWIMMING_POWERED = Animation.Builder.create(1.0154F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.5F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0F, AnimationHelper.createRotationalVector(-75.0F, 0.0F, -15.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.22F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.6667F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.22F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.51F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(0.7917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 15.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(1.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.51F), Transformation.Interpolations.LINEAR)
			))
			.build();

		public static final Animation TAIL_AXOLOTL_SITTING = Animation.Builder.create(4.2517F).looping()
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.2575F, AnimationHelper.createRotationalVector(5.019F, 4.9809F, 0.4369F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9341F, AnimationHelper.createRotationalVector(5.019F, -4.9809F, -0.4369F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(5.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(TAIL, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, 1.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG1, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.4371F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.1138F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 5.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(SEG2, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.6168F, AnimationHelper.createRotationalVector(0.0F, 0.0F, -7.22F), Transformation.Interpolations.CUBIC),
				new Keyframe(3.2934F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 7.22F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.2515F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
	
	public static class RabbitEars
	{
		protected static final String RIGHT_EAR = EntityModelPartNames.RIGHT_EAR;
		protected static final String LEFT_EAR = EntityModelPartNames.LEFT_EAR;
		
		public static final Animation EARS_RABBIT_IDLE = Animation.Builder.create(0.0F)
			.addBoneAnimation(RIGHT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 15.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, -15.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation EARS_RABBIT_CROUCHING = Animation.Builder.create(10.0F)
			.addBoneAnimation(RIGHT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-59.7864F, 6.4905F, 3.7661F), Transformation.Interpolations.CUBIC),
				new Keyframe(7.5F, AnimationHelper.createRotationalVector(-64.7864F, -6.4905F, -3.7661F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_EAR, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(3.5417F, AnimationHelper.createRotationalVector(-54.7864F, -6.4905F, -3.7661F), Transformation.Interpolations.CUBIC),
				new Keyframe(6.75F, AnimationHelper.createRotationalVector(-59.7864F, 6.4905F, 3.7661F), Transformation.Interpolations.CUBIC),
				new Keyframe(10.0F, AnimationHelper.createRotationalVector(-60.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_EAR, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
		
		public static final Animation EARS_RABBIT_FLYING = Animation.Builder.create(4.0984F).looping()
			.addBoneAnimation(RIGHT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-77.4885F, -2.4407F, -0.5414F), Transformation.Interpolations.LINEAR),
				new Keyframe(0.875F, AnimationHelper.createRotationalVector(-80.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.9583F, AnimationHelper.createRotationalVector(-72.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.0833F, AnimationHelper.createRotationalVector(-77.4885F, -2.4407F, -0.5414F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(RIGHT_EAR, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.0833F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_EAR, new Transformation(Transformation.Targets.ROTATE, 
				new Keyframe(0.0F, AnimationHelper.createRotationalVector(-77.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(1.375F, AnimationHelper.createRotationalVector(-65.0F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(2.5833F, AnimationHelper.createRotationalVector(-72.5F, 0.0F, 0.0F), Transformation.Interpolations.CUBIC),
				new Keyframe(4.0833F, AnimationHelper.createRotationalVector(-77.5F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.addBoneAnimation(LEFT_EAR, new Transformation(Transformation.Targets.TRANSLATE, 
				new Keyframe(0.0F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR),
				new Keyframe(4.0833F, AnimationHelper.createTranslationalVector(0.0F, -0.25F, 0.0F), Transformation.Interpolations.LINEAR)
			))
			.build();
	}
}
