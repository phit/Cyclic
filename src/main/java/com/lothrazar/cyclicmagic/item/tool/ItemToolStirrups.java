package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolStirrups extends BaseTool implements IHasRecipe {
  public ItemToolStirrups() {
    super(100);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    if (entity.worldObj.isRemote) { return false; }
    //TODO: possible blacklist?
    player.startRiding(entity, true);
    super.onUse(stack, player, player.worldObj, hand);
    return true;
  }
  @Override
  public String getTooltip() {
    return "item.tool_mount.tooltip";
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " ls",
        " sl",
        "ii ",
        'l', Items.LEAD,
        'i', Items.IRON_INGOT,
        's', Items.LEATHER);
  }
}
