package com.lying.init;

import java.util.function.Function;

import com.lying.VariousTypes;
import com.lying.block.SmokeBlock;
import com.lying.reference.Reference;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class VTBlocks
{
	private static final DeferredRegister<Block> BLOCKS	= DeferredRegister.create(Reference.ModInfo.MOD_ID, RegistryKeys.BLOCK);
	private static int tally = 0;
	
	public static final RegistrySupplier<Block> SMOKE	= register("smoke", settings -> new SmokeBlock(settings.dropsNothing().replaceable().suffocates(VTBlocks::never).blockVision(VTBlocks::always).pistonBehavior(PistonBehavior.DESTROY)));
	
	private static RegistrySupplier<Block> register(String nameIn, Function<AbstractBlock.Settings, Block> supplierIn)
	{
		tally++;
		return BLOCKS.register(Reference.ModInfo.prefix(nameIn), () -> supplierIn.apply(AbstractBlock.Settings.create()));
	}
	
	private static boolean never(BlockState state, BlockView world, BlockPos pos) { return false; }
	private static boolean always(BlockState state, BlockView world, BlockPos pos) { return true; }
	
	public static void init()
	{
		BLOCKS.register();
		VariousTypes.LOGGER.info(" # Registered {} blocks", tally);
	}
}
