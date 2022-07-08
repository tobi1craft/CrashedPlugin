package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedWaterfall;
import de.tobi1craft.crashed.listeners.restart.BeforeJoinPriorityListener;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class restart extends Command {

    CrashedWaterfall plugin = CrashedWaterfall.getPlugin();

    public restart() {
        super("grestart", "crashed.restart", "stop", "end");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> plugin.restartAll("restart", "pvp"), 1L, TimeUnit.SECONDS);
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> new BeforeJoinPriorityListener(plugin), 10L, TimeUnit.SECONDS);
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> plugin.restartAll("restart", "restart"), 15L, TimeUnit.SECONDS);
    }
}
