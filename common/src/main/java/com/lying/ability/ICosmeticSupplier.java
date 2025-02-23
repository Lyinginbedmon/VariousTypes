package com.lying.ability;

import java.util.List;

import com.lying.utility.Cosmetic;

public interface ICosmeticSupplier
{
	public List<Cosmetic> getCosmetics(AbilityInstance inst);
}
