package de.tobi1craft.crashed.util;

import de.tobi1craft.crashed.CrashedBungee;
import de.tobi1craft.crashed.mysql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.*;

public class BanMySQL {
    private final CrashedBungee plugin = CrashedBungee.getPlugin();
    private final MySQL mySQL = plugin.getMySQL();
    int daysInMonth;
    int monthh;
    Configuration config;
    String timeZone;


    public BanMySQL() {
        File file = new File(plugin.getDataFolder().getPath(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String translateNowToDatetime(String toFormat) {
        assert false;
        timeZone = config.getString("date.timeZoneId");
        int year = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.YEAR);
        int month = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.MINUTE);
        int second = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of(timeZone))).get(Calendar.SECOND);
        boolean isIn;
        boolean temp;

        isIn = toFormat.contains("Y");
        String[] a = toFormat.split("Y");
        if (!(a.length == 0 || Objects.equals(a[0], "")) && isIn) year = year + Integer.parseInt(a[0]);
        else {
            a = new String[1];
            a[0] = "";
        }

        temp = false;
        for (String i : a) {
            if (i.contains("M")) {
                temp = true;
                break;
            }
        }
        String[] b;
        if (a.length == 2) {
            b = a[1].split("M");
        } else {
            b = a[0].split("M");
        }
        if (!(b.length == 0 || Objects.equals(b[0], "")) && temp) month = month + Integer.parseInt(b[0]);
        else {
            b = new String[1];
            b[0] = "";
        }

        temp = false;
        for (String i : b) {
            if (i.contains("D")) {
                temp = true;
                break;
            }
        }
        String[] c;
        if (b.length == 2) {
            c = b[1].split("D");
        } else {
            c = b[0].split("D");
        }
        if (!(c.length == 0 || Objects.equals(c[0], "")) && temp) day = day + Integer.parseInt(c[0]);
        else {
            c = new String[1];
            c[0] = "";
        }

        temp = false;
        for (String i : c) {
            if (i.contains("h")) {
                temp = true;
                break;
            }
        }
        String[] d;
        if (c.length == 2) {
            d = c[1].split("h");
        } else {
            d = c[0].split("h");
        }
        if (!(d.length == 0 || Objects.equals(d[0], "")) && temp) hour = hour + Integer.parseInt(d[0]);
        else {
            d = new String[1];
            d[0] = "";
        }

        temp = false;
        for (String i : d) {
            if (i.contains("m")) {
                temp = true;
                break;
            }
        }
        String[] e;
        if (d.length == 2) {
            e = d[1].split("m");
        } else {
            e = d[0].split("m");
        }
        if (!(e.length == 0 || Objects.equals(e[0], "")) && temp) minute = minute + Integer.parseInt(e[0]);
        else {
            e = new String[1];
            e[0] = "";
        }

        temp = false;
        for (String i : e) {
            if (i.contains("s")) {
                temp = true;
                break;
            }
        }
        String[] f;
        if (e.length == 2) {
            f = e[1].split("s");
        } else {
            f = e[0].split("s");
        }
        if (!(Objects.equals(f[0], "")) && temp) second = second + Integer.parseInt(f[0]);

        return translateDateToDatetime(year, month, day, hour, minute, second);
    }

    public String translateDateToDatetime(int yearInt, int monthInt, int dayInt, int hourInt, int minuteInt, int secondInt) {
        assert false;
        timeZone = config.getString("date.timeZoneId");
        monthh = monthInt;

        while (secondInt >= 60) {
            secondInt = secondInt - 60;
            minuteInt++;
        }
        while (minuteInt >= 60) {
            minuteInt = minuteInt - 60;
            hourInt++;
        }
        while (hourInt >= 24) {
            hourInt = hourInt - 24;
            dayInt++;
        }
        while (monthh >= 13) {
            monthh = monthh - 12;
            yearInt++;
        }

        setDaysInMonth(yearInt);
        while (dayInt > daysInMonth) {
            dayInt = dayInt - daysInMonth;
            monthh++;
            while (monthh >= 13) {
                monthh = monthh - 12;
                yearInt++;
            }
            setDaysInMonth(yearInt);
        }
        while (monthh >= 13) {
            monthh = monthh - 12;
            yearInt++;
        }


        String year = String.valueOf(yearInt);
        StringBuilder month = new StringBuilder(String.valueOf(monthh));
        StringBuilder day = new StringBuilder(String.valueOf(dayInt));
        StringBuilder hour = new StringBuilder(String.valueOf(hourInt));
        StringBuilder minute = new StringBuilder(String.valueOf(minuteInt));
        StringBuilder second = new StringBuilder(String.valueOf(secondInt));

        while (month.length() < 2) {
            month.insert(0, "0");
        }
        while (day.length() < 2) {
            day.insert(0, "0");
        }
        while (hour.length() < 2) {
            hour.insert(0, "0");
        }
        while (minute.length() < 2) {
            minute.insert(0, "0");
        }
        while (second.length() < 2) {
            second.insert(0, "0");
        }
        return year + "-" + month + "-" + day + " " + hour + "-" + minute + "-" + second;
    }

    private void setDaysInMonth(int year) {
        switch (monthh) {
            case 1, 3, 5, 7, 8, 10, 12 -> daysInMonth = 31;
            case 4, 6, 9, 11 -> daysInMonth = 30;
            case 2 -> {
                int integer = year / 4;
                float floateger = year / 4f;
                daysInMonth = floateger == integer ? 29 : 28;
                ProxyServer.getInstance().getLogger().warning(floateger + " ---- " + integer);
                ProxyServer.getInstance().getLogger().warning(String.valueOf(daysInMonth));
            }
            default -> ProxyServer.getInstance().getLogger().warning("Error in Ban System (daysInMonth)");
        }
    }

    public void ban(UUID uuid, String reason, String datetime) {
        mySQL.connect();
        try {
            PreparedStatement st;
            if (isBanned(uuid)) {
                st = mySQL.getCon().prepareStatement("UPDATE `crashed_ban` SET `end` = ?,`reason` = ?,`whenbanned` = ? WHERE `player` = ?");
                st.setString(1, datetime);
                st.setString(2, reason);
                st.setString(3, this.translateNowToDatetime(""));
                st.setString(4, String.valueOf(uuid));
            } else {
                st = mySQL.getCon().prepareStatement("INSERT INTO `crashed_ban`(`player`,`end`,`reason`,`whenbanned`) VALUES (?,?,?,?)");
                st.setString(1, String.valueOf(uuid));
                st.setString(2, datetime);
                st.setString(3, reason);
                st.setString(4, this.translateNowToDatetime(""));
            }
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public void ban(UUID uuid, String reason) {
        mySQL.connect();
        try {
            PreparedStatement st;
            if (isBanned(uuid)) {
                st = mySQL.getCon().prepareStatement("UPDATE `crashed_ban` SET `end` = ?,`reason` = ?,`whenbanned` = ? WHERE `player` = ?");
                st.setString(1, "");
                st.setString(2, reason);
                st.setString(3, this.translateNowToDatetime(""));
                st.setString(4, String.valueOf(uuid));
            } else {
                st = mySQL.getCon().prepareStatement("INSERT INTO `crashed_ban`(`player`,`reason`,`whenbanned`) VALUES (?,?,?)");
                st.setString(1, String.valueOf(uuid));
                //st.setString(2, datetime);
                st.setString(2, reason);
                st.setString(3, this.translateNowToDatetime(""));
            }
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public void removeBan(UUID uuid) {
        mySQL.connect();
        PreparedStatement ps;
        try {
            ps = mySQL.getCon().prepareStatement("DELETE FROM `crashed_ban` WHERE `player` = ?");
            ps.setString(1, String.valueOf(uuid));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
    }

    public boolean isBanned(UUID uuid) {
        mySQL.connect();
        PreparedStatement st;
        try {
            st = mySQL.getCon().prepareStatement("SELECT `player` FROM `crashed_ban` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return false;
    }

    public String getReason(UUID uuid) {
        if (!isBanned(uuid)) return "&cDieser Spieler ist nicht gebannt";
        mySQL.connect();
        PreparedStatement st;
        String result = "";
        try {
            st = mySQL.getCon().prepareStatement("SELECT `reason` FROM `crashed_ban` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return result == null || result.isEmpty() ? "&cEs wurde kein Grund angegeben" : result;

    }

    public String getEnd(UUID uuid) {
        if (!isBanned(uuid)) return "&cDieser Spieler ist nicht gebannt";
        mySQL.connect();
        PreparedStatement st;
        String result = "";
        try {
            st = mySQL.getCon().prepareStatement("SELECT `end` FROM `crashed_ban` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getString("end");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return result == null || result.isEmpty() ? "forever" : result;

    }

    public String whenBanned(UUID uuid) {
        if (!isBanned(uuid)) return "&cDieser Spieler ist nicht gebannt";
        mySQL.connect();
        PreparedStatement st;
        String result = "";
        try {
            st = mySQL.getCon().prepareStatement("SELECT `whenbanned` FROM `crashed_ban` WHERE `player` = ?");
            st.setString(1, String.valueOf(uuid));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                result = rs.getString("whenbanned");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mySQL.disconnect();
        return result == null || result.isEmpty() ? "unknown or error" : result;

    }
}
