package com.lying.command;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.describeSpecies;
import static com.lying.utility.VTUtils.describeTemplate;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability.AbilitySource;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilityInstance.AbilityNbt;
import com.lying.ability.AbilitySet;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementHome;
import com.lying.component.element.ElementNonLethal;
import com.lying.component.module.AbstractSheetModule;
import com.lying.component.module.ModuleCustomAbilities;
import com.lying.component.module.ModuleCustomCosmetics;
import com.lying.component.module.ModuleCustomHome;
import com.lying.component.module.ModuleCustomTypes;
import com.lying.component.module.ModuleTemplates;
import com.lying.init.VTAbilities;
import com.lying.init.VTCosmetics;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.type.DummyType;
import com.lying.type.Type;
import com.lying.type.TypeSet;
import com.lying.utility.Cosmetic;
import com.lying.utility.VTUtils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class VTCommands
{
	private static final SimpleCommandExceptionType FAILED_GENERIC = make("generic");
	private static final SimpleCommandExceptionType FAILED_NO_OPTIONS = make("list_empty");
	private static final SimpleCommandExceptionType FAILED_NO_SPECIES = make("no_species_selected");
	private static final SimpleCommandExceptionType FAILED_UNKNOWN_SPECIES = make("unrecognised_species");
	private static final SimpleCommandExceptionType FAILED_UNKNOWN_TEMPLATE = make("unrecognised_template");
	public static final SuggestionProvider<ServerCommandSource> SPECIES_IDS = SuggestionProviders.register(new Identifier("species"), (context, builder) -> CommandSource.suggestIdentifiers(VTSpeciesRegistry.instance().getAllIDs(), builder));
	public static final SuggestionProvider<ServerCommandSource> TEMPLATE_IDS = SuggestionProviders.register(new Identifier("templates"), (context, builder) -> CommandSource.suggestIdentifiers(VTTemplateRegistry.instance().getAllIDs(), builder));
	public static final SuggestionProvider<ServerCommandSource> MODULE_IDS = SuggestionProviders.register(new Identifier("modules"), (context, builder) -> CommandSource.suggestMatching(VTSheetModules.commandNames(), builder));
	public static final SuggestionProvider<ServerCommandSource> TYPE_IDS = SuggestionProviders.register(new Identifier("types"), (context, builder) -> CommandSource.suggestIdentifiers(VTTypes.typeIds(), builder));
	public static final SuggestionProvider<ServerCommandSource> ABILITY_IDS = SuggestionProviders.register(new Identifier("abilities"), (context, builder) -> CommandSource.suggestIdentifiers(VTAbilities.abilityIds(), builder));
	public static final SuggestionProvider<ServerCommandSource> COSMETIC_IDS = SuggestionProviders.register(new Identifier("cosmetics"), (context, builder) -> CommandSource.suggestIdentifiers(VTCosmetics.cosmeticIds(), builder));
	private static final String PLAYER = "player";
	private static final String SPECIES = "species";
	private static final String TEMPLATE = "template";
	private static final String TYPE = "type";
	private static final String ABILITY = "ability";
	private static final String COSMETIC = "cosmetic";
	
	private static SimpleCommandExceptionType make(String name)
	{
		return new SimpleCommandExceptionType(translate("command", "failed_"+name.toLowerCase()));
	}
	
	public static void init()
	{
		CommandRegistrationEvent.EVENT.register((dispatcher, access, environment) -> 
		{
			dispatcher.register(literal(Reference.ModInfo.MOD_ID).requires(source -> source.hasPermissionLevel(2))
					.then(literal("list")
						.then(literal("homes")
							.executes(context -> EditHome.tryListHomes(context.getSource())))
						.then(literal("species")
							.executes(context -> EditSpecies.tryListSpecies(context.getSource())))
						.then(literal("template")
							.executes(context -> EditTemplates.tryListTemplates(context.getSource()))))
					.then(argument(PLAYER, EntityArgumentType.player())
						.then(literal("create")
							.executes(context -> EditSheet.tryCreate(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
						.then(literal("reset")
							.executes(context -> EditSheet.tryReset(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
						.then(literal("randomize")
							.executes(context -> EditSheet.tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), VariousTypes.config.maxPower(), context.getSource()))
							.then(argument("power", IntegerArgumentType.integer(0))
								.executes(context -> EditSheet.tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), IntegerArgumentType.getInteger(context, "power"), context.getSource()))))
						.then(literal("get")
							.then(literal("power")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									
									int power = sheetOpt.get().power();
									context.getSource().sendFeedback(() -> translate("command", "get.power.success", player.getDisplayName(), power), true);
									return Math.min(power, 15);
								}))
							.then(literal("home")
								.executes(context -> EditHome.tryGetHome(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(literal("abilities")
								.executes(context -> EditAbilities.tryGetAbilities(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(literal("types")
								.executes(context -> EditTypes.tryGetTypes(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(argument("module", StringArgumentType.word()).suggests(MODULE_IDS)
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Supplier<AbstractSheetModule> module = VTSheetModules.byName(StringArgumentType.getString(context, "module"));
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty() || module == null)
										throw FAILED_GENERIC.create();
									
									return sheetOpt.get().module(module).describeTo(context.getSource(), player).run(context);
								})))
						.then(literal("apply")
							.then(literal("custom_cosmetics")
								.then(argument(COSMETIC, IdentifierArgumentType.identifier()).suggests(COSMETIC_IDS)
									.executes(context -> EditCosmetics.tryAddCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), context.getSource()))
									.then(argument("color", IntegerArgumentType.integer(0))
										.executes(context -> EditCosmetics.tryAddTintedCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), IntegerArgumentType.getInteger(context, "color"), context.getSource())))))
							.then(literal("custom_home")
								.then(argument("dimension", DimensionArgumentType.dimension())
									.executes(context -> EditHome.tryApplyHome(EntityArgumentType.getPlayer(context, PLAYER), DimensionArgumentType.getDimensionArgument(context, "dimension"), context.getSource()))))
							.then(literal("custom_types")
								.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS)
									.executes(context -> EditTypes.tryApplyType(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TYPE), context.getSource())))
								.then(argument("dummy", NbtCompoundArgumentType.nbtCompound())
									.executes(context -> EditTypes.tryApplyDummyType(EntityArgumentType.getPlayer(context, PLAYER), NbtCompoundArgumentType.getNbtCompound(context, "dummy"), context.getSource()))))
							.then(literal("custom_abilities")
								.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
									.executes(context -> EditAbilities.tryAddAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), context.getSource()))
									.then(argument("nbt", NbtCompoundArgumentType.nbtCompound())
										.executes(context -> EditAbilities.tryAddConfiguredAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), NbtCompoundArgumentType.getNbtCompound(context, "nbt"), context.getSource())))))
							.then(literal("nonlethal")
								.then(argument("amount", FloatArgumentType.floatArg())
									.executes(context -> 
									{
										ServerCommandSource source = context.getSource();
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										float amount = FloatArgumentType.getFloat(context, "amount");
										VariousTypes.getSheet(player).ifPresent(sheet -> 
										{
											ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
											nonlethal.accrue(amount, player.getMaxHealth(), player);
											source.sendFeedback(() -> translate("command", "nonlethal.success", amount, player.getDisplayName()), true);
										});
										return 15;
									})))
							.then(literal("species")
								.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
									.executes(context -> EditSpecies.tryApplySpecies(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), context.getSource()))))
							.then(literal("template")
								.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
									.executes(context -> EditTemplates.tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), false, context.getSource()))
									.then(argument("force", BoolArgumentType.bool())
										.executes(context -> EditTemplates.tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), BoolArgumentType.getBool(context, "force"), context.getSource()))))))
						.then(literal("remove")
							.then(literal("custom_cosmetics")
								.then(argument(COSMETIC, IdentifierArgumentType.identifier()).suggests(COSMETIC_IDS)
									.executes(context -> EditCosmetics.tryRemoveCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), context.getSource()))
									.then(argument("color", IntegerArgumentType.integer(0))
										.executes(context -> EditCosmetics.tryRemoveTintedCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), IntegerArgumentType.getInteger(context, "color"), context.getSource()))))
								.then(literal("all")
									.executes(context -> EditCosmetics.tryClearCosmetics(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))))
							.then(literal("custom_home")
								.executes(context -> EditHome.tryClearHome(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))
								.then(argument("dimension", DimensionArgumentType.dimension())
									.executes(context -> EditHome.tryRemoveHome(EntityArgumentType.getPlayer(context, PLAYER), DimensionArgumentType.getDimensionArgument(context, "dimension"), context.getSource()))))
							.then(literal("custom_abilities")
								.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
									.executes(context -> EditAbilities.tryRemoveAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), context.getSource())))
								.then(literal("all")
									.executes(context -> EditAbilities.tryClearAbilities(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))))
							.then(literal("species")
								.executes(context -> EditSpecies.tryClearSpecies(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))
								.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
									.executes(context -> EditSpecies.tryRemoveSpecies(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), context.getSource()))))
							.then(literal("template")
								.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
									.executes(context -> EditTemplates.tryRemoveTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), context.getSource())))
								.then(literal("all")
									.executes(context -> EditTemplates.tryClearTemplates(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))))
							.then(literal("custom_types")
								.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS)
									.executes(context -> EditTypes.tryRemoveType(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TYPE), context.getSource())))
								.then(argument("dummy", NbtCompoundArgumentType.nbtCompound())
									.executes(context -> EditTypes.tryRemoveDummyType(EntityArgumentType.getPlayer(context, PLAYER), NbtCompoundArgumentType.getNbtCompound(context, "dummy"), context.getSource())))
								.then(literal("all")
									.executes(context -> EditTypes.tryClearTypes(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))))))
					.then(literal("test")));	// TODO Implement variants for detecting types/species/templates/abilities
		});
	}
	
	private static class EditSheet
	{
		private static int tryCreate(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			VariousTypes.getSheet(player).ifPresent(sheet ->
			player.openHandledScreen(new SimpleNamedScreenHandlerFactory((id, playerInventory, custom) -> new CharacterCreationScreenHandler(id, playerInventory.player), player.getDisplayName())));
			return 15;
		}
		
		private static int tryReset(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			VariousTypes.getSheet(player).ifPresent(sheet -> 
			{
				sheet.clear();
				sheet.buildSheet();
				source.sendFeedback(() -> translate("command","reset.success", player.getDisplayName()), true);
			});
			return 15;
		}
		
		private static int tryRandomize(PlayerEntity player, int power, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			sheetOpt.ifPresent(sheet -> sheet.clone(VTUtils.makeRandomSheet(player, power), true));
			source.sendFeedback(() -> translate("command", "randomize.success", player.getDisplayName()), true);
			return 15;
		}
	}
	
	private static class EditHome
	{
		private static int tryListHomes(ServerCommandSource source) throws CommandSyntaxException
		{
			Registry<DimensionOptions> registry = source.getServer().getCombinedDynamicRegistries().getCombinedRegistryManager().get(RegistryKeys.DIMENSION);
			Map<Identifier, RegistryKey<World>> worlds = new HashMap<>();
			for(Map.Entry<RegistryKey<DimensionOptions>, DimensionOptions> entry : registry.getEntrySet())
			{
				RegistryKey<DimensionOptions> option = entry.getKey();
				RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, option.getValue());
				worlds.put(option.getValue(), dimKey);
			}
			List<Identifier> set = Lists.newArrayList();
			set.addAll(worlds.keySet());
			if(set.isEmpty())
				throw FAILED_NO_OPTIONS.create();
			source.sendFeedback(() -> translate("command","list.homes.success", registry.size()), true);
			Collections.sort(set, (spec1, spec2) -> VTUtils.stringComparator(spec1.toString(), spec2.toString()));
			set.forEach(spec -> source.sendMessage(Text.literal(" * ").append(spec.toString())));
			return Math.min(set.size(), 15);
		}
		
		private static int tryGetHome(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			sheetOpt.ifPresent(sheet -> 
			{
				String home = ElementHome.get(sheet).getValue().toString();
				source.sendFeedback(() -> translate("command", "get.home.success", player.getDisplayName(), home), true);
			});
			return 15;
		}
		
		private static int tryApplyHome(PlayerEntity player, ServerWorld world, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || world == null)
				throw FAILED_GENERIC.create();
			
			RegistryKey<World> dim = world.getRegistryKey();
			sheetOpt.ifPresent(sheet -> 
			{
				sheet.module(VTSheetModules.HOME).set(dim);
				source.sendFeedback(() -> translate("command", "apply.custom_home.success", player.getDisplayName(), dim.getValue().toString()), true);
			});
			return 15;
		}
		
		private static int tryRemoveHome(PlayerEntity player, ServerWorld world, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomHome customHome = sheetOpt.get().module(VTSheetModules.HOME);
			if(!customHome.isPresent() || world == null || !world.getRegistryKey().equals(customHome.get()))
				throw FAILED_GENERIC.create();
			
			source.sendFeedback(() -> translate("command","custom_home.remove.success", player.getDisplayName(), ElementHome.get(sheetOpt.get()).getValue().toString()), true);
			return 15;
		}
		
		private static int tryClearHome(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else
				sheetOpt.ifPresent(sheet -> 
				{
					sheet.module(VTSheetModules.HOME).set(null);
					source.sendFeedback(() -> translate("command","custom_home.clear.success", player.getDisplayName(), ElementHome.get(sheet).getValue().toString()), true);
				});
			return 15;
		}
	}
	
	private static class EditSpecies
	{
		private static int tryListSpecies(ServerCommandSource source) throws CommandSyntaxException
		{
			List<Species> set = Lists.newArrayList();
			set.addAll(VTSpeciesRegistry.instance().getAll());
			if(set.isEmpty())
				throw FAILED_NO_OPTIONS.create();
			source.sendFeedback(() -> translate("command","list.species.success", set.size()), true);
			Collections.sort(set, (spec1, spec2) -> VTUtils.stringComparator(spec1.displayName().getString(), spec2.displayName().getString()));
			set.forEach(spec -> source.sendMessage(Text.literal(" * ").append(describeSpecies(spec))));
			return Math.min(set.size(), 15);
		}
		
		private static int tryApplySpecies(PlayerEntity player, Identifier species, ServerCommandSource source) throws CommandSyntaxException
		{
			if(VTSpeciesRegistry.instance().get(species).isEmpty())
				throw FAILED_UNKNOWN_SPECIES.create();
			Text speciesName = describeSpecies(VTSpeciesRegistry.instance().get(species).get());
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else if(sheetOpt.get().module(VTSheetModules.SPECIES).is(species))
				throw new SimpleCommandExceptionType(translate("command","species.apply.failed", player.getDisplayName(), speciesName)).create();
			else
				sheetOpt.ifPresent(sheet -> 
				{
					sheet.module(VTSheetModules.SPECIES).set(species);
					source.sendFeedback(() -> translate("command","species.apply.success", player.getDisplayName(), speciesName), true);
				});
			return 15;
		}
		
		private static int tryRemoveSpecies(PlayerEntity player, Identifier species, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Species> specOpt = VTSpeciesRegistry.instance().get(species);
			Text speciesName = specOpt.isPresent() ? describeSpecies(specOpt.get()) : Text.literal(species.toString());
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else if(sheetOpt.get().module(VTSheetModules.SPECIES).getMaybe().isEmpty())
				throw FAILED_NO_SPECIES.create();
			else if(!sheetOpt.get().module(VTSheetModules.SPECIES).is(species))
				throw new SimpleCommandExceptionType(translate("command","species.remove.failed", player.getDisplayName(), speciesName)).create();
			
			sheetOpt.ifPresent(sheet -> 
			{
				sheet.module(VTSheetModules.SPECIES).set(null);
				source.sendFeedback(() -> translate("command", "species.remove.success", speciesName, player.getDisplayName()), true);
			});
			return 15;
		}
		
		private static int tryClearSpecies(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else if(sheetOpt.get().module(VTSheetModules.SPECIES).getMaybe().isEmpty())
				throw FAILED_NO_SPECIES.create();
			
			Text oldSpecies = describeSpecies(sheetOpt.get().module(VTSheetModules.SPECIES).getMaybe().get());
			sheetOpt.ifPresent(sheet -> 
			{
				sheet.module(VTSheetModules.SPECIES).set(null);
				source.sendFeedback(() -> translate("command", "species.remove.success", oldSpecies, player.getDisplayName()), true);
			});
			return 15;
		}
	}
	
	private static class EditTemplates
	{
		private static int tryListTemplates(ServerCommandSource source) throws CommandSyntaxException
		{
			List<Template> set = Lists.newArrayList();
			set.addAll(VTTemplateRegistry.instance().getAll());
			if(set.isEmpty())
				throw FAILED_NO_OPTIONS.create();
			source.sendFeedback(() -> translate("command","list.templates.success", set.size()), true);
			Collections.sort(set, (tem1, tem2) -> VTUtils.stringComparator(tem1.displayName().getString(), tem2.displayName().getString()));
			set.forEach(tem -> source.sendMessage(Text.literal(" * ").append(describeTemplate(tem))));
			return Math.min(set.size(), 15);
		}
		
		private static int tryApplyTemplate(PlayerEntity player, Identifier template, boolean force, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Template> tem = VTTemplateRegistry.instance().get(template);
			if(tem.isEmpty())
				throw FAILED_UNKNOWN_TEMPLATE.create();
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else if(ModuleTemplates.hasTemplate(sheetOpt.get(), template))
				throw new SimpleCommandExceptionType(translate("command", "template.apply.failed.present", player.getDisplayName(), describeTemplate(tem.get()))).create();
			else if(!tem.get().validFor(sheetOpt.get(), player) && !force)
				throw new SimpleCommandExceptionType(translate("command", "template.apply.failed.invalid", describeTemplate(tem.get()), player.getDisplayName())).create();
			
			ModuleTemplates templates = sheetOpt.get().module(VTSheetModules.TEMPLATES);
			if(templates.addTemplate(template, (ServerPlayerEntity)player))
				source.sendFeedback(() -> translate("command", "template.apply.success", describeTemplate(tem.get()), player.getDisplayName()), true);
			else
				throw new SimpleCommandExceptionType(translate("command", "template.apply.failed.present", player.getDisplayName(), describeTemplate(tem.get()))).create();
			
			return 15;
		}
		
		private static int tryRemoveTemplate(PlayerEntity player, Identifier template, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Template> tempOpt = VTTemplateRegistry.instance().get(template);
			Text templateName = tempOpt.isPresent() ? describeTemplate(tempOpt.get()) : Text.literal(template.toString());
			
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else if(!ModuleTemplates.hasTemplate(sheetOpt.get(), template))
				throw new SimpleCommandExceptionType(translate("command","template.remove.failed.missing", player.getDisplayName(), templateName)).create();
			else
				sheetOpt.ifPresent(sheet -> 
				{
					sheet.module(VTSheetModules.TEMPLATES).remove(template);
					source.sendFeedback(() -> translate("command", "template.remove.success", templateName, player.getDisplayName()), true);
				});
			return 15;
		}
		
		private static int tryClearTemplates(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else
				sheetOpt.ifPresent(sheet -> 
				{
					sheet.module(VTSheetModules.TEMPLATES).clear();
					source.sendFeedback(() -> translate("command", "template.remove.all.success", player.getDisplayName()), true);
				});
			return 15;
		}
	}
	
	private static class EditTypes
	{
		private static int tryGetTypes(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else
				sheetOpt.ifPresent(sheet -> source.sendFeedback(() -> translate("command","get.types.success", player.getDisplayName(), sheet.<TypeSet>elementValue(VTSheetElements.TYPES).asNameList()), true));
			return 15;
		}
		
		private static int tryApplyType(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			Type type = VTTypes.get(registryId);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || type == null)
				throw FAILED_GENERIC.create();
			
			ModuleCustomTypes custTypes = sheetOpt.get().module(VTSheetModules.TYPES);
			if(!custTypes.add(type))
				throw new SimpleCommandExceptionType(translate("command","custom_types.add.failed.present", player.getDisplayName(), VTUtils.describeType(type))).create();
			
			source.sendFeedback(() -> translate("command", "custom_types.add.success", VTUtils.describeType(type), player.getDisplayName()), true);
			return 15;
		}
		
		private static int tryApplyDummyType(PlayerEntity player, NbtCompound nbt, ServerCommandSource source) throws CommandSyntaxException
		{
			Type type = DummyType.fromNbt(nbt);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || type == null)
				throw FAILED_GENERIC.create();
			
			ModuleCustomTypes custTypes = sheetOpt.get().module(VTSheetModules.TYPES);
			if(!custTypes.add(type))
				throw new SimpleCommandExceptionType(translate("command","custom_types.add.failed.present", player.getDisplayName(), VTUtils.describeType(type))).create();
			source.sendFeedback(() -> translate("command", "custom_types.add.success", VTUtils.describeType(type), player.getDisplayName()), true);
			return 15;
		}
		
		private static int tryRemoveType(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			Type type = VTTypes.get(registryId);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || type == null)
				throw FAILED_GENERIC.create();
			ModuleCustomTypes custTypes = sheetOpt.get().module(VTSheetModules.TYPES);
			if(!custTypes.remove(type))
				throw new SimpleCommandExceptionType(translate("command","custom_types.remove.failed.missing", player.getDisplayName(), VTUtils.describeType(type))).create();
			
			source.sendFeedback(() -> translate("command", "custom_types.remove.success", VTUtils.describeType(type), player.getDisplayName()), true);
			return 15;
		}
		
		private static int tryRemoveDummyType(PlayerEntity player, NbtCompound nbt, ServerCommandSource source) throws CommandSyntaxException
		{
			Type type = DummyType.fromNbt(nbt);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || type == null)
				throw FAILED_GENERIC.create();
			ModuleCustomTypes custTypes = sheetOpt.get().module(VTSheetModules.TYPES);
			
			if(custTypes.has(type))
			{
				final Type typeToRemove = custTypes.get(type.listID()).get();
				custTypes.remove(typeToRemove);
				source.sendFeedback(() -> translate("command", "custom_types.remove.success", VTUtils.describeType(typeToRemove), player.getDisplayName()), true);
			}
			else
				throw new SimpleCommandExceptionType(translate("command","custom_types.remove.failed.missing", player.getDisplayName(), VTUtils.describeType(type))).create();
			
			return 15;
		}
		
		private static int tryClearTypes(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			sheetOpt.get().module(VTSheetModules.TYPES).clear();
			source.sendFeedback(() -> translate("command", "custom_types.remove.all.success", player.getDisplayName()), true);
			return 15;
		}
	}
	
	private static class EditAbilities
	{
		private static int tryGetAbilities(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			else
				sheetOpt.ifPresent(sheet -> 
				{
					AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
					source.sendFeedback(() -> translate("command","get.abilities.success", player.getDisplayName(), abilities.size()), true);
					
					List<Text> entries = Lists.newArrayList();
					abilities.abilities().forEach(inst -> entries.add(Text.literal(" * ").append(VTUtils.describeAbility(inst))));
					entries.sort((a,b) -> VTUtils.stringComparator(a.getString(), b.getString()));
					entries.forEach(entry -> source.sendFeedback(() -> entry, false));
				});
			return 15;
		}
		
		private static int tryAddAbility(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			if(VTAbilities.get(registryId) == null)
				throw FAILED_GENERIC.create();
			
			return tryApplyAbility(player, VTAbilities.get(registryId).instance(AbilitySource.CUSTOM), source);
		}
		
		private static int tryAddConfiguredAbility(PlayerEntity player, Identifier registryId, NbtCompound nbt, ServerCommandSource source) throws CommandSyntaxException
		{
			if(VTAbilities.get(registryId) == null)
				throw FAILED_GENERIC.create();
			
			AbilityInstance inst = VTAbilities.get(registryId).instance(AbilitySource.CUSTOM);
			AbilityNbt.readFromNbt(nbt).applyTo(inst);
			
			return tryApplyAbility(player, inst, source);
		}
		
		private static int tryApplyAbility(PlayerEntity player, AbilityInstance inst, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomAbilities custAbilities = sheetOpt.get().module(VTSheetModules.ABILITIES);
			if(custAbilities.has(inst.mapName()))
					custAbilities.remove(inst.mapName());
			
			custAbilities.add(inst);
			source.sendFeedback(() -> translate("command", "custom_abilities.add.success", VTUtils.describeAbility(inst), player.getDisplayName()), true);
			return 15;
		}
		
		private static int tryRemoveAbility(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomAbilities custAbilities = sheetOpt.get().module(VTSheetModules.ABILITIES);
			try
			{
				AbilityInstance inst = custAbilities.get(registryId).copy();
				custAbilities.remove(registryId);
				source.sendFeedback(() -> translate("command", "custom_abilities.remove.success", VTUtils.describeAbility(inst), player.getDisplayName()), true);
			}
			catch(Exception e) { source.sendFeedback(() -> translate("command", "custom_abilities.remove.failed", registryId.toString(), player.getDisplayName()), true); }
			return 15;
		}
		
		private static int tryClearAbilities(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomAbilities custAbilities = sheetOpt.get().module(VTSheetModules.ABILITIES);
			int size = custAbilities.size();
			custAbilities.clear();
			source.sendFeedback(() -> translate("command", "custom_abilities.remove.all.success", size, player.getDisplayName()), true);
			return 15;
		}
	}
	
	private static class EditCosmetics
	{
		private static int tryAddCosmetic(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			return tryAddTintedCosmetic(player, registryId, -1, source);
		}
		
		private static int tryAddTintedCosmetic(PlayerEntity player, Identifier registryId, int colour, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Cosmetic> cosmetic = VTCosmetics.get(registryId);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || cosmetic.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomCosmetics custCosmetics = sheetOpt.get().module(VTSheetModules.COSMETICS);
			custCosmetics.add(cosmetic.get().tint(colour));
			source.sendFeedback(() -> translate("command", "custom_cosmetics.add.success", cosmetic.get().registryName().toString(), player.getName()), true);
			return 15;
		}
		
		private static int tryRemoveCosmetic(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Cosmetic> cosmetic = VTCosmetics.get(registryId);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || cosmetic.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomCosmetics custCosmetics = sheetOpt.get().module(VTSheetModules.COSMETICS);
			if(custCosmetics.remove(cosmetic.get().registryName()))
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.success", cosmetic.get().registryName().toString(), player.getName()), true);
			else
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.failed", cosmetic.get().registryName().toString(), player.getName()), true);
			return 15;
		}
		
		private static int tryRemoveTintedCosmetic(PlayerEntity player, Identifier registryId, int colour, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<Cosmetic> cosmetic = VTCosmetics.get(registryId);
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty() || cosmetic.isEmpty())
				throw FAILED_GENERIC.create();

			ModuleCustomCosmetics custCosmetics = sheetOpt.get().module(VTSheetModules.COSMETICS);
			
			Cosmetic cos = cosmetic.get().tint(colour);
			if(custCosmetics.remove(cos.registryName(), colour))
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.tinted.success", player.getName()), true);
			else
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.tinted.failed", player.getName()), true);
			return 15;
		}
		
		private static int tryClearCosmetics(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
		{
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				throw FAILED_GENERIC.create();
			
			ModuleCustomCosmetics custCosmetics = sheetOpt.get().module(VTSheetModules.COSMETICS);
			int count = custCosmetics.get().size();
			if(custCosmetics.get().isEmpty())
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.all.failed", player.getName()), true);
			else
			{
				custCosmetics.clear();
				source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.all.success", count, player.getName()), true);
			}
			return 15;
		}
	}
}
