package com.lying.client.entity;

import com.lying.client.init.ClientsideEntities;
import com.lying.client.utility.AnimationManager;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;

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

	public static final int ANIM_IDLE = 0;
	public static final int ANIM_TPOSE = 1;
	public static final int ANIM_WALK = 2;
	public static final int ANIM_LOOK_AROUND = 3;
	public static final int ANIM_FGAME = 4;
	public static final int ANIM_SIT = 5;
	public static final int ANIM_WOLOLO = 6;
	public static final int ANIM_PDANCE = 7;
	public static final int ANIM_SNEAK = 8;
	public static final int ANIM_SWAY = 9;
	public static final int ANIM_WAVE = 10;
	public final AnimationManager<AnimatedPlayerEntity> animations = new AnimationManager<>(
			Pair.of(ANIM_IDLE, 2F), 
			Pair.of(ANIM_TPOSE, 3F), 
			Pair.of(ANIM_WALK, 3F), 
			Pair.of(ANIM_LOOK_AROUND, 2F), 
			Pair.of(ANIM_FGAME, 3F), 
			Pair.of(ANIM_SIT, 5F), 
			Pair.of(ANIM_WOLOLO, 2.9167F), 
			Pair.of(ANIM_PDANCE, 3.5417F), 
			Pair.of(ANIM_SNEAK, 2.0833F), 
			Pair.of(ANIM_SWAY, 3.25F), 
			Pair.of(ANIM_WAVE, 1.5F));
	
	public AnimatedPlayerEntity(EntityType<? extends AnimatedPlayerEntity> typeIn, World worldIn)
	{
		super(typeIn, worldIn);
		animations.stopAll();
		animations.start(10, age);
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
			if(animations.lastAnim() == ANIM_IDLE && random.nextInt(5) == 0)
				animations.start(random.nextBetween(ANIM_IDLE, ANIM_SWAY), age);
			else
				animations.start(ANIM_IDLE, age);
	}
}
