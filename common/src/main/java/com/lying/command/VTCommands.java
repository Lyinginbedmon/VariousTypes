package com.lying.command;

import static com.lying.reference.Reference.ModInfo.translate;
import static com.lying.utility.VTUtils.describeSpecies;
import static com.lying.utility.VTUtils.describeTemplate;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
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
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;
import com.lying.init.VTSheetElements;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.init.VTTypes;
import com.lying.reference.Reference;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.template.Template;
import com.lying.type.DummyType;
import com.lying.type.Type;
import com.lying.type.TypeSet;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;
import com.lying.utility.VTUtils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.serialization.Codec;

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
import net.minecraft.util.StringIdentifiable;
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
	public static final SuggestionProvider<ServerCommandSource> COSMETIC_TYPE_IDS = SuggestionProviders.register(new Identifier("cosmetic_types"), (context, builder) -> CommandSource.suggestIdentifiers(VTCosmeticTypes.typeIds(), builder));
	private static final String PLAYER = "player";
	private static final String SPECIES = "species";
	private static final String TEMPLATE = "template";
	private static final String TYPE = "type";
	private static final String ABILITY = "ability";
	private static final String COSMETIC = "cosmetic";
	private static final String MODE = "mode";
	
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
							.executes(context -> Edit.Home.tryListHomes(context.getSource())))
						.then(literal("species")
							.executes(context -> Edit.Species.tryListSpecies(context.getSource())))
						.then(literal("templates")
							.executes(context -> Edit.Templates.tryListTemplates(context.getSource()))))
					
					.then(argument(PLAYER, EntityArgumentType.player())
						.then(literal("create")
							.executes(context -> Edit.Sheet.tryCreate(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
						.then(literal("reset")
							.executes(context -> Edit.Sheet.tryReset(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
						.then(literal("randomize")
							.executes(context -> Edit.Sheet.tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), VariousTypes.config.maxPower(), context.getSource()))
							.then(argument("power", IntegerArgumentType.integer(0))
								.executes(context -> Edit.Sheet.tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), IntegerArgumentType.getInteger(context, "power"), context.getSource()))))
						
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
								.executes(context -> Edit.Home.tryGetHome(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(literal("abilities")
								.executes(context -> Edit.Abilities.tryGetAbilities(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(literal("types")
								.executes(context -> Edit.Types.tryGetTypes(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
							.then(argument("module", StringArgumentType.word()).suggests(MODULE_IDS)
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Supplier<AbstractSheetModule> module = VTSheetModules.byName(StringArgumentType.getString(context, "module"));
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty() || module == null)
										throw FAILED_GENERIC.create();
									
									return sheetOpt.get().module(module).describeTo(context.getSource(), player).run(context);
								}))
							.then(literal("nonlethal")
								.executes(context -> Edit.Nonlethal.tryGetNonlethal(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))))
						
						.then(literal("edit")
							
							.then(literal("custom_cosmetics")
								.then(literal("apply")
									.then(argument(COSMETIC, IdentifierArgumentType.identifier()).suggests(COSMETIC_IDS)
										.executes(context -> Edit.Cosmetics.tryAddCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), context.getSource()))
										.then(argument("color", IntegerArgumentType.integer(0))
											.executes(context -> Edit.Cosmetics.tryAddTintedCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), IntegerArgumentType.getInteger(context, "color"), context.getSource())))))
								.then(literal("remove")
									.then(argument(COSMETIC, IdentifierArgumentType.identifier()).suggests(COSMETIC_IDS)
										.executes(context -> Edit.Cosmetics.tryRemoveCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), context.getSource()))
										.then(argument("color", IntegerArgumentType.integer(0))
											.executes(context -> Edit.Cosmetics.tryRemoveTintedCosmetic(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, COSMETIC), IntegerArgumentType.getInteger(context, "color"), context.getSource()))))
									.then(literal("all")
										.executes(context -> Edit.Cosmetics.tryClearCosmetics(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
									.then(literal("category")
										.then(argument("category", IdentifierArgumentType.identifier()).suggests(COSMETIC_TYPE_IDS)
											.executes(context -> Edit.Cosmetics.tryRemoveCategory(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, "category"), context.getSource()))))))
							
							.then(literal("custom_home")
								.then(literal("apply")
									.then(argument("dimension", DimensionArgumentType.dimension())
										.executes(context -> Edit.Home.tryApplyHome(EntityArgumentType.getPlayer(context, PLAYER), DimensionArgumentType.getDimensionArgument(context, "dimension"), context.getSource()))))
								.then(literal("remove")
									.executes(context -> Edit.Home.tryClearHome(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))
									.then(argument("dimension", DimensionArgumentType.dimension())
										.executes(context -> Edit.Home.tryRemoveHome(EntityArgumentType.getPlayer(context, PLAYER), DimensionArgumentType.getDimensionArgument(context, "dimension"), context.getSource())))))
							
							.then(literal("custom_types")
								.then(literal("apply")
									.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS)
										.executes(context -> Edit.Types.tryApplyType(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TYPE), context.getSource())))
									.then(argument("dummy", NbtCompoundArgumentType.nbtCompound())
										.executes(context -> Edit.Types.tryApplyDummyType(EntityArgumentType.getPlayer(context, PLAYER), NbtCompoundArgumentType.getNbtCompound(context, "dummy"), context.getSource()))))
								.then(literal("remove")
									.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS)
										.executes(context -> Edit.Types.tryRemoveType(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TYPE), context.getSource())))
									.then(argument("dummy", NbtCompoundArgumentType.nbtCompound())
										.executes(context -> Edit.Types.tryRemoveDummyType(EntityArgumentType.getPlayer(context, PLAYER), NbtCompoundArgumentType.getNbtCompound(context, "dummy"), context.getSource())))
									.then(literal("all")
										.executes(context -> Edit.Types.tryClearTypes(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))))
							
							.then(literal("custom_abilities")
								.then(literal("apply")
									.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
										.executes(context -> Edit.Abilities.tryAddAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), context.getSource()))
										.then(argument("nbt", NbtCompoundArgumentType.nbtCompound())
											.executes(context -> Edit.Abilities.tryAddConfiguredAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), NbtCompoundArgumentType.getNbtCompound(context, "nbt"), context.getSource())))))
								.then(literal("remove")
									.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
										.executes(context -> Edit.Abilities.tryRemoveAbility(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), context.getSource())))
									.then(literal("all")
										.executes(context -> Edit.Abilities.tryClearAbilities(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))))
							
							.then(literal("nonlethal")
								.then(literal("apply")
									.then(argument("amount", FloatArgumentType.floatArg(0))
										.executes(context -> Edit.Nonlethal.tryAddNonlethal(EntityArgumentType.getPlayer(context, PLAYER), FloatArgumentType.getFloat(context, "amount"), context.getSource()))))
								.then(literal("remove")
									.then(argument("amount", FloatArgumentType.floatArg(0))
										.executes(context -> Edit.Nonlethal.tryRemoveNonlethal(EntityArgumentType.getPlayer(context, PLAYER), FloatArgumentType.getFloat(context, "amount"), context.getSource()))))
								.then(literal("clear")
									.executes(context -> Edit.Nonlethal.tryClearNonlethal(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))
								.then(literal("set"))
									.then(argument("amount", FloatArgumentType.floatArg(0))
										.executes(context -> Edit.Nonlethal.trySetNonlethal(EntityArgumentType.getPlayer(context, PLAYER), FloatArgumentType.getFloat(context, "amount"), context.getSource()))))
							
							.then(literal("species")
								.then(literal("apply")
									.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
										.executes(context -> Edit.Species.tryApplySpecies(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), context.getSource()))))
								.then(literal("remove")
									.executes(context -> Edit.Species.tryClearSpecies(EntityArgumentType.getPlayer(context, PLAYER), context.getSource()))
									.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
										.executes(context -> Edit.Species.tryRemoveSpecies(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), context.getSource())))))
							
							.then(literal("templates")
								.then(literal("apply")
									.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
										.executes(context -> Edit.Templates.tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), false, context.getSource()))
										.then(argument("force", BoolArgumentType.bool())
											.executes(context -> Edit.Templates.tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), BoolArgumentType.getBool(context, "force"), context.getSource())))))
								.then(literal("remove")
									.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
										.executes(context -> Edit.Templates.tryRemoveTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), context.getSource())))
									.then(literal("all")
										.executes(context -> Edit.Templates.tryClearTemplates(EntityArgumentType.getPlayer(context, PLAYER), context.getSource())))))
							))
					.then(literal("test")	// TODO Implement variants for detecting types/species/templates/abilities/etc
						.then(argument(MODE, ModeArgumentType.mode())
							.then(argument(PLAYER, EntityArgumentType.players())
								.then(literal("species")
									.then(literal("is")
										.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
											.executes(context -> Test.Species.isOf(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), true, ModeArgumentType.getMode(context, MODE), context.getSource()))))
									.then(literal("is_not")
										.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
											.executes(context -> Test.Species.isOf(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, SPECIES), false, ModeArgumentType.getMode(context, MODE), context.getSource())))))
								
								.then(literal("templates"))
								
								.then(literal("types")
									.then(literal("is")
										.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS)))
									.then(literal("is_not")
										.then(argument(TYPE, IdentifierArgumentType.identifier()).suggests(TYPE_IDS))))
								
								.then(literal("abilities")
									.then(literal("has_any")
										.then(literal("map_name")
											.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
												.executes(context -> Test.Abilities.hasAnyById(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), (a,b) -> a.mapName().equals(b), ModeArgumentType.getMode(context, MODE), context.getSource()))))
										.then(literal("registry_name")
											.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
												.executes(context -> Test.Abilities.hasAnyById(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), (a,b) -> a.ability().registryName().equals(b), ModeArgumentType.getMode(context, MODE), context.getSource())))))
									.then(literal("has_none")
										.then(literal("map_name")
											.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
												.executes(context -> Test.Abilities.hasNoneById(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), (a,b) -> a.mapName().equals(b), ModeArgumentType.getMode(context, MODE), context.getSource()))))
										.then(literal("registry_name")
											.then(argument(ABILITY, IdentifierArgumentType.identifier()).suggests(ABILITY_IDS)
												.executes(context -> Test.Abilities.hasNoneById(EntityArgumentType.getPlayers(context, PLAYER), IdentifierArgumentType.getIdentifier(context, ABILITY), (a,b) -> a.ability().registryName().equals(b), ModeArgumentType.getMode(context, MODE), context.getSource()))))))
								
								.then(literal("cosmetics"))
								
								.then(literal("nonlethal")
									.then(literal("equal_to")
										.then(argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> Test.Nonlethal.compare(EntityArgumentType.getPlayers(context, PLAYER), IntegerArgumentType.getInteger(context, "amount"), (a,b) -> a==b, ModeArgumentType.getMode(context, MODE), context.getSource()))))
									.then(literal("unequal_to")
										.then(argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> Test.Nonlethal.compare(EntityArgumentType.getPlayers(context, PLAYER), IntegerArgumentType.getInteger(context, "amount"), (a,b) -> a!=b, ModeArgumentType.getMode(context, MODE), context.getSource()))))
									.then(literal("greater_than")
										.then(argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> Test.Nonlethal.compare(EntityArgumentType.getPlayers(context, PLAYER), IntegerArgumentType.getInteger(context, "amount"), (a,b) -> a>b, ModeArgumentType.getMode(context, MODE), context.getSource()))))
									.then(literal("less_than")
										.then(argument("amount", IntegerArgumentType.integer(0))
											.executes(context -> Test.Nonlethal.compare(EntityArgumentType.getPlayers(context, PLAYER), IntegerArgumentType.getInteger(context, "amount"), (a,b) -> a<b, ModeArgumentType.getMode(context, MODE), context.getSource())))))
								
								.then(literal("home"))
								)
							)));
		});
	}
	
	private static class Edit
	{
		private static class Sheet
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
		
		private static class Home
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
		
		private static class Species
		{
			private static int tryListSpecies(ServerCommandSource source) throws CommandSyntaxException
			{
				List<com.lying.species.Species> set = Lists.newArrayList();
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
				Optional<com.lying.species.Species> specOpt = VTSpeciesRegistry.instance().get(species);
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
		
		private static class Templates
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
		
		private static class Types
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
		
		private static class Abilities
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
		
		private static class Cosmetics
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
			
			private static int tryRemoveCategory(PlayerEntity player, Identifier registryId, ServerCommandSource source) throws CommandSyntaxException
			{
				Optional<CosmeticType> category = VTCosmeticTypes.get(registryId);
				Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
				if(sheetOpt.isEmpty() || category.isEmpty())
					throw FAILED_GENERIC.create();
				
				String categoryName = category.get().registryName().toString();
				ModuleCustomCosmetics custCosmetics = sheetOpt.get().module(VTSheetModules.COSMETICS);
				if(custCosmetics.removeAll(category.get()))
					source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.category.success", categoryName, player.getName()), true);
				else
					source.sendFeedback(() -> translate("command", "custom_cosmetics.remove.category.failed", categoryName, player.getName()), true);
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
		
		private static class Nonlethal
		{
			private static int tryGetNonlethal(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
			{
				Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
				if(sheetOpt.isEmpty())
					throw FAILED_GENERIC.create();
				
				sheetOpt.ifPresent(sheet -> 
				{
					float damage = sheet.elementValue(VTSheetElements.NONLETHAL);
					source.sendFeedback(() -> translate("command", "get.nonlethal.success", player.getDisplayName(), damage), true);
				});
				return 15;
			}
			
			private static int tryAddNonlethal(PlayerEntity player, float amount, ServerCommandSource source) throws CommandSyntaxException
			{
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
					nonlethal.accrue(amount, player.getMaxHealth(), player);
					source.sendFeedback(() -> translate("command", "nonlethal.apply.success", amount, player.getDisplayName()), true);
				});
				return 15;
			}
			
			private static int tryRemoveNonlethal(PlayerEntity player, float amount, ServerCommandSource source) throws CommandSyntaxException
			{
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
					nonlethal.accrue(-amount, player.getMaxHealth(), player);
					source.sendFeedback(() -> translate("command", "nonlethal.remove.success", amount, player.getDisplayName()), true);
				});
				return 15;
			}
			
			private static int tryClearNonlethal(PlayerEntity player, ServerCommandSource source) throws CommandSyntaxException
			{
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
					nonlethal.set(0F, player);
					source.sendFeedback(() -> translate("command", "nonlethal.clear.success", player.getDisplayName()), true);
				});
				return 15;
			}
			
			private static int trySetNonlethal(PlayerEntity player, float amount, ServerCommandSource source) throws CommandSyntaxException
			{
				VariousTypes.getSheet(player).ifPresent(sheet -> 
				{
					ElementNonLethal nonlethal = sheet.element(VTSheetElements.NONLETHAL);
					nonlethal.set(amount, player);
					source.sendFeedback(() -> translate("command", "nonlethal.set.success", amount, player.getDisplayName()), true);
				});
				return 15;
			}
		}
	}
	
	private static class Test
	{
		private static final SimpleCommandExceptionType TEST_FAILED = make("test");
		private static final Text TEST_PASSED = translate("command","test.success");
		
		private static int conductTest(Collection<ServerPlayerEntity> players, Function<CharacterSheet,Boolean> test, Mode mode, ServerCommandSource source) throws CommandSyntaxException
		{
			for(ServerPlayerEntity player : players)
			{
				Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
				if(sheetOpt.isPresent())
				{
					boolean result = test.apply(sheetOpt.get());
					switch(mode)
					{
						case ANY:
							if(result)
							{
								source.sendFeedback(() -> TEST_PASSED, true);
								return 15;
							}
							break;
						case NONE:
							if(result)
								throw TEST_FAILED.create();
							break;
						case ALL:
							if(!result)
								throw TEST_FAILED.create();
							break;
					}
				}
			}
			
			if(mode == Mode.ANY)
				throw TEST_FAILED.create();
			
			source.sendFeedback(() -> TEST_PASSED, true);
			return 15;
		}
		
		private static class Nonlethal
		{
			private static int compare(Collection<ServerPlayerEntity> players, int amount, BiPredicate<Integer, Integer> comparator, Mode mode, ServerCommandSource source) throws CommandSyntaxException
			{
				return conductTest(players, sheet -> comparator.test(((Float)sheet.elementValue(VTSheetElements.NONLETHAL)).intValue(), amount), mode, source);
			}
		}
		
		private static class Species
		{
			private static int isOf(Collection<ServerPlayerEntity> players, Identifier registryId, boolean matches, Mode mode, ServerCommandSource source) throws CommandSyntaxException
			{
				return conductTest(players, sheet -> sheet.module(VTSheetModules.SPECIES).is(registryId) == matches, mode, source);
			}
		}
		
		private static class Abilities
		{
			private static int hasAnyById(Collection<ServerPlayerEntity> players, Identifier abilityId, BiPredicate<AbilityInstance,Identifier> comparator, Mode mode, ServerCommandSource source) throws CommandSyntaxException
			{
				return conductTest(players, sheet -> 
				{
					AbilitySet abilities = sheet.element(VTSheetElements.ABILITIES);
					return abilities.abilities().stream().anyMatch(a -> comparator.test(a, abilityId));
				}, mode, source);
			}
			
			private static int hasNoneById(Collection<ServerPlayerEntity> players, Identifier abilityId, BiPredicate<AbilityInstance,Identifier> comparator, Mode mode, ServerCommandSource source) throws CommandSyntaxException
			{
				return conductTest(players, sheet -> 
				{
					AbilitySet abilities = sheet.element(VTSheetElements.ABILITIES);
					return abilities.abilities().stream().noneMatch(a -> comparator.test(a, abilityId));
				}, mode, source);
			}
		}
	}
	
	public static enum Mode implements StringIdentifiable
	{
		ANY,
		NONE,
		ALL;
		
		public static final Codec<Mode> CODEC = StringIdentifiable.createCodec(Mode::values);
		
		public String asString() { return name().toLowerCase(); }
	}
}
