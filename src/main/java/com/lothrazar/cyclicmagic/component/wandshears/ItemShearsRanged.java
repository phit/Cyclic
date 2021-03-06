package com.lothrazar.cyclicmagic.component.wandshears;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemShearsRanged extends BaseItemProjectile implements IHasRecipe {
  public ItemShearsRanged() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityShearingBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " cs",
        " sc",
        "t  ",
        'c', new ItemStack(Blocks.MOSSY_COBBLESTONE),
        't', new ItemStack(Blocks.CACTUS),
        's', new ItemStack(Items.SHEARS));
  }
  @Override
  public void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityShearingBolt(world, player));
    UtilItemStack.damageItem(player, held);
  }
  @Override
  public SoundEvent getSound() {
    return SoundEvents.ENTITY_EGG_THROW;
  }
  /**
   * Returns true if the item can be used on the given entity, e.g. shears on
   * sheep. COPY from vanilla SHEARS
   */
  @Override
  public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand) {
    if (entity.world.isRemote) {
      return false;
    }
    if (entity instanceof net.minecraftforge.common.IShearable) {
      net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) entity;
      BlockPos pos = new BlockPos(entity.posX, entity.posY, entity.posZ);
      if (target.isShearable(itemstack, entity.world, pos)) {
        java.util.List<ItemStack> drops = target.onSheared(itemstack, entity.world, pos,
            net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));
        java.util.Random rand = new java.util.Random();
        for (ItemStack stack : drops) {
          net.minecraft.entity.item.EntityItem ent = entity.entityDropItem(stack, 1.0F);
          ent.motionY += rand.nextFloat() * 0.05F;
          ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
          ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
        }
        itemstack.damageItem(1, entity);
      }
      return true;
    }
    return false;
  }
  /**
   * 
   * COPY from vanilla SHEARS
   */
  @Override
  public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, net.minecraft.entity.player.EntityPlayer player) {
    if (player.world.isRemote || player.capabilities.isCreativeMode) {
      return false;
    }
    Block block = player.world.getBlockState(pos).getBlock();
    if (block instanceof net.minecraftforge.common.IShearable) {
      net.minecraftforge.common.IShearable target = (net.minecraftforge.common.IShearable) block;
      if (target.isShearable(itemstack, player.world, pos)) {
        java.util.List<ItemStack> drops = target.onSheared(itemstack, player.world, pos,
            net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.FORTUNE, itemstack));
        java.util.Random rand = new java.util.Random();
        for (ItemStack stack : drops) {
          float f = 0.7F;
          double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
          net.minecraft.entity.item.EntityItem entityitem = new net.minecraft.entity.item.EntityItem(player.world, (double) pos.getX() + d, (double) pos.getY() + d1, (double) pos.getZ() + d2, stack);
          entityitem.setDefaultPickupDelay();
          player.world.spawnEntity(entityitem);
        }
        itemstack.damageItem(1, player);
        player.addStat(net.minecraft.stats.StatList.getBlockStats(block));
        player.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11); //TODO: Move to IShearable implementors in 1.12+
        return true;
      }
    }
    return false;
  }
  @Override
  public float getStrVsBlock(ItemStack stack, IBlockState state) {
    Block block = state.getBlock();
    if (block != Blocks.WEB && state.getMaterial() != Material.LEAVES) {
      return block == Blocks.WOOL ? 5.0F : super.getStrVsBlock(stack, state);
    }
    else {
      return 15.0F;
    }
  }
}
