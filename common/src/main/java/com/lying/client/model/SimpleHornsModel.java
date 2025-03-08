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

public class SimpleHornsModel<E extends LivingEntity> extends AbstractAccessoryModel<E>
{
	public static final String LEFT_HORN = EntityModelPartNames.LEFT_HORN;
	public static final String RIGHT_HORN = EntityModelPartNames.RIGHT_HORN;
	
	public SimpleHornsModel(ModelPart part)
	{
		super(part);
	}
	
	public static TexturedModelData createSaigaHorns()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		// New model, 16x16, made in 2025 using BlockBench
		ModelPartData rightHorn = head.addChild(RIGHT_HORN, ModelPartBuilder.create(), ModelTransform.of(3.5F, -6.5F, -0.75F, -0.974F, 0.0F, 0.3295F));
			rightHorn.addChild("cube_r1", ModelPartBuilder.create().uv(4, 9).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.of(0.0166F, -7.0168F, 0.1794F, -0.1745F, 0.1745F, 0.0F));
			rightHorn.addChild("cube_r2", ModelPartBuilder.create().uv(4, 5).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(0.0166F, -3.5168F, -0.0706F, -0.0873F, 0.1745F, 0.0F));
			rightHorn.addChild("cube_r3", ModelPartBuilder.create().uv(4, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(0.0166F, -1.7668F, -0.0706F, 0.0F, 0.1745F, 0.0F));
		
		ModelPartData leftHorn = head.addChild(LEFT_HORN, ModelPartBuilder.create(), ModelTransform.of(-3.5F, -6.5F, -0.75F, -0.974F, 0.0F, -0.3295F));
			leftHorn.addChild("cube_r4", ModelPartBuilder.create().uv(0, 9).cuboid(-0.5F, -1.5F, -0.5F, 1.0F, 2.0F, 1.0F, Dilation.NONE), ModelTransform.of(-0.0166F, -7.0168F, 0.1794F, -0.1745F, -0.1745F, 0.0F));
			leftHorn.addChild("cube_r5", ModelPartBuilder.create().uv(0, 5).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, new Dilation(0.2F)), ModelTransform.of(-0.0166F, -3.5168F, -0.0706F, -0.0873F, -0.1745F, 0.0F));
			leftHorn.addChild("cube_r6", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 4.0F, 1.0F, new Dilation(0.3F)), ModelTransform.of(-0.0166F, -1.7668F, -0.0706F, 0.0F, -0.1745F, 0.0F));
		
		// Original model, 32x16, made in 2021 using Techne
//		ModelPartData right_horn = head.addChild(RIGHT_HORN, ModelPartBuilder.create(), ModelTransform.of(4.5F, -6.5F, -0.5F, 0.4367F, 0.0395F, 0.0184F));
//		right_horn.addChild("cube_r1", ModelPartBuilder.create().uv(19, 0).cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)), ModelTransform.of(0.0F, 0.0F, 5.25F, -0.0602F, -0.1639F, 0.0922F));
//		right_horn.addChild("cube_r2", ModelPartBuilder.create().uv(12, 1).cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(-0.45F)), ModelTransform.of(0.0F, 0.0F, 4.5F, 0.0F, -0.0873F, 0.0436F));
//		right_horn.addChild("cube_r3", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.3F)), ModelTransform.of(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.1745F));
//		right_horn.addChild("cube_r4", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, Dilation.NONE), ModelTransform.of(-0.25F, 0.0F, 0.0F, 0.0F, 0.1745F, 0.0F));
//		
//		ModelPartData left_horn = head.addChild(LEFT_HORN, ModelPartBuilder.create(), ModelTransform.of(-4.5F, -6.5F, -0.5F, 0.4367F, -0.0395F, -0.0184F));
//		left_horn.addChild("cube_r5", ModelPartBuilder.create().uv(19, 0).mirrored().cuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 1.0F, new Dilation(-0.1F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 5.25F, -0.0602F, 0.1639F, -0.0922F));
//		left_horn.addChild("cube_r6", ModelPartBuilder.create().uv(12, 1).mirrored().cuboid(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 3.0F, new Dilation(-0.45F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 4.5F, 0.0F, 0.0873F, -0.0436F));
//		left_horn.addChild("cube_r7", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 4.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.1745F));
//		left_horn.addChild("cube_r8", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, Dilation.NONE).mirrored(false), ModelTransform.of(0.25F, 0.0F, 0.0F, 0.0F, -0.1745F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createUnicornHorn()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild("horn", ModelPartBuilder.create().uv(0, 0).cuboid(-0.5F, -5.1518F, -0.4386F, 1.0F, 6.0F, 1.0F, Dilation.NONE), ModelTransform.of(0.0F, -6.5F, -4.0F, 0.6109F, 0.0F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createDevilHorns()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		head.addChild(RIGHT_HORN, ModelPartBuilder.create().uv(4, 0).cuboid(-0.25F, -2.5F, -1.0F, 1.0F, 3.0F, 1.0F, Dilation.NONE), ModelTransform.of(2.25F, -7.0F, -3.25F, 0.6109F, -0.2618F, 0.0F));
		head.addChild(LEFT_HORN, ModelPartBuilder.create().uv(0, 0).cuboid(-0.75F, -2.5F, -1.0F, 1.0F, 3.0F, 1.0F, Dilation.NONE), ModelTransform.of(-2.25F, -7.0F, -3.25F, 0.6109F, 0.2618F, 0.0F));
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createRamHorns()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		addRamHorns(head, 0F, 0);
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createStagHorns()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		addStagHorns(head, 0F, 0);
		
		return TexturedModelData.of(modelData, 16, 16);
	}
	
	public static TexturedModelData createKirinHorns()
	{
		ModelData modelData = getRig();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.getChild(EntityModelPartNames.HEAD);
		
		addStagHorns(head, 0.01F, 0);
		addRamHorns(head, 0.01F, 16);
		
		return TexturedModelData.of(modelData, 32, 16);
	}
	
	private static ModelPartData addRamHorns(ModelPartData head, float dil, int texXOffset)
	{
		Dilation dilation = new Dilation(dil);
		ModelPartData right_horn = head.addChild(RIGHT_HORN, ModelPartBuilder.create().uv(texXOffset, 0).cuboid(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 3.0F, dilation.add(0.3F))
			.uv(texXOffset, 10).cuboid(-1.0F, 0.0F, 2.5F, 1.0F, 2.0F, 2.0F, dilation.add(0.2F)), ModelTransform.of(5.25F, -5.75F, -0.25F, 0.3054F, 0.0F, 0.0F));
		
		right_horn.addChild("cube_r1", ModelPartBuilder.create().uv(texXOffset + 8, 3).cuboid(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, dilation), ModelTransform.of(-0.5F, 3.25F, 1.5F, 0.2618F, 0.0F, 0.0F));
		right_horn.addChild("cube_r2", ModelPartBuilder.create().uv(texXOffset + 8, 0).cuboid(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, dilation.add(0.1F)), ModelTransform.of(-0.5F, 2.5F, 2.75F, 0.7854F, 0.0F, 0.0F));
		right_horn.addChild("cube_r3", ModelPartBuilder.create().uv(texXOffset, 5).cuboid(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, dilation.add(0.25F)), ModelTransform.of(-0.5F, -0.1737F, 2.8801F, -0.6545F, 0.0F, 0.0F));
		
		ModelPartData left_horn = head.addChild(LEFT_HORN, ModelPartBuilder.create().uv(texXOffset, 0).mirrored().cuboid(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 3.0F, dilation.add(0.3F)).mirrored(false)
			.uv(texXOffset, 10).mirrored().cuboid(0.0F, 0.0F, 2.5F, 1.0F, 2.0F, 2.0F, dilation.add(0.2F)).mirrored(false), ModelTransform.of(-5.25F, -5.75F, -0.25F, 0.3054F, 0.0F, 0.0F));
		
		left_horn.addChild("cube_r4", ModelPartBuilder.create().uv(texXOffset + 8, 3).mirrored().cuboid(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, dilation).mirrored(false), ModelTransform.of(0.5F, 3.25F, 1.5F, 0.2618F, 0.0F, 0.0F));
		left_horn.addChild("cube_r5", ModelPartBuilder.create().uv(texXOffset + 8, 0).mirrored().cuboid(-0.5F, -0.5F, -1.0F, 1.0F, 1.0F, 2.0F, dilation.add(0.1F)).mirrored(false), ModelTransform.of(0.5F, 2.5F, 2.75F, 0.7854F, 0.0F, 0.0F));
		left_horn.addChild("cube_r6", ModelPartBuilder.create().uv(texXOffset, 5).mirrored().cuboid(-0.5F, -1.0F, -1.5F, 1.0F, 2.0F, 3.0F, dilation.add(0.25F)).mirrored(false), ModelTransform.of(0.5F, -0.1737F, 2.8801F, -0.6545F, 0.0F, 0.0F));
		
		return head;
	}
	
	private static ModelPartData addStagHorns(ModelPartData head, float dil, int texXOffset)
	{
		Dilation dilation = new Dilation(dil);
		ModelPartData rightAntler = head.addChild("right_antler", ModelPartBuilder.create().uv(texXOffset, 0).cuboid(-0.479F, -3.7594F, -0.4353F, 1.0F, 4.0F, 1.0F, dilation), ModelTransform.of(2.25F, -8.0F, -0.25F, -0.2608F, 0.0226F, 0.0843F));
			rightAntler.addChild("cube_r1", ModelPartBuilder.create().uv(texXOffset + 12, 0).cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, dilation), ModelTransform.of(2.021F, -6.3408F, 1.6695F, -0.4363F, 0.0F, 0.8727F));
			rightAntler.addChild("cube_r2", ModelPartBuilder.create().uv(texXOffset + 8, 0).cuboid(-0.5F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F, dilation), ModelTransform.of(2.521F, -2.7594F, 0.0647F, -0.3873F, -0.0665F, -0.1615F));
			rightAntler.addChild("cube_r3", ModelPartBuilder.create().uv(texXOffset + 4, 0).cuboid(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, dilation), ModelTransform.of(0.271F, -2.2594F, 0.0647F, 0.0F, 0.2182F, 1.3526F));
		
		ModelPartData leftAntler = head.addChild("left_antler", ModelPartBuilder.create().uv(texXOffset, 0).mirrored().cuboid(-0.521F, -3.7594F, -0.4353F, 1.0F, 4.0F, 1.0F, dilation).mirrored(false), ModelTransform.of(-2.25F, -8.0F, -0.25F, -0.2608F, -0.0226F, -0.0843F));
			leftAntler.addChild("cube_r4", ModelPartBuilder.create().uv(texXOffset + 12, 0).mirrored().cuboid(-0.5F, -3.0F, -0.5F, 1.0F, 3.0F, 1.0F, dilation).mirrored(false), ModelTransform.of(-2.021F, -6.3408F, 1.6695F, -0.4363F, 0.0F, -0.8727F));
			leftAntler.addChild("cube_r5", ModelPartBuilder.create().uv(texXOffset + 8, 0).mirrored().cuboid(-0.5F, -7.0F, -0.5F, 1.0F, 7.0F, 1.0F, dilation).mirrored(false), ModelTransform.of(-2.521F, -2.7594F, 0.0647F, -0.3873F, 0.0665F, 0.1615F));
			leftAntler.addChild("cube_r6", ModelPartBuilder.create().uv(texXOffset + 4, 0).mirrored().cuboid(-0.5F, -5.0F, -0.5F, 1.0F, 5.0F, 1.0F, dilation).mirrored(false), ModelTransform.of(-0.271F, -2.2594F, 0.0647F, 0.0F, -0.2182F, -1.3526F));
		
		return head;
	}
}
