package com.lying.ability;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.lying.type.ActionHandler;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public abstract class AbilityBreathing extends Ability
{
	private final BiConsumer<ActionHandler, AbilityInstance> applyFunc;
	
	public AbilityBreathing(Identifier regName, BiConsumer<ActionHandler, AbilityInstance> func)
	{
		super(regName);
		this.applyFunc = func;
	}
	
	public static Fluid getFluid(AbilityInstance inst)
	{
		String name = inst.memory().getString("Fluid");
		Fluid fluid = Registries.FLUID.get(new Identifier(name));
		return fluid == null ? Fluids.EMPTY : fluid;
	}
	
	public void applyToActions(ActionHandler handler, AbilityInstance inst) { applyFunc.accept(handler, inst); }
	
	public static Consumer<NbtCompound> air() { return of(Fluids.EMPTY); }
	public static Consumer<NbtCompound> water() { return of(Fluids.WATER); }
	public static Consumer<NbtCompound> lava() { return of(Fluids.LAVA); }
	public static Consumer<NbtCompound> of(Fluid fluid) { return nbt -> nbt.putString("Fluid", fluid.arch$registryName().toString()); }
	
	public static class Allow extends AbilityBreathing
	{
		public Allow(Identifier regName)
		{
			super(regName, (action,inst) -> action.allowBreathe(getFluid(inst)));
		}
		
		public Identifier mapName(AbilityInstance inst) { return new Identifier(registryName().getNamespace(), "breathe_in_"+getFluid(inst).arch$registryName().getPath()); }
	}
	
	public static class Deny extends AbilityBreathing
	{
		public Deny(Identifier regName)
		{
			super(regName, (action,inst) -> action.denyBreathe(getFluid(inst)));
		}
		
		public Identifier mapName(AbilityInstance inst) { return new Identifier(registryName().getNamespace(), "suffocate_in_"+getFluid(inst).arch$registryName().getPath()); }
	}
}
