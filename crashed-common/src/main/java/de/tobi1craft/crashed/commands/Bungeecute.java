package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedCommon;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class Bungeecute implements CommandExecutor {
    private final CrashedCommon plugin = CrashedCommon.getPlugin();

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            return false;
        }
        StringBuilder cmd = new StringBuilder();
        Player player;

        if (args[0].equalsIgnoreCase("anti")) {
            player = (Player) Bukkit.getOfflinePlayerIfCached(args[1]);
            if (player == null) {
                Bukkit.getLogger().log(Level.SEVERE, "###################################################################################");
                Bukkit.getLogger().log(Level.WARNING, "Fehler: " + args[1] + " wurde nicht gefunden!!!");
                Bukkit.getLogger().log(Level.SEVERE, "###################################################################################");
                return false;
            }
            for (int i = 2; i < args.length; i++) {
                cmd.append(args[i]).append(" ");
            }

        } else {
            if (!(sender instanceof Player)) {
                return false;
            }
            for (String arg : args) {
                cmd.append(arg).append(" ");
            }
            player = (Player) sender;

        }

        plugin.commandOnBungee("commandbungee", cmd.toString(), player);
        return false;
    }
}
