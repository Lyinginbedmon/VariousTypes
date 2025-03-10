package com.lying.client.utility;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import com.lying.client.model.IBipedLikeModel;
import com.lying.client.model.IModelWithRoot;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;

public class ModelTransformHelper
{
	@Nullable
	public static ModelPart getRoot(EntityModel<?> contextModel)
	{
		if(contextModel instanceof IModelWithRoot)
			return ((IModelWithRoot)contextModel).getRoot();
		if(contextModel instanceof SinglePartEntityModel)
			return ((SinglePartEntityModel<?>)contextModel).getPart();
		return null;
	}
	
	@Nullable
	public static ModelPart getHead(EntityModel<?> contextModel)
	{
		if(contextModel instanceof BipedEntityModel)
			return ((BipedEntityModel<?>)contextModel).head;
		if(contextModel instanceof IBipedLikeModel)
			return ((IBipedLikeModel<?>)contextModel).getModelHead();
		return null;
	}
	
	/** Returns a vector representing the given transform's pivot point */
	public static Vector3f pivot(ModelTransform transform)
	{
		return new Vector3f(transform.pivotX, transform.pivotY, transform.pivotZ);
	}
	
	public static Vector3f rotation(ModelTransform transform)
	{
		return new Vector3f(transform.pitch, transform.yaw, transform.roll);
	}
	
	/** Returns the direction vector from the part's default pivot point to its current pivot point */
	public static Vector3f getTranslation(ModelPart part)
	{
		return pivot(part.getTransform()).sub(pivot(part.getDefaultTransform()));
	}
	
	public static Vector3f getTranslation(ModelTransform start, ModelTransform end)
	{
		return pivot(end).sub(pivot(start));
	}
	
	/** Returns a direction vector from the first part's default pivot point to the second's pivot point */
	public static Vector3f relativeTo(ModelPart a, ModelPart b)
	{
		return getTranslation(a.getDefaultTransform(), b.getDefaultTransform());
	}
	
	/** Returns the current position of part A's pivot point relative to part B */
	public static Vector3f getCurrentPivotRelativeto(ModelPart a, ModelPart b)
	{
		return rotateBy(relativeTo(b, a), b.getTransform());
	}
	
	public static Vector3f rotateBy(Vector3f vec, ModelTransform transform)
	{
		Vector3f rotation = rotation(transform);
		return vec.rotateX(rotation.x).rotateY(rotation.y).rotateZ(rotation.z);
	}
}
