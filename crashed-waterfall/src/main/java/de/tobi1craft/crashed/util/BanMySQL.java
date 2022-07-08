package de.tobi1craft.crashed.util;

import de.tobi1craft.crashed.CrashedWaterfall;
import de.tobi1craft.crashed.mysql.MySQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

public class BanMySQL {
    private final CrashedWaterfall plugin = CrashedWaterfall.getPlugin();
    private final MySQL mySQL = plugin.getMySQL();

    public String translateNowToDatetime(String toFormat) {
        int year = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.YEAR);
        int month = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.MINUTE);
        int second = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.systemDefault())).get(Calendar.SECOND);
        boolean isIn;
        boolean temp;

        isIn = toFormat.contains("Y");
        String[] a = toFormat.split("Y");
        if (!(Objects.equals(a[0], ""))) year = year + Integer.parseInt(a[0]);

        temp = a[0].contains("M"); //TODO: überall
        String[] b;
        if (a.length == 2) {
            b = a[1].split("M");
            isIn = true; //TODO: überall
        } else {
            b = a[0].split("M");
        }
        if (!(Objects.equals(b[0], "")) && !isIn) month = month + Integer.parseInt(b[0]);
        //TODO: überall
        isIn = temp; //TODO: überall

        String[] c;
        if (b.length == 2) {
            c = b[1].split("D");
        } else {
            c = b[0].split("D");
        }
        if (!(Objects.equals(c[0], ""))) day = day + Integer.parseInt(c[0]);

        String[] d;
        if (c.length == 2) {
            d = c[1].split("h");
        } else {
            d = c[0].split("h");
        }
        if (!(Objects.equals(d[0], ""))) hour = hour + Integer.parseInt(d[0]);

        String[] e;
        if (d.length == 2) {
            e = d[1].split("m");
        } else {
            e = d[0].split("m");
        }
        if (!(Objects.equals(e[0], ""))) minute = minute + Integer.parseInt(e[0]);

        String[] f;
        if (e.length == 2) {
            f = e[1].split("s");
        } else {
            f = e[0].split("s");
        }
        if (!(Objects.equals(f[0], ""))) second = second + Integer.parseInt(f[0]);

        return translateDateToDatetime(year, month, day, hour, minute, second);
    }

    public String translateDateToDatetime(int yearInt, int monthInt, int dayInt, int hourInt, int minuteInt, int secondInt) {
        String year = String.valueOf(yearInt);
        StringBuilder month = new StringBuilder(String.valueOf(monthInt));
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

    public void setNicked(UUID uuid, String datetime, String reason) {
        mySQL.connect();
        try {
            PreparedStatement st;
            if (isBanned(uuid)) {
                st = mySQL.getCon().prepareStatement("UPDATE `crashed_ban` SET `end` = ?,`reason` = ? WHERE `player` = ?");
                st.setString(1, datetime);
                st.setString(2, reason);
                st.setString(3, String.valueOf(uuid));
            } else {
                st = mySQL.getCon().prepareStatement("INSERT INTO `crashed_ban`(`player`,`end`,`reason`) VALUES (?,?,?)");
                st.setString(1, String.valueOf(uuid));
                st.setString(2, datetime);
                st.setString(3, reason);
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
        return result.isEmpty() ? "&cEs wurde kein Grund angegeben" : result;

    }

    public String getEnd(UUID uuid) {
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
        return result.isEmpty() ? "forever" : result;

    }
}
