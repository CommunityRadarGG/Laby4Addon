package de.bypander.communityradar.config;

import net.labymod.api.Laby;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget.ButtonSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.annotation.ConfigName;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.annotation.SpriteTexture;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.models.OperatingSystem;
import net.labymod.api.util.MethodOrder;

import java.util.Objects;

/**
 * Parts of the Icons are from the GrieferGames Addon: <a href="https://github.com/cosmohdx/LabyMod4-GrieferGames-Addon/">Addon</a>
 */
@ConfigName("settings")
@SpriteTexture("settings.png")
public class CommunityRadarConfig extends AddonConfig {

  @SpriteSlot(x = 0, y = 0)
  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @SpriteSlot(x = 1, y = 0)
  @SwitchSetting
  private final ConfigProperty<Boolean> markInChat = new ConfigProperty<>(true);

  @SpriteSlot(x = 2, y = 0)
  @SwitchSetting
  private final ConfigProperty<Boolean> markOverHead = new ConfigProperty<>(true);

  @SpriteSlot(x = 3, y = 0)
  private final ScammerSubConfig scammerSubConfig = new ScammerSubConfig();

  @SpriteSlot(x = 4, y = 0)
  private final TrustedMMSubConfig trustedMMSubConfig = new TrustedMMSubConfig();

  @SpriteSlot(x = 5, y = 0)
  @MethodOrder(after = "trustedMMSubConfig")
  @ButtonSetting
  public void openDiscord() {
    OperatingSystem.getPlatform().openUri("http://discord.community-radar.de/");
  }

  @SpriteSlot(x = 6, y = 0)
  @MethodOrder(after = "openDiscord")
  @ButtonSetting
  public void openGitHub() {
    OperatingSystem.getPlatform().openUri("https://github.com/CommunityRadarGG/");
  }

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public ConfigProperty<Boolean> getMarkInChat() {
    return markInChat;
  }

  public ConfigProperty<Boolean> getMarkOverHead() {
    return markOverHead;
  }

  public ScammerSubConfig getScammerSubConfig() {
    return scammerSubConfig;
  }

  public TrustedMMSubConfig getTrustedMMSubConfig() {
    return trustedMMSubConfig;
  }
}
