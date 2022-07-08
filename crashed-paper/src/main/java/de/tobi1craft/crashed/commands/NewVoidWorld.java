package de.tobi1craft.crashed.commands;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import org.bukkit.*;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NewVoidWorld implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 3) {
            MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
            assert core != null;
            MVWorldManager wM = core.getMVWorldManager();
            String name = args[0];
            String teams = args[1];
            String spielerproteam = args[2];
            String a = name;
            if (a.equalsIgnoreCase("bedwars") || a.equalsIgnoreCase("skywars") || a.equalsIgnoreCase("hide") || a.equalsIgnoreCase("shoot") || a.equalsIgnoreCase("okpvp")) {
                for (int i = 1; i <= 1000; ) {
                    if (core.getMVWorldManager().getMVWorld(a + i + "-" + teams + "x" + spielerproteam, true) != null) {
                        i++;
                    } else {
                        name = name + i;
                        name = name + "-";
                        name = name + teams;
                        name = name + "x";
                        name = name + spielerproteam;

                        wM.addWorld(name, World.Environment.NORMAL, null, WorldType.NORMAL, false, "VoidGen");
                        MultiverseWorld w = core.getMVWorldManager().getMVWorld(name);
                        w.setGameMode(GameMode.ADVENTURE);
                        w.setEnableWeather(false);
                        w.setAllowMonsterSpawn(false);
                        w.setHunger(false);
                        w.setBedRespawn(false);
                        w.setDifficulty(Difficulty.NORMAL);

                        sender.sendMessage(ChatColor.GREEN + "Neue Welt " + ChatColor.DARK_GREEN + name + ChatColor.GREEN + " wurde erstellt");
                        Bukkit.getLogger().warning("Neue Welt " + name + " wurde erstellt.");

                        if (sender instanceof Player) {
                            Player p = (Player) sender;
                            p.teleport(new Location(Bukkit.getWorld(name), 0, 65, 0, 0, 60));
                        }
                        break;
                    }
                }
            }
        } else sender.sendMessage(ChatColor.GREEN + "Benutze /newvoidworld [Spiel] [Teams] [Spieler pro Team]");
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length > 3) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("bedwars");
            list.add("skywars");
            list.add("hide");
            list.add("shoot");
            list.add("okpvp");
            Collections.sort(list);
            return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
        }
        if (args.length == 2) {
            List<String> list = new ArrayList<>();
            list.add("Teams");
            return new ArrayList<>(list);
        }
        if (args.length == 3) {
            List<String> list = new ArrayList<>();
            list.add("Spieler pro Team");
            return new ArrayList<>(list);
        }
        return null;
    }
}
