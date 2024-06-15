package de.bypander.communityradar.listener;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.render.PlayerNameTagRenderEvent;
import net.labymod.api.mojang.GameProfile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameTagListener {

  private CommunityRadar addon;
  ListManger manager;
  public NameTagListener(CommunityRadar addon) {
    this.addon = addon;
    manager = ListManger.get();
  }

  /*
  It is necessary to set the prefix every tick because the griefergames addon sets the prefix every tick to the display name
  (this is needed to show animations in the nametag) and would overwrite the nametag with the prefix.
   */
  @Subscribe(100)
  public void onRender(PlayerNameTagRenderEvent event) {
    if (!CommunityRadar.get().onGriefergames())
      return;

    if (!addon.configuration().getMarkOverHead().get())
      return;

    NetworkPlayerInfo info = event.playerInfo();
    if (info == null)
      return;
    GameProfile profile = info.profile();
    if (profile == null)
      return;
    String name = profile.getUsername();

    TextComponent prefix = manager.getPrefix(name.trim());
    if (prefix.getText().isBlank())
      return;

    TextComponent nameTag = prefix.append(event.nameTag());
    event.setNameTag(nameTag);
  }
}
