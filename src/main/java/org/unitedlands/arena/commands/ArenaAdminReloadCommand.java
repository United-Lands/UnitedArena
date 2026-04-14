package org.unitedlands.arena.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.unitedlands.arena.UnitedArena;
import org.unitedlands.classes.BaseCommandHandler;
import org.unitedlands.interfaces.IMessageProvider;
import org.unitedlands.utils.Messenger;

public class ArenaAdminReloadCommand extends BaseCommandHandler<UnitedArena> {

    public ArenaAdminReloadCommand(UnitedArena plugin, IMessageProvider messageProvider) {
        super(plugin, messageProvider);
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {

        plugin.reloadConfig();
        plugin.getBlockManager().reloadConfig();
        plugin.getCooldownManager().reloadConfig();

        Messenger.sendMessage(sender, messageProvider.get("messages.reload"), null, messageProvider.get("messages.prefix"));
    }

    @Override
    public List<String> handleTab(CommandSender sender, String[] args) {
        return null;
    }

}
