package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.playerext.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.component.playerext.ItemFoodInventory;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemStirrupsReverse;
import com.lothrazar.cyclicmagic.item.food.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.food.ItemAppleLapis;
import com.lothrazar.cyclicmagic.item.food.ItemAppleStep;
import com.lothrazar.cyclicmagic.item.food.ItemChorusCorrupted;
import com.lothrazar.cyclicmagic.item.food.ItemChorusGlowing;
import com.lothrazar.cyclicmagic.item.food.ItemHeartContainer;
import com.lothrazar.cyclicmagic.item.food.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.food.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemConsumeablesModule extends BaseModule implements IHasConfig {
  private boolean enableEmeraldApple;
  private boolean enableHeartContainer;
  private boolean enableInventoryCrafting;
  private boolean enableInventoryUpgrade;
  private boolean enableCorruptedChorus;
  private boolean enableHorseFoodUpgrades;
  private boolean enableGlowingChorus;
  private boolean enableLapisApple;
  private boolean foodStep;
  private boolean mountInverse;
  @Override
  public void onPreInit() {
    if (foodStep) {
      ItemAppleStep food_step = new ItemAppleStep();
      ItemRegistry.register(food_step, "food_step");
      ModCyclic.instance.events.register(food_step);
    }
    if (mountInverse) {
      ItemStirrupsReverse stirrup_inverse = new ItemStirrupsReverse();
      ItemRegistry.register(stirrup_inverse, "tool_mount_inverse");
    }
    if (enableHorseFoodUpgrades) {
      Item emerald_carrot = new ItemHorseUpgrade(HorseUpgradeType.TYPE, new ItemStack(Items.EMERALD));
      Item lapis_carrot = new ItemHorseUpgrade(HorseUpgradeType.VARIANT, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      Item diamond_carrot = new ItemHorseUpgrade(HorseUpgradeType.HEALTH, new ItemStack(Items.DIAMOND));
      Item redstone_carrot = new ItemHorseUpgrade(HorseUpgradeType.SPEED, new ItemStack(Items.REDSTONE));
      Item ender_carrot = new ItemHorseUpgrade(HorseUpgradeType.JUMP, new ItemStack(Items.ENDER_EYE));
      ItemRegistry.register(emerald_carrot, "horse_upgrade_type");
      ItemRegistry.register(lapis_carrot, "horse_upgrade_variant");
      ItemRegistry.register(diamond_carrot, "horse_upgrade_health");
      ItemRegistry.register(redstone_carrot, "horse_upgrade_speed");
      ItemRegistry.register(ender_carrot, "horse_upgrade_jump");
      ModCyclic.instance.events.register(this);//for SubcribeEvent hooks 
    }
    if (enableLapisApple) {
      ItemAppleLapis apple_lapis = new ItemAppleLapis();
      ItemRegistry.register(apple_lapis, "apple_lapis");
      ModCyclic.instance.events.register(apple_lapis);
    }
    if (enableEmeraldApple) {
      ItemAppleEmerald apple_emerald = new ItemAppleEmerald();
      ItemRegistry.register(apple_emerald, "apple_emerald");
      LootTableRegistry.registerLoot(apple_emerald);
      ModCyclic.instance.events.register(apple_emerald);
    }
    if (enableHeartContainer) {
      ItemHeartContainer heart_food = new ItemHeartContainer();
      ItemRegistry.register(heart_food, "heart_food");
      ModCyclic.instance.events.register(heart_food);
      LootTableRegistry.registerLoot(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(heart_food, ChestType.IGLOO);
    }
    if (enableInventoryCrafting) {
      ItemFoodCrafting crafting_food = new ItemFoodCrafting();
      ItemRegistry.register(crafting_food, "crafting_food");
      LootTableRegistry.registerLoot(crafting_food);
    }
    if (enableInventoryUpgrade) {
      ItemFoodInventory inventory_food = new ItemFoodInventory();
      ItemRegistry.register(inventory_food, "inventory_food");
      LootTableRegistry.registerLoot(inventory_food);
    }
    if (enableCorruptedChorus) {
      ItemChorusCorrupted corrupted_chorus = new ItemChorusCorrupted();
      ItemRegistry.register(corrupted_chorus, "corrupted_chorus");
      ModCyclic.instance.events.register(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.ENDCITY);
    }
    if (enableGlowingChorus) {
      ItemChorusGlowing glowing_chorus = new ItemChorusGlowing();
      ItemRegistry.register(glowing_chorus, "glowing_chorus");
      ModCyclic.instance.events.register(glowing_chorus);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    foodStep = config.getBoolean("AppleStature", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    mountInverse = config.getBoolean("StirrupInverse", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLapisApple = config.getBoolean("LapisApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableGlowingChorus = config.getBoolean("GlowingChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEmeraldApple = config.getBoolean("EmeraldApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHeartContainer = config.getBoolean("HeartContainer(food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryCrafting = config.getBoolean("InventoryCrafting(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryUpgrade = config.getBoolean("InventoryUpgrade(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCorruptedChorus = config.getBoolean("CorruptedChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHorseFoodUpgrades = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String category = Const.ConfigCategory.modpackMisc;
    ItemHorseUpgrade.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemHorseUpgrade.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemHorseUpgrade.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
  }
}
