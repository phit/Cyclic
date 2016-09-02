package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.item.charm.*;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class CharmModule extends BaseModule {
  private boolean enableFire;
  private boolean enableSea;
  private boolean enableVoid;
  private boolean enableWater;
  @Override
  public void onInit() {
    if (enableFire) {
      ItemCharmFire charm_fire = new ItemCharmFire();
      ItemRegistry.addItem(charm_fire, "charm_fire");
    }
    if (enableSea) {
      ItemCharmBoat charm_boat = new ItemCharmBoat();
      ItemRegistry.addItem(charm_boat, "charm_boat");
    }
    if (enableVoid) {
      ItemCharmVoid charm_void = new ItemCharmVoid();
      ItemRegistry.addItem(charm_void, "charm_void");
    }
    if (enableWater) {
      ItemCharmWater charm_water = new ItemCharmWater();
      ItemRegistry.addItem(charm_water, "charm_water");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    enableFire = config.getBoolean("FireCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSea = config.getBoolean("SailorCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVoid = config.getBoolean("VoidCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWater = config.getBoolean("WaterCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}