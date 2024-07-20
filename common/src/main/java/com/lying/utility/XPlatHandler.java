package com.lying.utility;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.lying.component.CharacterSheet;

import net.minecraft.entity.LivingEntity;

public interface XPlatHandler
{
	/** Returns an optional containing the character sheet of the given entity */
	public Optional<CharacterSheet> getSheet(@NotNull LivingEntity entity);
	
	/** Sets the character sheet of the given entity to be identical to the given character sheet */
	public void setSheet(LivingEntity entity, CharacterSheet sheet);
}
