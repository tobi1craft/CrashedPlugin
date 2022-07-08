package de.tobi1craft.crashed.commands;

import de.tobi1craft.crashed.CrashedWaterfall;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class otherexecute extends Command {
    private final CrashedWaterfall plugin = CrashedWaterfall.getPlugin();

    public otherexecute() {
        super("otherexecute", "crashed.otherexecute", "oe");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if((args.length  < 2)) {
            return;
        }
        StringBuilder cmd = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            cmd.append(args[i]).append(" ");
        }
        plugin.commandOnServer("command", cmd.toString(), args[0], sender);
    }
}
