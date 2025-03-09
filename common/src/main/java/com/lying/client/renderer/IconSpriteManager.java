package com.lying.client.renderer;

import static com.lying.reference.Reference.ModInfo.prefix;

import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasHolder;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

public class IconSpriteManager extends SpriteAtlasHolder
{
	public static Identifier ATLAS_ID = prefix("textures/atlas/icons.png");
	public static Identifier SOURCE_PATH = prefix("icons");
	public static IconSpriteManager INSTANCE = null;
	
	protected IconSpriteManager(TextureManager textureManager)
	{
		super(textureManager, ATLAS_ID, SOURCE_PATH);
	}
	
	public Sprite getIcon(Identifier icon)
	{
		return super.getSprite(icon);
	}
	
	public static void init(TextureManager manager)
	{
		if(INSTANCE == null)
			ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, INSTANCE = new IconSpriteManager(manager));
	}
}