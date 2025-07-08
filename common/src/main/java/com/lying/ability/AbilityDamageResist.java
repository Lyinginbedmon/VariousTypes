package com.lying.ability;

import static com.lying.reference.Reference.ModInfo.translate;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityDamageResist.ConfigDamageResist;
import com.lying.component.CharacterSheet;
import com.lying.event.PlayerEvents;
import com.lying.init.VTAbilities;
import com.lying.init.VTSheetElements;
import com.lying.utility.VTUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AbilityDamageResist extends Ability implements IComplexAbility<ConfigDamageResist>
{
	public AbilityDamageResist(Identifier regName, Category catIn)
	{
		super(regName, catIn);
	}
	
	public boolean remappable() { return true; }
	
	public Optional<Text> description(AbilityInstance instance, DynamicRegistryManager manager)
	{
		ConfigDamageResist values = memoryToValues(instance.memory());
		return Optional.of(translate("ability", registryName().getPath()+".desc", (int)(100 * (1F - values.amount)), VTUtils.tagListToString(values.types, ", ")));
	}
	
	public void registerEventHandlers()
	{
		PlayerEvents.MODIFY_DAMAGE_TAKEN_EVENT.register((living, source, amount) -> 
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(living);
			if(sheetOpt.isEmpty())
				return 1F;
			
			for(AbilityInstance inst : sheetOpt.get().<AbilitySet>elementValue(VTSheetElements.ABILITIES).getAbilitiesOfType(VTAbilities.FLAMEPROOF.get().registryName()))
			{
				ConfigDamageResist config = ((AbilityDamageResist)inst.ability()).instanceToValues(inst);
				if(config.types.isEmpty())
					continue;
				
				if(config.types.stream().anyMatch(tag -> source.isIn(tag)))
					return config.nullifies() ? 0F : config.amount;
			}
			
			return 1F;
		});
	}
	
	public ConfigDamageResist memoryToValues(NbtCompound data) { return ConfigDamageResist.fromNbt(data); }
	
	public static class ConfigDamageResist
	{
		protected static final Codec<ConfigDamageResist> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				TagKey.codec(RegistryKeys.DAMAGE_TYPE).listOf().optionalFieldOf("DamageTags").forGetter(v -> Optional.of(v.types)),
				Codec.FLOAT.optionalFieldOf("Amount").forGetter(v -> Optional.of(v.amount)))
					.apply(instance, ConfigDamageResist::new));
		
		protected List<TagKey<DamageType>> types = Lists.newArrayList();
		protected float amount;
		
		public ConfigDamageResist(Optional<List<TagKey<DamageType>>> listIn, Optional<Float> radiusIn)
		{
			listIn.ifPresentOrElse(val -> types.addAll(val), () -> types.add(DamageTypeTags.IS_FIRE));
			radiusIn.ifPresentOrElse(val -> amount = MathHelper.clamp(val, 0F, 1F), () -> amount = 0.5F);
		}
		
		public boolean nullifies() { return amount >= 1F; }
		
		public static ConfigDamageResist fromNbt(NbtCompound nbt)
		{
			return CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		}
	}
}
