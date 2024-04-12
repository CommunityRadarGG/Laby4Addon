package de.bypander.communityradar.config;

import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.ParentSwitch;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class TrustedMMSubConfig extends Config {

  @ParentSwitch
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);

  @TextFieldSetting
  private final ConfigProperty<String> prefix = new ConfigProperty<>("&7[&aMM&7]&r");

  public ConfigProperty<Boolean> getEnabled() {
    return enabled;
  }

  public ConfigProperty<String> getPrefix() {
    return prefix;
  }
}
