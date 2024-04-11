package de.bypander.communityradar.commands.radar.list;

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
    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    if (arguments.length < 2) {
      sb.append(I18n.translate("communityradar.command.missingargument"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    Boolean success = ListManger.get().addPrivateList(arguments[0], arguments[1]);
    if (success) {
      sb.append(I18n.translate("communityradar.command.list.add.success"));
      this.displayMessage(Component.text(sb.toString()));
      return true;
    }

    sb.append(I18n.translate("communityradar.command.list.add.failed"));
    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
