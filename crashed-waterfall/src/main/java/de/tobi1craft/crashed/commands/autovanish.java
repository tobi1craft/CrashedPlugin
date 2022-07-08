package de.tobi1craft.crashed.commands;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class autovanish extends Command implements TabExecutor {


    public autovanish() {
        super("autovanish", "crashed.av", "av");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        LuckPerms lp = LuckPermsProvider.get();


        if (sender instanceof ProxiedPlayer) {
            if (args.length > 2) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Bitte Benutze /av <on/off> <Spieler>"));
                return;
            }
            if (args.length == 0) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Bitte Benutze /av <on/off> <Spieler>"));
                return;
            }

            ProxiedPlayer player = null;
            boolean sistp = false;
            if (args.length == 1) {
                player = (ProxiedPlayer) sender;
                sistp = true;
            }
            if (args.length == 2) {
                player = ProxyServer.getInstance().getPlayer(args[1]);
            }
            if (player == null) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Bitte Benutze /av <on/off> <Spieler>"));
                return;
            }

            User user = lp.getUserManager().getUser(player.getUniqueId());

            if (args[0].equalsIgnoreCase("off")) {
                assert user != null;
                user.data().add(Node.builder("vanish.joinvanished").value(false).build());
                if (!sistp) {
                    sender.sendMessage(new TextComponent(ChatColor.GOLD + "" + player + ChatColor.GREEN + " wird jetzt nicht mehr automatisch unsichtbar"));
                }
                if (!sistp) {
                    player.sendMessage(new TextComponent(ChatColor.GOLD + "Du " + ChatColor.GREEN + "wirst jetzt nicht mehr automatisch unsichtbar"));
                }
                if (sistp) {
                    sender.sendMessage(new TextComponent(ChatColor.GOLD + "Du " + ChatColor.GREEN + "wirst jetzt nicht mehr automatisch unsichtbar"));
                }
            }
            if (args[0].equalsIgnoreCase("on")) {
                assert user != null;
                user.data().remove((Node.builder("vanish.joinvanished")).build());
                if (!sistp) {
                    sender.sendMessage(new TextComponent(ChatColor.GOLD + "" + player + ChatColor.GREEN + " wird jetzt automatisch unsichtbar"));
                }
                if (!sistp) {
                    player.sendMessage(new TextComponent(ChatColor.GOLD + "Du " + ChatColor.GREEN + "wirst jetzt automatisch unsichtbar"));
                }
                if (sistp) {
                    player.sendMessage(new TextComponent(ChatColor.GOLD + "Du " + ChatColor.GREEN + "wirst jetzt automatisch unsichtbar"));
                }
            }

            assert user != null;
            lp.getUserManager().saveUser(user);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 2) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("on");
            list.add("off");
            return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
        }

        if (args.length == 2) {
            List<String> list = new ArrayList<>();
            for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                list.add(p.getName());
            }
            return list.stream().filter(b -> b.startsWith(args[1])).collect(Collectors.toList());
        }

        return null;

    }
}
