package com.lying.neoforge;

import com.lying.VariousTypes;
import com.lying.reference.Reference;

import net.neoforged.fml.common.Mod;

@Mod(Reference.ModInfo.MOD_ID)
public final class VariousTypesNeoForge
{
    public VariousTypesNeoForge()
    {
        // Run our common setup.
        VariousTypes.commonInit();
    }
}
