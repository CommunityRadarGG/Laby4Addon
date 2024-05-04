package de.bypander.communityradar.listener;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
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

    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return;

    if (!addon.configuration().getMarkOverHead().get())
      return;

    Matcher matcher = nameTagRegex.matcher(componentToPlaneText(event.nameTag()));
    if (!matcher.find()) {
      return;
    }

    if (!manager.inList(matcher.group(2).trim()))
      return;

    String prefix = manager.getPrefix(matcher.group(2).trim());
    if (prefix.trim().equals("§scammer")) {
      if (!addon.configuration().getScammerSubConfig().getEnabled().get())
        return;
    }
    if (prefix.trim().equals("§trusted")) {
      if (!addon.configuration().getTrustedMMSubConfig().getEnabled().get())
        return;
    }
    prefix = CommunityRadar.prefix(prefix);

    event.setNameTag(
      Component.text(prefix.replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1")).append(event.nameTag()));
  }

  public String componentToPlaneText(Component component) {
    return Laby.references().componentRenderer().plainSerializer().serialize(component);
  }
}
