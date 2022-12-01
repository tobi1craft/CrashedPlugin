package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedMinigames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {

    CrashedMinigames pl = CrashedMinigames.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.teleport(pl.getSpawnLocation());
        }
        return false;
    }
}
