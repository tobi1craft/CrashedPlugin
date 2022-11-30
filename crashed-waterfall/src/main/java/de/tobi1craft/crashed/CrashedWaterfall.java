package de.tobi1craft.crashed;

import de.tobi1craft.crashed.commands.*;
import de.tobi1craft.crashed.commands.ban.ban;
import de.tobi1craft.crashed.commands.serverwechsel.lobby;
import de.tobi1craft.crashed.commands.serverwechsel.minigames;
import de.tobi1craft.crashed.commands.serverwechsel.rpg;
import de.tobi1craft.crashed.listeners.*;
import de.tobi1craft.crashed.mysql.MySQL;
import de.tobi1craft.crashed.party.PartyCommand;
import de.tobi1craft.crashed.party.PartyListeners;
import de.tobi1craft.crashed.util.BanMySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public final class CrashedWaterfall extends Plugin {

    private static CrashedWaterfall plugin;
    private static Configuration config;
    private static File file;
    private static File partiesFile;
    private static File playersFile;
    private static versions versions;
    private static MySQL mySQL;
    private static BanMySQL ban;

    @Override
    public void onEnable() {
        plugin = this;
        init();
        PluginManager PM = getProxy().getPluginManager();

        if (!config.contains("function.friday")) config.set("function.friday", true);
        if (!config.contains("function.autovanish")) config.set("function.autovanish", true);
        if (!config.contains("function.otherexecute")) config.set("function.otherexecute", true);
        if (!config.contains("function.allexecute")) config.set("function.allexecute", true);
        if (!config.contains("function.versionfilter")) config.set("function.versionfilter", true);
        if (!config.contains("function.serverswitch")) config.set("function.serverswitch", true);
        if (!config.contains("function.bungeecute")) config.set("function.bungeecute", true);
        if (!config.contains("function.allmsg")) config.set("function.allmsg", true);
        if (!config.contains("function.teleport")) config.set("function.teleport", true);
        if (!config.contains("function.party")) config.set("function.party", true);
        if (!config.contains("function.motd")) config.set("function.motd", true);
        if (!config.contains("function.restart")) config.set("function.restart", true);
        if (!config.contains("function.players")) config.set("function.players", true);
        if (config.getBoolean("function.motd")) {
            if (!config.contains("motd.text")) {
                config.set("motd.text.1", "&aThis is the standard MOTD");
                config.set("motd.text.2", "&3Crashed Plugin by tobi1craft");
            }
            if (!config.contains("motd.playerinfo"))
                config.set("motd.playerinfo", "&aThis is the standard Playerinfo\n&3Crashed Plugin by tobi1craft");
        }
        if (!config.contains("function.versions")) config.set("function.versions", true);
        if (!config.contains("function.bansystem")) config.set("function.bansystem", true);
        if (!config.contains("date.timeZoneId")) config.set("date.timeZoneId", "Europe/Berlin");

        saveConfig();

        if (config.getBoolean("function.autovanish")) PM.registerCommand(this, new autovanish());
        if (config.getBoolean("function.otherexecute")) PM.registerCommand(this, new otherexecute());
        if (config.getBoolean("function.allexecute")) PM.registerCommand(this, new allexecute());
        if (config.getBoolean("function.serverswitch")) {
            PM.registerCommand(this, new lobby());
            PM.registerCommand(this, new rpg());
            PM.registerCommand(this, new minigames());
        }
        if (config.getBoolean("function.allmsg")) PM.registerCommand(this, new allmsg());
        if (config.getBoolean("function.teleport")) PM.registerCommand(this, new teleport());
        if (config.getBoolean("function.party")) {
            PM.registerCommand(this, new PartyCommand());
            new PartyListeners(this);
        }
        if (config.getBoolean("function.motd")) PM.registerCommand(this, new motd());
        if (config.getBoolean("function.restart")) PM.registerCommand(this, new restart());
        if (config.getBoolean("function.versions")) {
            versions = new versions();
            PM.registerCommand(this, versions);
            try {
                PreparedStatement ps = mySQL.getCon().prepareStatement("CREATE TABLE IF NOT EXISTS `crashed_versions` (server VARCHAR(5),version TINYTEXT ,PRIMARY KEY (server),UNIQUE (server))");
                ps.execute();
            } catch (SQLException e) {
                getLogger().log(Level.WARNING, "Fehler beim Verbinden mit MySQL!");
                getLogger().log(Level.WARNING, "--------------------------------");
                e.printStackTrace();
                getLogger().log(Level.WARNING, "--------------------------------");
            }
        }
        if (config.getBoolean("function.bansystem")) {
            PM.registerCommand(this, new ban());
            try {
                PreparedStatement ps = mySQL.getCon().prepareStatement("CREATE TABLE IF NOT EXISTS `crashed_ban` (player VARCHAR(36),end VARCHAR(19),reason TEXT,whenbanned VARCHAR(19),PRIMARY KEY (player),UNIQUE (player))");
                ps.execute();
            } catch (SQLException e) {
                getLogger().log(Level.WARNING, "Fehler beim Verbinden mit MySQL!");
                getLogger().log(Level.WARNING, "--------------------------------");
                e.printStackTrace();
                getLogger().log(Level.WARNING, "--------------------------------");
            }
            //TODO: Ban Command und dann das hier löschen
            UUID uuid = UUID.randomUUID();
            ban.ban(uuid, ban.translateNowToDatetime("YDMhms"), "test ban", ban.translateNowToDatetime(""));
            getLogger().warning(ban.getEnd(uuid));
        }

        if (config.getBoolean("function.friday") || config.getBoolean("function.players")) new JoinListener(this);
        if (config.getBoolean("function.versionfilter")) new BeforeJoinListener(this);
        if (config.getBoolean("function.bungeecute")) new ChannelListener(this);
        if (config.getBoolean("function.motd")) new PingListener(this);
        if (config.getBoolean("function.players")) new LeaveListener(this);

        ProxyServer.getInstance().registerChannel("BungeeCord");

        getLogger().info("Crashed Plugin crashed successfully!");
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
    }

    public static CrashedWaterfall getPlugin() {
        return plugin;
    }

    public versions getVersions() {
        return versions;
    }

    private void init() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        file = new File(getDataFolder().getPath(), "config.yml");
        partiesFile = new File(getDataFolder().getPath(), "parties.yml");
        playersFile = new File(getDataFolder().getPath(), "players.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (!partiesFile.exists()) {
                partiesFile.createNewFile();
            }
            if (!playersFile.exists()) {
                playersFile.createNewFile();
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!config.contains("mysql.host")) {
            config.set("mysql.host", "ms2666.gamedata.io");
            config.set("mysql.port", 3306);
            config.set("mysql.database", "ni5086434_1_DB");
            config.set("mysql.user", "ni5086434_1_DB");
            config.set("mysql.password", "SPx0uE70");
        }
        mySQL = new MySQL(config.getString("mysql.host"), config.getInt("mysql.port"), config.getString("mysql.database"), config.getString("mysql.user"), config.getString("mysql.password"));
        ban = new BanMySQL();
    }

    private void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MySQL getMySQL() {
        return mySQL;
    }

    public BanMySQL getBan() {
        return ban;
    }

    public void commandOnServer(String channel, String cmd, String server, CommandSender sender) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);

        try {
            output.writeUTF(channel);
            output.writeUTF(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerInfo serverInfo = getProxy().getServerInfo(server);
        if (serverInfo == null) {
            sender.sendMessage(new TextComponent(ChatColor.RED + "Der Server '" + server + "' wurde nicht gefunden"));
            return;
        }
        serverInfo.sendData("BungeeCord", stream.toByteArray());
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Dein Befehl " + ChatColor.GOLD + cmd + ChatColor.GREEN + "wurde erfolreich an den Server " + ChatColor.GOLD + server + ChatColor.GREEN + " gesendet"));
    }

    public void commandOnServer(String channel, String cmd, CommandSender sender) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);

        try {
            output.writeUTF(channel);
            output.writeUTF(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ServerInfo server : getProxy().getServers().values()) {
            server.sendData("BungeeCord", stream.toByteArray());
        }
        sender.sendMessage(new TextComponent(ChatColor.GREEN + "Dein Befehl " + ChatColor.GOLD + cmd + ChatColor.GREEN + "wurde erfolreich an alle Server gesendet"));
    }

    public void teleportOnServer(String channel, String object, String dest, String server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(object);
            output.writeUTF(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerInfo serverInfo = getProxy().getServerInfo(server);
        if (serverInfo == null) {
            return;
        }
        serverInfo.sendData("BungeeCord", stream.toByteArray());
    }

    public void teleportOnServerCoords(String channel, String object, String x, String y, String z, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(object);
            output.writeUTF(x);
            output.writeUTF(y);
            output.writeUTF(z);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (server == null) {
            return;
        }
        server.sendData("BungeeCord", stream.toByteArray());
    }

    public void teleportOnServerCoords(String channel, String object, String x, String y, String z, String yaw, String pitch, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(object);
            output.writeUTF(x);
            output.writeUTF(y);
            output.writeUTF(z);
            output.writeUTF(yaw);
            output.writeUTF(pitch);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (server == null) {
            return;
        }
        server.sendData("BungeeCord", stream.toByteArray());
    }

    public void restartAll(String channel, String what) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
            output.writeUTF(what);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ServerInfo server : getProxy().getServers().values()) {
            server.sendData("BungeeCord", stream.toByteArray());
        }
    }

    public void getversion(String channel) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(stream);
        try {
            output.writeUTF(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ServerInfo server : getProxy().getServers().values()) {
            server.sendData("BungeeCord", stream.toByteArray());
        }
    }
}
