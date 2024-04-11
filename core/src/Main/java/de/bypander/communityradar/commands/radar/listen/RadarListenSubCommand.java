package de.bypander.communityradar.commands.radar.listen;

import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;

public class RadarListenSubCommand extends SubCommand {

  public RadarListenSubCommand() {
    super("listen");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    sb.append("§7").append(I18n.translate("communityradar.command.listen")).append(" ");

    ListManger manager = ListManger.get();
    for (String s : manager.getNamespaces()) {
      sb.append(s).append(", ");
    }

    this.displayMessage(Component.text(sb.substring(0, sb.length() - 2)));
    return true;
  }
}
