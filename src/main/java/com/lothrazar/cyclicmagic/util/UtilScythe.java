package com.lothrazar.cyclicmagic.util;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemScythe.ScytheType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class UtilScythe {
  private static String[] blacklist;
  private static ScytheConfig leafConfig = new ScytheConfig();
  private static ScytheConfig brushConfig = new ScytheConfig();
  private static class ScytheConfig {
    NonNullList<String> blockWhitelist = NonNullList.create();
    NonNullList<String> oreDictWhitelist = NonNullList.create();
  }
  public static void syncConfig(Configuration config) {
    //TODO: config it after its decided? maybe? maybe not?
/* @formatter:off */
    leafConfig.blockWhitelist = NonNullList.from(
        "extratrees:leaves.decorative.0"
       , "extratrees:leaves.decorative.1"
       , "extratrees:leaves.decorative.2"
       , "extratrees:leaves.decorative.3"
       , "extratrees:leaves.decorative.4"
       , "extratrees:leaves.decorative.5"
       , "forestry:leaves.decorative.0"
       , "forestry:leaves.decorative.1"
       , "terraqueous:foliage3:5"
       , "plants2:nether_leaves"
       , "plants2:crystal_leaves"
       , "plants2:leaves_0");
    
    leafConfig.oreDictWhitelist = NonNullList.from(
        "treeLeaves"
        );
    
    brushConfig.oreDictWhitelist = NonNullList.from("vine", "plant","flowerYellow");
    brushConfig.blockWhitelist = NonNullList.from(
        "plants2:cosmetic_0"
        ,"plants2:cosmetic_1"
        ,"plants2:cosmetic_2"
        ,"plants2:cosmetic_3"
        ,"plants2:cosmetic_4"
        ,"plants2:desert_0"
        ,"plants2:desert_1"
        ,"plants2:double_0"
        ,"plants2:cataplant"
        ,"botany:itemflower"
        ,"biomesoplenty:flower_0"
        ,"biomesoplenty:flower_1"
        ,"biomesoplenty:plant_0"
        ,"biomesoplenty:plant_1"
        ,"biomesoplenty:mushroom"
        ,"biomesoplenty:doubleplant"
        ,"biomesoplenty:flower_vine"
        ,"biomesoplenty:ivy"
        ,"biomesoplenty:tree_moss"
        ,"biomesoplenty:willow_vine"
        ,"croparia:fruit_grass"
        ,"plants2:androsace_a"
        ,"plants2:akebia_q_vine"
        ,"plants2:ampelopsis_a_vine"
        ,"plants2:adlumia_f"
        ,"abyssalcraft:wastelandsthorn"
        ,"abyssalcraft:luminousthistle"
        ,"harvestcraft:garden"
        );
    /* @formatter:on */
        String[] deflist = new String[] {
            "terraqueous:pergola"
        };
    // String category = Const.ConfigCategory.modpackMisc;
        blacklist = deflist;
        
        
        //= config.getStringList("HarvesterBlacklist", category, deflist, "Crops & bushes that are blocked from harvesting (Garden Scythe and Harvester).  Put an item that gets dropped to blacklist the harvest.  For example, add the item minecraft:potato to stop those from working");
  }
  private static boolean doesMatch(Block blockCheck, ScytheConfig type) {
    if (type.blockWhitelist.contains(blockCheck.getRegistryName().toString())) {
      return true;
    }
    else {
      ItemStack bStack = new ItemStack(blockCheck);
      for (String oreId : type.oreDictWhitelist) {
        if (OreDictionary.doesOreNameExist(oreId)) {
          for (ItemStack s : OreDictionary.getOres(oreId)) {
            if (OreDictionary.itemMatches(s, bStack, false)) {
              return true;
            }
          }
        }
      }
    }
    return false;//
  }
  public static boolean harvestSingle(World world, BlockPos posCurrent, ScytheType type) {
    boolean doBreakAbove = false;
    boolean doBreakBelow = false;
    boolean doBreak = false;
    IBlockState blockState = world.getBlockState(posCurrent);
    boolean addDropsToList = true;
    Block blockCheck = blockState.getBlock();
    if (blockCheck == Blocks.AIR) {
      return false;
    }
    Item seedItem = blockCheck.getItemDropped(blockCheck.getDefaultState(), world.rand, 0);//RuntimeException at this line
    if (isItemInBlacklist(seedItem)) {
      return false;
    }
    if (blockCheck.getRegistryName() == null) {
      //      ModCyclic.logger.error("Error: a block has not been registered");
    }
    else {
      switch (type) {
        case CROPS:
        break;
        case LEAVES:
          if (doesMatch(blockCheck, leafConfig)) {
            doBreak = true;
          }
        break;
        case WEEDS:
          if (doesMatch(blockCheck, brushConfig)) {
            doBreak = true;
          }
        break;
        default:
        break;
      }
    }
    IBlockState bsAbove = world.getBlockState(posCurrent.up());
    IBlockState bsBelow = world.getBlockState(posCurrent.down());
    final NonNullList<ItemStack> drops = NonNullList.create();
    // (A): garden scythe and harvester use this; 
    //      BUT type LEAVES and WEEDS harvester use DIFFERENT NEW class
    //     then each scythe has config list of what it breaks (maybe just scythe for all the modplants. also maybe hardcoded)
    // (B):  use this new "age" finding thing by default for harvest/replant
    // (C): figure out config system for anything that DOESNT work with this "age" system
    // (D): another  list of "just break it" blocks mapped by "mod:name"
    // PROBABLY: scythe will not have the break-it methods
    // "minecraft:pumpkin","minecraft:cactus", "minecraft:melon_block","minecraft:reeds"
    //  EXAMPLE: pumpkin, melon
    // (E): an ignore list of ones to skip EXAMPLE: stem
    switch (type) {
      case WEEDS:
        if (blockCheck instanceof BlockTallGrass) {// true for ItemScythe type WEEDS
          doBreak = true;
          if (blockCheck instanceof BlockTallGrass && bsAbove != null && bsAbove.getBlock() instanceof BlockTallGrass) {
            doBreakAbove = true;
          }
          if (bsBelow instanceof BlockTallGrass && bsBelow != null && bsBelow.getBlock() instanceof BlockTallGrass) {
            doBreakBelow = true;
          }
        }
        else if (blockCheck instanceof BlockDoublePlant) {// true for ItemScythe type WEEDS
          doBreak = true;
          if (blockCheck instanceof BlockDoublePlant && bsAbove != null && bsAbove.getBlock() instanceof BlockDoublePlant) {
            doBreakAbove = true;
          }
          if (bsBelow instanceof BlockDoublePlant && bsBelow != null && bsBelow.getBlock() instanceof BlockDoublePlant) {
            doBreakBelow = true;
          }
        }
        else if (blockCheck instanceof BlockMushroom) {//remove from harvester tile? used by weeds though
          doBreak = true;
        }
      break;
      case CROPS:
      break;
      case LEAVES:
        if (blockCheck instanceof BlockLeaves) {// true for ItemScythe type LEAVES
          doBreak = true;
        }
      break;
    }
    //cant do BlockBush, too generic, too many things use
    //many bushes are also crops.  
    //    else if (blockCheck == Blocks.RED_FLOWER || blockCheck == Blocks.YELLOW_FLOWER
    //        || blockCheck instanceof BlockFlower
    //        || blockClassString.equals("shadows.plants.block.PlantBase")
    //        || blockClassString.equals("shadows.plants.block.internal.cosmetic.BlockHarvestable")
    //        || blockClassString.equals("shadows.plants.block.internal.cosmetic.BlockMetaBush")
    //        || blockClassString.equals("de.ellpeck.actuallyadditions.mod.blocks.BlockBlackLotus")
    //        || blockClassString.equals("de.ellpeck.actuallyadditions.mod.blocks.base.BlockWildPlant")
    //        || blockClassString.equals("biomesoplenty.common.block.BlockBOPMushroom")
    //        || blockClassString.equals("rustic.common.blocks.crops.Herbs$1")) {
    //      if (conf.doesFlowers) { // true for ItemScythe type WEEDS
    //        doBreak = true;
    //      }
    //    }
    //    else if (blockCheck instanceof IShearable) {
    //      if (conf.doesIShearable) {
    //        addDropsToList = false;
    //        drops.addAll(((IShearable) blockCheck).onSheared(ItemStack.EMPTY, world, posCurrent, 0));
    //       
    //        doBreak = true;
    //      }
    //    }
    // 
    if (doBreak) {
      //break with false so that we can get the drops our own way
      world.destroyBlock(posCurrent, false);//false == no drops. literally just for the sound
      if (addDropsToList) {
        blockCheck.getDrops(drops, world, posCurrent, blockState, 0);
      }
      //break above first BECAUSE 2 high tallgrass otherwise will bug out if you break bottom first
      if (doBreakAbove) {
        world.destroyBlock(posCurrent.up(), false);
      }
      if (doBreakBelow) {
        world.destroyBlock(posCurrent.down(), false);
      }
      for (ItemStack drop : drops) {
        UtilItemStack.dropItemStackInWorld(world, posCurrent, drop);
      }
      return true;
    }
    return false;
  }
  private static boolean isItemInBlacklist(Item seedItem) {
    String itemName = UtilItemStack.getStringForItem(seedItem);
    for (String s : blacklist) {//dont use .contains on the list. must use .equals on string
      if (s != null && s.equals(itemName)) {
        return true;
      }
    }
    return false;
  }
}