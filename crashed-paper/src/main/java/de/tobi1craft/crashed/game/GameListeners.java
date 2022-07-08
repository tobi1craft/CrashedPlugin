package de.tobi1craft.crashed.game;

import de.tobi1craft.crashed.CrashedPaper;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class GameListeners implements Listener {

    CrashedPaper plugin = CrashedPaper.getPlugin();
    FileConfiguration settings = plugin.getSettings();

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (settings.getBoolean("beforegame")) {
            event.setCancelled(true);
        }
        if (event.getCause() == DamageCause.FALL && !settings.getBoolean("falldamage")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Material block = e.getBlockPlaced().getType();
        if (!(block == Material.WATER || block == Material.LAVA) && !settings.getBoolean("blockinteract")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Material block = e.getBlock().getType();
        if (!(block == Material.WATER || block == Material.LAVA) && !settings.getBoolean("blockinteract")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent e) {
        if (!settings.getBoolean("obbi")) {
            e.setCancelled(true);
        }
    }

}
