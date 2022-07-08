package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedWaterfall;
import de.tobi1craft.crashed.mysql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class versions extends Command {

    private final CrashedWaterfall plugin = CrashedWaterfall.getPlugin();
    private final MySQL mySQL = plugin.getMySQL();
    private String minigames = "Fehler";
    private String lobby = "Fehler";
    private String rpg = "Fehler";

    public versions() {
        super("versions", "crashed.versions");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(args.length == 0)) {
            return;
        }
        plugin.getversion("getversion");
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> sendVersionToPlayer(sender), 1L, TimeUnit.SECONDS);
    }

    private void sendVersionToPlayer(CommandSender sender) {
        mySQL.connect();
        PreparedStatement st;
        try {
            st = mySQL.getCon().prepareStatement("SELECT `version` FROM `crashed_versions` WHERE `server` = ?");
            st.setString(1, "lobby");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                lobby = rs.getString("version");
            } else lobby = "error";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st = mySQL.getCon().prepareStatement("SELECT `version` FROM `crashed_versions` WHERE `server` = ?");
            st.setString(1, "minis");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                minigames = rs.getString("version");
            } else minigames = "error";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            st = mySQL.getCon().prepareStatement("SELECT `version` FROM `crashed_versions` WHERE `server` = ?");
            st.setString(1, "rpg__");
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                rpg = rs.getString("version");
            } else rpg = "error";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        sender.sendMessage(new TextComponent(ChatColor.GOLD + "Lobby: " + ChatColor.YELLOW + lobby));
        sender.sendMessage(new TextComponent(ChatColor.GOLD + "Minigames: " + ChatColor.YELLOW + minigames));
        sender.sendMessage(new TextComponent(ChatColor.GOLD + "RPG: " + ChatColor.YELLOW + rpg));
    }
}
