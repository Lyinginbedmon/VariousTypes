package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.jetbrains.annotations.Nullable;

import com.lying.client.init.VTModelLayerParts;
import com.lying.client.model.ModelFullbody;
import com.lying.client.renderer.accessory.AccessoryBasic;
import com.lying.client.renderer.accessory.AccessoryGlowing;
import com.lying.client.renderer.accessory.OverheadIconRenderer;
import com.lying.entity.AnimatedPlayerEntity;
import com.lying.init.VTCosmeticTypes;
import com.lying.init.VTCosmetics;
import com.lying.init.VTEntityTypes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.SkinTextures.Model;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class MiscFeatureRenderer<E extends LivingEntity, M extends EntityModel<E>> extends AbstractAccessoryFeature<E, M>
{
	private static final MinecraftClient mc = MinecraftClient.getInstance();
	private EntityModel<E> slimFullbody, wideFullbody;
	
	public MiscFeatureRenderer(FeatureRendererContext<E, M> context)
	{
		super(VTCosmeticTypes.MISC, context);
	}
	
	protected void createEntityModels(EntityModelLoader loader)
	{
		slimFullbody = new ModelFullbody<>(loader.getModelPart(VTModelLayerParts.FULLBODY_SLIM));
		wideFullbody = new ModelFullbody<>(loader.getModelPart(VTModelLayerParts.FULLBODY_WIDE));
	}
	
	protected void populateRendererMap()
	{
		addRendererMap(
				VTCosmetics.MISC_GLOW_SPOTS,
				makeVerdineSpots());
		addRendererMap(
				VTCosmetics.MISC_THIRD_EYE,
				new OverheadIconRenderer<>(
					prefix("third_eye"),
					prefix("third_eye_tinted")));
	}
	
	@Nullable
	private static PlayerListEntry getPlayerList(LivingEntity e)
	{
		return mc.getNetworkHandler().getPlayerListEntry(e.getUuid());
	}
	
	@SuppressWarnings("unchecked")
	private AccessoryBasic<E,M> makeVerdineSpots()
	{
		Function<E, EntityModel<E>> modelFunc = e -> MiscFeatureRenderer.isSlimPlayer(e) ? slimFullbody : wideFullbody;
		
		Function<Boolean, Identifier> texSlim = b -> b ? texture("misc/verdine_spots_tinted_slim.png") : texture("misc/verdine_spots_slim.png");
		Function<Boolean, Identifier> texWide = b -> b ? texture("misc/verdine_spots_tinted_wide.png") : texture("misc/verdine_spots_wide.png");
		BiFunction<E, Boolean, Identifier> texFunc = (e,b) -> 
		{
			Model model = Model.WIDE;
			PlayerListEntry playerListEntry = getPlayerList(e);
			SkinTextures skin = null;
			if(e.getType() == VTEntityTypes.ANIMATED_PLAYER.get())
				skin = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(((AnimatedPlayerEntity)e).getGameProfile()) : playerListEntry.getSkinTextures();
			else if(e.getType() == EntityType.PLAYER)
				skin = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(((PlayerEntity)e).getGameProfile()) : playerListEntry.getSkinTextures();
			
			if(skin != null)
				model = skin.model();
			
			return model == Model.WIDE ? texWide.apply(b) : texSlim.apply(b);
		};
		
		return (AccessoryBasic<E,M>)AccessoryGlowing.create(modelFunc, texFunc);
	}
	
	public static boolean isSlimPlayer(LivingEntity e)
	{
		Model model = Model.WIDE;
		PlayerListEntry playerListEntry = getPlayerList(e);
		SkinTextures skin = null;
		if(e.getType() == VTEntityTypes.ANIMATED_PLAYER.get())
			skin = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(((AnimatedPlayerEntity)e).getGameProfile()) : playerListEntry.getSkinTextures();
		else if(e.getType() == EntityType.PLAYER)
			skin = playerListEntry == null ? DefaultSkinHelper.getSkinTextures(((PlayerEntity)e).getGameProfile()) : playerListEntry.getSkinTextures();
		
		if(skin != null)
			model = skin.model();
		
		return model == Model.SLIM;
	}
}
