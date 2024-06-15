package de.bypander.communityradar.commands.radar.list;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.util.I18n;

public class RadarListAddSubCommand extends SubCommand {

  protected RadarListAddSubCommand() {
    super("add");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (!CommunityRadar.get().onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();
    if (arguments.length < 2) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    Boolean success = ListManger.get().addPrivateList(arguments[0], Component.text(arguments[1].replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1")));
    if (success) {
      sb.append(I18n.translate("communityradar.command.list.add.success"));
      this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.list.add.failed"));

    this.displayMessage(CommunityRadar.get().getAddonPrefix().append(Component.text(sb.toString())));
    return true;
  }
}
