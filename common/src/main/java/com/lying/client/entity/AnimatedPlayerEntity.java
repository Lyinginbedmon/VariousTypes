package com.lying.client.entity;

import com.lying.client.init.ClientsideEntities;
import com.lying.client.renderer.VTAnimations;
import com.lying.client.utility.AnimationManager;
import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class AnimatedPlayerEntity extends LivingEntity
{
	public static final MinecraftClient mc = MinecraftClient.getInstance();
	private GameProfile gameProfile;
	private PlayerListEntry playerListEntry;
	
	public final AnimationManager<AnimatedPlayerEntity> animations = new AnimationManager<>(VTAnimations.PLAYER_IDLE, VTAnimations.PLAYER_TPOSE);
	
	public AnimatedPlayerEntity(EntityType<? extends AnimatedPlayerEntity> typeIn, World worldIn)
	{
		super(typeIn, worldIn);
	}
	
	public static AnimatedPlayerEntity of(GameProfile gameProfile)
	{
		AnimatedPlayerEntity entity = new AnimatedPlayerEntity(ClientsideEntities.ANIMATED_PLAYER.get(), mc.world);
		entity.gameProfile = gameProfile;
		entity.setUuid(gameProfile.getId());
		return entity;
	}
	
	protected PlayerListEntry getPlayerListEntry()
	{
		if(this.playerListEntry == null)
			this.playerListEntry = mc.getNetworkHandler().getPlayerListEntry(getUuid());
		return this.playerListEntry;
	}
	
	public GameProfile getGameProfile() { return this.gameProfile; }
	
	public SkinTextures getSkinTextures()
	{
		return getPlayerListEntry() == null ? DefaultSkinHelper.getSkinTextures(getUuid()) : getPlayerListEntry().getSkinTextures();
	}
	
	public Iterable<ItemStack> getArmorItems() { return DefaultedList.ofSize(4, ItemStack.EMPTY); }
	
	public ItemStack getEquippedStack(EquipmentSlot var1) { return ItemStack.EMPTY; }
	
	public void equipStack(EquipmentSlot var1, ItemStack var2) { }
	
	public Arm getMainArm() { return Arm.RIGHT; }
	
	public void tick()
	{
		super.tick();
		this.animations.tick(this);
		
		if(animations.currentAnim() == -1)
			if(random.nextInt(10) == 0)
				animations.start(random.nextInt(animations.size()), age);
			else
				animations.start(0, age);
	}
}
