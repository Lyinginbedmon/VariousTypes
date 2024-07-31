package com.lying.network;

import static com.lying.reference.Reference.ModInfo.prefix;

import net.minecraft.util.Identifier;

public class VTPacketHandler
{
	public static final Identifier FINISH_CHARACTER_ID	= prefix("finish_character_creation");
	public static final Identifier OPEN_ABILITY_MENU_ID	= prefix("open_ability_menu");
	public static final Identifier SET_FAVOURITE_ABILITY_ID	= prefix("set_favourite_ability");
	public static final Identifier ACTIVATE_ABILITY_ID	= prefix("activate_ability");
	public static final Identifier SYNC_ACTIONABLES_ID	= prefix("sync_actionables");
	public static final Identifier SYNC_FATIGUE_ID	= prefix("sync_fatigue");
}
