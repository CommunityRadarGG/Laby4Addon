package de.bypander.communityradar;

import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.commands.radar.RadarCommand;
import de.bypander.communityradar.config.CommunityRadarConfig;
import de.bypander.communityradar.listener.NameTagListener;
import de.bypander.communityradar.listener.ServerJoinListener;
import de.bypander.communityradar.listener.ServerLeaveListener;
import net.labymod.api.Constants;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.models.addon.annotation.AddonMain;
import de.bypander.communityradar.listener.MessageReceiveListener;
import net.labymod.api.util.I18n;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@AddonMain
public class CommunityRadar extends LabyAddon<CommunityRadarConfig> {
  private static CommunityRadar addon;

  private Boolean onGriefergames = false;

  private TextComponent addonPrefix;

  @Override
  protected void enable() {
    addon = this;
    this.registerSettingCategory();
    addonPrefix = Component.text(I18n.translate("communityradar.custom.prefix"));
    try {
      Files.createDirectories(Paths.get("communityradar", "lists"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    ListManger manger = new ListManger("communityradar/lists/");
    TextComponent scammer = Component.text(configuration().getScammerSubConfig().getPrefix().get().replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1"));
    TextComponent trust = Component.text(configuration().getTrustedMMSubConfig().getPrefix().get().replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1"));
    manger.addPublicList("communityradarscammer", scammer, "https://lists.community-radar.de/versions/v1/scammer.json");
    manger.addPublicList("verbvllerttrusted", trust, "https://lists.community-radar.de/versions/v1/trusted.json");

    this.registerListener(new MessageReceiveListener(this));
    this.registerListener(new NameTagListener(this));
    this.registerListener(new ServerJoinListener(this));
    this.registerListener(new ServerLeaveListener(this));
    this.registerCommand(new RadarCommand());

    this.logger().info("Enabled CommunityRadar");
  }

  public static CommunityRadar get() {
    return addon;
  }

  public Boolean onGriefergames() {
    return onGriefergames;
  }

  public void setOnGriefergames(Boolean onGriefergames) {
    this.onGriefergames = onGriefergames;
  }

  public TextComponent getAddonPrefix() {
    return this.addonPrefix.copy();
  }

  @Override
  protected Class<CommunityRadarConfig> configurationClass() {
    return CommunityRadarConfig.class;
  }
}
