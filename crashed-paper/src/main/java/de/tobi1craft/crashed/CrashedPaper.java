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
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class CrashedPaper extends JavaPlugin implements PluginMessageListener {

    private static CrashedPaper plugin;
    private static FileConfiguration config;
    private static File settingsFile;
    private static FileConfiguration settings;
    private static GameManager gameManager;
    private static Location spawnLocation;

    @Override
    public void onEnable() {
        plugin = this;
        gameManager = new GameManager();
        spawnLocation = new Location(Bukkit.getWorld("world"), 0, 4, 0, -180, 0);
        plugin.saveDefaultConfig();
        initFiles();
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
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
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
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

    public static CrashedPaper getPlugin() {
        return plugin;
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

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        try {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            String subChannel = stream.readUTF();
            if (subChannel.equals("restart")) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}