package com.lying.species;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;

import com.google.gson.JsonObject;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilitySet;
import com.lying.type.Type;
import com.lying.type.TypeSet;
import com.lying.utility.LoreDisplay;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
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
	private LoreDisplay display;
	private Optional<Identifier> creatorBacking;
	
	private int power;
	private RegistryKey<World> homeDim;
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	
	private Species(Identifier idIn, LoreDisplay displayIn, Optional<Identifier> textureIn)
	{
		id = idIn;
		display = displayIn;
		creatorBacking = textureIn;
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
	
	public JsonObject writeToJson(RegistryWrapper.WrapperLookup lookup)
	{
		JsonObject obj = new JsonObject();
		obj.add("Display", display.toJson(lookup));
		creatorBacking.ifPresent(tex -> obj.addProperty("Background", tex.toString()));
		
		if(power > 0)
			obj.addProperty("Power", power);
		if(homeDim != null)
			obj.addProperty("Home", homeDim.getValue().toString());
		if(!types.isEmpty())
			obj.add("Types", types.writeToJson(lookup));
		if(!abilities.isEmpty())
			obj.add("Abilities", abilities.writeToJson(lookup, true));
		
		return obj;
	}
	
	public static Species readFromJson(Identifier registryName, JsonObject data)
	{
		Species.Builder builder = Builder.of(registryName);
		
		if(data.has("Background"))
			builder.texture(new Identifier(data.get("Background").getAsString()));
		
		Species species = builder.build();
		species.display = LoreDisplay.fromJson(data.get("Display"));
		
		if(data.has("Power"))
			species.power = data.get("Power").getAsInt();
		
		if(data.has("Home"))
			species.homeDim = World.CODEC.parse(NbtOps.INSTANCE, NbtString.of(data.get("Home").getAsString())).resultOrPartial(VariousTypes.LOGGER::error).orElse(null);
		
		if(data.has("Types"))
			species.types = TypeSet.readFromJson(data.get("Types").getAsJsonArray());
		
		if(data.has("Abilities"))
			species.abilities = AbilitySet.readFromJson(data.get("Abilities").getAsJsonArray(), AbilitySource.SPECIES);
		
		return species;
	}
	
	public void clear()
	{
		power = 0;
		homeDim = null;
		types.clear();
		abilities.clear();
	}
	
	public static class Builder
	{
		private Identifier id;
		
		private Text displayName;
		private Optional<Text> displayDesc = Optional.empty();
		private Optional<Identifier> creatorTexture = Optional.empty();
		
		private int power = 0;
		private RegistryKey<World> homeDim = null;
		private TypeSet types = new TypeSet();
		private AbilitySet abilities = new AbilitySet();
		
		protected Builder(Identifier idIn)
		{
			id = idIn;
			displayName = Text.translatable("species."+idIn.getNamespace()+"."+idIn.getPath());
		}
		
		public static Builder of(Identifier idIn)
		{
			return new Builder(idIn);
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
		
		public Builder addAbility(Ability inst)
		{
			return addAbility(inst, Consumers.nop());
		}
		
		public Builder addAbility(Ability... abilities)
		{
			for(Ability inst : abilities)
				addAbility(inst);
			return this;
		}
		
		public Builder addAbility(Ability inst, Consumer<NbtCompound> dataModifier)
		{
			abilities.add(inst.instance(AbilitySource.SPECIES, dataModifier));
			return this;
		}
		
		public Species build()
		{
			Species species = new Species(id, new LoreDisplay(displayName, displayDesc), creatorTexture);
			species.power = power;
			species.homeDim = homeDim;
			species.types = types;
			species.abilities = abilities;
			return species;
		}
	}
}