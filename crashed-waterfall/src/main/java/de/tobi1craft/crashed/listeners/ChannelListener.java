package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedWaterfall;
import de.tobi1craft.crashed.commands.versions;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ChannelListener implements Listener {

    CrashedWaterfall plugin = CrashedWaterfall.getPlugin();
    versions versions = plugin.getVersions();

    public ChannelListener(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
        DataInputStream stream = new DataInputStream(new ByteArrayInputStream(event.getData()));

        try {
            String channel = stream.readUTF();

            if (channel.equals("commandbungee")) {
                String cmd = stream.readUTF();
                ProxyServer p = ProxyServer.getInstance();
                p.getPluginManager().dispatchCommand(p.getConsole(), cmd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
