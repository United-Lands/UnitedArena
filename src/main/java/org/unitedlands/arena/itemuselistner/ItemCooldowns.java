package org.unitedlands.arena.itemuselistner;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.unitedlands.arena.UnitedArena;

public class ItemCooldowns implements Listener {
    private UnitedArena plugin;
    
    public ItemCooldowns(UnitedArena plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onProjectileUse(ProjectileLaunchEvent event) {

        if (event.getEntity().getShooter() instanceof Player) {

            Player player = (Player) event.getEntity().getShooter();

            var customCooldowns = plugin.getConfig().getConfigurationSection("cooldowns");
            if (!customCooldowns.getKeys(false).contains(event.getEntityType().toString()))
                return;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setCooldown(Material.getMaterial(event.getEntityType().toString()), plugin.getConfig()
                        .getInt("cooldowns." + event.getEntityType().toString(), 1) * 20);
            }, 1L);

        }
    }
}
