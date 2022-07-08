package de.tobi1craft.crashed.listeners;

import de.tobi1craft.crashed.CrashedCommon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scoreboard.*;

import java.util.*;

public class MovementListener implements Listener {

    CrashedCommon plugin = CrashedCommon.getPlugin();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.hasChangedPosition()) {
            p = e.getPlayer();
            if (p.getName().equalsIgnoreCase("tobi1craft") || p.getName().equalsIgnoreCase("EAGKB") || p.getName().equalsIgnoreCase("Tjuli")) return;
            FileConfiguration config = CrashedCommon.getPlugin().getConfig();
            int y = p.getLocation().getBlockY();
            int configy = config.getInt("jar." + p.getName());
            if (y > configy) {
                if (p.getLocation().getBlockX() > -230) {
                    return;
                }
                config.set("jar." + p.getName(), y);
                CrashedCommon.getPlugin().saveConfig();
                Bukkit.getServer().getScheduler().runTask(plugin, run);
            }
            locToParticle(p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ());
        }
    }

    @EventHandler()
    public void PlayerJoin(PlayerJoinEvent e) {
        p = e.getPlayer();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, run, 0, 20 * 60);
        p.sendTitle(ChatColor.GOLD + "Jump And Run -> Rang", ChatColor.RED + "Mehr Infos " + ChatColor.RESET + "-" + ChatColor.BLUE + " /jnr", 10, 20 * 10, 10);
    }

    Player p;

    Runnable run = () -> {
        FileConfiguration config = CrashedCommon.getPlugin().getConfig();
        HashMap<Integer, String> map = new HashMap<>();
        for (String player : Objects.requireNonNull(config.getConfigurationSection("jar")).getKeys(true)) {
            map.put(config.getInt("jar." + player), player);
        }
        ArrayList<Integer> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        Map<String, Integer> inverted = new HashMap<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("high", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.GOLD + "Top");
        Score score = objective.getScore(ChatColor.BLUE + "Du");
        score.setScore(config.getInt("jar." + p.getName()));
        String s;
        if (Objects.requireNonNull(config.getConfigurationSection("jar")).getKeys(true).size() > 0) {
            s = map.get(list.get(0));
            Score score0 = objective.getScore(ChatColor.GREEN + s);
            score0.setScore(inverted.get(s));
        }
        if (Objects.requireNonNull(config.getConfigurationSection("jar")).getKeys(true).size() > 1) {
            s = map.get(list.get(1));
            Score score1 = objective.getScore(ChatColor.GREEN + s);
            score1.setScore(inverted.get(s));
        }

        if (Objects.requireNonNull(config.getConfigurationSection("jar")).getKeys(true).size() > 2) {
            s = map.get(list.get(2));
            Score score2 = objective.getScore(ChatColor.GREEN + s);
            score2.setScore(inverted.get(s));
        }
        if (Objects.requireNonNull(config.getConfigurationSection("jar")).getKeys(true).size() > 3) {
            s = map.get(list.get(3));
            Score score3 = objective.getScore(ChatColor.GREEN + s);
            score3.setScore(inverted.get(s));
        }
        Objects.requireNonNull(objective.getScoreboard()).resetScores(ChatColor.GREEN + p.getName());

        p.setScoreboard(board);
    };

    private void locToParticle(int x, int y, int z) {
        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 200, 200), 1.0F);
        if (x == -253 && y == 5 && z == -23) p.spawnParticle(Particle.REDSTONE, -254.5, 6, -22.5, 50, dustOptions);
        if (x == -255 && y == 6 && z == -23) p.spawnParticle(Particle.REDSTONE, -257.5, 7, -22.5, 50, dustOptions);
        if (x == -258 && y == 7 && z == -23) p.spawnParticle(Particle.REDSTONE, -257.5, 7, -26.5, 50, dustOptions);
        if (x == -258 && y == 7 && z == -27) p.spawnParticle(Particle.REDSTONE, -254.5, 8, -26.5, 50, dustOptions);
        if (x == -255 && y == 8 && z == -27) p.spawnParticle(Particle.REDSTONE, -256.5, 9, -24.5, 50, dustOptions);
        if (x == -257 && y == 9 && z == -25) p.spawnParticle(Particle.REDSTONE, -260.5, 10, -24.5, 50, dustOptions);
        if (x == -261 && y == 10 && z == -25) p.spawnParticle(Particle.REDSTONE, -260.5, 11, -20.5, 50, dustOptions);
        if (x == -261 && y == 11 && z == -21) p.spawnParticle(Particle.REDSTONE, -258.5, 12, -23.5, 50, dustOptions);
        if (x == -259 && y == 12 && z == -24) p.spawnParticle(Particle.REDSTONE, -258.5, 12, -26.5, 50, dustOptions);
        if (x == -259 && y == 12 && z == -27) p.spawnParticle(Particle.REDSTONE, -255.5, 12, -26.5, 50, dustOptions);
        if (x == -256 && y == 12 && z == -27) p.spawnParticle(Particle.REDSTONE, -253.5, 13, -30.5, 50, dustOptions);
        if (x == -254 && y == 13 && z == -31) p.spawnParticle(Particle.REDSTONE, -250.5, 14, -28.5, 50, dustOptions);
        if (x == -251 && y == 14 && z == -29) p.spawnParticle(Particle.REDSTONE, -252.5, 15, -26.5, 50, dustOptions);
        if (x == -253 && y == 15 && z == -27) p.spawnParticle(Particle.REDSTONE, -254.5, 16, -28.5, 50, dustOptions);
        if (x == -255 && y == 16 && z == -29) p.spawnParticle(Particle.REDSTONE, -256.5, 16, -28.5, 50, dustOptions);
        if (x == -257 && y == 16 && z == -29) p.spawnParticle(Particle.REDSTONE, -257.5, 17, -26.5, 50, dustOptions);
        if (x == -258 && y == 17 && z == -27) p.spawnParticle(Particle.REDSTONE, -255.5, 17, -24.5, 50, dustOptions);
        if (x == -256 && y == 17 && z == -25) p.spawnParticle(Particle.REDSTONE, -253.5, 18, -22.5, 50, dustOptions);
        if (x == -254 && y == 18 && z == -23) p.spawnParticle(Particle.REDSTONE, -253.5, 18, -17.5, 50, dustOptions);
        if (x == -254 && y == 18 && z == -18) p.spawnParticle(Particle.REDSTONE, -255.5, 19, -18.5, 50, dustOptions);
        if (x == -256 && y == 19 && z == -19) p.spawnParticle(Particle.REDSTONE, -257.5, 20, -20.5, 50, dustOptions);
        if (x == -258 && y == 20 && z == -21) p.spawnParticle(Particle.REDSTONE, -260.5, 21, -23.5, 50, dustOptions);
        if (x == -261 && y == 21 && z == -24) p.spawnParticle(Particle.REDSTONE, -256.5, 22, -23.5, 50, dustOptions);
        if (x == -257 && y == 22 && z == -24) p.spawnParticle(Particle.REDSTONE, -255.0, 26, -26.0, 50, dustOptions);
        if (x == -255 && y == 26 && z == -27) p.spawnParticle(Particle.REDSTONE, -256.5, 26, -26.5, 50, dustOptions);
        if (x == -257 && y == 26 && z == -27) p.spawnParticle(Particle.REDSTONE, -259.5, 27, -26.5, 50, dustOptions);
        if (x == -260 && y == 27 && z == -27) p.spawnParticle(Particle.REDSTONE, -259.5, 28, -21.5, 50, dustOptions);
        if (x == -259 && y == 28 && z == -22) p.spawnParticle(Particle.REDSTONE, -253.5, 28, -21.5, 50, dustOptions);
        if (x == -260 && y == 28 && z == -22) p.spawnParticle(Particle.REDSTONE, -253.5, 28, -21.5, 50, dustOptions);
        if (x == -254 && y == 28 && z == -22) p.spawnParticle(Particle.REDSTONE, -254.5, 29, -25.5, 50, dustOptions);
        if (x == -253 && y == 28 && z == -22) p.spawnParticle(Particle.REDSTONE, -254.5, 29, -25.5, 50, dustOptions);
        if (x == -255 && y == 29 && z == -26) p.spawnParticle(Particle.REDSTONE, -258.5, 30, -23.5, 50, dustOptions);
        if (x == -259 && y == 30 && z == -24) p.spawnParticle(Particle.REDSTONE, -254.5, 31, -24.5, 50, dustOptions);
        if (x == -255 && y == 31 && z == -25) p.spawnParticle(Particle.REDSTONE, -254.5, 32, -20.5, 50, dustOptions);
        if (x == -255 && y == 32 && z == -21) p.spawnParticle(Particle.REDSTONE, -0.5, 0, -0.5, 50, dustOptions);

    }
}
