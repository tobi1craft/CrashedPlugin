package de.tobi1craft.crashed.util;

import de.tobi1craft.crashed.CrashedCommon;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class UpdateChecker implements Listener {

    private static UpdateChecker updateChecker;
    PluginManager pm = Bukkit.getPluginManager();
    CrashedCommon pl = CrashedCommon.getPlugin();
    FileConfiguration config = pl.getConfig();
    BukkitRunnable run;

    public UpdateChecker() {
        updateChecker = this;
    }

    public static UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public void getUpdates(CommandSender p) {
        run = new BukkitRunnable() {
            @Override
            public void run() {
                if (p.hasPermission("crashed.update.notify")) {
                    HashMap<String, String> ids = new HashMap<>();
                    List<String> plugins = config.getStringList("updater.plugins");
                    for (String s : plugins) {
                        String[] seperated = s.split(":");
                        if (pm.getPlugin(seperated[0]) != null) ids.put(seperated[1], seperated[0]);
                    }
                    for (String id : ids.keySet()) {
                        if (checkUpdate(id, ids.get(id))) {
                            TextComponent text = new TextComponent(ChatColor.GOLD + "Update available for " + ChatColor.YELLOW + ids.get(id));
                            if (Bukkit.getPluginManager().getPlugin(ids.get(id)).getDescription().getWebsite() != null) {
                                text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(ids.get(id))).getDescription().getWebsite()));
                            }
                            p.sendMessage(text);
                        }
                    }
                }
            }
        };
        run.runTaskAsynchronously(pl);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoinUpdate(PlayerJoinEvent event) {
        getUpdates(event.getPlayer());
    }

    private boolean checkUpdate(String pluginID, String pluginName) {
        try {
            String url = "https://api.spigotmc.org/legacy/update.php?resource=";
            String localVersionString = Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(pluginName)).getDescription().getVersion();
            int localVersion = split(localVersionString);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url + pluginID).openConnection();
            connection.setRequestMethod("GET");
            String raw = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
            String remoteVersionString;
            if (raw.contains("-")) {
                remoteVersionString = raw.split("-")[0].trim();
            } else {
                remoteVersionString = raw;
            }
            int remoteversion = split(remoteVersionString);
            int locallenght = String.valueOf(localVersion).length();
            int remotelenght = String.valueOf(remoteversion).length();
            if (remotelenght > locallenght) {
                do {
                    localVersion = localVersion * 10;
                    locallenght = String.valueOf(localVersion).length();
                    remotelenght = String.valueOf(remoteversion).length();
                } while (remotelenght > locallenght);
            }
            if (remotelenght < locallenght) {
                do {
                    remoteversion = remoteversion * 10;
                    locallenght = String.valueOf(localVersion).length();
                    remotelenght = String.valueOf(remoteversion).length();
                } while (remotelenght < locallenght);
            }
            if (localVersion < remoteversion) return true;
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private int split(String toSplit) {
        String[] splitted = toSplit.split("[\\D]");
        StringBuilder builder = new StringBuilder();
        for (String s : splitted) {
            builder.append(s);
        }
        return Integer.parseInt(builder.toString());
    }
}
