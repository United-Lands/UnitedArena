package org.unitedlands.arena;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.unitedlands.arena.commands.ArenaAdminCommand;
import org.unitedlands.arena.commands.ArenaCommand;
import org.unitedlands.arena.itemuselistner.ItemCooldowns;
import org.unitedlands.arena.managers.BlockManager;
import org.unitedlands.arena.utils.MessageProvider;
import org.unitedlands.utils.Logger;

public class UnitedArena extends JavaPlugin {
    
    private MessageProvider messageProvider;

    private BlockManager blockManager; 

    @Override
    public void onEnable() {

        saveDefaultConfig();

        messageProvider = new MessageProvider(getConfig());
        blockManager = new BlockManager(this, messageProvider);

        var arenaCmd = new ArenaCommand(this, messageProvider);
        getCommand("arena").setExecutor(arenaCmd);
        getCommand("arena").setTabCompleter(arenaCmd);

        var arenaAdminCmd = new ArenaAdminCommand(this, messageProvider);
        getCommand("arenaadmin").setExecutor(arenaAdminCmd);
        getCommand("arenaadmin").setTabCompleter(arenaAdminCmd);

        Bukkit.getPluginManager().registerEvents(blockManager, this);
        Bukkit.getPluginManager().registerEvents(new ItemCooldowns(this), this);

        Logger.log("UnitedArena initialized.", "UnitedArena");
    }
    @Override
    public void onDisable(){
        blockManager.clearAllBlocks(); 
        Logger.log("UnitedArena disabled (clearing all blocks)", "UnitedArena");
    }
    public BlockManager getBlockManager() {
        return blockManager;
    }
}
