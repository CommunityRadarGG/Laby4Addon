package de.bypander.communityradar.commands.radar.help;

import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;

public class RadarHelpSubCommand extends SubCommand {

  public RadarHelpSubCommand() {
    super("help");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    StringBuilder sb = new StringBuilder();
    sb.append("§8§l§n--------- §cRadar-Hilfe §8§l§n---------§r\n");
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
    sb.append("\n§8§l§n--------- §cRadar-Hilfe §8§l§n---------§r");

    this.displayMessage(Component.text(sb.toString()));
    return true;
  }
}
