package de.bypander.communityradar.listener;

import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.ChatMessage;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.chat.ChatReceiveEvent;
import de.bypander.communityradar.CommunityRadar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceiveListener {

  private final CommunityRadar addon;

  private final Pattern globalChatRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\!?\\w{1,16})");

  public MessageReceiveListener(CommunityRadar addon) {
    this.addon = addon;
  }

  @Subscribe(-100)
  public void onMessage(ChatReceiveEvent event) {
    if (!CommunityRadar.get().onGriefergames())
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


    TextComponent prefix = manager.getPrefix(matcher.group(2).trim());


    int index = 0;
    List<Component> childs = event.chatMessage().component().getChildren();
    for(int i = 0; i < childs.size(); i++) {
      if (childs.get(i).style().getClickEvent() == null)
        continue;
      if (childs.get(i).style().getClickEvent().getValue().startsWith("/clan info")) {
        index = i;
        break;
      }
      if (childs.get(i).style().getClickEvent().getValue().startsWith("/msg")) {
        index = i;
        break;
      }
    }

    ArrayList<Component> arraylist = new ArrayList<>(childs);
    System.out.println(index);
    arraylist.add(index, prefix);
    event.setMessage(event.message().setChildren(arraylist));
  }
}
