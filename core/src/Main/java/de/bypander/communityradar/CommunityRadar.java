package de.bypander.communityradar;

import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.commands.radar.RadarCommand;
import de.bypander.communityradar.config.CommunityRadarConfig;
import de.bypander.communityradar.listener.NameTagListener;
import net.labymod.api.Constants;
import net.labymod.api.Laby;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import de.bypander.communityradar.listener.MessageReceiveListener;

@AddonMain
public class CommunityRadar extends LabyAddon<CommunityRadarConfig> {
  private static CommunityRadar addon;
  @Override
  protected void enable() {
    this.addon = this;
    this.registerSettingCategory();
    ListManger manger = new ListManger(Constants.Files.CONFIGS + "/communityradar/");
    manger.addPublicList("communityradarscammer", "§scammer", "https://communityradargg.github.io/TestPage/scammer.json");
    manger.addPublicList("verbvllerttrusted", "§trusted", "https://communityradargg.github.io/TestPage/trustedMM.json");

    this.registerListener(new MessageReceiveListener(this));
    this.registerListener(new NameTagListener(this));
    this.registerCommand(new RadarCommand());

    this.logger().info("Enabled CommunityRadar");
  }

  public static CommunityRadar get() {
    return addon;
  }

  @Override
  protected Class<CommunityRadarConfig> configurationClass() {
    return CommunityRadarConfig.class;
  }
}
