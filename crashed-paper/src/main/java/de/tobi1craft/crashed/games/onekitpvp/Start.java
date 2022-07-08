package de.tobi1craft.crashed.games.onekitpvp;

import de.tobi1craft.crashed.CrashedPaper;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Start {

    CrashedPaper plugin = CrashedPaper.getPlugin();
    FileConfiguration settings = plugin.getSettings();
    int i;

    public Start() {
        settings.set("beforegame", true);
        settings.set("minplayers", 2);
        settings.set("maxplayers", 6);
        settings.set("falldamage", true);
        settings.set("blockinteract", true);
        settings.set("obbi", true);
        plugin.saveSettings();
        i = 30;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i >= 0) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendActionBar(ChatColor.GOLD + String.valueOf(i));
                        if (i == 20 || i == 10 || i <= 5) {
                            p.sendTitle(ChatColor.YELLOW + String.valueOf(i), "Gleich geht's los", 0, 20, 5);
                        }
                    }
                    i--;
                } else {
                    this.cancel();
                    afterCountdown();
                }
            }
        }.runTaskTimer(plugin, 40L, 20L);
    }

    public void afterCountdown() {
        settings.set("beforegame", false);
        plugin.saveSettings();
        Bukkit.broadcast(new TextComponent(ChatColor.YELLOW + "Let's fight"));
    }
}
