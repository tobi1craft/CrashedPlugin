package de.tobi1craft.crashed.commands.ban;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ban extends Command implements TabExecutor {
    public ban() {
        super("ban", "crashed.ban", "crashedban", "cban", "gban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

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
                list.add("other example");
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
                list.add("YDMhms");
                return list.stream().filter(c -> c.startsWith(args[2])).collect(Collectors.toList());
            }
        }
        return null;
    }
}
