package org.unitedlands.arena.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.unitedlands.UnitedLib;
import org.unitedlands.arena.UnitedArena;
import org.unitedlands.classes.BaseCommandHandler;
import org.unitedlands.interfaces.IMessageProvider;
import org.unitedlands.utils.Messenger;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;

public class ArenaKitCommand extends BaseCommandHandler<UnitedArena> {

    public ArenaKitCommand(UnitedArena plugin, IMessageProvider messageProvider) {
        super(plugin, messageProvider);
    }

    @Override
    public List<String> handleTab(CommandSender sender, String[] args) {
        if (args.length != 1)
            new ArrayList<>();
        return new ArrayList<>(plugin.getConfig().getConfigurationSection("kits").getKeys(false));
    }

    @Override
    public void handleCommand(CommandSender sender, String[] args) {

        Player player = (Player) sender;

        if (args.length != 1) {
            Messenger.sendMessage(sender, messageProvider.get("messages.usage-kit"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }

        var allowedworlds = plugin.getConfig().getStringList("allowed-worlds");
        if (!allowedworlds.contains(player.getWorld().getName())) {
            Messenger.sendMessage(player, messageProvider.get("messages.error-wrong-world"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }

        var kitname = args[0];
        var kitsection = plugin.getConfig().getConfigurationSection("kits." + kitname);
        if (kitsection == null) {
            Messenger.sendMessage(player, messageProvider.get("messages.error-no-kit"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }
        var kitworlds = kitsection.getStringList("worlds");
        if (kitworlds != null && !kitworlds.contains(player.getWorld().getName())) {
            Messenger.sendMessage(player, messageProvider.get("messages.error-wrong-kit-world"), null,
                    messageProvider.get("messages.prefix"));
            return;
        }
        clearinventory(player);
        var setlist = kitsection.getStringList("set");
        for (var set : setlist) {
            giveSet(set, player);
        }

        return;
    }

    private void giveSet(String setname, Player player) {
        if (player == null)
            return;
        if (setname == null)
            return;
        var setsection = plugin.getConfig().getConfigurationSection("sets." + setname);
        if (setsection == null)
            return;
        var setitems = setsection.getKeys(false);
        for (var setitem : setitems) {
            var itemsection = setsection.getConfigurationSection(setitem);

            var material = itemsection.getString("material");
            if (material == null)
                return;

            var amount = itemsection.getInt("amount");
            var slot = itemsection.getString("slot");
            var enchants = itemsection.getConfigurationSection("enchants");
            var potiontype = itemsection.getString("potiontype");

            ItemStack newitem = UnitedLib.getInstance().getItemFactory().getItemStack(material, amount);

            if (potiontype != null) {
                if (newitem.getItemMeta() instanceof PotionMeta potionMeta) {
                    var registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.POTION);
                    var key = TypedKey.create(RegistryKey.POTION, potiontype.toLowerCase());
                    var basePotionType = registry.get(key);
                    if (basePotionType != null) {
                        potionMeta.setBasePotionType(basePotionType);
                        newitem.setItemMeta(potionMeta);
                    }
                }
            }

            if (enchants != null) {
                var registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
                for (var enchant : enchants.getKeys(false)) {
                    var key = TypedKey.create(RegistryKey.ENCHANTMENT, enchant.toLowerCase());
                    var newEnchantment = registry.get(key);
                    if (newEnchantment != null) {
                        var level = enchants.getInt(enchant, 1);
                        newitem.addEnchantment(newEnchantment, level);
                    }
                }
            }

            if (slot != null) {
                if (slot.equals("helmet")) {
                    player.getInventory().setHelmet(newitem);
                } else if (slot.equals("chestplate")) {
                    player.getInventory().setChestplate(newitem);
                } else if (slot.equals("leggings")) {
                    player.getInventory().setLeggings(newitem);
                } else if (slot.equals("boots")) {
                    player.getInventory().setBoots(newitem);
                } else if (slot.equals("offhand")) {
                    player.getInventory().setItemInOffHand(newitem);
                } else {
                    try {
                        var slotnumber = Integer.parseInt(slot);
                        player.getInventory().setItem(slotnumber, newitem);
                    } catch (Exception exception) {
                        player.getInventory().addItem(newitem);
                    }

                }
            } else {
                player.getInventory().addItem(newitem);
            }
        }

    }

    private void clearinventory(Player player) {
        if (player == null)
            return;
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.getInventory().setItemInOffHand(null);
        player.updateInventory();
        player.clearActivePotionEffects();
        player.heal(20);
        player.setSaturation(20);
        player.setFoodLevel(20);
    }

}
