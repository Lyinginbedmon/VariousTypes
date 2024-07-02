package com.lying.neoforge.data;

import com.lying.reference.Reference;

import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Reference.ModInfo.MOD_ID)
public class VTDataGenerators
{
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event)
    {
//    	ExistingFileHelper fileHelper = event.getExistingFileHelper();
//    	DataGenerator generator = event.getGenerator();
//    	generator.addProvider(event.includeServer(), new VTSpeciesProvider(generator, fileHelper));
    }
}
