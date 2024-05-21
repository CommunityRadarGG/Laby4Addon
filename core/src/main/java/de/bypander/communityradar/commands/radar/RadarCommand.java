package de.bypander.communityradar.commands.radar;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.commands.radar.check.RadarCheckSubCommand;
import de.bypander.communityradar.commands.radar.help.RadarHelpSubCommand;
import de.bypander.communityradar.commands.radar.list.RadarListSubCommand;
import de.bypander.communityradar.commands.radar.listen.RadarListenSubCommand;
import de.bypander.communityradar.commands.radar.player.RadarPlayerSubCommand;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.network.server.ServerData;

public class RadarCommand extends Command {


  public RadarCommand() {
    super("radar", "communityradar", "scammer", "trustedmm", "mm");
    this.withSubCommand(new RadarHelpSubCommand());
    this.withSubCommand(new RadarListenSubCommand());
    this.withSubCommand(new RadarListSubCommand());
    this.withSubCommand(new RadarCheckSubCommand());
    this.withSubCommand(new RadarPlayerSubCommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {

    if (!CommunityRadar.get().onGriefergames())
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
