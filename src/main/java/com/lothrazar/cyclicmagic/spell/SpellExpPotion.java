package com.lothrazar.cyclicmagic.spell;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.util.UtilSound;

public class SpellExpPotion extends  BaseSpellExp implements ISpell{

	private int potionId;
	private int potionDuration;
	private int potionAmp;

	public SpellExpPotion setPotion(int id, int effectDuration, int effectAmplifier){
		potionId = id;
		potionDuration = effectDuration;
		potionAmp = effectAmplifier;
		return this;
	}

	@Override
	public void cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target)
	{
		ModMain.addOrMergePotionEffect(player,new PotionEffect(potionId,potionDuration,potionAmp));
	}
	
	@Override
	public void onCastSuccess(World world, EntityPlayer player, BlockPos pos)
	{
		UtilSound.playSoundAt(player, "random.drink");

		super.onCastSuccess(world, player, pos);
	}
}