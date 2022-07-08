package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class teleport extends Command implements TabExecutor {
    private final CrashedWaterfall plugin = CrashedWaterfall.getPlugin();

    public teleport() {
        super("gtp", "crashed.teleport");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return;
        }
        if (args.length == 0) {
            return;
        }
        ProxiedPlayer object = null;
        ProxiedPlayer destplayer = null;
        if (args.length == 1 || args.length == 2) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(new TextComponent("/gtp <player>"));
                    sender.sendMessage(new TextComponent("/gtp <object> <player>"));
                    sender.sendMessage(new TextComponent("/gtp <object> <x> <y> <z> <yaw> <pitch> <server>"));
                    return;
                }
                object = (ProxiedPlayer) sender;
                destplayer = ProxyServer.getInstance().getPlayer(args[0]);
            }
            if (args.length == 2) {
                if (!(Objects.equals(args[0], "@a") || Objects.equals(args[0], "@s"))) {
                    object = ProxyServer.getInstance().getPlayer(args[0]);
                } else if (args[0].equals("@s")) {
                    object = (ProxiedPlayer) sender;
                    if (Objects.equals(args[1], "@s")) {
                        destplayer = (ProxiedPlayer) sender;
                    } else {
                        destplayer = ProxyServer.getInstance().getPlayer(args[1]);
                    }
                }
            }
            if (object == null || destplayer == null) {
                String a;
                if (object == null) {
                    a = args[0];
                } else a = args[1];
                sender.sendMessage(new TextComponent(ChatColor.DARK_AQUA + a + ChatColor.RED + " ist kein gültiger Spieler"));
                return;
            }
            if (Objects.equals(args[0], "@a")) {
                if (args.length == 2) {
                    for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                        if (Objects.equals(p.getServer().getInfo().getName(), destplayer.getServer().getInfo().getName())) {
                            plugin.teleportOnServer("tpplayer", p.getName(), destplayer.getName(), p.getServer().getInfo().getName());
                        }
                        p.connect(destplayer.getServer().getInfo());
                        ProxiedPlayer finalDestplayer = destplayer;
                        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> plugin.teleportOnServer("tpplayer", p.getName(), finalDestplayer.getName(), p.getServer().getInfo().getName()), 1, TimeUnit.SECONDS);
                    }
                } else {
                    sender.sendMessage(new TextComponent(ChatColor.RED + "Du kannst dich nicht zu allen teleportieren"));
                    return;
                }
            } else {
                if (Objects.equals(object.getServer().getInfo().getName(), destplayer.getServer().getInfo().getName())) {
                    plugin.teleportOnServer("tpplayer", object.getName(), destplayer.getName(), object.getServer().getInfo().getName());
                } else {
                    object.connect(destplayer.getServer().getInfo());
                    ProxiedPlayer finalObject = object;
                    ProxiedPlayer finalDestplayer = destplayer;
                    ProxyServer.getInstance().getScheduler().schedule(plugin, () -> plugin.teleportOnServer("tpplayer", finalObject.getName(), finalDestplayer.getName(), finalObject.getServer().getInfo().getName()), 1, TimeUnit.SECONDS);
                }
            }
        }
        if (args.length == 4 || args.length == 5 || args.length == 6 || args.length == 7) {
            if (Objects.equals(args[0], "@a") || Objects.equals(args[0], "@s")) {
                object = (ProxiedPlayer) sender;
            } else object = ProxyServer.getInstance().getPlayer(args[0]);
            if (object == null) {
                return;
            }
            String x = null;
            String y = null;
            String z = null;
            String yaw = null;
            String pitch = null;
            ServerInfo server = null;
            if (args.length == 4) {
                x = args[1];
                y = args[2];
                z = args[3];
                object = ProxyServer.getInstance().getPlayer(args[0]);
            }
            if (args.length == 5) {
                server = ProxyServer.getInstance().getServerInfo(args[4]);
                if (server == null) {
                    //TODO Nachricht
                    return;
                }
                x = args[1];
                y = args[2];
                z = args[3];
                object = ProxyServer.getInstance().getPlayer(args[0]);
            }
            if (args.length == 6) {
                x = args[1];
                y = args[2];
                z = args[3];
                yaw = args[4];
                pitch = args[5];
                object = ProxyServer.getInstance().getPlayer(args[0]);
            }
            if (args.length == 7) {
                server = ProxyServer.getInstance().getServerInfo(args[6]);
                if (server == null) {
                    //TODO Nachricht
                    return;
                }
                x = args[1];
                y = args[2];
                z = args[3];
                yaw = args[4];
                pitch = args[5];
                object = ProxyServer.getInstance().getPlayer(args[0]);
            }
            if (args.length == 4 || args.length == 5) {
                if (server == null) {
                    plugin.teleportOnServerCoords("tpcoords", object.getName(), x, y, z, object.getServer().getInfo());
                } else plugin.teleportOnServerCoords("tpcoords", object.getName(), x, y, z, server);
            }
            if (args.length == 6 || args.length == 7) {
                if (server == null) {
                    plugin.teleportOnServerCoords("tpcoordsyp", object.getName(), x, y, z, yaw, pitch, object.getServer().getInfo());
                } else plugin.teleportOnServerCoords("tpcoordsyp", object.getName(), x, y, z, yaw, pitch, server);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            if (args.length > 2) {
                return Collections.emptyList();
            }
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                list.add("@s");
                list.add("@a");
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    list.add(players.getName());
                }
                return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
            }
            if (args.length == 2) {
                List<String> list = new ArrayList<>();
                list.add("@s");
                list.add("@a");
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    list.add(players.getName());
                }
                return list.stream().filter(b -> b.startsWith(args[1])).collect(Collectors.toList());
            }
        }
        return null;
    }
}
