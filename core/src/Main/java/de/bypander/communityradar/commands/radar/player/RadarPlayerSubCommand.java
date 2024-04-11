package de.bypander.communityradar.commands.radar.player;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;

public class RadarPlayerSubCommand extends SubCommand {

  public RadarPlayerSubCommand() {
    super("player");
    this.withSubCommand(new RadarPlayerAddSubCommand());
    this.withSubCommand(new RadarPlayerRemoveSubCommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (arguments.length == 0) {
      Laby.references().commandService().fireCommand("radar", new String[]{"help"});
      return true;
    }

    Boolean validSubPrefix = false;
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
