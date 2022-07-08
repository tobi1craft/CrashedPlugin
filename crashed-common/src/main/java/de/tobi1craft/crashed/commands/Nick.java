package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedCommon;
import de.tobi1craft.crashed.mysql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class Nick implements CommandExecutor {

    CrashedCommon pl = CrashedCommon.getPlugin();
    MySQL mySQL = pl.getMySQL();

    public Nick() {
        Objects.requireNonNull(pl.getCommand("nick")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            new BukkitRunnable() {
                public void run() {
                    Player p = (Player) sender;
                    UUID uuid = p.getUniqueId();
                    switch (args.length) {
                        case 1:
                            setNicked(uuid, args[0]);
                            p.sendMessage(ChatColor.GREEN + "Du heißt jetzt " + getNicked(uuid));
                            break;
                        case 0:
                            if (isNicked(uuid)) {
                                removeNicked(uuid);
                                p.sendMessage(ChatColor.GREEN + "Dein Nickname wurde entfernt.");
                            }
                            break;
                        default:
                            p.sendMessage(ChatColor.RED + "Zu viele Argumente!");
                            break;
                    }
                }
            }.runTaskAsynchronously(pl);
        } else {
            Bukkit.getLogger().warning("Du kannst dich nur als Spieler nicken!");
        }
        return false;
    }

    public void setNicked(UUID uuid, String nickname) {
        mySQL.connect();
        try {
            PreparedStatement st;
            if (isNicked(uuid)) {
                st = mySQL.getCon().prepareStatement("UPDATE `crashed_nick` SET `nick` = ? WHERE `player` = ?");
                st.setString(1, nickname);
                st.setString(2, String.valueOf(uuid));
            } else {
                st = mySQL.getCon().prepareStatement("INSERT INTO `crashed_nick`(`player`,`nick`) VALUES (?,?)");
                st.setString(1, String.valueOf(uuid));
                st.setString(2, nickname);
            }
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public void removeNicked(UUID uuid) {
        mySQL.connect();
        PreparedStatement ps;
        try {
            ps = mySQL.getCon().prepareStatement("DELETE FROM `crashed_nick` WHERE `player` = ?");
            ps.setString(1, String.valueOf(uuid));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public boolean isNicked(UUID uuid) {
        mySQL.connect();
        PreparedStatement st;
        try {
            st = mySQL.getCon().prepareStatement("SELECT `player` FROM `crashed_nick` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return false;
    }

    public String getNicked(UUID uuid) {
        mySQL.connect();
        PreparedStatement st;
        try {
            st = mySQL.getCon().prepareStatement("SELECT `nick` FROM `crashed_nick` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getString("nick");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName();

    }

}
