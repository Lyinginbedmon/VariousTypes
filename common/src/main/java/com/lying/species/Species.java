package com.lying.species;

import java.util.function.Consumer;

import org.apache.commons.lang3.function.Consumers;

import com.lying.ability.Ability;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilitySet;
import com.lying.type.Type;
import com.lying.type.TypeSet;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

/** A configurable package of types and abilities applied to a creature */
public class Species
{
	private final Identifier id;
	
	private RegistryKey<World> homeDim;
	private TypeSet types = new TypeSet();
	private AbilitySet abilities = new AbilitySet();
	
	public Species(Identifier idIn)
	{
		id = idIn;
	}
	
	public Identifier registryName() { return id; }
	
	public RegistryKey<World> homeDimension() { return homeDim; }
	
	public boolean hasConfiguredHome() { return homeDim != null; }
	
	public TypeSet types() { return types.copy(); }
	
	public AbilitySet abilities() { return abilities.copy(); }
	
	public static class Builder
	{
		private Identifier id;
		
		private RegistryKey<World> homeDim = null;
		private TypeSet types = new TypeSet();
		private AbilitySet abilities = new AbilitySet();
		
		protected Builder(Identifier idIn)
		{
			this.id = idIn;
		}
		
		public static Builder of(Identifier idIn)
		{
			return new Builder(idIn);
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
		
		public Builder addAbility(Ability inst, Consumer<NbtCompound> dataModifier)
		{
			abilities.add(inst.instance(AbilitySource.SPECIES, dataModifier));
			return this;
		}
		
		public Species build()
		{
			Species species = new Species(id);
			species.homeDim = homeDim;
			species.types = types;
			species.abilities = abilities;
			return species;
		}
	}
}