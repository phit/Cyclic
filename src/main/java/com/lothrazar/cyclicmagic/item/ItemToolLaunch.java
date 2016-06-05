package com.lothrazar.cyclicmagic.item;

import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolLaunch extends BaseTool implements IHasRecipe {

	private static final int durability 	= 1000;
	private static final double	power		= 1.5;
	private static final int cooldown 		= 21;
	private static final double	mountPower	= power - 0.5;
 
	public ItemToolLaunch() {
		super(durability);  
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){
		//if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return repair != null && repair.getItem() == Items.DIAMOND;
    }
    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected){
    	if(isSelected){
    		entityIn.fallDistance = 0;
    		Entity ridingEntity = entityIn.getRidingEntity();

    		if (ridingEntity != null) {
    			ridingEntity.fallDistance = 0;
    		}
    	}
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player, EnumHand hand) {

	//	rotationYaw -89.250336
		// rotationPitch  :  57.14997
		
		float rotationYaw = player.rotationYaw ;
		//fixed vertical launch angle
		float rotationPitch = 57;//player.rotationPitch ;
		 
		
		double velX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);
		double velZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);

		double velY = (double) (-MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power);

		// launch the player up and forward at minimum angle
		// regardless of look vector
		if (velY < 0) {
			velY *= -1;// make it always up never down
		}
//		if (velY < 0.4) {
//			System.out.println("A");
//			velY = 0.4 + player.jumpMovementFactor;
//		}
//		boolean isLookingDown = (player.getLookVec().yCoord < -20);
//		if(isLookingDown){
//			System.out.println("B");
//			velY += 2.5;
//		}
		
		Entity ridingEntity = player.getRidingEntity();

		if (ridingEntity != null) {
			
			// boost power a bit, horses are heavy as F
			ridingEntity.motionY = 0;
			ridingEntity.addVelocity(velX * mountPower, velY * mountPower, velZ * mountPower);

		}
		else {
			player.motionY = 0;
			player.addVelocity(velX, velY, velZ);
		}

		UtilParticle.spawnParticle(worldIn, EnumParticleTypes.CRIT_MAGIC, player.getPosition());
		UtilSound.playSound(player, player.getPosition(), SoundRegistry.bwoaaap);

		player.getCooldownTracker().setCooldown(this, cooldown);
		super.onUse(itemStackIn, player, worldIn, hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@Override
	public void addRecipe() {
		BrewingRecipeRegistry.addRecipe(
				 new ItemStack(Items.ELYTRA),
				 new ItemStack(Items.NETHER_STAR),
				 new ItemStack(this));
	}
	
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack){
	    return true;
	}
}