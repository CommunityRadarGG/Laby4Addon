package de.bypander.communityradar.listener;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameTagListener {

  private CommunityRadar addon;
  private final Pattern nameTagRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\!?\\w{1,16})");
  ListManger manager;
  public NameTagListener(CommunityRadar addon) {
    this.addon = addon;
    manager = ListManger.get();
  }

  @Subscribe(100)
  public void onRender(PlayerNameTagRenderEvent event) {
    if (!CommunityRadar.get().onGriefergames())
      return;

    if (!addon.configuration().getMarkOverHead().get())
      return;

    Matcher matcher = nameTagRegex.matcher(componentToPlaneText(event.nameTag()));
    if (!matcher.find()) {
      return;
    }

    if (!manager.inList(matcher.group(2).trim()))
      return;

    TextComponent prefix = manager.getPrefix(matcher.group(2).trim());
    TextComponent nameTag = prefix.append(event.nameTag());
    event.setNameTag(nameTag);
  }

  public String componentToPlaneText(Component component) {
    return Laby.references().componentRenderer().plainSerializer().serialize(component);
  }
}
