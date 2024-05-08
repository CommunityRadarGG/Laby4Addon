package de.bypander.communityradar.commands.radar.player;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.network.server.ServerData;

public class RadarPlayerSubCommand extends SubCommand {

  public RadarPlayerSubCommand() {
    super("player");
    this.withSubCommand(new RadarPlayerAddSubCommand());
    this.withSubCommand(new RadarPlayerRemoveSubCommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return false;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return false;

    if (arguments.length == 0) {
      Laby.references().commandService().fireCommand("radar", new String[]{"help"});
      return true;
    }

    boolean validSubPrefix = false;
    for (SubCommand s : getSubCommands()) {
      if (s.getPrefix().equalsIgnoreCase(arguments[0])) {
        validSubPrefix = true;
        break;
      }
    }
    if (!validSubPrefix)
      Laby.references().commandService().fireCommand("radar", new String[]{"help"});

    return true;
  }
}
