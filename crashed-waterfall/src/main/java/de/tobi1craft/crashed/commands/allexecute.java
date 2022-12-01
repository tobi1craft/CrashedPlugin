package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class allexecute extends Command {
    private final CrashedBungee plugin = CrashedBungee.getPlugin();

    public allexecute() {
        super("allexecute", "crashed.allexecute", "ae");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if ((args.length == 0)) {
            return;
        }
        StringBuilder cmd = new StringBuilder();
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        plugin.commandOnServer("command", cmd.toString(), sender);
    }
}
