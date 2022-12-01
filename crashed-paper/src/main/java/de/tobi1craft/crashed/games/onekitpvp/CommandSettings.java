package de.tobi1craft.crashed.games.onekitpvp;

import de.tobi1craft.crashed.CrashedMinigames;
import de.tobi1craft.crashed.util.ItemBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CommandSettings implements CommandExecutor, Listener {

    static CrashedMinigames plugin = CrashedMinigames.getPlugin();
    static FileConfiguration settings = plugin.getSettings();
    private Inventory okpinv;

    static void onekitpvpStart() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Runde startet -> Let's go");
    }

    public CommandSettings() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Objects.requireNonNull(plugin.getCommand("settings")).setExecutor(this);
    }

    public void Inventory(Player p) {
        okpinv = Bukkit.createInventory(null, 3 * 9, "§cSettings");
        if (settings.getBoolean("falldamage")) {
            okpinv.setItem(0, new ItemBuilder(Material.LAVA_BUCKET).setDisplayname(ChatColor.GOLD + "Fallschaden: " + ChatColor.GREEN + "true").setLocalizedName("falldamage").build());
        } else {
            okpinv.setItem(0, new ItemBuilder(Material.WATER_BUCKET).setDisplayname(ChatColor.GOLD + "Fallschaden: " + ChatColor.RED + "false").setLocalizedName("falldamage").build());
        }
        if (settings.getBoolean("blockinteract")) {
            okpinv.setItem(1, new ItemBuilder(Material.MAP).setDisplayname(ChatColor.GOLD + "Mit Blöcken interagieren: " + ChatColor.GREEN + "true").setLocalizedName("blockinteract").build());
        } else {
            okpinv.setItem(1, new ItemBuilder(Material.IRON_SWORD).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setDisplayname(ChatColor.GOLD + "Mit Blöcken interagieren: " + ChatColor.RED + "false").setLocalizedName("blockinteract").build());
        }
        if (settings.getBoolean("obbi")) {
            okpinv.setItem(4, new ItemBuilder(Material.OBSIDIAN).setDisplayname(ChatColor.GOLD + "Wasser + Lava: " + ChatColor.GREEN + "true").setLocalizedName("obbi").build());
        } else {
            okpinv.setItem(4, new ItemBuilder(Material.CRYING_OBSIDIAN).setDisplayname(ChatColor.GOLD + "Wasser + Lava: " + ChatColor.RED + "false").setLocalizedName("obbi").build());
        }
        okpinv.setItem(2, new ItemBuilder(Material.ARROW).setDisplayname(ChatColor.GOLD + "Maximale Spieler: " + ChatColor.YELLOW + settings.getInt("maxplayers")).setLocalizedName("maxplayers").build());
        okpinv.setItem(3, new ItemBuilder(Material.ARROW).setDisplayname(ChatColor.GOLD + "Minimale Spieler: " + ChatColor.YELLOW + settings.getInt("minplayers")).setLocalizedName("minplayers").build());
        p.openInventory(okpinv);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length > 0) {
            return false;
        }

        Player p = (Player) sender;
        onekitpvpStart();
        Inventory(p);
        return true;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory() == okpinv)) {
            return;
        }
        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }
        if (!event.getCurrentItem().getItemMeta().hasLocalizedName()) {
            return;
        }
        Player p = (Player) event.getWhoClicked();
        switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
            case "falldamage":
                if (settings.getBoolean("falldamage")) {
                    settings.set("falldamage", false);
                } else {
                    settings.set("falldamage", true);
                }
                break;
            case "blockinteract":
                if (settings.getBoolean("blockinteract")) {
                    settings.set("blockinteract", false);
                } else {
                    settings.set("blockinteract", true);
                }
                break;
            case "obbi":
                if (settings.getBoolean("obbi")) {
                    settings.set("obbi", false);
                } else {
                    settings.set("obbi", true);
                }
                break;
            case "maxplayers":
                if (event.isLeftClick()) {
                    if (settings.getInt("maxplayers") > 2 && settings.getInt("maxplayers") > settings.getInt("minplayers")) {
                        settings.set("maxplayers", settings.getInt("maxplayers") - 1);
                    } else
                        p.sendMessage(new TextComponent(ChatColor.RED + "Die Maximale Spieleranzahl darf nicht kleiner als das Minimum oder 2 sein!"));
                } else {
                    settings.set("maxplayers", settings.getInt("maxplayers") + 1);
                }
                break;
            case "minplayers":
                if (event.isLeftClick()) {
                    if (settings.getInt("minplayers") > 2) {
                        settings.set("minplayers", settings.getInt("minplayers") - 1);
                    } else
                        p.sendMessage(new TextComponent(ChatColor.RED + "Die Minimale Spieleranzahl darf nicht kleiner als 2 sein!"));
                } else {
                    if (settings.getInt("minplayers") < settings.getInt("maxplayers")) {
                        settings.set("minplayers", settings.getInt("minplayers") + 1);
                    } else
                        p.sendMessage(new TextComponent(ChatColor.RED + "Die Minimale Spieleranzahl darf nicht größer als das Maximum sein!"));
                }
                break;
            default:
                p.closeInventory();
                break;
        }
        plugin.saveSettings();
        Inventory(p);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.setCancelled(true);
        Player p = e.getEntity();
        Player killer = e.getEntity().getKiller();
        p.setGameMode(GameMode.SPECTATOR);
        if (killer != null) {
            killer.playSound(killer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 100, 1);
            Bukkit.broadcast(new TextComponent(ChatColor.YELLOW + p.displayName().toString() + ChatColor.GOLD + " wurde von " + ChatColor.YELLOW + killer.displayName().toString() + ChatColor.GOLD + " getötet."));
        } else
            Bukkit.broadcast(new TextComponent(ChatColor.YELLOW + p.displayName().toString() + ChatColor.GOLD + " ist ausgeschieden."));
        if (Bukkit.getOnlinePlayers().size() == 1) {
            for (Player winner : Bukkit.getOnlinePlayers()) {
                if (winner.getGameMode() == GameMode.SURVIVAL) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.teleport(plugin.getSpawnLocation());
                        pl.sendTitle(ChatColor.GREEN + winner.getName(), ChatColor.DARK_GREEN + "hat die Runde gewonnen.", 5, 30, 20);
                        HandlerList.unregisterAll(this);
                    }
                    break;
                }
            }
        }
    }
}
