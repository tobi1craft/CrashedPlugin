package de.tobi1craft.crashed.util;

import de.tobi1craft.crashed.CrashedCommon;
import de.tobi1craft.crashed.mysql.MySQL;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SetVersionMySQL {

    CrashedCommon plugin = CrashedCommon.getPlugin();
    MySQL mySQL = plugin.getMySQL();
    FileConfiguration config = plugin.getConfig();

    public void setVersion() {
        mySQL.connect();
        try {
            PreparedStatement st;
            if (isIn()) {
                st = mySQL.getCon().prepareStatement("UPDATE `crashed_versions` SET `version` = ? WHERE `server` = ?");
                st.setString(1, plugin.getDescription().getVersion());
                st.setString(2, config.getString("name"));
            } else {
                st = mySQL.getCon().prepareStatement("INSERT INTO `crashed_versions`(`server`,`version`) VALUES (?,?)");
                st.setString(1, config.getString("name"));
                st.setString(2, plugin.getDescription().getVersion());
            }
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public boolean isIn() {
        mySQL.connect();
        PreparedStatement st;
        try {
            st = mySQL.getCon().prepareStatement("SELECT `server` FROM `crashed_versions` WHERE `server` = ?");
            st.setString(1, config.getString("name"));
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return false;
    }

}
