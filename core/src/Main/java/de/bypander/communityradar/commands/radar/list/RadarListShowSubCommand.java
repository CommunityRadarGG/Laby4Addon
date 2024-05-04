package de.bypander.communityradar.commands.radar.list;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListItem;
import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.ListManager.Player;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.util.I18n;

public class RadarListShowSubCommand extends SubCommand {

  protected RadarListShowSubCommand() {
    super("show");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return false;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return false;

    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    if (arguments.length < 1) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    ListItem list = ListManger.get().getListItem(arguments[0]);
    if (list != null) {
      String p = list.getPrefix();
      p = CommunityRadar.prefix(p);

      sb.append(I18n.translate("communityradar.command.list.show.success")
        .replace("{list}", arguments[0])
        .replace("{prefix}", p.replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1")))
        .append(" ");

      StringBuilder names = new StringBuilder();
      for (Player player : list.getPlayerMap().values()) {
        names.append("§6").append(player.name()).append("§7, ");
      }
      if (names.length() >= 4)
        names.replace(names.length() - 4, names.length(),  "");

      sb.append(names);
        //.append(String.valueOf(list.getPlayerMap().keySet())
        //.replace("[", "[§6")
        //.replace("]", "§7]")
        //.replaceAll(",","§7,§6"));

      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.list.show.failed"));
    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
