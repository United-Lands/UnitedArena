package org.unitedlands.arena.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.unitedlands.arena.UnitedArena;
import org.unitedlands.arena.utils.MessageProvider;
import org.unitedlands.utils.Messenger;

public class BlockManager implements Listener {

    private final UnitedArena plugin;
    private final MessageProvider messageProvider;
    private int maxblocks;

    public BlockManager(UnitedArena plugin, MessageProvider messageProvider) {
        this.plugin = plugin;
        this.messageProvider = messageProvider;
        reloadConfig();
    }

    private Map<String, Set<Location>> blocklocations = new HashMap<>();
    private List<String> allowedworlds;

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        var blocklocation = event.getBlock().getLocation();
        var world = blocklocation.getWorld().getName();
        if (!allowedworlds.contains(world))
            return;

        var worldblocklocations = blocklocations.computeIfAbsent(world, k -> new HashSet<>());
        worldblocklocations.add(blocklocation);
        if (worldblocklocations.size() == maxblocks) {
            clearBlocks(world);
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        var blocklocation = event.getBlock().getLocation();
        var world = blocklocation.getWorld().getName();
        if (!allowedworlds.contains(world))
            return;

        var worldblocklocations = blocklocations.computeIfAbsent(world, k -> new HashSet<>());
        if (!worldblocklocations.contains(blocklocation)) {
            Messenger.sendMessage(event.getPlayer(), messageProvider.get("messages.error-break"), null,
                    messageProvider.get("messages.prefix"));
            event.setCancelled(true);
        }

    }

    public void clearBlocks(String world) {
        var worldblocklocations = blocklocations.get(world);
        if (worldblocklocations == null)
            return;
        for (var blocklocation : worldblocklocations) {
            blocklocation.getBlock().setType(Material.AIR);
        }
        worldblocklocations.clear();
    }

    public void reloadConfig() {
        allowedworlds = plugin.getConfig().getStringList("allowed-worlds");
        maxblocks = plugin.getConfig().getInt("maxblocks");
    }

    public void clearAllBlocks() {
        for (var world : allowedworlds) {
            clearBlocks(world);
        }
    }
}
