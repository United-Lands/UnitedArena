package org.unitedlands.arena.commands;

import org.unitedlands.arena.UnitedArena;
import org.unitedlands.classes.BaseCommandExecutor;
import org.unitedlands.interfaces.IMessageProvider;

public class ArenaAdminCommand extends BaseCommandExecutor<UnitedArena> {

    public ArenaAdminCommand(UnitedArena plugin, IMessageProvider messageProvider) {
        super(plugin, messageProvider);
    }

    @Override
    protected void registerHandlers() {
        handlers.put("reload", new ArenaAdminReloadCommand(plugin, messageProvider));
    }

}
