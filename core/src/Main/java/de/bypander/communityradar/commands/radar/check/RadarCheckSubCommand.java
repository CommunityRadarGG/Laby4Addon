package de.bypander.communityradar.commands.radar.check;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.ListManager.Player;
import net.labymod.api.Laby;
import net.labymod.api.LabyAPI;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.labynet.LabyNetController;
import net.labymod.api.util.I18n;

public class RadarCheckSubCommand extends SubCommand {

  public RadarCheckSubCommand() {
    super("check");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    StringBuilder sb = new StringBuilder("§8[§cCommunityRadar§8]§r ");
    if (arguments.length < 1) {
      Laby.references().commandService().fireCommand("radar", new String[]{"help"});
      return true;
    }

    if (arguments[0].equals("*")) {
      sb.append("§a").append(I18n.translate("communityradar.command.check.everyone"));
      for (net.labymod.api.client.entity.player.Player p : Laby.references().clientWorld().getPlayers()) {
        String pr = ListManger.get().getPrefix(p.getName());
        if (pr.equals("§scammer"))
          pr = CommunityRadar.get().configuration().getScammerSubConfig().getPrefix().get();

        if (p.equals("§trusted"))
          pr = CommunityRadar.get().configuration().getTrustedMMSubConfig().getPrefix().get();

        if (pr.isEmpty())
          continue;
        sb.append("\n§c").append(p.getName()).append("§7:§e ").append(pr);
      }
      if (sb.toString().endsWith(I18n.translate("communityradar.command.check.everyone")))
        sb.append(I18n.translate("communityradar.command.check.nopersoninlist"));
    } else if (ListManger.get().inList(arguments[0])) {
      String p = ListManger.get().getPrefix(arguments[0]);
      if (p.equals("§scammer"))
        p = CommunityRadar.get().configuration().getScammerSubConfig().getPrefix().get();

      if (p.equals("§trusted"))
        p = CommunityRadar.get().configuration().getTrustedMMSubConfig().getPrefix().get();

      Player player = ListManger.get().getPlayer(arguments[0]);

      assert player != null;
      sb.append("§a").append(I18n.translate("communityradar.command.check.singleplayer.found"))
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.prefix")).append(" §7").append(p)
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.name")).append(" §7").append(player.name())
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.notice")).append(" §7").append(player.notice())
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.dates")).append(" §7").append(player.date().toString());
    } else {
      sb.append(I18n.translate("communityradar.command.check.failed"));
    }

    this.displayMessage(Component.text(sb.toString().replaceAll("&([0-9a-fA-FlmokrMOKR])", "§$1")));
    return true;
  }
}
