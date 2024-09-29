package com.lying.species;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;

import com.google.gson.JsonObject;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.type.Type;
import com.lying.type.TypeSet;
import com.lying.utility.LootBag;
import com.lying.utility.LoreDisplay;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** A configurable package of types and abilities applied to a creature */
public class Species
{
	public static final Identifier BACKING_DEFAULT = prefix("textures/gui/sheet/creator_default.png");
	public static final Identifier BACKING_BASTION = prefix("textures/gui/sheet/creator_bastion.png");
	public static final Identifier BACKING_END_CITY = prefix("textures/gui/sheet/creator_end_city.png");
	public static final Identifier BACKING_END_ISLAND = prefix("textures/gui/sheet/creator_end_island.png");
	public static final Identifier BACKING_END_PORTAL = prefix("textures/gui/sheet/creator_end_portal.png");
	public static final Identifier BACKING_MINESHAFT = prefix("textures/gui/sheet/creator_mineshaft.png");
	public static final Identifier BACKING_SHIPWRECK = prefix("textures/gui/sheet/creator_shipwreck.png");
	public static final Identifier BACKING_STRONGHOLD = prefix("textures/gui/sheet/creator_stronghold.png");
	public static final Identifier BACKING_TRIAL = prefix("textures/gui/sheet/creator_trial.png");
	
	private final Identifier id;
	private final LoreDisplay display;
	private final Optional<Identifier> creatorBacking;
	
	private int power;
	private RegistryKey<World> homeDim = null;
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	
	private Optional<LootBag> lootOnApplied = Optional.empty();
	
	private Species(Identifier idIn, LoreDisplay displayIn, Optional<Identifier> textureIn)
	{
		id = idIn;
		display = displayIn;
		creatorBacking = textureIn;
	}
	
	public Builder toBuilder()
	{
		Builder builder = new Builder(id);
		builder.displayName = display.title();
		builder.displayDesc = display.description();
		creatorBacking.ifPresent(tex -> builder.texture(tex));
		
		builder.power(power);
		builder.from(homeDim);
		builder.setTypes(types.contents().toArray(new Type[0]));
		builder.addAbilities(abilities.abilities());
		builder.startingLoot = lootOnApplied;
		
		return builder;
	}
	
	public Identifier registryName() { return id; }
	
	public LoreDisplay display() { return display; }
	
	public Text displayName() { return display.title(); }
	
	public Identifier creatorBackground() { return creatorBacking.isPresent() ? creatorBacking.get() : BACKING_DEFAULT; }
	
	public int power() { return power; }
	
	public RegistryKey<World> homeDimension() { return homeDim; }
	
	public boolean hasConfiguredHome() { return homeDim != null; }
	
	public TypeSet types() { return types.copy(); }
	
	public AbilitySet abilities() { return abilities.copy(); }
	
	public boolean hasStartingGear() { return lootOnApplied.isPresent(); }
	
	public void giveLootTo(ServerPlayerEntity player) { lootOnApplied.ifPresent(loot -> loot.giveTo(player)); }
	
	public JsonObject writeToJson(RegistryWrapper.WrapperLookup lookup)
	{
		JsonObject obj = (JsonObject)Builder.CODEC.encodeStart(JsonOps.INSTANCE, toBuilder()).getOrThrow();
		obj.remove("ID");
		if(!abilities.isEmpty())
			obj.add("Abilities", abilities.writeToJson(lookup, true));
		return obj;
	}
	
	public static Species readFromJson(Identifier registryName, JsonObject data)
	{
		data.addProperty("ID", registryName.toString());
		Species.Builder builder = Builder.CODEC.parse(JsonOps.INSTANCE, data).getOrThrow();
		
		if(data.has("Abilities"))
			builder.addAbilities(AbilitySet.readFromJson(data.get("Abilities").getAsJsonArray(), AbilitySource.SPECIES).abilities());
		
		return builder.build();
	}
	
	public static class Builder
	{
		public static final Codec<Builder> CODEC	= RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("ID").forGetter(v -> v.id),
				LoreDisplay.CODEC.optionalFieldOf("Display").forGetter(v -> Optional.of(new LoreDisplay(v.displayName, v.displayDesc))),
				Identifier.CODEC.optionalFieldOf("Background").forGetter(v -> v.creatorTexture),
				Codec.INT.optionalFieldOf("Power").forGetter(v -> Optional.of(v.power)),
				RegistryKey.createCodec(RegistryKeys.WORLD).optionalFieldOf("Home").forGetter(v -> v.homeDim == null ? Optional.empty() : Optional.of(v.homeDim)),
				TypeSet.CODEC.optionalFieldOf("Types").forGetter(v -> Optional.of(v.types)),
				LootBag.CODEC.optionalFieldOf("StartingLoot").forGetter(v -> v.startingLoot))
					.apply(instance, Builder::new));
		
		private final Identifier id;
		
		private Text displayName;
		private Optional<Text> displayDesc = Optional.empty();
		private Optional<Identifier> creatorTexture = Optional.empty();
		
		private int power = 0;
		private RegistryKey<World> homeDim = null;
		private TypeSet types = new TypeSet();
		private AbilitySet abilities = new AbilitySet();
		
		private Optional<LootBag> startingLoot = Optional.empty();
		
		protected Builder(Identifier idIn, Optional<LoreDisplay> displayIn, Optional<Identifier> backingIn, Optional<Integer> powerIn, Optional<RegistryKey<World>> homeIn, Optional<TypeSet> typesIn, Optional<LootBag> lootIn)
		{
			this(idIn);
			power(powerIn.orElse(0));
			
			LoreDisplay display = displayIn.orElse(new LoreDisplay(Text.translatable("species."+idIn.getNamespace()+"."+idIn.getPath()), Optional.empty()));
			display(display.title());
			display.description().ifPresent(desc -> description(desc));
			backingIn.ifPresent(tex -> texture(tex));
			
			homeIn.ifPresent(dim -> from(dim));
			types = typesIn.orElse(new TypeSet());
			
			startingLoot = lootIn;
		}
		
		protected Builder(Identifier idIn)
		{
			id = idIn;
			displayName = Text.translatable("species."+idIn.getNamespace()+"."+idIn.getPath());
		}
		
		public static Builder of(Identifier idIn)
		{
			return new Builder(idIn);
		}
		
		public Species build()
		{
			Species species = new Species(id, new LoreDisplay(displayName, displayDesc), creatorTexture);
			species.power = power;
			species.homeDim = homeDim;
			species.types = types;
			species.abilities = abilities;
			species.lootOnApplied = startingLoot;
			return species;
		}
		
		public Builder display(Text nameIn)
		{
			displayName = nameIn;
			return this;
		}
		
		public Builder description(Text descIn)
		{
			displayDesc = Optional.of(descIn);
			return this;
		}
		
		public Builder texture(Identifier textureIn)
		{
			creatorTexture = Optional.of(textureIn);
			return this;
		}
		
		public Builder power(int powerIn)
		{
			power = Math.max(0, powerIn);
			return this;
		}
		
		public Builder startingLoot(LootBag bagIn)
		{
			startingLoot = bagIn == null ? Optional.empty() : Optional.of(bagIn);
			return this;
		}
		
		public Builder from(RegistryKey<World> world)
		{
			homeDim = world;
			return this;
		}
		
		public Builder setTypes(Type... typesIn)
		{
			types = new TypeSet();
			for(Type type : typesIn)
				types.add(type);
			return this;
		}
		
		public Builder addAbility(Ability ability)
		{
			return addAbility(ability, Consumers.nop());
		}
		
		public Builder addAbility(Ability... abilities)
		{
			for(Ability inst : abilities)
				addAbility(inst);
			return this;
		}
		
		public Builder addAbility(Ability ability, Consumer<NbtCompound> dataModifier)
		{
			abilities.add(ability.instance(AbilitySource.SPECIES, dataModifier));
			return this;
		}
		
		public Builder addAbility(AbilityInstance inst)
		{
			if(inst.source() == AbilitySource.SPECIES)
				abilities.add(inst);
			else
			{
				AbilityInstance conformed = inst.ability().instance(AbilitySource.SPECIES);
				conformed.copyDetails(inst);
				abilities.add(inst);
			}
			return this;
		}
		
		public Builder addAbilities(Collection<AbilityInstance> inst)
		{
			inst.forEach(instance -> addAbility(instance));
			return this;
		}
	}
}