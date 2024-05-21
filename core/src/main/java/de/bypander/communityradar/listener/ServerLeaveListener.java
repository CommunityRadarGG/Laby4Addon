package de.bypander.communityradar.listener;

import de.bypander.communityradar.CommunityRadar;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.client.network.server.ServerJoinEvent;

public class ServerLeaveListener {

  private final CommunityRadar addon;

  public ServerLeaveListener(CommunityRadar addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onDisconnect (ServerDisconnectEvent event) {
    addon.setOnGriefergames(false);
  }
}
