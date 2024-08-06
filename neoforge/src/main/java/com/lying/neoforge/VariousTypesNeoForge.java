package com.lying.neoforge;

import java.util.Optional;

import com.lying.VariousTypes;
import com.lying.component.CharacterSheet;
import com.lying.neoforge.component.PlayerSheetHandler;
import com.lying.reference.Reference;
import com.lying.utility.XPlatHandler;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(Reference.ModInfo.MOD_ID)
public final class VariousTypesNeoForge
{
	private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Reference.ModInfo.MOD_ID);
	
	private static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerSheetHandler>> HANDLER = ATTACHMENTS.register("handler", () -> AttachmentType.serializable(() -> new PlayerSheetHandler()).build());
	
	public VariousTypesNeoForge(IEventBus eventBus, ModContainer modContainer)
	{
		// Run our common setup.
		VariousTypes.commonInit();
		ATTACHMENTS.register(modContainer.getEventBus());
		
		VariousTypes.setPlatHandler(new XPlatHandler() 
		{
			public Optional<CharacterSheet> getSheet(LivingEntity entity)
			{
				return entity.getType() != EntityType.PLAYER ? Optional.empty() : Optional.of(entity.getData(HANDLER).setOwner(entity));
			}
			
			public void setSheet(LivingEntity entity, CharacterSheet sheet)
			{
				if(entity.getType() == EntityType.PLAYER)
					entity.setData(HANDLER.get(), (PlayerSheetHandler)sheet.setOwner(entity));
			}
		});
	}
}
