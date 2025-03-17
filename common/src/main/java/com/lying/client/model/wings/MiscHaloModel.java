package com.lying.client.model.wings;

import com.lying.client.init.VTAnimations;
import com.lying.client.model.AbstractAccessoryModel;
import com.lying.entity.AccessoryAnimationInterface;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class MiscHaloModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public static final String HALO = "halo";
	
	protected final ModelPart halo;
	
	public MiscHaloModel(ModelPart root)
	{
		super(root);
		this.halo = body.getChild(HALO);
	}
	
	public static TexturedModelData createBodyLayer()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.getChild(EntityModelPartNames.BODY);
		
		ModelPartData halo = body.addChild(HALO, ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -1.0F, 6.0F, 2.0F, 2.0F, new Dilation(-0.5F))
			.uv(0, 4).cuboid(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 0.0F, Dilation.NONE), ModelTransform.of(0.0F, 3.0F, 4.0F, 0.0F, 0.0F, 0.7854F));
			halo.addChild("cube_r1", ModelPartBuilder.create().uv(16, 0).cuboid(-2.0F, -3.0F, -1.0F, 4.0F, 2.0F, 2.0F, new Dilation(-0.5F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));
			halo.addChild("cube_r2", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, -1.0F, 6.0F, 2.0F, 2.0F, new Dilation(-0.5F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));
			halo.addChild("cube_r3", ModelPartBuilder.create().uv(16, 0).cuboid(-2.0F, -3.0F, -1.0F, 4.0F, 2.0F, 2.0F, new Dilation(-0.5F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	public void setAngles(E livingEntity, float limbAngle, float limbDistance, float age, float headYaw, float headPitch)
	{
		halo.traverse().forEach(part -> part.resetTransform());
		
		if(livingEntity instanceof AccessoryAnimationInterface)
			updateAnimation(((AccessoryAnimationInterface)livingEntity).getIdleAnimation(), VTAnimations.AngelWings.MISC_ANGEL_HALO, age);
	}
}
