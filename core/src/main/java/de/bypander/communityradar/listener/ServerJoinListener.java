package de.bypander.communityradar.listener;

import de.bypander.communityradar.CommunityRadar;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerJoinEvent;

public class ServerJoinListener {

  private final CommunityRadar addon;

  public ServerJoinListener(CommunityRadar addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerJoin (ServerJoinEvent event) {
    addon.setOnGriefergames(event.serverData().address().getHost().toLowerCase().contains("griefergames"));
  }
}
