package com.lying.client;

import java.util.Optional;
import java.util.function.Consumer;

import com.lying.VariousTypes;
import com.lying.ability.AbilityInstance;
import com.lying.client.init.VTKeybinds;
import com.lying.component.element.ElementActionables;
import com.lying.init.VTSheetElements;
import com.lying.network.ActivateAbilityPacket;
import com.lying.network.OpenAbilityMenuPacket;
import com.lying.reference.Reference;

import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class KeybindHandling
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void init()
	{
		ClientTickEvent.CLIENT_POST.register((client) -> 
		{
			handleKey(VTKeybinds.keyOpenAbilities, key -> VariousTypes.getSheet(mc.player).ifPresent(sheet -> 
			{
				ElementActionables abilities = sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES);
				switch(abilities.size())
				{
					case 0:
						mc.player.sendMessage(Text.translatable("gui.vartypes.no_abilities_to_activate"));
						return;
					case 1:
						Optional<Identifier> abilityID = abilities.getFirstActivated();
						AbilityInstance ability = abilities.get(abilityID.get());
						if(ability.cooldown() <= Reference.Values.TICKS_PER_SECOND && !abilities.coolingDown())
						{
							sendActivationPacket(abilityID);
							break;
						}
					case 2:
					default:
						OpenAbilityMenuPacket.send();
						break;
				}
			}));
			
			for(int i=0; i<VTKeybinds.keyFavAbility.length; i++)
			{
				final int slot = i;
				handleKey(VTKeybinds.keyFavAbility[slot], key -> 
					VariousTypes.getSheet(mc.player).ifPresent(sheet -> sendActivationPacket(sheet.<ElementActionables>element(VTSheetElements.ACTIONABLES).getFavourite(slot))));
			}
		});
	}
	
	private static void handleKey(KeyBinding keyIn, Consumer<KeyBinding> ifPressed)
	{
		while(keyIn.wasPressed())
		{
			ifPressed.accept(keyIn);
		}
	}
	
	public static void sendActivationPacket(Optional<Identifier> mapName)
	{
		mapName.ifPresentOrElse(id -> ActivateAbilityPacket.send(id), () -> mc.player.sendMessage(Text.translatable("gui.vartypes.no_ability_favourited")));
	}
}
