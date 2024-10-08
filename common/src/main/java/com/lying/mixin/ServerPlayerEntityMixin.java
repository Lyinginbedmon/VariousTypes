package com.lying.mixin;

import java.util.Collection;
import java.util.List;
import java.util.OptionalInt;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.lying.VariousTypes;
import com.lying.event.PlayerEvents;
import com.lying.init.VTSheetElements;
import com.lying.type.Action;
import com.lying.type.ActionHandler;
import com.mojang.datafixers.util.Either;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerRecipeBook;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin extends PlayerEntityMixin
{
	@Shadow
	public void setSpawnPoint(RegistryKey<World> dimension, @Nullable BlockPos pos, float angle, boolean forced, boolean sendMessage) { }
	
	@Shadow
	public ServerStatHandler getStatHandler() { return null; }
	
	@Shadow
	public ServerRecipeBook getRecipeBook() { return null; }
	
	@Inject(method = "trySleep(Lnet/minecraft/util/math/BlockPos;)Lcom/mojang/datafixers/util/Either;", at = @At("HEAD"), cancellable = true)
	private void vt$trySleep(BlockPos pos, final CallbackInfoReturnable<Either<PlayerEntity.SleepFailureReason, Unit>> ci)
	{
		VariousTypes.getSheet((PlayerEntity)(Object)this).ifPresent(sheet -> 
		{
			// Prevents sleeping at beds if the player cannot sleep
			if(!sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
			{
				setSpawnPoint(getWorld().getRegistryKey(), pos, getYaw(), false, true);
				ci.setReturnValue(Either.left(PlayerEntity.SleepFailureReason.OTHER_PROBLEM));
			}
		});
	}
	
	@Inject(method = "tick()V", at = @At("TAIL"))
	private void vt$serverPlayerTick(final CallbackInfo ci)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			// Prevents phantoms from spawning from players who cannot sleep
			if(!sheet.<ActionHandler>elementValue(VTSheetElements.ACTIONS).can(Action.SLEEP.get()))
				getStatHandler().setStat(player, Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST), 0);
		});
	}
	
	@Inject(method = "openHandledScreen(Lnet/minecraft/screen/NamedScreenHandlerFactory;)Ljava/util/OptionalInt;", at = @At("HEAD"), cancellable = true)
	private void vt$openHandledScreen(@Nullable NamedScreenHandlerFactory factory, final CallbackInfoReturnable<OptionalInt> ci)
	{
		if(factory == null) return;
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		VariousTypes.getSheet(player).ifPresent(sheet -> 
		{
			ScreenHandlerType<?> type = factory.createMenu(0, getInventory(), player).getType();
			if(PlayerEvents.CAN_USE_SCREEN_EVENT.invoker().canPlayerUseScreen(player, type).isFalse())
				ci.setReturnValue(OptionalInt.empty());
		});
	}
	
	@Inject(method = "unlockRecipes(Ljava/util/Collection;)I", at = @At("HEAD"), cancellable = true)
	private void vt$unlockRecipes(Collection<RecipeEntry<?>> recipes, final CallbackInfoReturnable<Integer> ci)
	{
		ServerPlayerEntity player = (ServerPlayerEntity)(Object)this;
		int tally = 0;
		for(RecipeEntry<?> recipe : recipes)
		{
			if(PlayerEvents.CAN_UNLOCK_RECIPE_EVENT.invoker().canPlayerUnlockRecipeEvent(recipe, player).isFalse()) continue;
			tally += getRecipeBook().unlockRecipes(List.of(recipe), player);
		}
		ci.setReturnValue(tally);
	}
}
