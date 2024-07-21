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

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.component.module.ModuleHome;
import com.lying.init.VTSheetModules;
import com.lying.init.VTSpeciesRegistry;
import com.lying.init.VTTemplateRegistry;
import com.lying.reference.Reference;
import com.lying.screen.CharacterCreationScreenHandler;
import com.lying.species.Species;
import com.lying.template.Template;
import com.lying.utility.VTUtils;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
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
	private static final SimpleCommandExceptionType FAILED_NO_TEMPLATES = make("no_templates_applied");
	private static final SimpleCommandExceptionType FAILED_UNKNOWN_SPECIES = make("unrecognised_species");
	private static final SimpleCommandExceptionType FAILED_UNKNOWN_TEMPLATE = make("unrecognised_template");
	public static final SuggestionProvider<ServerCommandSource> SPECIES_IDS = SuggestionProviders.register(new Identifier("species"), (context, builder) -> CommandSource.suggestIdentifiers(VTSpeciesRegistry.instance().getAllIDs(), builder));
	public static final SuggestionProvider<ServerCommandSource> TEMPLATE_IDS = SuggestionProviders.register(new Identifier("templates"), (context, builder) -> CommandSource.suggestIdentifiers(VTTemplateRegistry.instance().getAllIDs(), builder));
	private static final String PLAYER = "player";
	private static final String SPECIES = "species";
	private static final String TEMPLATE = "template";
	
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
							.executes(context -> 
							{
								ServerCommandSource source = context.getSource();
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
							}))
						.then(literal("species")
							.executes(context -> 
							{
								ServerCommandSource source = context.getSource();
								List<Species> set = Lists.newArrayList();
								set.addAll(VTSpeciesRegistry.instance().getAll());
								if(set.isEmpty())
									throw FAILED_NO_OPTIONS.create();
								source.sendFeedback(() -> translate("command","list.species.success", set.size()), true);
								Collections.sort(set, (spec1, spec2) -> VTUtils.stringComparator(spec1.displayName().getString(), spec2.displayName().getString()));
								set.forEach(spec -> source.sendMessage(Text.literal(" * ").append(describeSpecies(spec))));
								return Math.min(set.size(), 15);
							}))
						.then(literal("template")
							.executes(context -> 
							{
								ServerCommandSource source = context.getSource();
								List<Template> set = Lists.newArrayList();
								set.addAll(VTTemplateRegistry.instance().getAll());
								if(set.isEmpty())
									throw FAILED_NO_OPTIONS.create();
								source.sendFeedback(() -> translate("command","list.templates.success", set.size()), true);
								Collections.sort(set, (tem1, tem2) -> VTUtils.stringComparator(tem1.displayName().getString(), tem2.displayName().getString()));
								set.forEach(tem -> source.sendMessage(Text.literal(" * ").append(describeTemplate(tem))));
								return Math.min(set.size(), 15);
							})))
					.then(argument(PLAYER, EntityArgumentType.player())
						.then(literal("create")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									VariousTypes.getSheet(player).ifPresent(sheet ->
										player.openHandledScreen(new SimpleNamedScreenHandlerFactory((id, playerInventory, custom) -> new CharacterCreationScreenHandler(id, playerInventory.player), player.getDisplayName())));
									return 15;
								}))
						.then(literal("reset")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									VariousTypes.getSheet(player).ifPresent(sheet -> 
									{
										sheet.clear(true);
										context.getSource().sendFeedback(() -> translate("command","reset.success", player.getDisplayName()), true);
									});
									return 15;
								}))
						.then(literal("randomize")
							.executes(context -> tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), VariousTypes.POWER, context.getSource()))	// TODO Replace static power with server config value
							.then(argument("power", IntegerArgumentType.integer(0))
								.executes(context -> tryRandomize(EntityArgumentType.getPlayer(context, PLAYER), IntegerArgumentType.getInteger(context, "power"), context.getSource()))))
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
								.executes(context ->
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									
									String home = sheetOpt.get().homeDimension().getValue().toString();
									if(sheetOpt.get().module(VTSheetModules.HOME).isPresent())
										context.getSource().sendFeedback(() -> translate("command", "get.home.success.custom", player.getDisplayName(), home), true);
									else
										context.getSource().sendFeedback(() -> translate("command", "get.home.success", player.getDisplayName(), home), true);
									return 15;
								}))
							.then(literal("species")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									else if(!sheetOpt.get().hasASpecies())
										throw FAILED_NO_SPECIES.create();
									context.getSource().sendFeedback(() -> translate("command","get.species.success", player.getDisplayName(), describeSpecies(sheetOpt.get().getSpecies().get())), true);
									return 15;
								}))
							.then(literal("templates")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									List<Template> templates;
									if((templates = sheetOpt.get().getTemplates()).isEmpty())
										throw FAILED_NO_TEMPLATES.create();
									
									context.getSource().sendFeedback(() -> translate("command","get.templates.success",player.getDisplayName(),templates.size()), true);
									templates.forEach(tem -> context.getSource().sendFeedback(() -> Text.literal(" * ").append(describeTemplate(tem)), false));
									return 15;
								})))
						.then(literal("apply")
							.then(literal("home")
								.then(argument("dimension", DimensionArgumentType.dimension())
									.executes(context -> 
									{
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										ServerWorld world = null;
										try
										{
											world = DimensionArgumentType.getDimensionArgument(context, "dimension");
										}
										catch(Exception e) { }
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty() || world == null)
											throw FAILED_GENERIC.create();
										
										RegistryKey<World> dim = world.getRegistryKey();
										sheetOpt.ifPresent(sheet -> 
										{
											sheet.setHomeDimension(dim);
											context.getSource().sendFeedback(() -> translate("command", "apply.home.success", player.getDisplayName(), dim.getValue().toString()), true);
										});
										return 15;
									})))
							.then(literal("species")
								.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
									.executes(context -> 
									{
										ServerCommandSource source = context.getSource();
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										Identifier species = IdentifierArgumentType.getIdentifier(context, SPECIES);
										if(VTSpeciesRegistry.instance().get(species).isEmpty())
											throw FAILED_UNKNOWN_SPECIES.create();
										Text speciesName = describeSpecies(VTSpeciesRegistry.instance().get(species).get());
										
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty())
											throw FAILED_GENERIC.create();
										else if(sheetOpt.get().isSpecies(species))
											throw new SimpleCommandExceptionType(translate("command","species.apply.failed", player.getDisplayName(), speciesName)).create();
										else
											sheetOpt.ifPresent(sheet -> 
											{
												sheet.setSpecies(species);
												source.sendFeedback(() -> translate("command","species.apply.success", player.getDisplayName(), speciesName), true);
											});
										return 15;
									})))
							.then(literal("template")
								.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
									.executes(context -> tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), false, context.getSource()))
									.then(argument("force", BoolArgumentType.bool())
										.executes(context -> tryApplyTemplate(EntityArgumentType.getPlayer(context, PLAYER), IdentifierArgumentType.getIdentifier(context, TEMPLATE), BoolArgumentType.getBool(context, "force"), context.getSource()))))))
						.then(literal("remove")
							.then(literal("home")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									else
										sheetOpt.ifPresent(sheet -> 
										{
											sheet.clearHomeDimension();
											context.getSource().sendFeedback(() -> translate("command","home.clear.success", player.getDisplayName(), sheet.homeDimension().getValue().toString()), true);
										});
									return 15;
								})
								.then(argument("dimension", DimensionArgumentType.dimension())
									.executes(context -> 
									{
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										ServerWorld world = null;
										try
										{
											world = DimensionArgumentType.getDimensionArgument(context, "dimension");
										}
										catch(Exception e) { }
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty())
											throw FAILED_GENERIC.create();
										
										ModuleHome customHome = sheetOpt.get().module(VTSheetModules.HOME);
										if(!customHome.isPresent() || world == null || !world.getRegistryKey().equals(customHome.get()))
											throw FAILED_GENERIC.create();
										
										context.getSource().sendFeedback(() -> translate("command","home.remove.success", player.getDisplayName(), sheetOpt.get().homeDimension().getValue().toString()), true);
										return 15;
									})))
							.then(literal("species")
								.executes(context -> 
								{
									PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
									Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
									if(sheetOpt.isEmpty())
										throw FAILED_GENERIC.create();
									else if(!sheetOpt.get().hasASpecies())
										throw FAILED_NO_SPECIES.create();
									
									Text oldSpecies = describeSpecies(sheetOpt.get().getSpecies().get());
									sheetOpt.ifPresent(sheet -> 
									{
										sheet.setSpecies(null);
										context.getSource().sendFeedback(() -> translate("command", "species.remove.success", oldSpecies, player.getDisplayName()), true);
									});
									return 15;
								})
								.then(argument(SPECIES, IdentifierArgumentType.identifier()).suggests(SPECIES_IDS)
									.executes(context -> 
									{
										Identifier species = IdentifierArgumentType.getIdentifier(context, SPECIES);
										Optional<Species> specOpt = VTSpeciesRegistry.instance().get(species);
										Text speciesName = specOpt.isPresent() ? describeSpecies(specOpt.get()) : Text.literal(species.toString());
										
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty())
											throw FAILED_GENERIC.create();
										else if(!sheetOpt.get().hasASpecies())
											throw FAILED_NO_SPECIES.create();
										else if(!sheetOpt.get().isSpecies(species))
											throw new SimpleCommandExceptionType(translate("command","species.remove.failed", player.getDisplayName(), speciesName)).create();
										
										sheetOpt.ifPresent(sheet -> 
										{
											sheet.setSpecies(null);
											context.getSource().sendFeedback(() -> translate("command", "species.remove.success", speciesName, player.getDisplayName()), true);
										});
										return 15;
									})))
							.then(literal("template")
								.then(argument(TEMPLATE, IdentifierArgumentType.identifier()).suggests(TEMPLATE_IDS)
									.executes(context -> 
									{
										ServerCommandSource source = context.getSource();
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										Identifier template = IdentifierArgumentType.getIdentifier(context, TEMPLATE);
										Optional<Template> tempOpt = VTTemplateRegistry.instance().get(template);
										Text templateName = tempOpt.isPresent() ? describeTemplate(tempOpt.get()) : Text.literal(template.toString());
										
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty())
											throw FAILED_GENERIC.create();
										else if(!sheetOpt.get().hasTemplate(template))
											throw new SimpleCommandExceptionType(translate("command","template.remove.failed.missing", player.getDisplayName(), templateName)).create();
										else
											sheetOpt.ifPresent(sheet -> 
											{
												sheet.removeTemplate(template);
												source.sendFeedback(() -> translate("command", "template.remove.success", templateName, player.getDisplayName()), true);
											});
										return 15;
									}))
								.then(literal("all")
									.executes(context -> 
									{
										ServerCommandSource source = context.getSource();
										PlayerEntity player = EntityArgumentType.getPlayer(context, PLAYER);
										Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
										if(sheetOpt.isEmpty())
											throw FAILED_GENERIC.create();
										else
											sheetOpt.ifPresent(sheet -> 
											{
												sheet.clearTemplates();
												source.sendFeedback(() -> translate("command", "template.remove.all.success", player.getDisplayName()), true);
											});
										return 15;
									}))))));
		});
	}
	
	private static int tryApplyTemplate(PlayerEntity player, Identifier template, boolean force, ServerCommandSource source) throws CommandSyntaxException
	{
		Optional<Template> tem = VTTemplateRegistry.instance().get(template);
		if(tem.isEmpty())
			throw FAILED_UNKNOWN_TEMPLATE.create();
		
		Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
		if(sheetOpt.isEmpty())
			throw FAILED_GENERIC.create();
		else if(sheetOpt.get().hasTemplate(template))
			throw new SimpleCommandExceptionType(translate("command", "template.apply.failed.present", player.getDisplayName(), describeTemplate(tem.get()))).create();
		else if(!tem.get().validFor(sheetOpt.get(), player) && !force)
			throw new SimpleCommandExceptionType(translate("command", "template.apply.failed.invalid", describeTemplate(tem.get()), player.getDisplayName())).create();
		
		sheetOpt.ifPresent(sheet -> 
		{
			sheet.addTemplate(template);
			source.sendFeedback(() -> translate("command", "template.apply.success", describeTemplate(tem.get()), player.getDisplayName()), true);
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
