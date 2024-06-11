package de.bypander.communityradar.commands.radar.help;

import de.bypander.communityradar.CommunityRadar;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;
import net.labymod.api.util.I18n;

public class RadarHelpSubCommand extends SubCommand {

  public RadarHelpSubCommand() {
    super("help");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!CommunityRadar.get().onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();

    sb.append(I18n.translate("communityradar.command.help.separator") + "\n");
    sb.append(I18n.translate("communityradar.command.help.text"));
    sb.append("\n" + I18n.translate("communityradar.command.help.separator"));

    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
