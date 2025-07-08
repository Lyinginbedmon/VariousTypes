package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.ability.AbilitySculksight.ConfigSculksight;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionables;
import com.lying.event.MiscEvents;
import com.lying.init.VTSheetElements;
import com.lying.network.HighlightPacket;
import com.lying.reference.Reference;
import com.lying.utility.Highlight;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class AbilitySculksight extends ToggledAbility implements IComplexAbility<ConfigSculksight>
{
	public AbilitySculksight(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigSculksight values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", 
				(int)values.range));
	}
	
	public void registerEventHandlers()
	{
		MiscEvents.ON_VIBRATION_EVENT.register((gameEvent, entity, block, world) -> 
		{
			if(world.isClient())
				return;
			
			long currentTime = world.getTime();
			Vec3d blockVec = new Vec3d(block.getX() + 0.5D, block.getY() + 0.5D, block.getZ() + 0.5D);
			world.getPlayers().stream()
				.filter(player -> entity.isEmpty() || entity.get() != player)
				.filter(Entity::isAlive)
				.filter(player -> !player.isSpectator())
				.filter(player -> 
				{
					Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
					return sheetOpt.isPresent() && ToggledAbility.hasActive(sheetOpt.get().elementValue(VTSheetElements.ACTIONABLES), registryName());
				})
					.forEach(player -> VariousTypes.getSheet(player).ifPresent(sheet -> 
						{
							AbilityInstance inst = sheet.<ElementActionables>elementValue(VTSheetElements.ACTIONABLES).get(registryName());
							ConfigSculksight config = ((AbilitySculksight)inst.ability()).instanceToValues(inst);
							if(player.squaredDistanceTo(blockVec) > config.rangeSqr)
								return;
							
							Highlight.Block blockH = new Highlight.Block(block, currentTime, config.blockTicks, 0x024050);
							HighlightPacket.send((ServerPlayerEntity)player, entity.isPresent() ? List.of(blockH, new Highlight.Entity(entity.get().getUuid(), currentTime, config.entityTicks)) : List.of(blockH));
						}));
		});
	}
	
	public ConfigSculksight memoryToValues(NbtCompound nbt) { return ConfigSculksight.fromNbt(nbt); }
	
	public static class ConfigSculksight
	{
		protected static final Codec<ConfigSculksight> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.DOUBLE.optionalFieldOf("Range").forGetter(v -> Optional.of(v.range)),
				Codec.INT.optionalFieldOf("BlockTicks").forGetter(v -> Optional.of(v.blockTicks)),
				Codec.INT.optionalFieldOf("EntityTicks").forGetter(v -> Optional.of(v.entityTicks)))
					.apply(instance, ConfigSculksight::new));
		
		protected double range, rangeSqr;
		protected int blockTicks, entityTicks;
		
		public ConfigSculksight(Optional<Double> rangeIn, Optional<Integer> blockIn, Optional<Integer> entityIn)
		{
			range = rangeIn.orElse(12D);
			rangeSqr = range * range;
			
			blockTicks = blockIn.orElse(Reference.Values.TICKS_PER_SECOND * 5);
			entityTicks = entityIn.orElse(Reference.Values.TICKS_PER_SECOND * 3);
		}
		
		public static ConfigSculksight fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
