package de.tobi1craft.crashed.party;

import de.tobi1craft.crashed.CrashedWaterfall;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PartyListeners implements Listener {

    CrashedWaterfall plugin = CrashedWaterfall.getPlugin();

    public PartyListeners(Plugin plugin) {
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent e) throws IOException {
        ProxiedPlayer p = e.getPlayer();
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        String partyofp = config.getString("partyofplayer." + p.getName());
        if (Objects.equals(config.getString(partyofp + "." + p.getName()), "owner")) {
            ProxiedPlayer player;
            for (String s : config.getSection(partyofp).getKeys()) {
                player = ProxyServer.getInstance().getPlayer(s);
                if (!(Objects.equals(p.getServer().getInfo().getName(), player.getServer().getInfo().getName()))) {
                    player.connect(p.getServer().getInfo());
                }
            }
            p.sendMessage(new TextComponent(ChatColor.GREEN + "Die Party ist deinem Server beigetreten"));
        }
    }
}
