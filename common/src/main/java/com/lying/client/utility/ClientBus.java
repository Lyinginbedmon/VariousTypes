package com.lying.client.utility;

import java.util.List;
import java.util.Optional;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.google.common.collect.Lists;
import com.lying.VariousTypes;
import com.lying.ability.AbilityFaeskin;
import com.lying.ability.AbilityInstance;
import com.lying.ability.AbilitySet;
import com.lying.client.VariousTypesClient;
import com.lying.client.event.RenderEvents;
import com.lying.client.init.VTPlayerSpecialRenderingRegistry;
import com.lying.client.renderer.AnimatedPlayerEntityRenderer;
import com.lying.client.renderer.EarsFeatureRenderer;
import com.lying.client.renderer.HornsFeatureRenderer;
import com.lying.client.renderer.IconFeatureRenderer;
import com.lying.client.renderer.MiscFeatureRenderer;
import com.lying.client.renderer.NoseFeatureRenderer;
import com.lying.client.renderer.ParentedParticlesFeatureRenderer;
import com.lying.client.renderer.TailFeatureRenderer;
import com.lying.client.renderer.WingsFeatureRenderer;
import com.lying.client.screen.FavouriteAbilityButton;
import com.lying.client.utility.highlights.HighlightManager;
import com.lying.component.CharacterSheet;
import com.lying.component.element.ElementActionables;
import com.lying.component.element.ElementCosmetics;
import com.lying.effect.DazzledStatusEffect;
import com.lying.entity.AccessoryAnimationInterface;
import com.lying.init.VTAbilities;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTEntityTypes;
import com.lying.init.VTSheetElements;
import com.lying.init.VTStatusEffects;
import com.lying.mixin.AccessorEntityRenderDispatcher;
import com.lying.mixin.AccessorLivingEntityRenderer;
import com.lying.reference.Reference;
import com.lying.utility.Cosmetic;
import com.lying.utility.CosmeticType;
import com.lying.utility.PlayerPose;
import com.lying.utility.VTUtils;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

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
			
			RegistryEntry<StatusEffect> effect = VTStatusEffects.getEntry(player.getRegistryManager(), VTStatusEffects.DAZZLED);
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
				appendAccessoryFeatures((PlayerEntityRenderer)accessor.getModelRenderers().get(model));
			
			appendAccessoryFeatures((AnimatedPlayerEntityRenderer)accessor.getRenderers().get(VTEntityTypes.ANIMATED_PLAYER.get()));
		});
		
		// Perform cosmetic visual effects
		TickEvent.PLAYER_POST.register((player) -> 
		{
			World world = mc.world;
			if(world == null) return;
			AccessoryAnimationInterface anim = ((AccessoryAnimationInterface)player); 
			PlayerPose pose = anim.currentlyRunning();
			for(CosmeticType type : VTCosmeticTypes.types())
				VTUtils.getEntityCosmetics(player, type).stream().forEach(cosmetic -> cosmetic.onClientTick(player, world, pose, world.random));
		});
	}
	
	private static <
		E extends LivingEntity, 
		M extends EntityModel<E>,
		R extends LivingEntityRenderer<E, M>> void appendAccessoryFeatures(R renderer)
	{
		FeatureRendererContext<E, M> context = (FeatureRendererContext<E, M>)renderer;
		AccessorLivingEntityRenderer accessor = (AccessorLivingEntityRenderer)renderer;
		accessor.appendFeature(new WingsFeatureRenderer<>(context));
		accessor.appendFeature(new NoseFeatureRenderer<>(context));
		accessor.appendFeature(new EarsFeatureRenderer<>(context));
		accessor.appendFeature(new HornsFeatureRenderer<>(context));
		accessor.appendFeature(new TailFeatureRenderer<>(context));
		accessor.appendFeature(new IconFeatureRenderer<>(context));
		accessor.appendFeature(new MiscFeatureRenderer<>(context));
		
		accessor.appendFeature(new ParentedParticlesFeatureRenderer<>(context));
	}
	
	/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
	private static void registerAbilityRenderFuncs()
	{	
		RenderEvents.GET_PLAYER_COLOR_EVENT.register((LivingEntity player) -> 
		{
			Vector3f color = new Vector3f(1F, 1F, 1F);
			VariousTypes.getSheet(player).ifPresent(sheet -> 
			{
				for(Cosmetic inst : ((ElementCosmetics)sheet.element(VTSheetElements.COSMETICS)).value().values())
					color.mul(VTPlayerSpecialRenderingRegistry.doColorMods(player, inst));
			});
			return color;
		});
		
		RenderEvents.GET_PLAYER_ALPHA_EVENT.register((LivingEntity player) -> 
		{
			float alpha = 1F;
			List<Cosmetic> cosmetics = Lists.newArrayList();
			VariousTypes.getSheet(player).ifPresent(sheet -> cosmetics.addAll(((ElementCosmetics)sheet.element(VTSheetElements.COSMETICS)).value().values()));
			for(Cosmetic inst : cosmetics)
				alpha *= VTPlayerSpecialRenderingRegistry.doAlphaMods(player, inst);
			return alpha;
		});
		
		RenderEvents.BEFORE_RENDER_PLAYER_EVENT.register((player, yaw, tickDelta, matrices, vertexConsumers, light, renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> 
			((ElementCosmetics)sheet.element(VTSheetElements.COSMETICS)).value().values().forEach(inst -> VTPlayerSpecialRenderingRegistry.doPreRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
		
		RenderEvents.AFTER_RENDER_PLAYER_EVENT.register((player, yaw, tickDelta, matrices, vertexConsumers, light, renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> 
			((ElementCosmetics)sheet.element(VTSheetElements.COSMETICS)).value().values().forEach(inst -> VTPlayerSpecialRenderingRegistry.doPostRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
	}
	
	private static void registerHighlights()
	{
		ClientTickEvent.CLIENT_POST.register(client -> 
		{
			if(client.player == null || client.player.getWorld() == null) return;
			long currentTime = client.player.getWorld().getTime();
			for(HighlightManager<?> manager : VariousTypesClient.HIGHLIGHTS)
				manager.tick(currentTime);
			
			VariousTypesClient.PARENTED_PARTICLES.tick(currentTime);
		});
		
		RenderEvents.AFTER_WORLD_RENDER_EVENT.register((float tickDelta, Camera camera, GameRenderer renderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f1, Matrix4f matrix4f2, VertexConsumerProvider vertexConsumerProvider) -> 
		{
			if(mc.player == null || mc.player.getWorld() == null) return;
			VariousTypesClient.BLOCK_HIGHLIGHTS.renderHighlightedBlocks(new MatrixStack(), vertexConsumerProvider, matrix4f1, matrix4f2, camera, tickDelta);
		});
		
		PlayerEvent.PLAYER_QUIT.register(player -> 
		{
			if(player.getUuid() != mc.player.getUuid()) return;
			
			for(HighlightManager<?> manager : VariousTypesClient.HIGHLIGHTS)
				manager.clear();
			
			VariousTypesClient.ENTITY_HIGHLIGHTS.clear();
			VariousTypesClient.PARENTED_PARTICLES.clear();
		});
		
		PlayerEvent.CHANGE_DIMENSION.register((player, w1, w2) -> 
		{
			if(player.getUuid() != mc.player.getUuid()) return;
			
			VariousTypesClient.BLOCK_HIGHLIGHTS.clear(w1);
			VariousTypesClient.ENTITY_HIGHLIGHTS.clear();
			VariousTypesClient.PARENTED_PARTICLES.clear();
		});
	}
}