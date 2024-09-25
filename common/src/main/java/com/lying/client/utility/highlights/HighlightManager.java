package com.lying.client.utility.highlights;

import com.lying.utility.Highlight;

import net.minecraft.client.MinecraftClient;

public abstract class HighlightManager<T extends Highlight>
{
	protected static final MinecraftClient mc = MinecraftClient.getInstance();
	
	protected final Highlight.Type type;
	
	protected HighlightManager(Highlight.Type typeIn) { type = typeIn; }
	
	public abstract void clear();
	
	public abstract boolean isEmpty();
	
	public abstract void tick(long currentTime);
	
	public final boolean canAccept(Highlight highlight) { return highlight.type() == type; }
	
	public abstract void add(T highlight);
}
