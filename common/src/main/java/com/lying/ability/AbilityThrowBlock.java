package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilityThrowBlock.ConfigThrowBlock;
import com.lying.entity.ThrownBlockEntity;
import com.lying.init.VTAbilities;
import com.lying.reference.Reference;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbilityThrowBlock extends ActivatedAbility implements IComplexAbility<ConfigThrowBlock>
{
	public AbilityThrowBlock(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigThrowBlock values = instanceToValues(instance);
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.state.getBlock().getName()));
	}
	
	public static AbilityInstance ofBlock(BlockState state, double power, boolean hurt, boolean sticky)
	{
		AbilityInstance inst = VTAbilities.WEBSPINNER.get().instance();
		inst.setMemory(new ConfigThrowBlock(state, power, hurt, sticky).toNbt());
		return inst;
	}
	
	public int cooldownDefault() { return Reference.Values.TICKS_PER_SECOND * 5; }
	
	protected void activate(LivingEntity owner, AbilityInstance instance)
	{
		ConfigThrowBlock config = instanceToValues(instance);
		World world = owner.getEntityWorld();
		ThrownBlockEntity block = config.create(world, new Vec3d(owner.getX(), owner.getEyeY(), owner.getZ()));
		block.setOwner(owner);
		Vec3d lookVec = AbilityHelper.getLookVec(owner);
		block.setVelocity(lookVec.multiply(config.power));
		world.spawnEntity(block);
	}
	
	public ConfigThrowBlock memoryToValues(NbtCompound data) { return ConfigThrowBlock.fromNbt(data); }
	
	public static class ConfigThrowBlock
	{
		protected static final Codec<ConfigThrowBlock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				BlockState.CODEC.optionalFieldOf("Block").forGetter(c -> Optional.of(c.state)),
				Codec.DOUBLE.optionalFieldOf("Power").forGetter(c -> Optional.of(c.power)),
				Codec.BOOL.optionalFieldOf("HurtEntities").forGetter(c -> Optional.of(c.hurtEntities)),
				Codec.BOOL.optionalFieldOf("DropItem").forGetter(c -> Optional.of(c.dropItem)),
				Codec.BOOL.optionalFieldOf("CancelDrop").forGetter(c -> Optional.of(c.destroyedOnLanding)),
				Codec.BOOL.optionalFieldOf("Sticky").forGetter(c -> Optional.of(c.sticky)))
					.apply(instance, ConfigThrowBlock::new));
		
		private BlockState state;
		private double power;
		private boolean hurtEntities, dropItem, destroyedOnLanding, sticky;
		
		public ConfigThrowBlock(BlockState stateIn, double powerIn, boolean hurtIn, boolean stickyIn)
		{
			this(Optional.of(stateIn), Optional.of(powerIn), Optional.of(hurtIn), Optional.empty(), Optional.empty(), Optional.of(stickyIn));
		}
		
		public ConfigThrowBlock(Optional<BlockState> stateIn, Optional<Double> powerIn, Optional<Boolean> hurtIn, Optional<Boolean> dropIn, Optional<Boolean> destroyIn, Optional<Boolean> stickyIn)
		{
			state = stateIn.orElse(Blocks.COBWEB.getDefaultState());
			power = powerIn.orElse(1.5D);
			hurtEntities = hurtIn.orElse(true);
			dropItem = dropIn.orElse(false);
			destroyedOnLanding = destroyIn.orElse(false);
			sticky = stickyIn.orElse(true);
		}
		
		public ThrownBlockEntity create(World world, Vec3d spawnPos)
		{
			return new ThrownBlockEntity(world, spawnPos, state)
					.hurtEntities(hurtEntities)
					.dropItem(dropItem)
					.destroyOnLanding(destroyedOnLanding)
					.sticky(sticky);
		}
		
		public NbtCompound toNbt() { return (NbtCompound)CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(); }
		
		public static ConfigThrowBlock fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
