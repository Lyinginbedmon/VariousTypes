package com.lying.command;

import com.lying.command.VTCommands.Mode;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.argument.EnumArgumentType;
import net.minecraft.server.command.ServerCommandSource;

public class ModeArgumentType extends EnumArgumentType<Mode>
{
	protected ModeArgumentType()
	{
		super(Mode.CODEC, Mode::values);
	}
	
	public static EnumArgumentType<Mode> mode() { return new ModeArgumentType(); }
	
	public static Mode getMode(CommandContext<ServerCommandSource> context, String id)
	{
		return context.getArgument(id, Mode.class);
	}
}