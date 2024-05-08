package de.bypander.communityradar;

import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.commands.radar.RadarCommand;
import de.bypander.communityradar.config.CommunityRadarConfig;
import de.bypander.communityradar.listener.NameTagListener;
import net.labymod.api.Constants;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.models.addon.annotation.AddonMain;
import de.bypander.communityradar.listener.MessageReceiveListener;

@AddonMain
public class CommunityRadar extends LabyAddon<CommunityRadarConfig> {
  private static CommunityRadar addon;
  @Override
  protected void enable() {
    addon = this;
    this.registerSettingCategory();
    ListManger manger = new ListManger(Constants.Files.CONFIGS + "/communityradar/");
    manger.addPublicList("communityradarscammer", "§scammer", "https://lists.community-radar.de/versions/v1/scammer.json");
    manger.addPublicList("verbvllerttrusted", "§trusted", "https://lists.community-radar.de/versions/v1/trusted.json");

    this.registerListener(new MessageReceiveListener(this));
    this.registerListener(new NameTagListener(this));
    this.registerCommand(new RadarCommand());

    this.logger().info("Enabled CommunityRadar");
  }

  public static CommunityRadar get() {
    return addon;
  }

  public static String prefix(String prefix) {
    if (prefix.trim().equals("§scammer"))
      return CommunityRadar.get().configuration().getScammerSubConfig().getPrefix().get() + " ";

    if (prefix.trim().equals("§trusted"))
      return  CommunityRadar.get().configuration().getTrustedMMSubConfig().getPrefix().get() + " ";

    return prefix;
  }

  @Override
  protected Class<CommunityRadarConfig> configurationClass() {
    return CommunityRadarConfig.class;
  }
}
