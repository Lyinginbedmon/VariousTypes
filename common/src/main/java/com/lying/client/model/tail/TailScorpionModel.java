package com.lying.client.model.tail;

import com.lying.client.init.VTAnimations;
import com.lying.entity.AccessoryAnimationInterface;
import com.lying.utility.PlayerPose;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class TailScorpionModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public TailScorpionModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);

		ModelPartData tail = body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-1F, 0F, -1F, 2F, 6F, 2F, new Dilation(0.5F)), ModelTransform.of(0F, 10.5F, 1.75F, 1.5708F, 0F, 0F));
		ModelPartData seg1 = tail.addChild(SEG1, ModelPartBuilder.create().uv(8, 0).cuboid(-1F, 0F, -1F, 2F, 8F, 2F, new Dilation(0.25F)), ModelTransform.pivot(0F, 6F, 0F));
		ModelPartData seg2 = seg1.addChild(SEG2, ModelPartBuilder.create().uv(24, 0).cuboid(-1F, -0.5F, -1F, 2F, 7F, 2F, Dilation.NONE), ModelTransform.pivot(0F, 8F, 0F));
		
		seg2.addChild(SEG3, ModelPartBuilder.create().uv(10, 4).cuboid(-2F, -1.5F, -1.5F, 4F, 3F, 6F, Dilation.NONE)
				.uv(4, 8).cuboid(-0.5F, -3F, 3.5F, 1F, 3F, 2F, Dilation.NONE), ModelTransform.pivot(0F, 7F, 0F));
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
	{
		PlayerPose currentPose = anims.currentlyRunning();
		if(currentPose != null)
		{
			switch(currentPose)
			{
				case PlayerPose.CROUCHING:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.ScorpionTail.TAIL_SCORPION_CROUCHING, age);
					break;
				case PlayerPose.FLYING_IDLE:
				case PlayerPose.FLYING_POWERED:
				case PlayerPose.CRAWLING:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.ScorpionTail.TAIL_SCORPION_FLYING, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.ScorpionTail.TAIL_SCORPION_IDLE, age);
					double lift = Math.toRadians(15D); 
					tail.pitch += Math.min(limbDistance * lift, lift);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.ScorpionTail.TAIL_SCORPION_IDLE, age);
	}
}
