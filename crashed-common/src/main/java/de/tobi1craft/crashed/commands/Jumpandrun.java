package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedCommon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Jumpandrun implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 1 && args[0].equalsIgnoreCase("reset") && p.getName().equals("tobi1craft")) {
            FileConfiguration config = CrashedCommon.getPlugin().getConfig();
            config.set("jar", null);
            CrashedCommon.getPlugin().saveConfig();
            return true;
        }

        Location loc = new Location(Bukkit.getWorld("world"), -130.5, 3, 12.5, 0, -90);
        p.teleport(loc);
        p.sendActionBar("Du bist jetzt beim Jump And Run");
        return true;
    }
}
