package com.lying.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.lying.reference.Reference.ModInfo;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** Actions conducive to general survival */
public class Action
{
	private static final Map<Identifier, Supplier<Action>> ACTIONS = new HashMap<>();
	
	public static final Supplier<Action> EAT		= register(prefix("eat"), () -> new Action(prefix("eat")));
	public static final Supplier<Action> BREATHE	= register(prefix("breathe"), () -> new Action(prefix("breathe")));
	public static final Supplier<Action> SLEEP		= register(prefix("sleep"), () -> new Action(prefix("sleep")));
	public static final Supplier<Action> REGEN		= register(prefix("regen"), () -> new Action(prefix("regen")));
	
	private static Identifier prefix(String name) { return ModInfo.prefix(name); }
	
	private static Supplier<Action> register(Identifier nameIn, Supplier<Action> actionIn)
	{
		ACTIONS.put(nameIn, actionIn);
		return actionIn;
	}
	
	public static Collection<Supplier<Action>> actions() { return ACTIONS.values(); }
	
	@Nullable
	public static Action get(Identifier name)
	{
		return ACTIONS.getOrDefault(name, () -> null).get();
	}
	
	private final Identifier registryName;
	
	private Action(Identifier nameIn)
	{
		registryName = nameIn;
	}
	
	public final Identifier registryName() { return registryName; }
	
	public Text translate() { return ModInfo.translate("action", registryName.getPath()); }
}
