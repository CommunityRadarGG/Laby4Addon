package de.bypander.communityradar.commands.radar.list;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListItem;
import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.ListManager.Player;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;

public class RadarListShowSubCommand extends SubCommand {

  protected RadarListShowSubCommand() {
    super("show");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CommunityRadar addon = CommunityRadar.get();
    if (!addon.onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();
    if (arguments.length < 1) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    ListItem list = ListManger.get().getListItem(arguments[0]);
    if (list != null) {
      String p = list.getPrefix().getText();

      sb.append(I18n.translate("communityradar.command.list.show.success")
        .replace("{list}", arguments[0])
        .replace("{prefix}", p))
        .append(" ");

      StringBuilder names = new StringBuilder();
      for (Player player : list.getPlayerMap().values()) {
        names.append(I18n.translate("communityradar.command.list.show.name").replace("{name}", player.name()));
      }
      if (names.length() >= 4)
        names.replace(names.length() - 4, names.length(),  "");
      sb.append(names);

      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.list.show.failed"));
    this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
    return true;
  }
}
