package de.bypander.communityradar.commands.radar.player;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.util.I18n;

public class RadarPlayerRemoveSubCommand extends SubCommand {

  protected RadarPlayerRemoveSubCommand() {
    super("remove");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!CommunityRadar.get().onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();
    if (arguments.length < 1) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    if (!ListManger.get().inList(arguments[0])) {
      sb.append(I18n.translate("communityradar.command.player.remove.notinlist"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    if (ListManger.get().removePlayer(arguments[0])) {
      sb.append(I18n.translate("communityradar.command.player.remove.success"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.player.remove.failed"));
    this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
    return true;
  }
}
