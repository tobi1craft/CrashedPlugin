package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedCommon;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    FileConfiguration config = CrashedCommon.getPlugin().getConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String configwert = config.getString("customJoinMessage.text");
        assert configwert != null;
        configwert = PlaceholderAPI.setPlaceholders(event.getPlayer(), configwert);

        if (config.getBoolean("function.customJoinMessage")) {
            event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', configwert));
        }
    }

}
