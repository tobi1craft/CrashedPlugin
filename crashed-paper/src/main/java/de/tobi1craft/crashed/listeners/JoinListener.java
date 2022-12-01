package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedMinigames;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    FileConfiguration config = CrashedMinigames.getPlugin().getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (config.getBoolean("function.cpGameOnStart")) {
            if (!config.getBoolean("highest.gamevote")) {
                int pcount = 0;
                for (@SuppressWarnings("unused") Player p : Bukkit.getOnlinePlayers()) {
                    pcount++;
                }
                if (pcount == 1) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cp GameSelector " + event.getPlayer().getDisplayName());
                }
            } else {
                if (config.getBoolean("highest.gamevote")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cp GameSelectorVote " + event.getPlayer().getDisplayName());
                }
            }
        }
    }

}
