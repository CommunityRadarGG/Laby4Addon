package de.bypander.communityradar.commands.radar.list;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListItem;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;

public class RadarListShowSubCommand extends SubCommand {

  protected RadarListShowSubCommand() {
    super("show");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    if (arguments.length < 1) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    ListItem list = ListManger.get().getListItem(arguments[0]);
    if (list != null) {
      String p = list.getPrefix();
      if (p.equals("§scammer"))
        p = CommunityRadar.get().configuration().getScammerSubConfig().getPrefix().get();
      if (p.equals("§trusted"))
        p = CommunityRadar.get().configuration().getTrustedMMSubConfig().getPrefix().get();

      sb.append(I18n.translate("communityradar.command.list.show.success").replace("{list}", arguments[0]).replace("{prefix}", p)).append(" ");
      sb.append("§7").append(list.getPlayerMap().keySet());
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.list.show.failed"));
    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
