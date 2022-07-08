package de.tobi1craft.crashed.commands.serverwechsel;


import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class minigames extends Command implements TabExecutor{



	public minigames() {
		super("minigames", "crashed.server.minigames", "mg");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			return;
		}
		if(args.length > 1) {
			return;
		}
		ProxiedPlayer p = (ProxiedPlayer) sender;
		if(args.length == 0) {
			p.connect(ProxyServer.getInstance().getServers().get("minigames"));
		}
		if(args.length == 1) {
			if(p.hasPermission("crashed.serverother.minigames")) {
				ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[0]);
				if(player == null) {
					p.sendMessage(new TextComponent( ChatColor.RED + args[0] + "ist nicht auf dem Server-Netzwerk"));
					return;
				}
				player.connect(ProxyServer.getInstance().getServers().get("minigames"));
				p.sendMessage(new TextComponent(ChatColor.GREEN + "Du hast " + player.getName() + " zu den Minigames verschoben"));
			}
		}
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		if(args.length > 1) {return Collections.emptyList();}
		
		if(args.length == 1) {
			List<String> list = new ArrayList<>();
			for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
                list.add(p.getName());
            }
			return list.stream().filter(b -> b.startsWith(args[0])).collect(Collectors.toList());}
		
		return null;
			
	}
}
