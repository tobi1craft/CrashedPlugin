package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedMinigames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class Highest implements CommandExecutor {

    CrashedMinigames plugin = CrashedMinigames.getPlugin();
    FileConfiguration config = plugin.getConfig();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (args.length == 0) {
            int x = 1;
            int y = 1;
            for (int i = 0; i <= 1000; i++) {
                if (config.getInt("highest.votemap" + x) < config.getInt("highest.votemap" + y)) {
                    y = 1;
                    x++;
                } else if (y < 20) {
                    y++;
                } else {
                    Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Map wurde ermittelt");
                    config.set("highest.mapzahl", x);
                    break;
                }
            }

        } else {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("votegame")) {
                    int x = 1;
                    int y = 1;
                    for (int i = 0; i <= 1000; i++) {
                        if (config.getInt("highest.votegame" + x) < config.getInt("highest.votegame" + y)) {
                            y = 1;
                            x++;
                        } else if (y < 20) {
                            y++;
                        } else {
                            Bukkit.broadcastMessage("§aSpiel wurde ermittelt");
                            config.set("highest.gamezahl", x);
                            break;
                        }
                    }
                } else if (args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage("/highest - Auswertung der Map");
                    sender.sendMessage("/highest votegame - Auswertung des Spiels");
                    sender.sendMessage("/highest [Zahl] - Für Map abstimmen");
                    sender.sendMessage("/highest game [Spiel] - Spiel auswählen");
                    sender.sendMessage("/highest votegame [Zahl] - Für Spiel abstimmen");
                    sender.sendMessage("/highest gamevote [true/false] - Soll das Spiel abgestimmt werden?");
                } else {
                    String arg = args[0];
                    if (!config.contains("highest.votemap" + arg)) {
                        config.set("highest.votemap" + arg, 0);
                    }
                    CrashedMinigames.getPlugin().saveConfig();
                    int temp = config.getInt("highest.votemap" + arg);
                    temp++;
                    config.set("highest.votemap" + arg, temp);
                    CrashedMinigames.getPlugin().saveConfig();
                    sender.sendMessage("Du hast für eine Map abgestimmt");
                }
            } else {
                if (args.length == 2) {

                    if (args[0].equalsIgnoreCase("game")) {
                        String arg = args[1];
                        config.set("highest.game", arg);
                        CrashedMinigames.getPlugin().saveConfig();
                        sender.sendMessage("Du hast das Spiel ausgewählt");

                    } else {
                        if (args[0].equalsIgnoreCase("votegame")) {
                            String arg = args[1];
                            if (!config.contains("highest.votegame" + arg)) {
                                config.set("highest.votegame" + arg, 0);
                            }
                            CrashedMinigames.getPlugin().saveConfig();
                            int temp = config.getInt("highest.votegame" + arg);
                            temp++;
                            config.set("highest.votegame" + arg, temp);
                            CrashedMinigames.getPlugin().saveConfig();
                            sender.sendMessage("Du hast für ein Spiel abgestimmt");

                        } else {
                            if (args[0].equalsIgnoreCase("gamevote")) {
                                String arg = args[1];
                                if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
                                    config.set("highest.gamevote", arg);
                                    CrashedMinigames.getPlugin().saveConfig();
                                    sender.sendMessage("Es wird über das Spiel abgestimmt: " + arg);
                                } else {
                                    sender.sendMessage("/highest gamevote [true/false]");
                                }
                            } else {
                                sender.sendMessage("§a/highest help");
                            }

                        }
                    }
                } else {
                    sender.sendMessage("§a/highest help");
                }
            }
        }
        return false;
    }
}