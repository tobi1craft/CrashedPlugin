package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class motd extends Command implements TabExecutor {

    CrashedBungee plugin = CrashedBungee.getPlugin();

    public motd() {
        super("motd", "crashed.motd", "messageoftheday");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        File file = new File(plugin.getDataFolder().getPath(), "config.yml");
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            if (args.length == 0) {
                sender.sendMessage(new TextComponent(ChatColor.GOLD + "Aktuelle MOTD:"));
                TextComponent text1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.text.1")));
                text1.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, config.getString("motd.text.1")));
                text1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, config.getString("motd.text.1")));
                sender.sendMessage(text1);
                TextComponent text2 = new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.text.2")));
                text2.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, config.getString("motd.text.2")));
                text2.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, config.getString("motd.text.2")));
                sender.sendMessage(text2);
                sender.sendMessage();

                sender.sendMessage(new TextComponent(ChatColor.GOLD + "Aktuelle Playerinfo:"));
                TextComponent playerinfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.playerinfo")));
                playerinfo.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, config.getString("motd.playerinfo")));
                playerinfo.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, config.getString("motd.playerinfo")));
                sender.sendMessage(playerinfo);
                return;
            }
            if (args.length == 1) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Es wurde keine neue MOTD angegeben"));
                return;
            }
            if (!(args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("2"))) {
                sender.sendMessage(new TextComponent(ChatColor.RED + "Zeile 1 oder 2?"));
                return;
            }
            StringBuilder motd = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                motd.append(args[i]).append(" ");
            }
            config.set("motd.text." + args[0], motd.toString());
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            sender.sendMessage(new TextComponent(ChatColor.GREEN + "Die MOTD lautet jetzt"));
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.text.1"))));
            sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', config.getString("motd.text.2"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 1) return Collections.emptyList();

        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("1");
            list.add("2");
            return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
        }
        return null;
    }
}
