package de.tobi1craft.crashed.party;

import de.tobi1craft.crashed.CrashedBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
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
import java.util.Objects;
import java.util.stream.Collectors;

public class PartyCommand extends Command implements TabExecutor {
    public PartyCommand() {
        super("party", "crashed.party", "p");
    }

    CrashedBungee plugin = CrashedBungee.getPlugin();
    private static PartyCommand partyplugin;

    public static PartyCommand getPartyplugin() {
        return partyplugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        partyplugin = this;
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("Du darfst nur als Spieler eine Party benutzen!"));
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(new TextComponent("So funktioniert der Party Befehl:"));
            sender.sendMessage(new TextComponent("/p invite <Spieler> - Lädt einen Spieler ein - Partyrang: Helfer"));
            sender.sendMessage(new TextComponent("/p <Spieler> - Lädt einen Spieler ein - Kurzform"));
            sender.sendMessage(new TextComponent("/p leave - Party verlassen - Partyrang: Mitglied"));
            sender.sendMessage(new TextComponent("/p delete - Party löschen - Passiert auch automatisch - Partyrang: Besitzer"));
            sender.sendMessage(new TextComponent("/p kick <Spieler> - Entfernt jemanden aus der Party - Partyrang: Helfer"));
            sender.sendMessage(new TextComponent("/p promote <Spieler> - Lässt jemanden in der Party aufsteigen - Partyrang: Besitzer"));
            sender.sendMessage(new TextComponent("/p demote <Spieler> - Lässt jemanden in der Party absteigen - Partyrang: Besitzer"));
            sender.sendMessage(new TextComponent("/p warp - Teleportiert alle Party Mitglieder zu dir - Partyrang: Besitzer"));
            sender.sendMessage(new TextComponent("/p join <player> - Nimmt eine Beitrittsanfrage an - Partyrang: keiner"));
            return;
        }
        if (!(args.length == 1) && !(args.length == 2)) {
            return;
        }
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }
            File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
            if (!file.exists()) {
                file.createNewFile();
            }

            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            ProxiedPlayer p = (ProxiedPlayer) sender;
            String partyofp = config.getString("partyofplayer." + p.getName());
            int plevel = 0;
            if (Objects.equals(config.getString(partyofp + "." + p.getName()), "member")) {
                plevel = 1;
            }
            if (Objects.equals(config.getString(partyofp + "." + p.getName()), "helper")) {
                plevel = 2;
            }
            if (Objects.equals(config.getString(partyofp + "." + p.getName()), "owner")) {
                plevel = 3;
            }
            switch (args[0]) {
                case "invite":
                    if (!(config.contains(partyofp + "." + partyofp))) {
                        if (this.createParty(p.getName())) {
                            plevel = 3;
                            partyofp = p.getName();
                        } else {
                            p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                        }
                    }
                    if (plevel < 2) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    ProxiedPlayer invited = ProxyServer.getInstance().getPlayer(args[1]);
                    if (invited == null) {
                        p.sendMessage(new TextComponent(ChatColor.DARK_RED + args[1] + ChatColor.RED + " konnte nicht gefunden werden"));
                        break;
                    }
                    if (this.invitePlayer(partyofp, invited.getName())) {
                        this.sendPartyMessage(partyofp, new TextComponent(ChatColor.DARK_GREEN + invited.getName() + ChatColor.GREEN + " wurde in die Party eingeladen"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
                case "leave":
                    if (plevel > 0) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du bist in keiner Party"));
                        break;
                    }
                    if (this.kickPlayer(partyofp, p.getName())) {
                        this.sendPartyMessage(partyofp, new TextComponent(ChatColor.GOLD + p.getName() + ChatColor.YELLOW + " hat die Party verlassen"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
                case "delete":
                    if (!(plevel == 3)) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    this.sendPartyMessage(partyofp, new TextComponent(ChatColor.RED + "Die Party wurde gelöscht"));
                    if (!(this.deleteParty(partyofp))) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
                case "kick":
                    if (plevel < 2) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    if (plevel == 2 && Objects.equals(config.getString(partyofp + "." + ProxyServer.getInstance().getPlayer(args[1])), "helper")) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    if (this.kickPlayer(partyofp, args[1])) {
                        this.sendPartyMessage(partyofp, new TextComponent(ChatColor.GOLD + args[1] + ChatColor.YELLOW + " hat die Party verlassen"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
                case "promote":
                    if (plevel < 3) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(new TextComponent(ChatColor.DARK_RED + args[1] + ChatColor.RED + " konnte nicht gefunden werden"));
                        break;
                    }
                    String returned = this.prodismote(partyofp, player.getName(), true);
                    if (returned.equals("false")) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Das geht nicht"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.DARK_GREEN + player.getName() + ChatColor.GREEN + " wurde zum " + returned));
                    }
                case "demote":
                    if (plevel < 3) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    ProxiedPlayer player2 = ProxyServer.getInstance().getPlayer(args[1]);
                    if (player2 == null) {
                        p.sendMessage(new TextComponent(ChatColor.DARK_RED + args[1] + ChatColor.RED + " konnte nicht gefunden werden"));
                        break;
                    }
                    String returnedval = this.prodismote(partyofp, player2.getName(), false);
                    if (returnedval.equals("false")) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Das geht nicht"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.DARK_GREEN + player2.getName() + ChatColor.GREEN + " wurde zum " + returnedval));
                    }
                case "warp":
                    if (plevel < 2) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    if (this.warpParty(partyofp, p.getName())) {
                        this.sendPartyMessage(partyofp, new TextComponent(ChatColor.GREEN + "Die Party wurde teleportiert"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
                case "join":
                    if (this.joinParty(args[1], p.getName())) {
                        this.sendPartyMessage(args[1], new TextComponent(ChatColor.DARK_GREEN + p.getName() + ChatColor.GREEN + " ist der Party beigetreten"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du wurdest nicht in die Party eingeladen oder etwas anderes ist schiefgelaufen"));
                    }
                    break;
                default:
                    if (!(config.contains(partyofp + "." + partyofp))) {
                        if (this.createParty(p.getName())) {
                            plevel = 3;
                            partyofp = p.getName();
                        } else {
                            p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                        }
                    }
                    if (plevel < 2) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    ProxiedPlayer inviteded = ProxyServer.getInstance().getPlayer(args[0]);
                    if (inviteded == null) {
                        p.sendMessage(new TextComponent(ChatColor.DARK_RED + args[0] + ChatColor.RED + " konnte nicht gefunden werden"));
                        break;
                    }
                    if (this.invitePlayer(partyofp, inviteded.getName())) {
                        this.sendPartyMessage(partyofp, new TextComponent(ChatColor.DARK_GREEN + inviteded.getName() + ChatColor.GREEN + " wurde in die Party eingeladen"));
                    } else {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Etwas ist schiefgelaufen"));
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean createParty(String id) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        config.set(ProxyServer.getInstance().getPlayer(id).getName() + "." + ProxyServer.getInstance().getPlayer(id).getName(), "owner");
        config.set("partyofplayer." + ProxyServer.getInstance().getPlayer(id).getName(), ProxyServer.getInstance().getPlayer(id).getName());
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        return true;
    }

    private boolean invitePlayer(String id, String p) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        ProxiedPlayer pl = ProxyServer.getInstance().getPlayer(p);
        config.set(id + ".invites." + pl.getName(), true);
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        TextComponent message = new TextComponent(ChatColor.YELLOW + "Du wurdest in die Party von " + ChatColor.GOLD + ProxyServer.getInstance().getPlayer(id).getName() + ChatColor.YELLOW + " eingeladen. " + ChatColor.GREEN + "Klicke um beizutreten");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + ProxyServer.getInstance().getPlayer(id).getName()));
        pl.sendMessage(message);
        return true;
    }

    private boolean joinParty(String id, String p) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        if (config.getBoolean(ProxyServer.getInstance().getPlayer(id).getName() + ".invites." + ProxyServer.getInstance().getPlayer(p).getName())) {
            config.set(ProxyServer.getInstance().getPlayer(id).getName() + "." + ProxyServer.getInstance().getPlayer(p).getName(), "member");
            config.set(ProxyServer.getInstance().getPlayer(id).getName() + ".invites." + ProxyServer.getInstance().getPlayer(p).getName(), null);
            config.set("partyofplayer." + ProxyServer.getInstance().getPlayer(p).getName(), ProxyServer.getInstance().getPlayer(id).getName());
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            return true;
        }
        return false;
    }

    private String prodismote(String id, String p, boolean prodis) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        String state;
        if (config.get(ProxyServer.getInstance().getPlayer(id).getName() + "." + ProxyServer.getInstance().getPlayer(p).getName()) == "member") {
            if (prodis) {
                state = "helper";
            } else {
                return "false";
            }
        } else if (config.get(ProxyServer.getInstance().getPlayer(id).getName() + "." + ProxyServer.getInstance().getPlayer(p).getName()) == "helper") {
            if (prodis) {
                state = "owner";
            } else {
                state = "member";
            }
        } else return "false";

        config.set(ProxyServer.getInstance().getPlayer(id).getName() + "." + ProxyServer.getInstance().getPlayer(p).getName(), state);
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        return state;
    }

    private boolean kickPlayer(String id, String p) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        config.set(id + "." + ProxyServer.getInstance().getPlayer(p).getName(), null);
        config.set(id + ".invites." + ProxyServer.getInstance().getPlayer(p).getName(), null);
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        return true;
    }

    private boolean deleteParty(String id) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        for (String s : config.getSection(id).getKeys()) {
            config.set("partyofplayer." + s, null);
        }
        config.set(id, null);
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        return true;
    }

    public boolean warpParty(String id, String destp) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        ProxiedPlayer p;
        ServerInfo idserver = ProxyServer.getInstance().getPlayer(id).getServer().getInfo();
        for (String s : config.getSection(id).getKeys()) {
            p = ProxyServer.getInstance().getPlayer(s);
            if (!(p == null)) {
                if (Objects.equals(p.getServer().getInfo().getName(), idserver.getName())) {
                    plugin.teleportOnServer("tpplayer", p.getName(), destp, idserver.getName());
                } else {
                    p.connect(idserver);
                    plugin.teleportOnServer("tpplayer", p.getName(), destp, idserver.getName());
                }
            }
        }
        return true;
    }

    private void sendPartyMessage(String id, TextComponent message) throws IOException {
        File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
        Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        ProxiedPlayer p;
        for (String s : config.getSection(id).getKeys()) {
            p = ProxyServer.getInstance().getPlayer(s);
            if (!(p == null)) {
                p.sendMessage(message);
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            return Collections.emptyList();
        }
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("invite");
            list.add("leave");
            list.add("delete");
            list.add("kick");
            list.add("promote");
            list.add("demote");
            list.add("warp");
            list.add("join");
            for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                list.add(players.getName());
            }
            return list.stream().filter(a -> a.startsWith(args[0])).collect(Collectors.toList());
        }
        if (args.length == 2) {
            File file = new File(plugin.getDataFolder().getPath(), "parties.yml");
            Configuration config = null;
            try {
                config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<String> list = new ArrayList<>();
            if (args[0].equalsIgnoreCase("invite")) {
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    list.add(players.getName());
                }
            }
            if (args[0].equalsIgnoreCase("kick")) {
                assert config != null;
                for (String s : config.getSection(config.getString("partyofplayer." + p.getName())).getKeys()) {
                    if (Objects.equals(config.getString(config.getString("partyofplayer." + p.getName())), "helper") && Objects.equals(config.getString(config.getString("partyofplayer." + p.getName()) + "." + ProxyServer.getInstance().getPlayer(s)), "helper")) {
                        p.sendMessage(new TextComponent(ChatColor.RED + "Du darfst das nicht"));
                        break;
                    }
                    list.add(s);
                }
            }
            if (args[0].equalsIgnoreCase("demote")) {
                assert config != null;
                for (String s : config.getSection(config.getString("partyofplayer." + p.getName())).getKeys()) {
                    if (!(ProxyServer.getInstance().getPlayer(s) == null)) {
                        if ((config.getString(config.getString("partyofplayer." + p.getName())) + "." + s).equals("helper")) {
                            list.add(s);
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("promote")) {
                assert config != null;
                for (String s : config.getSection(config.getString("partyofplayer." + p.getName())).getKeys()) {
                    if (!(ProxyServer.getInstance().getPlayer(s) == null)) {
                        if (config.getString(config.getString("partyofplayer." + p.getName()) + "." + s).equals("helper") || config.getString(config.getString("partyofplayer." + p.getName()) + "." + s).equals("member")) {
                            list.add(s);
                        }
                    }
                }
            }
            return list.stream().filter(a -> a.startsWith(args[1])).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
