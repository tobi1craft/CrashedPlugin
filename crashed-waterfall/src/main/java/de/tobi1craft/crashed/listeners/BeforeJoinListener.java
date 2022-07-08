package de.tobi1craft.crashed.listeners;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BeforeJoinListener implements Listener {

    public BeforeJoinListener(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void beforePlayerJoin(LoginEvent e) {
        if (e.getConnection().getVersion() < 754) {
            e.setCancelled(true);
            e.setCancelReason(new TextComponent(ChatColor.RED + "Bitte nutze 1.16 oder neuer!" + ChatColor.GREEN + " Bald auch ab 1.8"));
        }
    }
}
