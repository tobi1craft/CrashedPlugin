package de.tobi1craft.crashed;

import de.tobi1craft.crashed.commands.Highest;
import de.tobi1craft.crashed.commands.NewVoidWorld;
import de.tobi1craft.crashed.commands.Spawn;
import de.tobi1craft.crashed.commands.WorldCMD;
import de.tobi1craft.crashed.game.GameManager;
import de.tobi1craft.crashed.games.onekitpvp.CommandSettings;
import de.tobi1craft.crashed.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CrashedPaper extends JavaPlugin {

    private static CrashedPaper plugin;
    private static FileConfiguration config;
    private static File settingsFile;
    private static FileConfiguration settings;
    private static GameManager gameManager;
    private static Location spawnLocation;

    public static CrashedPaper getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();
        spawnLocation = new Location(Bukkit.getWorld("world"), 0, 4, 0, -180, 0);
        plugin.saveDefaultConfig();
        initFiles();
        PluginManager pluginManager = Bukkit.getPluginManager();

        if (config.getBoolean("function.highest"))
            Objects.requireNonNull(getCommand("highest")).setExecutor(new Highest());

        if (config.getBoolean("function.newVoidWorld"))
            Objects.requireNonNull(getCommand("newvoidworld")).setExecutor(new NewVoidWorld());

        if (config.getBoolean("function.spawnCommand"))
            Objects.requireNonNull(getCommand("spawn")).setExecutor(new Spawn());

        if (config.getBoolean("function.worldCommand"))
            Objects.requireNonNull(getCommand("world")).setExecutor(new WorldCMD());

        if (config.getBoolean("function.cpGameOnStart"))
            pluginManager.registerEvents(new JoinListener(), this);

        getLogger().info("Crashed Plugin crashed successfully!");

        new CommandSettings();
    }

    @Override
    public void onDisable() {
    }

    private void initFiles() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        config = plugin.getConfig();
        settingsFile = new File(CrashedPaper.getPlugin().getDataFolder().getPath(), "onekitpvp.yml");
        if (!settingsFile.exists()) {
            try {
                settingsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        settings = YamlConfiguration.loadConfiguration(settingsFile);

    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public void saveSettings() {
        try {
            settings.save(settingsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}