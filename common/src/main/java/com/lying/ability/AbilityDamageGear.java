package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityDamageGear.ConfigDamageGear;
import com.lying.init.VTParticleTypes;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class AbilityDamageGear extends AbilityOnMeleeHit implements IComplexAbility<ConfigDamageGear>
{
	private final List<EquipmentSlot> affectedSlots = Lists.newArrayList();
	
	public AbilityDamageGear(Identifier regName, Category catIn, EquipmentSlot... slots)
	{
		super(regName, catIn);
		for(EquipmentSlot slot : slots)
			affectedSlots.add(slot);
	}
	
	public Optional<Text> description(AbilityInstance instance)
	{
		ConfigDamageGear values = ConfigDamageGear.fromNbt(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", values.describe()));
	}
	
	public void onMeleeHit(LivingEntity victim, LivingEntity attacker, AbilityInstance inst, boolean isClient)
	{
		if(isClient) return;
		
		List<EquipmentSlot> slots = Lists.newArrayList();
		slots.addAll(affectedSlots);
		
		ConfigDamageGear config = instanceToValues(inst);
		int attempts = config.attempts;
		
		Random rand = attacker.getRandom();
		EquipmentSlot slot = null;
		ItemStack stack = ItemStack.EMPTY;
		do
		{
			stack = victim.getEquippedStack(slot = slots.remove(rand.nextInt(slots.size())));
			--attempts;
		}
		while(stack.isEmpty() && !slots.isEmpty() && attempts > 0);
		
		if(!stack.isEmpty())
		{
			if(stack.isDamageable())
				stack.damage(config.damage(rand), victim, slot);
			else
			{
				victim.dropStack(stack.copy());
				victim.equipStack(slot, ItemStack.EMPTY);
			}
			
			// FIXME Add rend sound effect
			double xOffset = -MathHelper.sin(attacker.getYaw() * ((float)Math.PI / 180));
			double zOffset = MathHelper.cos(attacker.getYaw() * ((float)Math.PI / 180));
			VTUtils.spawnParticles((ServerWorld)attacker.getWorld(), VTParticleTypes.REND.get(), attacker.getPos().add(xOffset, attacker.getHeight() * 0.5D, zOffset), new Vec3d(xOffset, 0, zOffset));
		}
	}
	
	public ConfigDamageGear memoryToValues(NbtCompound nbt) { return ConfigDamageGear.fromNbt(nbt); }
	
	public static class ConfigDamageGear
	{
		protected static final Codec<ConfigDamageGear> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.INT.optionalFieldOf("Attempts").forGetter(c -> Optional.of(c.attempts)),
				Codec.INT.optionalFieldOf("DamageMin").forGetter(c -> Optional.of(c.damageMin())),
				Codec.INT.optionalFieldOf("DamageMax").forGetter(c -> Optional.of(c.damageMax())))
					.apply(instance, ConfigDamageGear::new));
		
		private int attempts, a, b;
		
		public ConfigDamageGear(Optional<Integer> attemptsIn, Optional<Integer> minIn, Optional<Integer> maxIn)
		{
			attempts = attemptsIn.orElse(1);
			a = minIn.orElse(8);
			b = maxIn.orElse(16);
		}
		
		public int damageMax() { return Math.max(b, a); }
		
		public int damageMin() { return Math.min(b, a); }
		
		public int damage(Random rand) { return a == b ? a : rand.nextBetween(damageMin(), damageMax()); }
		
		public Text describe() { return a == b ? Text.literal(String.valueOf(a)) : Text.literal(String.valueOf(damageMin())+"-"+String.valueOf(damageMax())); }
		
		public static ConfigDamageGear fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}