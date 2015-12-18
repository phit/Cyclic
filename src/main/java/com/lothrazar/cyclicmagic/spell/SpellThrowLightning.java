package com.lothrazar.cyclicmagic.spell;

import com.lothrazar.cyclicmagic.projectile.EntityLightningballBolt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class SpellThrowLightning extends BaseSpellExp implements ISpell {

	private final int cooldown = 25;

	@Override
	public int getCastCooldown() {
		return cooldown;
	}

	@Override
	public boolean cast(World world, EntityPlayer player, BlockPos pos, EnumFacing side, Entity target) {

		return world.spawnEntityInWorld(new EntityLightningballBolt(world, player));
	}
}