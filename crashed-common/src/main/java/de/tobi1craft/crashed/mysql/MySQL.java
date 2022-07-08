package de.tobi1craft.crashed.mysql;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQL {

    private Connection con;
    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public MySQL(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        connect();
    }

    public void connect() {
        if (!hasConnection()) try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getLogger().log(Level.WARNING, "MySQL disconneted");
        }
    }

    public void disconnect() {
        try {
            if (hasConnection()) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasConnection() {
        try {
            return this.con != null && con.isValid(28800);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Connection getCon() {
        return con;
    }
}
