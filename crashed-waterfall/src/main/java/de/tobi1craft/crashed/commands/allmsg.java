package de.tobi1craft.crashed.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class allmsg extends Command {
    public allmsg() {
        super("allmsg", "crashed.allmsg", "amsg", "say");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }
        String s = "console";
        if (sender instanceof ProxiedPlayer) s = ((ProxiedPlayer) sender).getDisplayName();
        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }
        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers())
            p.sendMessage(new TextComponent(ChatColor.RED + s + ">> " + ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', String.valueOf(message))));
    }
}
