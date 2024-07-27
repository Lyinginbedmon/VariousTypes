package com.lying.client.init;

import com.lying.reference.Reference;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class VTKeybinds
{
	public static KeyBinding keyOpenAbilities;
	public static KeyBinding[] keyFavAbility = new KeyBinding[4];
	
	public static KeyBinding make(String name, InputUtil.Type type, int standard)
	{
		KeyBinding binding = new KeyBinding(
				"key."+Reference.ModInfo.MOD_ID+"."+name,
				type,
				standard,
				"category."+Reference.ModInfo.MOD_ID+".keybindings");
		KeyMappingRegistry.register(binding);
		return binding;
	}
}
