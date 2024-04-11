package de.bypander.communityradar.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class ScammerSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @TextFieldSetting
  private final ConfigProperty<String> prefix = new ConfigProperty<>("&7[&cScammer&7]&r");

  public ConfigProperty<Boolean> getEnabled() {
    return enabled;
  }

  public ConfigProperty<String> getPrefix() {
    return prefix;
  }
}
