package org.unitedlands.arena.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.unitedlands.arena.UnitedArena;
import org.unitedlands.classes.BaseCommandHandler;
import org.unitedlands.interfaces.IMessageProvider;
import org.unitedlands.utils.Messenger;

public class ArenaTpCommand extends BaseCommandHandler<UnitedArena> {

    public ArenaTpCommand(UnitedArena plugin, IMessageProvider messageProvider) {
        super(plugin, messageProvider);
    }

    @Override
    public List<String> handleTab(CommandSender sender, String[] args) {
        if (args.length == 1)
            return plugin.getConfig().getStringList("allowed-worlds");
        return null;
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {

        var player = (Player) sender;
        if (args.length != 1) {
            Messenger.sendMessage(sender, messageProvider.get("messages.usage-tp"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }

        var allowedWorlds = plugin.getConfig().getStringList("allowed-worlds");
        if (!allowedWorlds.contains(args[0])) {
            Messenger.sendMessage(sender, messageProvider.get("messages.no-world"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv tp " + player.getName() + " " + args[0]);
        Messenger.sendMessage(Bukkit.getServer(), messageProvider.get("messages.broadcast-tp"),
                Map.of("player", player.getName(), "arena", args[0]), messageProvider.get("messages.prefix"));
    }

}
