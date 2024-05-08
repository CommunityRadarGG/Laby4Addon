package de.bypander.communityradar.listener;

import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import de.bypander.communityradar.CommunityRadar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parts of the logic is from the Scammerinfo Addon: <a href="https://github.com/Neocraftr/LabyMod-ScammerInfo/tree/master">Addon</a>
 */
public class MessageReceiveListener {

  private final CommunityRadar addon;

  private final Pattern globalChatRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\!?\\w{1,16})");

  public MessageReceiveListener(CommunityRadar addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onMessage(ChatReceiveEvent event) {
    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return;

    if (!addon.configuration().getMarkInChat().get())
      return;

    Matcher matcher = globalChatRegex.matcher(event.chatMessage().getPlainText());
    if (!matcher.find()) {
      return;
    }

    ListManger manager = ListManger.get();
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


    int index = 0;
    for (int i = 0; i < event.chatMessage().component().getChildren().size(); i++) {
      TextComponent child = (TextComponent) event.chatMessage().component().getChildren().get(i);
      Matcher m = globalChatRegex.matcher(componentToPlaneText(child));
      if (!m.find())
        continue;

      if (i > 0) {
        String clanTag = componentToFormattedText(event.chatMessage().component().getChildren().get(i - 1));
        if (clanTag.contains("§r§6[") && clanTag.contains("§r§6]"))
          index--;
      }
    }
    List<Component> childs = event.chatMessage().component().getChildren();
    ArrayList<Component> arraylist = new ArrayList<>(childs);
    arraylist.add(index, Component.text(prefix.replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1")).clickEvent(ClickEvent.changePage("")));
    event.setMessage(event.message().setChildren(arraylist));
  }

  public String componentToFormattedText(Component component) {
    return Laby.references().componentRenderer().legacySectionSerializer().serialize(component);
  }

  public String componentToPlaneText(Component component) {
    return Laby.references().componentRenderer().plainSerializer().serialize(component);
  }
}
