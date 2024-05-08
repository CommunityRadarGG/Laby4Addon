package de.bypander.communityradar.commands.radar.help;

import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.network.server.ServerData;

public class RadarHelpSubCommand extends SubCommand {

  public RadarHelpSubCommand() {
    super("help");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    ServerData data = Laby.references().serverController().getCurrentServerData();
    if (data == null)
      return false;
    if (!data.address().getHost().toLowerCase().contains("griefergames"))
      return false;

    StringBuilder sb = new StringBuilder();
    /**
     * Dear Labymod-Team,
     * I didn't use internationalization because the addon only works on GrieferGames, which is a
     * fully german server where everyone speaks german. Therefore, I didn't find it necessary to
     * translate the output into English.
     */
    sb.append("§8§l§m--------- §8[ §cRadar-Hilfe §8] §8§l§m---------§r\n");
    sb.append(
      "§7Custom listen:\n" +
        "§c/radar listen §7--> §6Zeigt einem alle Listen an.\n" +
        "§c/radar list add §e<Name> <Prefix> §7--> §6Erstellt eine Liste.\n" +
        "§c/radar list prefix §e<Name> <Prefix> §7--> §6Ändert den Prefix einer Liste.\n" +
        "§c/radar list delete §e<Name> §7--> §6Löscht eine Liste.\n" +
        "§c/radar list show §e<Liste> §7--> §6Zeigt einem alle Spieler einer Liste.\n" +
        "\n" +
        "§7Checken:\n" +
        "§c/radar check §e<Name> §7--> §6Checkt einen Spieler, ob er auf irgendeiner Liste ist.\n" +
        "§c/radar check * §7--> §6Checkt den ganzen CB und gibt die Spieler aus, auf welcher Liste diese sind.\n" +
        "\n" +
        "§7Spieler und Listen:\n" +
        "§c/radar player add §e<Liste> <Name> <Notizen...> §7--> §6Fügt einen Spieler auf eine Liste hinzu.\n" +
        "§c/radar player remove §e<Name> §7--> §6entfernt einen Spieler von einer Liste.");
    sb.append("\n§8§l§m--------- §8[ §cRadar-Hilfe §8] §8§l§m---------§r");

    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
