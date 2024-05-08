package de.bypander.communityradar.commands.radar.player;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListItem;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.util.I18n;

public class RadarPlayerAddSubCommand extends SubCommand {

  protected RadarPlayerAddSubCommand() {
    super("add");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return false;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return false;

    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    if (arguments.length < 2) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    if (ListManger.get().inList(arguments[1])) {
      sb.append("§c").append(I18n.translate("communityradar.command.player.add.inlist"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    StringBuilder notice = new StringBuilder();
    if (arguments.length > 2) {
      for (int i = 2; i < arguments.length; i++)
        notice.append(arguments[i]).append(" ");
    }

    if (ListManger.get().addPlayer(arguments[0], arguments[1], notice.toString())) {
      sb.append(I18n.translate("communityradar.command.player.add.success"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.player.add.failed"));
    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
