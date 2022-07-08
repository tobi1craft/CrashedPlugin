package de.tobi1craft.crashed.hide;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Start {
		
	public void hideStart() {
		Location location = new Location(Bukkit.getWorld("hide"), 0, 65, 0);
		int pcount = 0;
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.teleport(location);
			pcount ++;
		}
		
		
	}

}
