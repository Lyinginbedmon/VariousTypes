package com.lying.client.utility;

import java.util.Optional;

import org.joml.Vector3f;

import com.lying.VariousTypes;
import com.lying.ability.Ability;
import com.lying.ability.AbilityInstance;
import com.lying.client.renderer.AbilityRenderingRegistry;
import com.lying.client.screen.FavouriteAbilityButton;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;

import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class ClientBus
{
	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void init()
	{
		registerAbilityRenderFuncs();
		
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
	}
	
	/** Handles rendering effects applied by abilities that aren't already handled by supplementary feature renderers */
	private static void registerAbilityRenderFuncs()
	{	
		ClientEvents.Rendering.GET_PLAYER_COLOR_EVENT.register((PlayerEntity player) -> 
		{
			Vector3f color = new Vector3f(1F, 1F, 1F);
			for(AbilityInstance inst : Ability.getAllOf(Ability.class, player))
				color.mul(AbilityRenderingRegistry.doColorMods(player, inst));
			return color;
		});
		
		ClientEvents.Rendering.GET_PLAYER_ALPHA_EVENT.register((PlayerEntity player) -> 
		{
			float alpha = 1F;
			for(AbilityInstance inst : Ability.getAllOf(Ability.class, player))
				alpha *= AbilityRenderingRegistry.doAlphaMods(player, inst);
			return alpha;
		});
		
		ClientEvents.Rendering.BEFORE_RENDER_PLAYER_EVENT.register((PlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> Ability.getAllOf(Ability.class, player).forEach(inst -> AbilityRenderingRegistry.doPreRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
		
		ClientEvents.Rendering.AFTER_RENDER_PLAYER_EVENT.register((PlayerEntity player, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderer renderer) -> 
			VariousTypes.getSheet(player).ifPresent(sheet -> Ability.getAllOf(Ability.class, player).forEach(inst -> AbilityRenderingRegistry.doPostRender(player, inst, matrices, vertexConsumers, renderer, yaw, tickDelta, light))));
	}
}