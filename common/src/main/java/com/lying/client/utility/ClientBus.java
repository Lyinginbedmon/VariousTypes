package com.lying.client.utility;

import java.util.List;
import java.util.Optional;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilityFaeskin;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.client.VariousTypesClient;
import com.lying.client.event.RenderEvents;
import com.lying.client.init.VTAbilityRenderingRegistry;
import com.lying.client.model.AnimatedPlayerEntityModel;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.renderer.EarsFeatureRenderer;
import com.lying.client.renderer.HornsFeatureRenderer;
import com.lying.client.renderer.MiscFeatureRenderer;
import com.lying.client.renderer.NoseFeatureRenderer;
import com.lying.client.renderer.TailFeatureRenderer;
import com.lying.client.renderer.WingsFeatureRenderer;
import com.lying.client.screen.FavouriteAbilityButton;
import com.lying.client.utility.highlights.HighlightManager;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionables;
import com.lying.effect.DazzledStatusEffect;
import com.lying.entity.AnimatedPlayerEntity;
import com.lying.init.VTAbilities;
import com.lying.init.VTCosmetics;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.mixin.AccessorEntityRenderDispatcher;
import com.lying.mixin.AccessorLivingEntityRenderer;
import com.lying.reference.Reference;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ClientBus
{
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void init()
	{
		registerAbilityRenderFuncs();
		registerHighlights();
		
		// Favourite ability slots
		ClientGuiEvent.RENDER_HUD.register((context, tickDelta) -> VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
		{
			if(mc.options.hudHidden)
				return;
			
			ElementActionables element = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
			DefaultedList<Optional<Identifier>> favourites = element.getFavourites();
			// Don't bother rendering if the player has no favourited abilities
			if(favourites.stream().allMatch(Optional::isEmpty))
				return;
			
			int scale = mc.getWindow().calculateScaleFactor(mc.options.getGuiScale().getValue(), mc.forcesUnicodeFont());
			int count = favourites.size();
			int buttonHeight = 20;
			int buttonSpacing = 1 * scale;
			int buttonSize = buttonHeight;
			int y = (context.getScaledWindowHeight() / 2) - ((count * buttonSize) + (count - 1) * buttonSpacing) / 2;
			
			for(int i=0; i<favourites.size(); i++)
			{
				FavouriteAbilityButton f1 = new FavouriteAbilityButton(1, y + i * (buttonSize + buttonSpacing), buttonHeight, i, (button) -> {});
				favourites.get(i).ifPresent(id -> f1.set(element.get(id)));
				f1.render(context, 0, 0, tickDelta);
			}
		}));
		
		// Dazzled vignette
		RenderEvents.BEFORE_HUD_RENDER_EVENT.register((mc, player, sheetOpt, tickDelta, context) -> 
		{
			if(player == null || mc.options.hudHidden)
				return;
			
			RegistryEntry<StatusEffect> effect = VTStatusEffects.getEntry(VTStatusEffects.DAZZLED);
			if(!player.hasStatusEffect(effect) || player.getStatusEffect(effect).getDuration() <= 0)
				return;
			
			RenderSystem.disableDepthTest();
			RenderSystem.depthMask(false);
			RenderSystem.enableBlend();
			context.setShaderColor(1F, 1F, 1F, player.getStatusEffect(effect).getFadeFactor(player, tickDelta));
			context.drawTexture(DazzledStatusEffect.VIGNETTE, 0, 0, -100, 0F, 0F, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
			RenderSystem.disableBlend();
			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
			context.setShaderColor(1F, 1F, 1F, 1F);
		});
		
		// Faeskin item tooltip
		ClientTooltipEvent.ITEM.register((stack, lines, tooltipContext, flag) -> 
		{
			PlayerEntity player = mc.player;
			Optional<CharacterSheet> sheetOpt = VariousTypes.getSheet(player);
			if(sheetOpt.isEmpty())
				return;
			
			CharacterSheet sheet = sheetOpt.get();
			AbilitySet abilities = sheet.elementValue(VTSheetElements.ABILITIES);
			Identifier id = VTAbilities.FAESKIN.get().registryName();
			if(!abilities.hasAbility(id))
				return;
			
			AbilityInstance inst = abilities.get(id);
			AbilityFaeskin faeskin = (AbilityFaeskin)inst.ability();
			if(faeskin.instanceToValues(inst).penalisesItem(stack))
			{
				Text name = lines.get(0).copy().formatted(Formatting.RED);
				List<Text> remainder = Lists.newArrayList();
				if(lines.size() > 1)
					remainder.addAll(lines.subList(1, lines.size()));
				lines.clear();
				lines.add(name);
				lines.add(Reference.ModInfo.translate("gui", "faeskin_penalty").copy().formatted(Formatting.RED, Formatting.ITALIC));
				lines.addAll(remainder);
			}
		});
		
		// Additional render layers
		RenderEvents.ADD_FEATURE_RENDERERS_EVENT.register((dispatcher) -> 
		{
			AccessorEntityRenderDispatcher accessor = (AccessorEntityRenderDispatcher)dispatcher;
			
			for(Model model : SkinTextures.Model.values())
			{
				PlayerEntityRenderer renderer = (PlayerEntityRenderer)accessor.getModelRenderers().get(model);
				AccessorLivingEntityRenderer renderAccessor = (AccessorLivingEntityRenderer)renderer;
				FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureContext = (FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>>)renderer;
				renderAccessor.appendFeature(new WingsFeatureRenderer<>(featureContext));
				renderAccessor.appendFeature(new MiscFeatureRenderer<>(featureContext, model == SkinTextures.Model.SLIM));
				renderAccessor.appendFeature(new NoseFeatureRenderer<>(featureContext));
				renderAccessor.appendFeature(new EarsFeatureRenderer<>(featureContext));
				renderAccessor.appendFeature(new HornsFeatureRenderer<>(featureContext));
				renderAccessor.appendFeature(new TailFeatureRenderer<>(featureContext));
			}
			
			AnimatedPlayerEntityRenderer animPlayer = (AnimatedPlayerEntityRenderer) accessor.getRenderers().get(VTEntityTypes.ANIMATED_PLAYER.get());
			AccessorLivingEntityRenderer renderAccessor = (AccessorLivingEntityRenderer)animPlayer;
			FeatureRendererContext<AnimatedPlayerEntity, AnimatedPlayerEntityModel<AnimatedPlayerEntity>> featureContext = (FeatureRendererContext<AnimatedPlayerEntity, AnimatedPlayerEntityModel<AnimatedPlayerEntity>>)animPlayer;
			renderAccessor.appendFeature(new WingsFeatureRenderer<>(featureContext));
			renderAccessor.appendFeature(new MiscFeatureRenderer<>(featureContext, true));	// FIXME Ensure model arm width matches renderer's
			renderAccessor.appendFeature(new NoseFeatureRenderer<>(featureContext));
			renderAccessor.appendFeature(new EarsFeatureRenderer<>(featureContext));
			renderAccessor.appendFeature(new HornsFeatureRenderer<>(featureContext));
			renderAccessor.appendFeature(new TailFeatureRenderer<>(featureContext));
		});
	}
	
	/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
	private static void registerAbilityRenderFuncs()
	{	
		RenderEvents.GET_PLAYER_COLOR_EVENT.register((LivingEntity player) -> 
		{
			Vector3f color = new Vector3f(1F, 1F, 1F);
			for(AbilityInstance inst : Ability.getAllOf(Ability.class, player))
				color.mul(VTAbilityRenderingRegistry.doColorMods(player, inst));
			return color;
		});
		
		RenderEvents.GET_PLAYER_ALPHA_EVENT.register((LivingEntity player) -> 
		{
			float alpha = 1F;
			for(AbilityInstance inst : Ability.getAllOf(Ability.class, player))
				alpha *= VTAbilityRenderingRegistry.doAlphaMods(player, inst);
			return alpha;
		});
		
		RenderEvents.BEFORE_RENDER_PLAYER_EVENT.register((player, yaw, tickDelta, matrices, vertexConsumers, light, renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> 
				Ability.getAllOf(Ability.class, player).forEach(inst -> VTAbilityRenderingRegistry.doPreRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
		
		RenderEvents.AFTER_RENDER_PLAYER_EVENT.register((player, yaw, tickDelta, matrices, vertexConsumers, light, renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> 
				Ability.getAllOf(Ability.class, player).forEach(inst -> VTAbilityRenderingRegistry.doPostRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
		
		RenderEvents.GET_LIVING_COSMETICS_EVENT.register((living, type, set) -> 
		{
			if(living.getType() == EntityType.PLAYER && ((PlayerEntity)living).getName().getString().equalsIgnoreCase("_Lying"))
				set.add(VTCosmetics.WINGS_WITCH.get());
		});
	}
	
	private static void registerHighlights()
	{
		ClientTickEvent.CLIENT_POST.register(client -> 
		{
			if(client.player == null || client.player.getWorld() == null) return;
			long currentTime = client.player.getWorld().getTime();
			for(HighlightManager<?> manager : VariousTypesClient.HIGHLIGHTS)
				manager.tick(currentTime);
		});
		
		RenderEvents.AFTER_WORLD_RENDER_EVENT.register((float tickDelta, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f1, Matrix4f matrix4f2, VertexConsumerProvider vertexConsumerProvider) -> 
		{
			if(mc.player == null || mc.player.getWorld() == null) return;
			VariousTypesClient.BLOCK_HIGHLIGHTS.renderHighlightedBlocks(new MatrixStack(), vertexConsumerProvider, matrix4f1, matrix4f2, camera, tickDelta);
		});
		
		PlayerEvent.PLAYER_QUIT.register(player -> 
		{
			if(player.getUuid() == mc.player.getUuid())
				for(HighlightManager<?> manager : VariousTypesClient.HIGHLIGHTS)
					manager.clear();
		});
		
		PlayerEvent.CHANGE_DIMENSION.register((player, w1, w2) -> 
		{
			if(player.getUuid() == mc.player.getUuid())
			{
				VariousTypesClient.BLOCK_HIGHLIGHTS.clear(w1);
				VariousTypesClient.ENTITY_HIGHLIGHTS.clear();
			}
		});
	}
}