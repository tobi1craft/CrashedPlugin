package de.tobi1craft.crashed.commands.ban;

import de.tobi1craft.crashed.CrashedWaterfall;
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
    public ban() {
        super("ban", "crashed.ban", "crashedban", "cban", "gban");
    }

    private final CrashedWaterfall plugin = CrashedWaterfall.getPlugin();
    private final BanMySQL ban = plugin.getBan();

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0){
            sender.sendMessage(new TextComponent(ChatColor.RED + "?"));
            return;
        }
        ProxiedPlayer toBan = ProxyServer.getInstance().getPlayer(args[0]);
        if(toBan == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Spieler nicht gefunden"));
            return;
        }
        UUID toBanUUID = toBan.getUniqueId();

        ban.ban(toBanUUID, args[1], ban.translateNowToDatetime(args[2]), ban.translateNowToDatetime(""));
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
                list.add("example");
                list.add("otherExample");
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
