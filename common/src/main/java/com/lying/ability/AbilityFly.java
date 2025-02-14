package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityFly.ConfigFly;
import com.lying.component.CharacterSheet;
import com.lying.event.LivingEvents;
import com.lying.event.PlayerEvents;
import com.lying.init.VTSheetElements;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.architectury.event.EventResult;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.Vec3d;

public class AbilityFly extends Ability implements IComplexAbility<ConfigFly>
{
	public static final double DEFAULT_SPEED = 1D;
	public static final float DEFAULT_EXHAUSTION = 0.15F;
	
	public AbilityFly(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	protected List<Text> addTooltipDetails(AbilityInstance instance)
	{
		List<Text> details = Lists.newArrayList();
		ConfigFly values = instanceToValues(instance);
		details.add(translate("gui","wing_style", values.type().translated()));
		if(values.color.isPresent())
			details.add(translate("gui", "colour", "#"+Integer.toHexString(values.color.get())));
		return details;
	}
	
	public void registerEventHandlers()
	{
		LivingEvents.CUSTOM_ELYTRA_CHECK_EVENT.register((living, ticking) -> 
		{
			if(living.getType() != EntityType.PLAYER)
				return EventResult.pass();
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return EventResult.pass();
			
			PlayerEntity player = (PlayerEntity)living;
			if(player.getHungerManager().getFoodLevel() <= 3)
				return EventResult.pass();
			
			if(ticking && player.isFallFlying() && (!player.getWorld().isClient() || player.isMainPlayer()))
			{
				double input = Math.max(0, player.forwardSpeed);
				if(input > 0)
				{
					AbilityInstance flight = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
					double multiplier = instanceToValues(flight).speed;
					
					Vec3d direction = player.getRotationVector();
					double forward = input * 0.3F * player.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED) * multiplier;
					player.addVelocity(direction.multiply(forward));
				}
			}
			
			return EventResult.interruptTrue();
		});
		
		PlayerEvents.PLAYER_FLIGHT_INPUT_EVENT.register((player, forward, strafe, jump, sneak) -> 
		{
			if(player.getHungerManager().getFoodLevel() <= 3 || !player.isFallFlying())
				return;
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || !sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).hasAbility(registryName()))
				return;
			
			AbilityInstance flight = sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).get(registryName());
			float exhaustion = instanceToValues(flight).food;
			
			double input = Math.max(0, forward);
			if(input > 0)
				player.addExhaustion((float)Math.abs(input) * exhaustion);
		});
	}
	
	public ConfigFly memoryToValues(NbtCompound data) { return ConfigFly.fromNbt(data); }
	
	public static enum WingStyle implements StringIdentifiable
	{
		BUTTERFLY,
		DRAGONFLY,
		FAIRY,	//
		ENERGY,	// FIXME Finish model & animations
		BEETLE,	// FIXME Proper textures
		BIRD,	// FIXME Dyed texture
		ANGEL,	// FIXME Dyed texture
		BAT,	// FIXME Proper texture
		DRAGON,	//
		ELYTRA,
		NONE;
		
		public static Codec<WingStyle> CODEC = StringIdentifiable.createBasicCodec(WingStyle::values);
		
		public String asString() { return name().toLowerCase(); }
		
		public Text translated() { return translate("wings", asString()); }
		
		public static WingStyle fromString(String nameIn)
		{
			for(WingStyle type : values())
				if(type.asString().equalsIgnoreCase(nameIn))
					return type;
			return BIRD;
		}
	}
	
	public static class ConfigFly
	{
		protected static final Codec<ConfigFly> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Codec.DOUBLE.optionalFieldOf("Speed").forGetter(v -> Optional.of(v.speed)),
				Codec.FLOAT.optionalFieldOf("Food").forGetter(v -> Optional.of(v.food)),
				WingStyle.CODEC.optionalFieldOf("Style").forGetter(v -> Optional.of(v.style)),
				Codec.INT.optionalFieldOf("Color").forGetter(v -> v.color))
					.apply(instance, ConfigFly::new));
		
		protected double speed;
		protected float food;
		
		protected WingStyle style;
		protected Optional<Integer> color;
		
		public ConfigFly(Optional<Double> speedIn, Optional<Float> foodIn, Optional<WingStyle> typeIn, Optional<Integer> colorIn)
		{
			speed = speedIn.orElse(DEFAULT_SPEED);
			food = foodIn.orElse(DEFAULT_EXHAUSTION);
			
			style = typeIn.orElse(WingStyle.BIRD);
			color = colorIn;
		}
		
		public WingStyle type() { return this.style; }
		public Optional<Integer> colour() { return color; }
		
		public static ConfigFly fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
