package com.lying.template;

import static com.lying.reference.Reference.ModInfo.prefix;
import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.lying.ability.SingleAttributeAbility;
import com.lying.init.VTAbilities;
import com.lying.init.VTTypes;
import com.lying.template.operation.AbilityOperation;
import com.lying.template.operation.Operation;
import com.lying.template.operation.TypesOperation;
import com.lying.template.precondition.Precondition;
import com.lying.template.precondition.TypeCondition;

import net.minecraft.util.Identifier;

public class TemplateDefaults
{
	private static final Map<Identifier, Supplier<Template>> DEFAULTS = new HashMap<>();
	
	public static final Supplier<Template> AQUATIC		= register(prefix("aquatic"), () -> Template.Builder.of(prefix("aquatic"))
			.power(1)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.None.of(VTTypes.AQUATIC.get()))
			.operation(TypesOperation.Add.of(VTTypes.AQUATIC.get())).build());
	public static final Supplier<Template> DLUITH		= register(prefix("dluith"), () -> Template.Builder.of(prefix("dluith"))	// Half-Aberration
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.None.of(VTTypes.ADUAIN.get()))
			.operation(TypesOperation.SetSupertypes.of(VTTypes.ADUAIN.get())).build());
	public static final Supplier<Template> GRAVEKIN		= register(prefix("gravekin"), () -> Template.Builder.of(prefix("gravekin")).description(translate("template","gravekin.desc"))
			.power(0)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.None.of(VTTypes.OOZE.get(), VTTypes.ADUAIN.get()))
			.operation(TypesOperation.SetSupertypes.of(VTTypes.UNDEAD.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.MENDING.get())).build());
	public static final Supplier<Template> INSECTILE	= register(prefix("insectile"), () -> Template.Builder.of(prefix("insectile")).description(translate("template","insectile.desc"))
			.power(2)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.All.of(VTTypes.HUMAN.get()))
			.operation(TypesOperation.SetSupertypes.of(VTTypes.ARTHROPOD.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.CLIMB.get(), VTAbilities.SCULK_SIGHT.get()))
			.operation(AbilityOperation.Add.of(SingleAttributeAbility.Armour.of(2))).build());
	public static final Supplier<Template> SIAR			= register(prefix("siar"), () -> Template.Builder.of(prefix("siar"))	// Lich
			.power(4)
			.condition(TypeCondition.Any.of(VTTypes.HUMAN.get(), VTTypes.OTHALL.get(), VTTypes.DRAGON.get()))
			.operation(TypesOperation.Set.of(VTTypes.UNDEAD.get()))
			.operation(AbilityOperation.Add.of(SingleAttributeAbility.Armour.of(5))).build());
	public static final Supplier<Template> REPTILIAN	= register(prefix("reptilian"), () -> Template.Builder.of(prefix("reptilian")).description(translate("template","reptilian.desc"))
			.power(2)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.All.of(VTTypes.HUMAN.get()))
			.condition(TypeCondition.None.of(VTTypes.REPTILIAN.get()))
			.operation(TypesOperation.Add.of(VTTypes.REPTILIAN.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.NIGHT_VISION.get(), VTAbilities.DEEP_BREATH.get()))
			.operation(AbilityOperation.Add.of(SingleAttributeAbility.Armour.of(2))).build());
	public static final Supplier<Template> VAMPIRE		= register(prefix("vampire"), () -> Template.Builder.of(prefix("vampire"))
			.power(8)
			.condition(TypeCondition.Any.of(VTTypes.HUMAN.get(), VTTypes.OTHALL.get(), VTTypes.DRAGON.get()))
			.operation(TypesOperation.Set.of(VTTypes.UNDEAD.get(), VTTypes.ALTERED.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.CLIMB.get(), VTAbilities.BURN_IN_SUN.get()))
			.operation(AbilityOperation.Add.of(SingleAttributeAbility.Armour.of(6))).build());
	public static final Supplier<Template> WINGED		= register(prefix("winged"), () -> Template.Builder.of(prefix("winged")).description(translate("template","winged.desc"))
			.power(2)
			.condition(TypeCondition.Any.of(VTTypes.ANIMAL.get(), VTTypes.HUMAN.get(), VTTypes.ARTHROPOD.get()))
			.operation(AbilityOperation.Add.of(VTAbilities.FLY.get())).build());
	public static final Supplier<Template> ZOMBIE		= register(prefix("zombie"), () -> Template.Builder.of(prefix("zombie"))
			.power(1)
			.condition(Precondition.IS_LIVING.get())
			.condition(TypeCondition.None.of(VTTypes.OOZE.get(), VTTypes.ADUAIN.get()))
			.operation(TypesOperation.SetSupertypes.of(VTTypes.UNDEAD.get()))
			.operation(Operation.LOSE_DUMMY_SUBTYPES.get())
			.operation(AbilityOperation.Add.of(VTAbilities.BURN_IN_SUN.get(), VTAbilities.MINDLESS.get(), VTAbilities.DROPS.get()))
			.operation(AbilityOperation.Add.of(SingleAttributeAbility.Armour.of(2))).build());
	
	private static Supplier<Template> register(Identifier name, Supplier<Template> template)
	{
		DEFAULTS.put(name, template);
		return template;
	}
	
	public static Collection<Supplier<Template>> defaultTemplates() { return DEFAULTS.values(); }
}