package de.tobi1craft.crashed.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFadeEvent;

public class BlockFadeListener implements Listener {

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        if (e.getBlock().getType() == Material.SNOW) e.setCancelled(true);
    }
}
