package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import com.lothrazar.cyclicmagic.event.*;
import com.lothrazar.cyclicmagic.util.Const;

public class EventRegistry {

	private static ArrayList<Object>	events	= new ArrayList<Object>();

	private static boolean						nameTagDeath;

	private static boolean						playerWakeup;

	private static boolean						signSkullName;

	private static boolean						playerDeathCoords;

	private static boolean farmDropBuffs;

	private static boolean monsterDropsNerfed;

	private static boolean endermanDrop;

	private static boolean nameVillagerTag;

	private static boolean editableSigns;

	private static boolean mountedPearl;

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "Mobs";

		config.setCategoryComment(category, "Changes to mobs");

		farmDropBuffs = config.getBoolean("Farm Drops Buffed", category, true, "Increase drops of farm animals: more leather, more wool from shearing, pigs drop a bit more pork");
		
		monsterDropsNerfed = config.getBoolean("Monster Drops Nerfed", category, true, "Zombies no longer drops crops or iron");
		
		nameTagDeath = config.getBoolean("Name Tag Death", category, true, "When an entity dies that is named with a tag, it drops the nametag");
		endermanDrop = config.getBoolean("Enderman Block", category, true, "Enderman will always drop block they are carrying 100%");


		category = Const.MODCONF + "Misc";
		
		signSkullName = config.getBoolean("Name Player Skulls with Sign", category, true, "Use a player skull on a sign to name the skull based on the top line");
		

		category = Const.MODCONF + "Player";
		config.setCategoryComment(category, "Changes to player properties or actions");
		
		nameVillagerTag = config.getBoolean("Villager Nametag", category, true, "Let players name villagers with nametags");

		editableSigns = config.getBoolean("Editable Signs", category, true, "Allow editing a sign with right click");
		
		playerWakeup = config.getBoolean("Wakeup Curse", category, true, "Using a bed to skip the night has some mild potion effect related drawbacks");

		playerDeathCoords = config.getBoolean("Death Coords", category, true, "Display your coordinates in chat when you die");
	
		mountedPearl = config.getBoolean("Pearls On Horseback", category, true, "Enderpearls work on a horse, bringing it with you");
		
	}

	public static void register() {

		// some just always have to happen no matter what. for other features.
		//they will do nothing if for example their items do not exist or otherwise disabled
		events.add(new EventConfigChanged());
		events.add(new EventPotions());
		events.add(new EventSpells());
		events.add(new EventKeyInput());
		events.add(new EventHorseFood());
		// no reason to turn these off
		
		// TODO: consider configs for these
		events.add(new EventGuiAddButtons());
		events.add(new EventFoodDetails());
		events.add(new EventFragileTorches());
		events.add(new EventFurnaceStardew());
		
		if(mountedPearl){
			events.add(new EventMountedPearl());
		}
		events.add(new EventBucketBlocksBreak());
		
		

		if(editableSigns){
			events.add(new EventEditSign());
		}

		if(nameVillagerTag){
			events.add(new EventNameVillager());
		}

		if(endermanDrop){
			events.add(new EventEndermanDropBlock());
		}
		if(farmDropBuffs){
			
			events.add(new EventAnimalDropBuffs());
		}
		
		if(monsterDropsNerfed){
			events.add(new EventMobDropsReduced());
		}
		if (nameTagDeath) {
			events.add(new EventNametagDeath());
		}
		if (playerDeathCoords) {
			events.add(new EventPlayerDeathCoords());
		}
		if (playerWakeup) {
			events.add(new EventPlayerWakeup());
		}
		if (signSkullName) {
			events.add(new EventSignSkullName());
		}
		for (Object e : events) {
			MinecraftForge.EVENT_BUS.register(e);
		}
	}

	/*
	 * @SubscribeEvent public void onEntityJoinWorldEvent(EntityJoinWorldEvent
	 * event) {
	 * 
	 * if(event.entity instanceof EntityLivingBase && event.world.isRemote) {
	 * EntityLivingBase
	 * living = (EntityLivingBase)event.entity;
	 * 
	 * if(living instanceof EntityWolf && ((EntityWolf)living).isTamed()) {
	 * setMaxHealth(living,heartsWolfTamed*2); } if(living instanceof EntityOcelot
	 * &&
	 * ((EntityOcelot)living).isTamed()) { setMaxHealth(living,heartsCatTamed*2);
	 * }
	 * 
	 * if(living instanceof EntityVillager && ((EntityVillager)living).isChild()
	 * == false) {
	 * setMaxHealth(living,heartsVillager*2); } } }
	 */

}
