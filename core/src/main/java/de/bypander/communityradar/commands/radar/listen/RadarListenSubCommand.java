package de.bypander.communityradar.commands.radar.listen;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.util.I18n;

public class RadarListenSubCommand extends SubCommand {

  public RadarListenSubCommand() {
    super("listen");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!CommunityRadar.get().onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();
    sb.append(I18n.translate("communityradar.command.listen.prefix"));

    ListManger manager = ListManger.get();
    for (String s : manager.getNamespaces()) {
      sb.append(I18n.translate("communityradar.command.listen.list").replace("{list}", s));
    }
    this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.substring(0, sb.length() - 2))));
    return true;
  }
}
