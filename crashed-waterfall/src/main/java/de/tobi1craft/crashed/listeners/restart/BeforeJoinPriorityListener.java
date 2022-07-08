package de.tobi1craft.crashed.listeners.restart;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BeforeJoinPriorityListener implements Listener {

    public BeforeJoinPriorityListener(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void beforePlayerJoin(LoginEvent e) {
        e.setCancelReason(new TextComponent(ChatColor.GOLD + "Das Netzwerk führt gerade einen Neustart durch.\n\nEs wird gleich wieder verfügbar sein.\n\n" + ChatColor.GREEN + "Vielen Dank für deine Geduld.\n\n" + ChatColor.RED + "<3"));
        e.setCancelled(true);
    }
}
