package org.unitedlands.arena.commands;

import org.unitedlands.arena.UnitedArena;
import org.unitedlands.classes.BaseCommandExecutor;
import org.unitedlands.interfaces.IMessageProvider;

public class ArenaCommand extends BaseCommandExecutor<UnitedArena> {

    public ArenaCommand(UnitedArena plugin, IMessageProvider messageProvider) {
        super(plugin, messageProvider);
    }

    @Override
    protected void registerHandlers() {
        handlers.put("kit", new ArenaKitCommand(plugin, messageProvider));
        handlers.put("tp", new ArenaTpCommand(plugin, messageProvider));
    }

}
