package de.tobi1craft.crashed.game;

import de.tobi1craft.crashed.CrashedMinigames;
import de.tobi1craft.crashed.games.onekitpvp.Start;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class GameManager {

    CrashedMinigames plugin = CrashedMinigames.getPlugin();
    FileConfiguration config = plugin.getConfig();
    FileConfiguration settings = plugin.getSettings();

    public void start(int game, int map) {
        World world = Bukkit.getWorld(translateMap(map, 1));
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(new Location(world, 0.5, 65, 0.5));
        }
        PluginManager pM = Bukkit.getPluginManager();
        pM.registerEvents(new GameListeners(), plugin);
        new Start();
    }

    /*
    private String translateGame(int game) {
        String value = null;
        switch (game) {
            case 1: value =
        }

        return value;
    }
    */

    private String translateMap(int map, int game) {
        String value = null;
        switch (game) {
            case 1:
                switch (map) {
                    case 1:
                        value = "okpvp1-0x0";
                    case 2:
                }
            case 2:
                switch (map) {
                    case 1:
                    case 2:
                }
        }
        return value;
    }

}
