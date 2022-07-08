package de.tobi1craft.crashed.listeners.shutdown;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void StopPlayerDamage(EntityDamageByEntityEvent e) {
        e.setCancelled(true);
    }
}
