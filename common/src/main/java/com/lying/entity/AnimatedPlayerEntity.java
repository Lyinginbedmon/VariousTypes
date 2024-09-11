package com.lying.entity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.lying.init.VTEntityTypes;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class AnimatedPlayerEntity extends LivingEntity
{
	private GameProfile gameProfile;

	public static final int ANIM_IDLE = 0;
	public static final int ANIM_TPOSE = 1;
	public static final int ANIM_LOOK_AROUND = 3;
	public static final int ANIM_FGAME_START = 4;
	public static final int ANIM_FGAME_MAIN = 5;
	public static final int ANIM_FGAME_END = 6;
	public static final int ANIM_SIT_START = 7;
	public static final int ANIM_SIT_MAIN = 8;
	public static final int ANIM_SIT_END = 9;
	public static final int ANIM_WOLOLO = 10;
	public static final int ANIM_PDANCE = 11;
	public static final int ANIM_SNEAK = 12;
	public static final int ANIM_SWAY = 13;
	public static final int ANIM_WAVE = 14;
	public final AnimationManager<AnimatedPlayerEntity> animations = new AnimationManager<>(
			Pair.of(ANIM_IDLE, 2F), 
			Pair.of(ANIM_TPOSE, 3F), 
			Pair.of(ANIM_LOOK_AROUND, 2F), 
			Pair.of(ANIM_FGAME_START, 0.3333F),
			Pair.of(ANIM_FGAME_MAIN, 2.25F),
			Pair.of(ANIM_FGAME_END, 0.3333F),
			Pair.of(ANIM_SIT_START, 0.7917F),
			Pair.of(ANIM_SIT_MAIN, 3.0833F),
			Pair.of(ANIM_SIT_END, 1.0833F),
			Pair.of(ANIM_WOLOLO, 2.9167F), 
			Pair.of(ANIM_PDANCE, 3.5417F), 
			Pair.of(ANIM_SNEAK, 2.0833F), 
			Pair.of(ANIM_SWAY, 3.25F), 
			Pair.of(ANIM_WAVE, 1.5F));
	public final AnimationStateEngine stateEngine = new AnimationStateEngine(Map.of(
			ANIM_IDLE, List.of(ANIM_IDLE, ANIM_IDLE, ANIM_IDLE, ANIM_IDLE, ANIM_FGAME_START, ANIM_SIT_START, ANIM_TPOSE, ANIM_LOOK_AROUND, ANIM_PDANCE, ANIM_SNEAK, ANIM_SWAY),
			ANIM_FGAME_START, List.of(ANIM_FGAME_MAIN),
			ANIM_FGAME_MAIN, List.of(ANIM_FGAME_MAIN, ANIM_FGAME_MAIN, ANIM_FGAME_END),
			ANIM_SIT_START, List.of(ANIM_SIT_MAIN),
			ANIM_SIT_MAIN, List.of(ANIM_SIT_MAIN, ANIM_SIT_MAIN, ANIM_SIT_END)
			));
	
	public AnimatedPlayerEntity(EntityType<? extends AnimatedPlayerEntity> typeIn, World worldIn)
	{
		super(typeIn, worldIn);
		gameProfile = new GameProfile(UUID.randomUUID(), "Player");
		animations.stopAll();
		animations.start(ANIM_WAVE, age);
	}
	
	public static AnimatedPlayerEntity of(GameProfile gameProfile, World world)
	{
		AnimatedPlayerEntity entity = new AnimatedPlayerEntity(VTEntityTypes.ANIMATED_PLAYER.get(), world);
		entity.gameProfile = gameProfile;
		entity.setUuid(gameProfile.getId());
		return entity;
	}
	
	public GameProfile getGameProfile() { return this.gameProfile; }
	
	public Iterable<ItemStack> getArmorItems() { return DefaultedList.ofSize(4, ItemStack.EMPTY); }
	
	public ItemStack getEquippedStack(EquipmentSlot var1) { return ItemStack.EMPTY; }
	
	public void equipStack(EquipmentSlot var1, ItemStack var2) { }
	
	public Arm getMainArm() { return Arm.RIGHT; }
	
	public void tick()
	{
		super.tick();
		this.animations.tick(this);
		
		if(animations.currentAnim() == -1)
		{
			List<Integer> candidates = stateEngine.getTransitions(animations.lastAnim());
			animations.start(candidates.get(random.nextInt(candidates.size())), age);
		}
	}
}
