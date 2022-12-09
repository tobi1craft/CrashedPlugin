package de.tobi1craft.crashed.commands.ban;

import de.tobi1craft.crashed.CrashedBungee;
import de.tobi1craft.crashed.util.BanMySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ban extends Command implements TabExecutor {
    private final CrashedBungee plugin = CrashedBungee.getPlugin();
    private final BanMySQL ban = plugin.getBan();

    public ban() {
        super("ban", "crashed.ban", "crashedban", "cban", "gban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent info = new TextComponent(ChatColor.RED + "Error\n" + ChatColor.GREEN + "Wörter\n" + ChatColor.GREEN + "Wörter\n" + ChatColor.GREEN + "Wörter\n");
        if (args.length == 0) {
            sender.sendMessage(new TextComponent(info));
            return;
        }
        ProxiedPlayer toBan = ProxyServer.getInstance().getPlayer(args[0]);
        if (toBan == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Spieler nicht gefunden"));
            return;
        }
        String reason = "";
        boolean forever = true;
        String datetime = "";
        switch (args.length) {
            case 1:
                break;
            case 2:
                reason = args[1];
                break;
            case 3:
                reason = args[1];
                forever = false;
                datetime = args[2];
                break;
            default:
                sender.sendMessage(info);
                break;
        }


        UUID toBanUUID = toBan.getUniqueId();
        if (forever) ban.ban(toBanUUID, reason);
        else ban.ban(toBanUUID, reason, ban.translateNowToDatetime(datetime));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length > 3) {
                return Collections.emptyList();
            }
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    list.add(players.getName());
                }
                return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
            }
            if (args.length == 2) {
                List<String> list = new ArrayList<>();
                list.add("reason");
                list.add("otherReason");
                /*
                TODO: presets in und aus config
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    list.add(players.getName());
                }
                 */
                return list.stream().filter(b -> b.startsWith(args[1])).collect(Collectors.toList());
            }
            if (args.length == 3) {
                List<String> list = new ArrayList<>();
                list.add("YMDhms");
                return list.stream().filter(c -> c.startsWith(args[2])).collect(Collectors.toList());
            }
        }
        return null;
    }
}
