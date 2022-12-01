package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

public class JoinListener implements Listener {

    CrashedBungee plugin = CrashedBungee.getPlugin();
    File file = new File(plugin.getDataFolder().getPath(), "config.yml");
    Configuration config;

    public JoinListener(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxiedPlayer p = event.getPlayer();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int time = calendar.get(Calendar.HOUR_OF_DAY) - 1;

        if (p.hasPermission("vanish.joinvanished")) {
            p.sendMessage(new TextComponent(ChatColor.GOLD + "Du bist unsichtbar. Benutze " + ChatColor.BLUE + "/v" + ChatColor.GOLD + " um sichtbar zu werden"));
            p.sendMessage(new TextComponent(ChatColor.GOLD + "Benutze " + ChatColor.BLUE + "/av" + ChatColor.GOLD + " um nicht mehr automatisch unsichtbar zu werden"));
        } else if (Objects.equals(p.getDisplayName(), "Tjuli")) {
            p.sendMessage(new TextComponent(ChatColor.RED + "Du bist nicht vanished @Tjuli!"));
        }

        if (config.getBoolean("function.friday")) {
            if (day == 5) {
                if (time >= 12) {
                    p.sendMessage(new TextComponent(ChatColor.GREEN + "It's Friday then..."));
                    p.sendMessage(new TextComponent(ChatColor.GOLD + "https://www.youtube.com/watch?v=U6n2NcJ7rLc"));
                }
            }
        }
        if (config.getBoolean("function.players")) {
            try {
                if (!plugin.getDataFolder().exists()) {
                    plugin.getDataFolder().mkdir();
                }
                File PlayersFile = new File(plugin.getDataFolder().getPath(), "players.yml");
                if (!file.exists()) {
                    file.createNewFile();
                }
                Configuration players = ConfigurationProvider.getProvider(YamlConfiguration.class).load(PlayersFile);
                players.set(p.getName(), "online");
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(players, PlayersFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
