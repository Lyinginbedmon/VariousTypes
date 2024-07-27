package com.lying.ability;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import com.lying.data.VTTags;
import com.lying.reference.Reference;
import com.lying.type.ActionHandler;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AbilityBreathing extends Ability
{
	private final BiConsumer<ActionHandler, AbilityInstance> applyFunc;
	
	public AbilityBreathing(Identifier regName, BiConsumer<ActionHandler, AbilityInstance> func)
	{
		super(regName, Category.UTILITY);
		this.applyFunc = func;
	}
	
	@NotNull
	public static TagKey<Fluid> getFluid(AbilityInstance inst)
	{
		return !inst.memory().contains("Fluid", NbtElement.STRING_TYPE) ? VTTags.AIR : TagKey.of(RegistryKeys.FLUID, new Identifier(inst.memory().getString("Fluid")));
	}
	
	public void applyToActions(ActionHandler handler, AbilityInstance inst) { applyFunc.accept(handler, inst); }
	
	public boolean isHidden(AbilityInstance instance) { return true; }
	
	public static Consumer<NbtCompound> air() { return of(VTTags.AIR); }
	public static Consumer<NbtCompound> water() { return of(FluidTags.WATER); }
	public static Consumer<NbtCompound> lava() { return of(FluidTags.LAVA); }
	public static Consumer<NbtCompound> of(TagKey<Fluid> fluid) { return nbt -> nbt.putString("Fluid", fluid.id().toString()); }
	
	public static class Allow extends AbilityBreathing
	{
		public Allow(Identifier regName)
		{
			super(regName, (action,inst) -> action.allowBreathe(getFluid(inst)));
		}
		
		public Identifier mapName(AbilityInstance inst) { return new Identifier(registryName().getNamespace(), "breathe_in_"+getFluid(inst).id().getPath()); }
		
		public Text displayName(AbilityInstance inst) { return Text.translatable("ability."+Reference.ModInfo.MOD_ID+".breathe_in", getFluid(inst).id().getPath()); }
	}
	
	public static class Deny extends AbilityBreathing
	{
		public Deny(Identifier regName)
		{
			super(regName, (action,inst) -> action.denyBreathe(getFluid(inst)));
		}
		
		public Identifier mapName(AbilityInstance inst) { return new Identifier(registryName().getNamespace(), "suffocate_in_"+getFluid(inst).id().getPath()); }
		
		public Text displayName(AbilityInstance inst) { return Text.translatable("ability."+Reference.ModInfo.MOD_ID+".suffocate_in", getFluid(inst).id().getPath()); }
	}
}
