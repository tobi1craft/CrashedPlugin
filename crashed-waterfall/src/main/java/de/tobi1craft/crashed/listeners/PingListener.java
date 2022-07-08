package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PingListener implements Listener {

    CrashedWaterfall plugin = CrashedWaterfall.getPlugin();

    public PingListener(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPing(ProxyPingEvent e) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "config.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        ServerPing ping = e.getResponse();
        ping.setDescriptionComponent(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.text.1") + "\n" + config.getString("motd.text.2"))));
        ping.getPlayers().setSample(new ServerPing.PlayerInfo[]{new ServerPing.PlayerInfo(ChatColor.translateAlternateColorCodes('&', config.getString("motd.playerinfo")), UUID.randomUUID())});
        e.setResponse(ping);
    }
}
