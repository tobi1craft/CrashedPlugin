package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedPaper;
import de.tobi1craft.crashed.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldCMD implements CommandExecutor, TabExecutor {

    CrashedPaper plugin = CrashedPaper.getPlugin();
    GameManager gameManager = plugin.getGameManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2) {
            gameManager.start(1, 1);
        }
        if (sender instanceof Player && args.length == 1) {
            Player p = (Player) sender;
            Bukkit.dispatchCommand(p, "mvtp " + args[0]);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        for (World w : Bukkit.getWorlds()) {
            list.add(w.getName());
        }
        return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
    }
}
