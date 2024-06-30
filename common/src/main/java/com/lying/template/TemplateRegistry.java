package com.lying.template;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.lying.reference.Reference.ModInfo;
import com.lying.template.operation.Operation;
import com.lying.template.precondition.Precondition;

import net.minecraft.util.Identifier;

public class TemplateRegistry
{
	private static final Map<Identifier, Template> TEMPLATES = new HashMap<>();
	
	private static final Map<Identifier, Supplier<Template>> DEFAULTS = new HashMap<>();
	
	public static final Supplier<Template> AQUATIC		= register(prefix("aquatic"), () -> Template.Builder.of(prefix("aquatic"))
			.power(1).build());	// Adds the AQUATIC subtype
	public static final Supplier<Template> DLUITH		= register(prefix("dluith"), () -> Template.Builder.of(prefix("dluith")).build());	// Half-Aberration
	public static final Supplier<Template> GRAVEKIN		= register(prefix("gravekin"), () -> Template.Builder.of(prefix("gravekin"))	// Necropolitan
			.power(0)
			.condition(Precondition.IS_LIVING.get()).build());
	public static final Supplier<Template> INSECTILE	= register(prefix("insectile"), () -> Template.Builder.of(prefix("insectile"))
			.power(2)
			.condition(Precondition.IS_LIVING.get()).build());
	public static final Supplier<Template> SIAR			= register(prefix("siar"), () -> Template.Builder.of(prefix("siar"))	// Lich
			.power(4)
			.condition(Precondition.IS_LIVING.get()).build());
	public static final Supplier<Template> REPTILIAN	= register(prefix("reptilian"), () -> Template.Builder.of(prefix("reptilian"))
			.power(2)
			.condition(Precondition.IS_LIVING.get()).build());
	public static final Supplier<Template> VAMPIRE		= register(prefix("vampire"), () -> Template.Builder.of(prefix("vampire"))
			.power(8)
			.condition(Precondition.IS_LIVING.get()).build());
	public static final Supplier<Template> WINGED		= register(prefix("winged"), () -> Template.Builder.of(prefix("winged"))
			.power(2).build());
	public static final Supplier<Template> ZOMBIE		= register(prefix("zombie"), () -> Template.Builder.of(prefix("zombie"))
			.power(1)
			.condition(Precondition.IS_LIVING.get())
			.operation(Operation.LOSE_DUMMY_SUBTYPES.get()).build());
	
	private static Identifier prefix(String nameIn) { return ModInfo.prefix(nameIn); }
	
	private static Supplier<Template> register(Identifier name, Supplier<Template> template)
	{
		DEFAULTS.put(name, template);
		return template;
	}
	
	@Nullable
	public static Template get(Identifier registryName)
	{
		return TEMPLATES.getOrDefault(registryName, null);
	}
}