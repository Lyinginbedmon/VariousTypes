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

public class TailAxolotlModel<E extends LivingEntity> extends AbstractTailModel<E>
{
	public TailAxolotlModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData tail = body.addChild(TAIL, ModelPartBuilder.create().uv(0, -5).cuboid(0.0F, -1.75F, -2.5F, 0.0F, 6.0F, 5.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 10.5F, 1.75F, 1.5708F, 0.0F, 0.0F));
		ModelPartData seg1 = tail.addChild(SEG1, ModelPartBuilder.create().uv(0, 1).cuboid(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.25F, 0.0F));
		seg1.addChild(SEG2, ModelPartBuilder.create().uv(0, 5).cuboid(0.0F, 0.0F, -2.5F, 0.0F, 4.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 4.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	protected void animateTail(AccessoryAnimationInterface anims, boolean isMoving, float limbAngle, float limbDistance, float age)
	{
		PlayerPose currentPose = anims.currentlyRunning();
		if(currentPose != null)
		{
			switch(currentPose)
			{
				case PlayerPose.SWIMMING_IDLE:
				case PlayerPose.SWIMMING_POWERED:
					if(isMoving)
						updateAnimation(anims.getAnimation(currentPose), VTAnimations.AxolotlTail.TAIL_AXOLOTL_SWIMMING_POWERED, age);
					else
						updateAnimation(anims.getAnimation(currentPose), VTAnimations.AxolotlTail.TAIL_AXOLOTL_FLYING, age);
					break;
				case PlayerPose.FLYING_IDLE:
				case PlayerPose.FLYING_POWERED:
				case PlayerPose.CRAWLING:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.AxolotlTail.TAIL_AXOLOTL_FLYING, age);
					break;
				case PlayerPose.SITTING:
					updateAnimation(anims.getIdleAnimation(), VTAnimations.AxolotlTail.TAIL_AXOLOTL_SITTING, age);
					break;
				default:
					updateAnimation(anims.getAnimation(currentPose), VTAnimations.AxolotlTail.TAIL_AXOLOTL_IDLE, age);
					double lift = Math.toRadians(55D);
					tail.pitch += Math.min(limbDistance * lift, lift);
					break;
			}
		}
		else
			updateAnimation(anims.getIdleAnimation(), VTAnimations.FoxTail.TAIL_FOX_IDLE, age);
	}
}
