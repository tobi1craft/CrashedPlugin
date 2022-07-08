package de.tobi1craft.crashed.util;

import de.tobi1craft.crashed.CrashedCommon;
import de.tobi1craft.crashed.commands.Nick;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlaceholderAPI extends PlaceholderExpansion {

    CrashedCommon plugin = CrashedCommon.getPlugin();
    Nick nick = plugin.getNick();

    @Override
    public @NotNull String getIdentifier() {
        return "crashed";
    }

    @Override
    public @NotNull String getAuthor() {
        return "tobi1craft";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        UUID uuid = player.getUniqueId();
        if (params.equalsIgnoreCase("nick")) {
            return nick.getNicked(uuid);
        }
        if (params.equalsIgnoreCase("nicked")) {
            return String.valueOf(nick.isNicked(uuid));
        }
        return null;
    }
}
