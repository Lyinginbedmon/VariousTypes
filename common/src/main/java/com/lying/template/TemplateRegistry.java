package com.lying.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.lying.ability.Ability.AbilitySource;
import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.reference.Reference.ModInfo;
import com.lying.template.operation.AbilityOperation;
import com.lying.template.operation.Operation;
import com.lying.template.operation.TypesOperation;
import com.lying.template.precondition.TypeCondition;
import com.lying.template.precondition.Precondition;

import net.minecraft.util.Identifier;

public class TemplateRegistry
{
	private static final Map<Identifier, Template> TEMPLATES = new HashMap<>();
	
	private static final Map<Identifier, Supplier<Template>> DEFAULTS = new HashMap<>();
	
	public static final Supplier<Template> AQUATIC		= register(prefix("aquatic"), () -> Template.Builder.of(prefix("aquatic"))
			.power(1)
			.condition(Precondition.IS_LIVING.get())
			.operation(TypesOperation.Add.of(VTTypes.AQUATIC.get())).build());
//	public static final Supplier<Template> DLUITH		= register(prefix("dluith"), () -> Template.Builder.of(prefix("dluith"))	// Half-Aberration
//			.condition(Precondition.IS_LIVING.get()).build());
//	public static final Supplier<Template> GRAVEKIN		= register(prefix("gravekin"), () -> Template.Builder.of(prefix("gravekin"))	// Necropolitan
//			.power(0)
//			.condition(Precondition.IS_LIVING.get())
//			.operation(TypesOperation.Set.of(VTTypes.UNDEAD.get())).build());
	public static final Supplier<Template> INSECTILE	= register(prefix("insectile"), () -> Template.Builder.of(prefix("insectile"))
			.power(2)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.All.of(VTTypes.HUMAN.get()))
			.operation(Operation.LOSE_SUPERTYPES.get())
			.operation(TypesOperation.Add.of(VTTypes.ADUAIN.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.CLIMB.get().instance(AbilitySource.TEMPLATE))).build());
//	public static final Supplier<Template> SIAR			= register(prefix("siar"), () -> Template.Builder.of(prefix("siar"))	// Lich
//			.power(4)
//			.condition(Precondition.IS_LIVING.get())
//			.operation(TypesOperation.Set.of(VTTypes.UNDEAD.get())).build());
	public static final Supplier<Template> REPTILIAN	= register(prefix("reptilian"), () -> Template.Builder.of(prefix("reptilian"))
			.power(2)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.All.of(VTTypes.HUMAN.get()))
			.condition(TypeCondition.None.of(VTTypes.REPTILIAN.get()))
			.operation(TypesOperation.Add.of(VTTypes.REPTILIAN.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.NIGHT_VISION.get().instance(AbilitySource.TEMPLATE))).build());
//	public static final Supplier<Template> VAMPIRE		= register(prefix("vampire"), () -> Template.Builder.of(prefix("vampire"))
//			.power(8)
//			.condition(Precondition.IS_LIVING.get())
//			.operation(TypesOperation.Set.of(VTTypes.UNDEAD.get(), VTTypes.ALTERED.get()))
//			.operation(AbilityOperation.Add.of(VTAbilities.CLIMB.get().instance(AbilitySource.TEMPLATE))).build());
	public static final Supplier<Template> WINGED		= register(prefix("winged"), () -> Template.Builder.of(prefix("winged"))
			.power(2)
			.condition(TypeCondition.Any.of(VTTypes.ANIMAL.get(), VTTypes.HUMAN.get(), VTTypes.ARTHROPOD.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.FLY.get().instance(AbilitySource.TEMPLATE))).build());
	public static final Supplier<Template> ZOMBIE		= register(prefix("zombie"), () -> Template.Builder.of(prefix("zombie"))
			.power(1)
			.condition(Precondition.IS_LIVING.get())
			.operation(Operation.LOSE_DUMMY_SUBTYPES.get())
			.operation(Operation.LOSE_SUPERTYPES.get())
			.operation(TypesOperation.Add.of(VTTypes.UNDEAD.get())).build());
	
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
	
	public static Collection<Supplier<Template>> defaultTemplates() { return DEFAULTS.values(); }
}