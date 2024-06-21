package com.lying.type;

import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.lying.ability.AbilitySet;
import com.lying.init.VTTypes;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DummyType extends Type
{
	protected NbtCompound data = new NbtCompound();
	protected Identifier listID;
	protected Text displayName = null;
	
	protected DummyType(Identifier nameIn, Tier tierIn, AbilitySet abilitiesIn, Predicate<Type> compIn, Identifier listID, @Nullable Text displayNameIn)
	{
		super(nameIn, tierIn, abilitiesIn, compIn);
		listID = nameIn;
		displayName = displayNameIn;
		data.putString("ID", listID.toString());
	}
	
	public static DummyType create(Identifier listID, @Nullable Text displayName)
	{
		return new DummyType(VTTypes.DUMMY.get().registryName(), Tier.SUBTYPE, new AbilitySet(), Predicates.alwaysTrue(), listID, displayName);
	}
	
	public Identifier listID() { return listID; }
	
	protected void write(NbtCompound compound, DynamicRegistryManager manager)
	{
		compound.putString("ID", listID.toString());
		compound.putString("DisplayName", Text.Serialization.toJsonString(displayName(manager), manager));
	}
	
	public void read(NbtCompound compound)
	{
		listID = new Identifier(data.getString("ID"));
		data = compound;
		displayName = null;
	}
	
	public Text displayName(DynamicRegistryManager manager)
	{
		if(displayName != null)
			return displayName;
		
		Text name = super.displayName(manager);
		if(data.contains("DisplayName"))
		{
			String s = data.getString("DisplayName");
			try
			{
				name = (Text)Text.Serialization.fromJson((String)s, manager);
			}
			catch (Exception exception) { }
		}
		return displayName = name;
	}
	
	public static class Builder extends Type.Builder
	{
		private static final Function<Builder, Type> BUILD = builder -> 
		{
			DummyType type = new DummyType(builder.name, builder.tier, builder.abilities, builder.compCheck, builder.spoofName, builder.display);
			return type;
		};
		protected Text display;
		protected Identifier spoofName;
		
		protected Builder(Identifier nameIn, Tier tierIn)
		{
			super(nameIn, tierIn);
		}
		
		public static Builder of(Identifier nameIn, Tier tierIn)
		{
			return of(nameIn, tierIn, nameIn, null);
		}
		
		public static Builder of(Identifier nameIn, Tier tierIn, Identifier idIn, Text displayIn)
		{
			Builder builder = new Builder(nameIn, tierIn);
			builder.spoofName = idIn;
			builder.display = displayIn;
			return builder;
		}
		
		public Type build()
		{
			return build(BUILD);
		}
	}
}
