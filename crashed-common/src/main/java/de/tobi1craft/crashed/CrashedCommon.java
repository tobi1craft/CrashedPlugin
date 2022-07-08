package de.tobi1craft.crashed;

import de.tobi1craft.crashed.commands.Bungeecute;
import de.tobi1craft.crashed.commands.Jumpandrun;
import de.tobi1craft.crashed.commands.Nick;
import de.tobi1craft.crashed.commands.Updates;
import de.tobi1craft.crashed.listeners.BlockFadeListener;
import de.tobi1craft.crashed.listeners.JoinListener;
import de.tobi1craft.crashed.listeners.MovementListener;
import de.tobi1craft.crashed.listeners.shutdown.DamageListener;
import de.tobi1craft.crashed.mysql.MySQL;
import de.tobi1craft.crashed.util.PlaceholderAPI;
import de.tobi1craft.crashed.util.SetVersionMySQL;
import de.tobi1craft.crashed.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

public final class CrashedCommon extends JavaPlugin implements PluginMessageListener {

    private static CrashedCommon plugin;
    private static FileConfiguration config;
    private static PluginManager PM;
    private static MySQL mySQL;
    private static Nick nick;
    private static SetVersionMySQL versionMySQL;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.saveDefaultConfig();
        init();
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        PM = Bukkit.getPluginManager();

        if (config.getBoolean("function.bungeecute")) {
            Objects.requireNonNull(getCommand("bungeecute")).setExecutor(new Bungeecute());
        }
        if (config.getBoolean("function.jumpAndRun")) {
            PM.registerEvents(new MovementListener(), this);
            Objects.requireNonNull(getCommand("jumpandrun")).setExecutor(new Jumpandrun());
        }
        if (config.getBoolean("function.nick")) {
            nick = new Nick();
            try {
                PreparedStatement ps = mySQL.getCon().prepareStatement("CREATE TABLE IF NOT EXISTS `crashed_nick` (player VARCHAR(36),nick TINYTEXT,PRIMARY KEY (player),UNIQUE (player))");
                ps.execute();
            } catch (SQLException e) {
                getLogger().log(Level.WARNING, "Fehler beim Verbinden mit MySQL!");
                getLogger().log(Level.WARNING, "--------------------------------");
                e.printStackTrace();
                getLogger().log(Level.WARNING, "--------------------------------");
            }
        }
        if (config.getBoolean("function.returnVersion")) {
            versionMySQL = new SetVersionMySQL();
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> versionMySQL.setVersion());
        }
        if (config.getBoolean("function.updateChecker")) {
            PM.registerEvents(new UpdateChecker(), this);
            Objects.requireNonNull(getCommand("updates")).setExecutor(new Updates());
        }
        if (config.getBoolean("function.customJoinMessage")) PM.registerEvents(new JoinListener(), this);
        if (config.getBoolean("function.noBlockFade")) PM.registerEvents(new BlockFadeListener(), this);

        new PlaceholderAPI().register();

        getLogger().info("Crashed Plugin crashed successfully!");

    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord", this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
        mySQL.disconnect();
    }

    public static CrashedCommon getPlugin() {
        return plugin;
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public Nick getNick() {
        return nick;
    }

    private void init() {
        config = plugin.getConfig();
        mySQL = new MySQL(config.getString("MySQL.host"), config.getInt("MySQL.port"), config.getString("MySQL.database"), config.getString("MySQL.user"), config.getString("MySQL.password"));
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        try {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(message));
            String subChannel = stream.readUTF();
            if (subChannel.equals("command")) {
                String input = stream.readUTF();
                Bukkit.dispatchCommand(getServer().getConsoleSender(), input);
            }
            if (subChannel.equals("tpplayer")) {
                String objet = stream.readUTF();
                String dest = stream.readUTF();
                Player p = Bukkit.getPlayer(objet);
                Player destp = Bukkit.getPlayer(dest);
                assert p != null;
                assert destp != null;
                p.teleport(destp);
            }
            if (subChannel.equals("tpcoords")) {
                String objet = stream.readUTF();
                String x = stream.readUTF();
                String y = stream.readUTF();
                String z = stream.readUTF();
                Location loc = new Location(Objects.requireNonNull(Bukkit.getPlayer(objet)).getWorld(), Long.parseLong(x), Long.parseLong(y), Long.parseLong(z));
                Objects.requireNonNull(Bukkit.getPlayer(objet)).teleport(loc);
            }
            if (subChannel.equals("tpcoordsyp")) {
                String objet = stream.readUTF();
                String x = stream.readUTF();
                String y = stream.readUTF();
                String z = stream.readUTF();
                String yaw = stream.readUTF();
                String pitch = stream.readUTF();
                Location loc = new Location(Objects.requireNonNull(Bukkit.getPlayer(objet)).getWorld(), Long.parseLong(x), Long.parseLong(y), Long.parseLong(z), Float.parseFloat(yaw), Float.parseFloat(pitch));
                Objects.requireNonNull(Bukkit.getPlayer(objet)).teleport(loc);
            }
            if (subChannel.equals("restart")) {
                switch (stream.readUTF()) {
                    case "restart":
                        Bukkit.spigot().restart();
                        break;
                    case "pvp":
                        PM.registerEvents(new DamageListener(), plugin);
                        break;
                }
            }
            if (subChannel.equals("getversion")) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> versionMySQL.setVersion());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commandOnBungee(String channel, String cmd, Player player) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(this, "BungeeCord", stream.toByteArray());

        player.sendMessage(ChatColor.GREEN + "Dein Befehl " + ChatColor.GOLD + cmd + ChatColor.GREEN + "wurde erfolreich an den Bungee gesendet");

    }
}
