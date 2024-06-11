package de.bypander.communityradar.commands.radar.check;

import de.bypander.communityradar.CommunityRadar;
import de.bypander.communityradar.ListManager.ListManger;
import de.bypander.communityradar.ListManager.Player;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.TextComponent;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.util.I18n;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadarCheckSubCommand extends SubCommand {

  private final Pattern nameTagRegex = Pattern.compile("([A-Za-z\\-]+\\+?) \\u2503 (\\!?\\w{1,16})");

  public RadarCheckSubCommand() {
    super("check");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    CommunityRadar addon = CommunityRadar.get();
    if (!addon.onGriefergames())
      return false;

    StringBuilder sb = new StringBuilder();
    if (arguments.length < 1) {
      Laby.references().commandService().fireCommand("radar", new String[]{"help"});
      return true;
    }
    ListManger manager = ListManger.get();
    if (arguments[0].equals("*")) {
      sb.append("§a").append(I18n.translate("communityradar.command.check.everyone"));
      for (NetworkPlayerInfo playerInfo : Laby.labyAPI().minecraft().getClientPacketListener().getNetworkPlayerInfos()) {
        if(playerInfo == null) continue;

        Matcher matcher = nameTagRegex.matcher(componentToPlainText(playerInfo.displayName()));
        if (!matcher.find()) {
          continue;
        }

        String name = matcher.group(2).trim();
        if (!manager.inList(name))
          continue;

        String pr = addon.prefix(manager.getPrefix(name));
        sb.append("\n§c").append(name).append("§7:§e ").append(pr);
      }

      if (sb.toString().endsWith(I18n.translate("communityradar.command.check.everyone")))
        sb.append(I18n.translate("communityradar.command.check.nopersoninlist"));
    } else if (ListManger.get().inList(arguments[0])) {
      String p = addon.prefix(manager.getPrefix(arguments[0]));

      Player player = manager.getPlayer(arguments[0]);
      assert player != null;
      sb.append("§a").append(I18n.translate("communityradar.command.check.singleplayer.found"))
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.prefix")).append(" §7").append(p)
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.name")).append(" §7").append(player.name())
        .append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.notice")).append(" §7").append(player.cause());
      LocalDateTime date = player.entryCreatedAt();
      String d = date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();
      sb.append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.dates")).append(" §7").append(d);
      if (player.expiryDays() != -1)
        sb.append("\n§c").append(I18n.translate("communityradar.command.check.singleplayer.expires").replace("{days}", "§7" + player.expiryDays() + "§c"));
    } else {
      sb.append(I18n.translate("communityradar.command.check.failed"));
    }
    TextComponent messagePrefix = addon.getAddonPrefix();
    this.displayMessage(messagePrefix.append(Component.text(sb.toString().replaceAll("&([0-9a-fA-FlmokrnNMOKR])", "§$1"))));
    return true;
  }

  private String componentToPlainText(Component component) {
    StringBuilder builder = new StringBuilder();
    Laby.references().componentRenderer().getColorStrippingFlattener().flatten(component, builder::append);
    return builder.toString();
  }
}
