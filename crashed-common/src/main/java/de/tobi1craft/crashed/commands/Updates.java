package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedCommon;
import de.tobi1craft.crashed.util.UpdateChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Updates implements CommandExecutor {

    CrashedCommon pl = CrashedCommon.getPlugin();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        UpdateChecker.getUpdateChecker().getUpdates(sender);
        return false;
    }
}
