package com.lying.type;

import static com.lying.reference.Reference.ModInfo.prefix;
import static com.lying.reference.Reference.ModInfo.translate;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.lying.utility.LoreDisplay;
import com.lying.utility.VTUtils;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/** Actions conducive to general survival */
public class Action
{
	public static final Comparator<Action> SORT = (a,b) -> VTUtils.stringComparator(a.translated().getString(), b.translated().getString());
	private static final Map<Identifier, Supplier<Action>> ACTIONS = new HashMap<>();
	
	public static final Supplier<Action> EAT		= register(prefix("eat"), () -> Builder.of(prefix("eat")).describe(translate("action","eat.desc")).texture(prefix("textures/gui/sheet/action_eat.png")).build());
	public static final Supplier<Action> BREATHE	= register(prefix("breathe"), () -> Builder.of(prefix("breathe")).describe(translate("action","breathe.desc")).texture(prefix("textures/gui/sheet/action_breathe.png")).build());
	public static final Supplier<Action> SLEEP		= register(prefix("sleep"), () -> Builder.of(prefix("sleep")).describe(translate("action","sleep.desc")).texture(prefix("textures/gui/sheet/action_sleep.png")).build());
	public static final Supplier<Action> REGEN		= register(prefix("regen"), () -> Builder.of(prefix("regen")).describe(translate("action","regen.desc")).texture(prefix("textures/gui/sheet/action_regen.png")).build());
	
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
	private final LoreDisplay display;
	private final Identifier texture;
	
	private Action(Identifier nameIn, LoreDisplay displayIn, Identifier textureIn)
	{
		registryName = nameIn;
		display = displayIn;
		texture = textureIn;
	}
	
	public final Identifier registryName() { return registryName; }
	
	public Text translated() { return display.title(); }
	
	public Optional<Text> description() { return display.description(); }
	
	public Identifier texture() { return this.texture; }
	
	public final boolean equals(Action other) { return registryName().equals(other.registryName()); }
	
	public static class Builder
	{
		private final Identifier registryName;
		private Identifier texture;
		
		private Text translation;
		private Optional<Text> description = Optional.empty();
		
		private Builder(Identifier nameIn)
		{
			registryName = nameIn;
			translation = Text.translatable("action."+nameIn.getNamespace()+"."+nameIn.getPath());
		}
		
		public static Builder of(Identifier name) { return new Builder(name); }
		
		public Builder texture(Identifier texIn)
		{
			texture = texIn;
			return this;
		}
		
		public Builder translation(Text translationIn)
		{
			translation = translationIn;
			return this;
		}
		
		public Builder describe(Text descriptionIn)
		{
			description = Optional.of(descriptionIn);
			return this;
		}
		
		public final Action build()
		{
			return new Action(registryName, new LoreDisplay(translation, description), texture);
		}
	}
}
