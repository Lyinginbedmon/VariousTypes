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
import net.minecraft.util.math.MathHelper;

public class TailWhaleModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public TailWhaleModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData tail = body.addChild(TAIL, ModelPartBuilder.create().uv(0, 0).cuboid(-2F, 0F, -2F, 4F, 5F, 3F, Dilation.NONE), ModelTransform.of(0F, 10.5F, 1.75F, 1.5708F, 0F, 0F));
		
		ModelPartData seg1 = tail.addChild(SEG1, ModelPartBuilder.create().uv(0, 8).cuboid(-1.5F, 0F, -1F, 3F, 4F, 2F, Dilation.NONE), ModelTransform.pivot(0F, 5F, -0.25F));
		
		seg1.addChild(SEG2, ModelPartBuilder.create().uv(14, 0).cuboid(-1F, -0.25F, -1.25F, 2F, 3F, 2F, new Dilation(-0.1F))
				.uv(12, 8).cuboid(-5F, 0.75F, -0.25F, 10F, 5F, 0F, Dilation.NONE), ModelTransform.pivot(0F, 4F, 0.25F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
	{
		PlayerPose currentPose = anims.currentlyRunning();
		if(currentPose != null)
		{
			switch(currentPose)
			{
				case PlayerPose.FLYING_IDLE:
				case PlayerPose.FLYING_POWERED:
				case PlayerPose.CRAWLING:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WhaleTail.TAIL_WHALE_FLYING, age);
					break;
				case PlayerPose.SITTING:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WhaleTail.TAIL_WHALE_SITTING, age);
					break;
				case PlayerPose.SWIMMING_IDLE:
				case PlayerPose.SWIMMING_POWERED:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WhaleTail.TAIL_WHALE_SWIMMING, age);
					break;
				default:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.WhaleTail.TAIL_WHALE_IDLE, age);
					tail.yaw += MathHelper.cos(limbAngle * 0.6662f) * 0.3f * limbDistance;
					double lift = Math.toRadians(25D); 
					tail.pitch += Math.min(limbDistance * lift, lift);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.WhaleTail.TAIL_WHALE_IDLE, age);
	}
}
