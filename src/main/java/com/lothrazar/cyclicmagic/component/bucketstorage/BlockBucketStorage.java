package com.lothrazar.cyclicmagic.component.bucketstorage;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBase;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBucketStorage extends BlockBase implements ITileEntityProvider, IHasRecipe, IBlockHasTESR {
  public BlockBucketStorage() {
    super(Material.IRON);
    this.setHardness(7F);
    this.setResistance(7F);
    this.setSoundType(SoundType.GLASS);
    this.setHarvestLevel("pickaxe", 1);
  }
  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //?? TE null? http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677315-solved-tileentity-returning-null
    //http://www.minecraftforge.net/forum/index.php?/topic/38048-19-solved-blockgetdrops-and-tileentity/
    List<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = Item.getItemFromBlock(this);//this.getItemDropped(state, rand, fortune);
    TileEntity ent = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(item);
    if (ent != null && ent instanceof TileEntityBucketStorage) {
      TileEntityBucketStorage te = (TileEntityBucketStorage) ent;
      FluidStack fs = te.getCurrentFluidStack();
      if (fs != null) {
        UtilNBT.setItemStackNBTVal(stack, NBT_FLUIDSIZE, fs.amount);
        String resourceStr = FluidRegistry.getFluidName(fs.getFluid());
        UtilNBT.setItemStackNBTVal(stack, NBT_FLUIDTYPE, resourceStr);
      }
    }
    ret.add(stack);
    return ret;
  }
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      NBTTagCompound tags = stack.getTagCompound();
      int fluidAmt = tags.getInteger(NBT_FLUIDSIZE);
      String resourceStr = tags.getString(NBT_FLUIDTYPE);
      TileEntityBucketStorage container = (TileEntityBucketStorage) worldIn.getTileEntity(pos);
      Fluid fluidObj = FluidRegistry.getFluid(resourceStr);//should never be null if fluidAmt > 0 
      if (fluidObj != null)
        container.fill(new FluidStack(fluidObj, fluidAmt), true);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT; // http://www.minecraftforge.net/forum/index.php?topic=18754.0
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) { // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
    return false;
  }
  @Override
  public TileEntity createNewTileEntity(World worldIn, int meta) {
    return new TileEntityBucketStorage();
  }
  //start of 'fixing getDrops to not have null tile entity', using pattern from forge BlockFlowerPot patch
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if (willHarvest) {
      return true;
    } //If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }
  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }
  //end of fixing getdrops
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "igi",
        "gog",
        "igi",
        'o', "obsidian", 'i', "ingotIron", 'g', "blockGlass");
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityBucketStorage te = (TileEntityBucketStorage) world.getTileEntity(pos);
    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (world.isRemote == false) { //server side
        FluidStack fs = te.getCurrentFluidStack();
        if (fs != null) {
          String amtStr = fs.amount + " / " + TileEntityBucketStorage.TANK_FULL + " ";
          UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + amtStr + fs.getLocalizedName());
        }
        else {
          UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.empty"));
        }
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBucketStorage.class, new FluidTESR());
  }
}
